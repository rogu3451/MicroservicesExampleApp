package pl.karolrogozinski.courses.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter

public class Course {
    @Id
    private String code;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Future
    private LocalDateTime startDate;
    @NotNull
    @Future
    private LocalDateTime endDate;
    @Min(0)
    private Long participantsLimit;
    @NotNull
    @Min(0)
    private Long participantsNumber;
    @NotNull
    private Status status;

    private List<CourseMember> courseMember = new ArrayList<>();

    public enum Status{
        ACTIVE,
        INACTIVE,
        FULL
    }

    public void incrementParticipantsNumbers(){
        participantsNumber++;
        if(participantsNumber.equals(participantsLimit)){
            setStatus(Course.Status.FULL);
        }
    }
}
