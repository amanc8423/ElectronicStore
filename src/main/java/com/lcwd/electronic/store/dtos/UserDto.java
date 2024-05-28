package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.valid.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min=3,max=15,message="Invalid Name")
    private String name;

    //@Email(message = "invalid user email")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message="Invalid email"  )
    private String email;

    @NotBlank(message="password is required")
    private String password;

    @Size(min=4,max=6,message = "Invalid gender" )
    private String gender;

    @NotBlank(message="write something")
    private String about;

    //@Pattern
    //custom validator

    @ImageNameValid
    private String imageName;

}
