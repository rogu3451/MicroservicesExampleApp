package pl.karolrogozinski.students.exception;

public enum StudentError {
    STUDENT_NOT_FOUND("Student does not exist"),
    STUDENT_EMAIL_ALREADY_EXIST("Student's email allready exists"),
    STUDENT_STATUS_INCORRECT("Student's status is incorrect"),
    STUDENT_INACTIVE("Student is inactive");


    private String message;

    StudentError(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
