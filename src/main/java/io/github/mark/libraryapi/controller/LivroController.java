package io.github.mark.libraryapi.controller;

import io.github.mark.libraryapi.controller.dto.CadastroLivroRequest;
import io.github.mark.libraryapi.controller.dto.ResultadoPesquisaLivroRequest;
import io.github.mark.libraryapi.controller.mappers.LivroMapper;
import io.github.mark.libraryapi.model.GeneroLivro;
import io.github.mark.libraryapi.model.Livro;
import io.github.mark.libraryapi.services.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{
    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CadastroLivroRequest cadastroLivroRequest){
            Livro livro = livroMapper.toEntity(cadastroLivroRequest);
            livroService.salvar(livro);
            URI location = gerarHeaderLocation(livro.getId());

            return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaLivroRequest> obterDetalhes(@PathVariable UUID id){
        var idLivro = UUID.fromString(id.toString());

        return livroService.obterDetalhes(idLivro).map(livro -> {
            ResultadoPesquisaLivroRequest resultadoPesquisaLivroRequest = livroMapper.toResultadoPesquisaLivroRequest(livro);
            return ResponseEntity.ok(resultadoPesquisaLivroRequest);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id){
        return livroService.obterDetalhes(UUID.fromString(id)).map(livro -> {
            livroService.deletar(livro);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<ResultadoPesquisaLivroRequest>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nome-autor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false)GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false) Integer anoPulicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10") Integer tamanhoPagina
            ) {
        Page<Livro> paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPulicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroRequest> resultado =  paginaResultado.map((livroMapper::toResultadoPesquisaLivroRequest));

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroRequest cadastroLivroRequest){
        return livroService.obterDetalhes(UUID.fromString(id)).map(livro -> {
            Livro dto = livroMapper.toEntity(cadastroLivroRequest);
            livro.setIsbn(dto.getIsbn());
            livro.setTitulo(dto.getTitulo());
            livro.setDataPublicacao(dto.getDataPublicacao());
            livro.setGenero(dto.getGenero());
            livro.setPreco(dto.getPreco());
            livro.setAutor(dto.getAutor());
            livroService.atualizar(livro);

            return ResponseEntity.noContent().build();
        }).orElseGet( () -> ResponseEntity.notFound().build());
    }
}
