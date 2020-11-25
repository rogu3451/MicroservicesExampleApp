package pl.karolrogozinski.courses.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StudentDto {


    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;

    @NotNull
    private Status status;

    public enum Status{
        ACTIVE,
        INACTIVE
    }




}
