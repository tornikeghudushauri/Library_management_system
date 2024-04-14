package com.azry.library_management_system.dto;

import com.azry.library_management_system.model.ApplicationUser;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public ApplicationUserDTO(ApplicationUser applicationUser) {
        this.username =applicationUser.getUsername();
        this.password = applicationUser.getPassword();
    }
}