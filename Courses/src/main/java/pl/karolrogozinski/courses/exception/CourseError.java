package pl.karolrogozinski.courses.exception;

public enum CourseError {
    COURSE_NOT_FOUND("Course does not exist"),
    COURSE_ALREADY_DELETED("Course is already deleted"),
    COURSE_DATE_INCORRECT("Start date of course is after end date of course"),
    COURSE_PARTICIPANTS_INCORRECT("Number of participants is bigger than number of participants limit"),
    COURSE_CAN_NOT_SET_FULL_STATUS("Number of participant is not equal participant limit, you can not set FULL status!"),
    COURSE_CAN_NOT_SET_ACTIVE_STATUS("Number of participant is equal participant limit, you can not set ACTIVE status!"),
    COURSE_CAN_NOT_SET_INACTIVE_STATUS("Number of participant is equal participant limit, you can not set INACTIVE status!"),
    STUDENT_ALREADY_ENROLLED("The student is already enrolled on this course!"),
    COURSE_IS_NOT_ACTIVE("The course is not ACTIVE"),
    COURSE_IS_INACTIVE("The course is INACTIVE"),
    STUDENT_IS_NOT_ACTIVE("The student is not ACTIVE"),
    STUDENT_CANNOT_BE_ENROLL("The student cannot be enroll on course");

    private String message;

    CourseError(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
