package pl.karolrogozinski.students.controller;

import antlr.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.karolrogozinski.students.repository.StudentRepository;
import pl.karolrogozinski.students.model.Student;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import pl.karolrogozinski.students.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping("/students")
public class StudentController {

    //Wstrzykiwanie zaleznosci za pomoca konstruktora jest zalecana medotda ze wzgledu na testy
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //@RequestMapping(value = "/hello", method = RequestMethod.GET)
//    @GetMapping("/hello")
//    public String sayHello(){
//        return "Witaj";
//    }
//
//    @GetMapping("/student")
//    public Student getStudent(){
//        Student student = new Student();
//        student.setFirstName("Arnold");
//        student.setLastName("Boczek");
//        student.setEmail("boczek@gmail.com");
//        return student;
//    }


    @GetMapping
    public List<Student> getStudents(@RequestParam(required = false) Student.Status status) {
            return studentService.getStudents(status);
    }

    @PostMapping("/emails")
    public List<Student> getStudentByEmails(@RequestBody List<String> emails) {
            return studentService.getStudentByEmails(emails);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody @Valid Student student) {
        return studentService.addStudent(student);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id){
        Optional<Student> studentOptional = studentRepository.findById(id); //Ctrl+Alt+V
        if(studentOptional.isPresent()){
            return ResponseEntity.ok(studentOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id){
       return studentService.getStudent(id);
    }

    @GetMapping("/email/{email}")
    public Student getStudentByEmail(@PathVariable String email){
        return studentService.getStudentByEmail(email);
    }

//    @PostMapping("/{id}")
//    public Student recreateStudent(@PathVariable Long id, @RequestBody Student student){
//        return studentRepository.findById(id).get();
//    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
/*
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id){
        try{
            studentRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){

        }
    }*/


    /*// Zmodyfikuj studenta a jesli nie istnieje to go utworz
    @PutMapping()
    public ResponseEntity<Student> putStudent(@RequestBody @Valid Student student){
                    return ResponseEntity.ok().body(studentRepository.save(student));
    }*/


    @PutMapping("/{id}")
    public Student putStudent(@PathVariable Long id, @RequestBody @Valid Student student){
        return studentService.putStudent(id,student);
    }


    @PatchMapping("/{id}")
    public Student patchStudent(@PathVariable Long id, @RequestBody  Student student){
        return studentService.patchStudent(id, student);
    }

    /*
    //Paginacja przyklad
    @GetMapping("/lastname")
    public List<Student> findStudent(@RequestParam String lastName, @RequestParam int numberOfPage){
          // Rozwiazanie nieoptymalne
        *//*return studentRepository.findAll().stream()
                    .filter(student -> student.getLastName().equals(lastName))
                    .collect(Collectors.toList());*//*

        System.out.println("OK");
        Pageable pageable = PageRequest.of(numberOfPage,2, Sort.by("firstName"));

        return studentRepository.findByLastName(lastName,pageable);
    }

    @GetMapping("/find")
    public List<Student> findStudent2(@RequestParam String lastName, @RequestParam String firstName){
        return studentRepository.findByLastNameAndFirstNameIsNotLikeAllIgnoreCase(lastName, firstName);
    }

    @GetMapping("/marian")
    public List<Student> findStudent3(){
        return studentRepository.findStudentsWithNameMarian();
    }*/

}
