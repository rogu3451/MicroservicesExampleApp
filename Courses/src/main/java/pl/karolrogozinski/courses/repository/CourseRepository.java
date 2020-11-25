package pl.karolrogozinski.courses.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.karolrogozinski.courses.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course,String> {
    List<Course> findAllByStatus(Course.Status status);
}
