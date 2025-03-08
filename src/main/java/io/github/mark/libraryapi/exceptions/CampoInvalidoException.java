package io.github.mark.libraryapi.exceptions;

import lombok.Getter;

public class CampoInvalidoException extends RuntimeException {
    @Getter
    String campo;
    public CampoInvalidoException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }
}
