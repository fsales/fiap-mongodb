package br.com.fosales.springblog.controller;

import br.com.fosales.springblog.model.Autor;
import br.com.fosales.springblog.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping("/{codigo}")
    public ResponseEntity<Autor> obterPorCodigo(
            @PathVariable String codigo
    ) {

        return ResponseEntity.ok(
                autorService.obterPorCodigo(
                        codigo
                )
        );
    }

    @PostMapping
    public ResponseEntity<Autor> criar(
            @RequestBody Autor autor,
            UriComponentsBuilder uriComponentsBuilder
    ) {

        var autorSalvo = autorService.criar(
                autor
        );

        var uri = uriComponentsBuilder
                .path("/autores/{codigo}")
                .buildAndExpand(
                        autorSalvo.getCodigo()
                )
                .toUri();
        return ResponseEntity
                .created(
                        uri
                )
                .body(
                        autorSalvo
                );
    }
}
