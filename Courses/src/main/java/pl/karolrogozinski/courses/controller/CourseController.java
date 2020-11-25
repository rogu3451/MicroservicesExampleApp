package pl.karolrogozinski.courses.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.karolrogozinski.courses.model.Course;
import pl.karolrogozinski.courses.model.dto.StudentDto;
import pl.karolrogozinski.courses.service.CourseService;
import pl.karolrogozinski.courses.service.StudentServiceClient;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final StudentServiceClient studentServiceClient;

    public CourseController(CourseService courseService, StudentServiceClient studentServiceClient) {
        this.courseService = courseService;
        this.studentServiceClient = studentServiceClient;
    }

    @GetMapping
    public List<Course> getCourses(@RequestParam(required = false) Course.Status status){
        return  courseService.getCourses(status);
    }

    @PostMapping
    public Course addCourse(@Valid @RequestBody Course course){
        return courseService.addCourse(course);
    }

    @GetMapping("/{code}")
    public Course getCourse(@PathVariable String code){
        return courseService.getCourse(code);
    }

    @DeleteMapping("/{code}")
    public void deleteCourse(@PathVariable String code){
        courseService.deleteCourse(code);
    }

    @PutMapping("/{code}")
    public Course putCourse(@PathVariable String code, @RequestBody @Valid Course course){
        return courseService.putCourse(code,course);
    }

    @PatchMapping("/{code}")
    public Course patchCourse(@PathVariable String code, @RequestBody Course course){
        return courseService.patchCourse(code,course);
    }

    @PostMapping("/{courseCode}/student/{studentId}")
    public ResponseEntity<?> courseEnrollment(@PathVariable String courseCode, @PathVariable Long studentId){
        courseService.courseEnrollment(courseCode,studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseCode}/members")
    public List<StudentDto> getCourseMembers(@PathVariable String courseCode){
        return courseService.getCourseMembers(courseCode);
    }

    @PostMapping("/{courseCode}/finish-enroll")
    public ResponseEntity<?> courseFinishEnroll(@PathVariable String courseCode){
        courseService.courseFinishEnroll(courseCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public List<StudentDto> testFeignClient(){
        return studentServiceClient.getStudents();
    }


}
