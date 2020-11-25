package pl.karolrogozinski.students.service;

import pl.karolrogozinski.students.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getStudents(Student.Status status);

    Student getStudent(Long id);

    Student getStudentByEmail(String email);

    Student addStudent(Student student);

    void deleteStudent(Long id);

    Student putStudent(Long id, Student student);

    Student patchStudent(Long id, Student student);

    List<Student> getStudentByEmails(List<String> emails);
}
