package pl.karolrogozinski.courses.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.karolrogozinski.courses.exception.CourseError;
import pl.karolrogozinski.courses.exception.CourseException;
import pl.karolrogozinski.courses.model.Course;
import pl.karolrogozinski.courses.model.CourseMember;
import pl.karolrogozinski.courses.model.dto.NotificationInfoDto;
import pl.karolrogozinski.courses.model.dto.StudentDto;
import pl.karolrogozinski.courses.repository.CourseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CourseServiceImpl implements CourseService {

    public static final String EXCHANGE_ENROLL_FINISH = "enroll_finish";
    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;
    private final RabbitTemplate rabbitTemplate;


    public CourseServiceImpl(CourseRepository courseRepository, StudentServiceClient studentServiceClient, RabbitTemplate rabbitTemplate) {
        this.courseRepository = courseRepository;
        this.studentServiceClient = studentServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Course> getCourses(Course.Status status) {
        if (status != null) {
            return courseRepository.findAllByStatus(status);
        }
        return courseRepository.findAll();
    }


    public Course getCourse(String code) {
        return courseRepository.findById(code)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    public Course addCourse(Course course) {
        validateCourse(course);
        return courseRepository.save(course);
    }

    public void deleteCourse(String code) {
        Course course = courseRepository.findById(code)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        if (course.getStatus().equals(Course.Status.INACTIVE)) {
            throw new CourseException(CourseError.COURSE_ALREADY_DELETED);
        }
        course.setStatus(Course.Status.INACTIVE);
        courseRepository.save(course);
    }

    public Course putCourse(String code, Course course) {
        validateCourse(course);
        return courseRepository.findById(code)
                .map(courseFromDb -> {
                    courseFromDb.setName(course.getName());
                    courseFromDb.setDescription(course.getDescription());
                    courseFromDb.setStartDate(course.getStartDate());
                    courseFromDb.setEndDate(course.getEndDate());
                    courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
                    courseFromDb.setStatus(course.getStatus());
                    return courseRepository.save(courseFromDb);
                }).orElseThrow(()-> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    public Course patchCourse(String code, Course course) {
        validateCourse(course);
        return courseRepository.findById(code)
                .map(courseFromDb -> {
                    if(!isEmpty(course.getName())){
                        courseFromDb.setName(course.getName());
                    }
                    if(!isEmpty(course.getDescription())){
                        courseFromDb.setDescription(course.getDescription());
                    }
                    if(!isEmpty(course.getStartDate())){
                        courseFromDb.setStartDate(course.getStartDate());
                    }
                    if(!isEmpty(course.getEndDate())){
                        courseFromDb.setEndDate(course.getEndDate());
                    }
                    if(!isEmpty(course.getParticipantsLimit())){
                        courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    }
                    if(!isEmpty(course.getParticipantsNumber())){
                        courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
                    }
                    if(!isEmpty(course.getStatus())){
                        courseFromDb.setStatus(course.getStatus());
                    }
                    return courseRepository.save(courseFromDb);
                }).orElseThrow(()-> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    public void courseEnrollment(String courseCode, Long studentId) {
        Course course = getCourse(courseCode);
        validateCourseStatus(course);
        StudentDto student = studentServiceClient.getStudentById(studentId);
        validateStudentBeforeCourseEnrollment(course, student);
        course.incrementParticipantsNumbers();
        course.getCourseMember().add(new CourseMember(student.getEmail()));
        courseRepository.save(course);
    }

    @Override
    public List<StudentDto> getCourseMembers(String courseCode) {
        Course course = getCourse(courseCode);
        List<String> emailsMembers = getCourseMembersEmails(course);
        return studentServiceClient.getStudentByEmails(emailsMembers);
    }

    private List<String> getCourseMembersEmails(Course course) {
        return course.getCourseMember().stream()
                .map(CourseMember::getEmail).collect(Collectors.toList());
    }

    public void courseFinishEnroll(String courseCode) {
        Course course = getCourse(courseCode);

        if(Course.Status.INACTIVE.equals(course.getStatus())) {
            throw new CourseException(CourseError.COURSE_IS_INACTIVE);
        }

        course.setStatus(Course.Status.INACTIVE);
        courseRepository.save(course);

        sendMessageToRabbitMq(course);

    }

    private void sendMessageToRabbitMq(Course course) {
        NotificationInfoDto notificationInfoDto = createNotification(course);

        rabbitTemplate.convertAndSend(EXCHANGE_ENROLL_FINISH, notificationInfoDto);
    }

    private NotificationInfoDto createNotification(Course course) {
        List<String> emailsMembers = getCourseMembersEmails(course);

        return NotificationInfoDto.builder()
                .courseCode(course.getCode())
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .courseStartDate(course.getStartDate())
                .courseEndDate(course.getEndDate())
                .emails(emailsMembers)
                .build();
    }

    private void validateStudentBeforeCourseEnrollment(Course course, StudentDto student) {
        if(!StudentDto.Status.ACTIVE.equals(student.getStatus())){
            throw new CourseException(CourseError.STUDENT_IS_NOT_ACTIVE);
        }

        if(course.getCourseMember().stream()
                .anyMatch(member -> student.getEmail().equals(member.getEmail()))){
            throw new CourseException(CourseError.STUDENT_ALREADY_ENROLLED);
        }
    }

    private void validateCourseStatus(Course course) {
        if(!Course.Status.ACTIVE.equals(course.getStatus())){
            throw new CourseException(CourseError.COURSE_IS_NOT_ACTIVE);
        }
    }

    private void checkIfDateIsCorrect(LocalDateTime startDate, LocalDateTime endDate){
        if(startDate.isAfter(endDate)){
            throw new CourseException(CourseError.COURSE_DATE_INCORRECT);
        }
    }

    private void validateParticipantsLimit(Long participantsNumber, Long participantsLimit){
        if(participantsNumber > participantsLimit){
            throw new CourseException(CourseError.COURSE_PARTICIPANTS_INCORRECT);
        }
    }

    private void validateStatus(Long participantsNumber, Long participantsLimit, Course.Status status){
        if(Course.Status.FULL.equals(status) && !participantsNumber.equals(participantsLimit)){
            throw new CourseException(CourseError.COURSE_CAN_NOT_SET_FULL_STATUS);
        }
        if(Course.Status.ACTIVE.equals(status) && participantsNumber.equals(participantsLimit)){
            throw new CourseException(CourseError.COURSE_CAN_NOT_SET_ACTIVE_STATUS);
        }
        if(Course.Status.INACTIVE.equals(status) && participantsNumber.equals(participantsLimit)){
            throw new CourseException(CourseError.COURSE_CAN_NOT_SET_INACTIVE_STATUS);
        }
    }



    private void validateCourse(Course course){
        checkIfDateIsCorrect(course.getStartDate(),course.getEndDate()); // If returns true date is correst otherwise exception
        validateParticipantsLimit(course.getParticipantsNumber(),course.getParticipantsLimit()); // true = number of participants is correct otherwise exception
        validateStatus(course.getParticipantsNumber(),course.getParticipantsLimit(),course.getStatus()); // true = you can set FULL status otherwise exception
    }


}
