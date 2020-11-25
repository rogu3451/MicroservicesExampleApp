package pl.karolrogozinski.students.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.karolrogozinski.students.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email); // True gdy istnieje student o tym emailu

    List<Student> findAllByStatus(Student.Status status);

    Student findByEmailAndStatus(String email, Student.Status status);

    List<Student> findByEmailIn(List<String> emails);

  /* List<Student> findByLastName(String lastName, Pageable pageable);

    List<Student> findByLastNameAndFirstNameIsNotLikeAllIgnoreCase(String lastName, String firstName);

    @Query(" SELECT s FROM Student s WHERE s.firstName = 'Marian' ")
    List<Student> findStudentsWithNameMarian();*/
}
