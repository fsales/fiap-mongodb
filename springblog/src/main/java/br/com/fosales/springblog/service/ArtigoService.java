package br.com.fosales.springblog.service;

import br.com.fosales.springblog.model.Artigo;

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
}
