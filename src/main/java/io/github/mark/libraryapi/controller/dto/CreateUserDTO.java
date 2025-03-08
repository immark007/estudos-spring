package io.github.mark.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;

import java.util.List;

public record CreateUserDTO(String login, @Email String email, String senha, List<String> roles) {
}
