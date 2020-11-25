package pl.karolrogozinski.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.karolrogozinski.students.exception.StudentError;
import pl.karolrogozinski.students.exception.StudentException;
import pl.karolrogozinski.students.model.Student;
import pl.karolrogozinski.students.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(Student.Status status) {
        if(status!=null){
            return studentRepository.findAllByStatus(status);
        }
        return studentRepository.findAll();
    }



    public Student getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));

        if(!Student.Status.ACTIVE.equals(student.getStatus())){
            throw new StudentException(StudentError.STUDENT_INACTIVE);
        }else{
            return student;
        }

    }

    public Student getStudentByEmail(String email) {
        Student student = studentRepository.findByEmailAndStatus(email,Student.Status.ACTIVE);
        if(student==null){
            throw new StudentException(StudentError.STUDENT_NOT_FOUND);
        }
        return student;
    }


    public Student addStudent(Student student) {
        validateStudentEmailExist(student);
        return studentRepository.save(student);
    }


    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));

        student.setStatus(Student.Status.INACTIVE);
        studentRepository.save(student);
    }

    public Student putStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(studentFromDb -> {
                    if (!studentFromDb.getEmail().equals(student.getEmail()) &&
                            studentRepository.existsByEmail(student.getEmail())
                    ) {
                        throw new StudentException(StudentError.STUDENT_EMAIL_ALREADY_EXIST);
                    }
                    studentFromDb.setFirstName(student.getFirstName());
                    studentFromDb.setLastName(student.getLastName());
                    studentFromDb.setEmail(student.getEmail());
                    studentFromDb.setStatus(student.getStatus());
                    return studentRepository.save(studentFromDb);
                }).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }

    public Student patchStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(studentFromDb -> {
                    if(!isEmpty(student.getFirstName())){
                        studentFromDb.setFirstName(student.getFirstName());
                    }

                    if(!isEmpty(student.getLastName())){
                        studentFromDb.setLastName(student.getLastName());
                    }

                    if(!isEmpty(student.getStatus())){
                        studentFromDb.setStatus(student.getStatus());
                    }

                    return studentRepository.save(studentFromDb);
                }).orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }

    public List<Student> getStudentByEmails(List<String> emails) {
        return studentRepository.findByEmailIn(emails);
    }

    private void validateStudentEmailExist(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new StudentException(StudentError.STUDENT_EMAIL_ALREADY_EXIST);
        }
    }
}
