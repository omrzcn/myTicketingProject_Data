package com.cydeo.dto;

import com.cydeo.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {


    private Long id ;

    @NotBlank
    @Size(max = 15, min = 2)
    private String firstName;

    @NotBlank
    @Size(max = 15, min = 2)
    private String lastName;

    @NotBlank
    @Email
    private String userName;

    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    private String passWord;

    @NotNull
    private String confirmPassWord;

    private boolean enabled;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String phone;

    @NotNull
    private RoleDTO role;

    @NotNull
    private Gender gender;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
        checkConfirmPassword();
    }

    public String getConfirmPassWord() {
        return confirmPassWord;
    }

    public void setConfirmPassWord(String confirmPassWord) {
        this.confirmPassWord = confirmPassWord;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if (this.passWord == null || this.confirmPassWord == null) {
            return;
        } else if (!this.passWord.equals(this.confirmPassWord)) {
            this.confirmPassWord = null;
        }
    }



}
