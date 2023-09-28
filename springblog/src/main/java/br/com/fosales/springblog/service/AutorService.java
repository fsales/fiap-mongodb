package br.com.fosales.springblog.service;

import br.com.fosales.springblog.model.Autor;

public interface AutorService {
    Autor obterPorCodigo(String codigo);


    Autor criar(Autor autor);
}
