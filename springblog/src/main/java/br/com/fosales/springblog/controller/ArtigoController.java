package br.com.fosales.springblog.controller;

import br.com.fosales.springblog.model.Artigo;
import br.com.fosales.springblog.model.ArtigoStatusCount;
import br.com.fosales.springblog.service.ArtigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    private final ArtigoService artigoService;

    @GetMapping
    public ResponseEntity<List<Artigo>> obterTodos(

    ) {

        return ResponseEntity.ok(
                artigoService.obterTodos()
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Artigo> obterPorCodigo(
            @PathVariable String codigo
    ) {

        return ResponseEntity.ok(
                artigoService.obterPorCodigo(
                        codigo
                )
        );
    }

    @PostMapping
    public ResponseEntity<Artigo> criar(
            @RequestBody Artigo artigo,
            UriComponentsBuilder uriComponentsBuilder
    ) {

        var artigoSalvo = artigoService.criar(
                artigo
        );

        var uri = uriComponentsBuilder
                .path("/artigos/{codigo}")
                .buildAndExpand(
                        artigoSalvo.getCodigo()
                )
                .toUri();
        return ResponseEntity
                .created(
                        uri
                )
                .body(
                        artigoSalvo
                );
    }

    @PutMapping
    public ResponseEntity<Artigo> atualizar(
            @RequestBody Artigo artigo
    ) {

        var artigoSalvo = artigoService.atualizar(
                artigo
        );

        return ResponseEntity
                .ok(
                        artigoSalvo
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUrl(
            @PathVariable String id,
            @RequestBody String novaUrl
    ) {

        artigoService.atualizar(
                id,
                novaUrl
        );

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/maior-data")
    public ResponseEntity<List<Artigo>> findByDataGreaterThan(
            @RequestParam("data") LocalDateTime data
    ) {
        return ResponseEntity.ok(
                artigoService.findByDataGreaterThan(
                        data
                )
        );
    }

    @GetMapping("/maior-data-status")
    public ResponseEntity<List<Artigo>> findByDataAndStatus(
            @RequestParam("data") LocalDateTime data,
            @RequestParam("status") Integer status
    ) {
        return ResponseEntity.ok(
                artigoService.findByDataAndStatus(
                        data,
                        status
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {
        artigoService
                .deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteArtigoById(
            @PathVariable String id
    ) {
        artigoService
                .deleteArtigoById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/status-maior-data")
    public ResponseEntity<List<Artigo>> findByStatusAndDataGreaterThan(
            @RequestParam("status") Integer status,
            @RequestParam("data") LocalDateTime data
    ) {

        return ResponseEntity.ok(
                artigoService.findByStatusAndDataGreaterThan(
                        status,
                        data
                )
        );
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Artigo>> obterArtigoPorDataHora(
            @RequestParam("de") LocalDateTime de,
            @RequestParam("ate") LocalDateTime ate
    ) {
        return ResponseEntity.ok(
                artigoService.obterArtigoPorDataHora(
                        de,
                        ate
                )
        );
    }

    @GetMapping("/artigo-complexo")
    public ResponseEntity<List<Artigo>> encontrarArtigoComplexo(
            @RequestParam("status") Integer status,
            @RequestParam("data") LocalDateTime data,
            @RequestParam("titulo") String titulo) {

        return ResponseEntity.ok(
                artigoService.encontrarArtigoComplexo(
                        status,
                        data,
                        titulo
                )
        );
    }

    @GetMapping("/pagina-artigo")
    public ResponseEntity<Page<Artigo>> listaArtigos(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(
                artigoService.listaArtigos(
                        pageable
                )
        );
    }

    @GetMapping("/status-ordenado")
    public ResponseEntity<List<Artigo>> findByStatusOrderByTituloAsc(
            @RequestParam("status") Integer status
    ) {

        return ResponseEntity.ok(
                artigoService.findByStatusOrderByTituloAsc(
                        status
                )
        );
    }

    @GetMapping("/status-query-ordenado")
    public ResponseEntity<List<Artigo>> obterArtigoPorStatusComOrdenacao(
            @RequestParam("status") Integer status
    ) {

        return ResponseEntity.ok(
                artigoService.obterArtigoPorStatusComOrdenacao(
                        status
                )
        );
    }

    @GetMapping("/busca-texto")
    public ResponseEntity<List<Artigo>> findByTexto(
            @RequestParam("searchTerm") String searchTerm
    ) {

        return ResponseEntity.ok(
                artigoService.findByTexto(
                        searchTerm
                )
        );
    }

    @GetMapping("/aggregation-contar-artigo")
    public ResponseEntity<List<ArtigoStatusCount>> contarArtigosPorStatus() {

        return ResponseEntity.ok(
                artigoService.contarArtigosPorStatus()
        );
    }
}
