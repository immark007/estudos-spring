package io.github.mark.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

//Aqui eu criei um dto para passar os Erros de Resposta, passando um int, que vai ser o número do código de status
//A mensagem do erro, e uma lista com os possíveis erros
public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {

    //Aqui vou passar as duas opções de erro dos meus métodos, um é o BadRequest
    public static ErroResposta erroResposta(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }
    //O outro é o conflict
    public static ErroResposta conflito(String mensagem) {
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }

    public static ErroResposta BadRequest(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }
}
