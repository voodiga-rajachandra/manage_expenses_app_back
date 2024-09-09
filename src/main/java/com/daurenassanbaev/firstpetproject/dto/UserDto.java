package com.daurenassanbaev.firstpetproject.dto;

import com.daurenassanbaev.firstpetproject.validation.group.CreateAction;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Username (email) is required")
    private String username;

    @NotBlank(groups = CreateAction.class, message = "Password is required when creating a user")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String rawPassword;

    private List<AccountDto> accounts;
}
