package io.github.mark.libraryapi.controller;

import io.github.mark.libraryapi.controller.dto.CreateUserDTO;
import io.github.mark.libraryapi.controller.mappers.UsuarioMapper;
import io.github.mark.libraryapi.model.Usuario;
import io.github.mark.libraryapi.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements GenericController{

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CreateUserDTO createUserDTO) {
        Usuario usuario = usuarioMapper.toEntity(createUserDTO);
        usuarioService.salvar(usuario);
        URI location = gerarHeaderLocation(usuario.getId());

        return ResponseEntity.created(location).build();

    }
}
