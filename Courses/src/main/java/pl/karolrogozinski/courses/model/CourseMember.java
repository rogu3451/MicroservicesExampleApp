package pl.karolrogozinski.courses.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document
@Setter
@Getter
public class CourseMember {

    @NotNull
    private String email;

    @NotNull
    private LocalDateTime enrollmentDate;

    public CourseMember(@NotNull String email){
        this.enrollmentDate = LocalDateTime.now();
        this.email = email;
    }


}
