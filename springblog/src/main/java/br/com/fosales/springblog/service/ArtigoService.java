package br.com.fosales.springblog.service;

import br.com.fosales.springblog.model.Artigo;
import br.com.fosales.springblog.model.ArtigoStatusCount;
import br.com.fosales.springblog.model.AutorTotalArtigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    /**
     * @return
     */
    List<Artigo> obterTodos();

    /**
     * @param codigo
     * @return
     */
    Artigo obterPorCodigo(String codigo);


    /**
     * @param artigo
     * @return
     */
    Artigo criar(Artigo artigo);

    List<Artigo> findByDataGreaterThan(LocalDateTime data);

    List<Artigo> findByDataAndStatus(
            LocalDateTime data,
            Integer status
    );

    Artigo atualizar(Artigo artigo);

    void atualizar(String codigo, String novaUrl);

    void deleteById(String id);

    void deleteArtigoById(String id);

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);

    List<Artigo> encontrarArtigoComplexo(Integer status,
                                         LocalDateTime data,
                                         String titulo);

    Page<Artigo> listaArtigos(Pageable pageable);

    List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status);

    List<Artigo> findByTexto(String searchTerm);

    List<ArtigoStatusCount> contarArtigosPorStatus();

    List<AutorTotalArtigo> calularTotalArtigosAutorPorPeriodo(LocalDate dataInicio,
                                                              LocalDate dataFim);
}
