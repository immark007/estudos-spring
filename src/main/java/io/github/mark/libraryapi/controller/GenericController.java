package io.github.mark.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {
    default URI gerarHeaderLocation(UUID id){
        return  ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id) // Agora o ID será preenchido corretamente.
                .toUri();
    }
}
