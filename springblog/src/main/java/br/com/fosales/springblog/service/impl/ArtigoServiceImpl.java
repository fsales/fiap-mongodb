package br.com.fosales.springblog.service.impl;

import br.com.fosales.springblog.model.Artigo;
import br.com.fosales.springblog.model.Autor;
import br.com.fosales.springblog.repository.ArtigoRepository;
import br.com.fosales.springblog.repository.AutorRepository;
import br.com.fosales.springblog.service.ArtigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor

@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    private final ArtigoRepository artigoRepository;

    private final AutorRepository autorRepository;

    @Override
    public List<Artigo> obterTodos() {

        return artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {

        return artigoRepository
                .findById(codigo)
                .orElseThrow(
                        () -> new IllegalArgumentException("Artigo não existe!")
                );
    }

    @Override
    public Artigo criar(Artigo artigo) {

        if (Objects.nonNull(artigo.getAutor()) &&
            Objects.nonNull(artigo.getAutor().getCodigo())) {
            Autor autor = autorRepository
                    .findById(
                            artigo.getAutor().getCodigo()
                    )
                    .orElseThrow(
                            () -> new IllegalArgumentException("Autor inexistente!")
                    );
            artigo.setAutor(
                    autor
            );
        } else {
            artigo.setAutor(null);
        }


        return artigoRepository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        Query query = new Query(
                Criteria.where(
                        "data"
                ).gt(
                        data
                )
        );

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {

        Query query = new Query(
                Criteria.where(
                                "data"
                        ).is(
                                data
                        )
                        .and("status")
                        .is(
                                status
                        )
        );


        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Artigo atualizar(Artigo artigo) {
        return artigoRepository.save(artigo);
    }

    @Override
    public void atualizar(String codigo, String novaUrl) {
        Query query = new Query(
                Criteria.where(
                        "_id"
                ).is(
                        codigo
                )
        );

        Update update = new Update().set("url", novaUrl);

        mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public void deleteById(String id) {
        artigoRepository.deleteById(id);
    }

    @Override
    public void deleteArtigoById(String id) {
        Query query = new Query(
                Criteria.where(
                        "_id"
                ).is(
                        id
                )
        );

        mongoTemplate.remove(query, Artigo.class);
    }

}
