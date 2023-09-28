package br.com.fosales.springblog.service.impl;

import br.com.fosales.springblog.model.Autor;
import br.com.fosales.springblog.repository.AutorRepository;
import br.com.fosales.springblog.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    @Override
    public Autor obterPorCodigo(String codigo) {
        return autorRepository
                .findById(codigo)
                .orElseThrow(
                        () -> new IllegalArgumentException("Autor não existe!")
                );
    }

    @Override
    public Autor criar(Autor autor) {
        return autorRepository.save(
                autor
        );
    }
}
