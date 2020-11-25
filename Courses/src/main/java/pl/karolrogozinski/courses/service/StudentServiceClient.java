package pl.karolrogozinski.courses.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.karolrogozinski.courses.model.dto.StudentDto;

import java.util.List;

@FeignClient(name = "STUDENT-SERVICE")
@RequestMapping("/students")
public interface StudentServiceClient {

    @GetMapping
    List<StudentDto> getStudents();

    @GetMapping("/{studentId}")
    StudentDto getStudentById(@PathVariable Long studentId);

    @PostMapping("/emails")
    List<StudentDto> getStudentByEmails(@RequestBody List<String> emails);

}
