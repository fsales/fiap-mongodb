package br.com.fosales.springblog.service.impl;

import br.com.fosales.springblog.model.Artigo;
import br.com.fosales.springblog.model.ArtigoStatusCount;
import br.com.fosales.springblog.model.Autor;
import br.com.fosales.springblog.repository.ArtigoRepository;
import br.com.fosales.springblog.repository.AutorRepository;
import br.com.fosales.springblog.service.ArtigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
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

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return artigoRepository.obeterArtigoPorDataHora(de, ate);
    }

    @Override
    public List<Artigo> encontrarArtigoComplexo(Integer status, LocalDateTime data, String titulo) {

        Criteria criteria = new Criteria();
        criteria.and("data").lte(data);

        if (status != null)
            criteria.and("status").is(status);

        if (titulo != null && !titulo.isBlank())
            //regex para ingnorar case sensitive (regex(titulo, "i"))
            criteria.and("titulo").regex(titulo, "i");

        Query query = new Query(criteria);

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Page<Artigo> listaArtigos(Pageable pageable) {
        Sort sort = Sort
                .by("titulo")
                .ascending();
        Pageable paginacao = PageRequest
                .of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        sort
                );


        return artigoRepository.findAll(paginacao);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status) {
        return artigoRepository.obterArtigoPorStatusComOrdenacao(status);
    }

    @Override
    public List<Artigo> findByTexto(String searchTerm) {
        // requisistos: adicinar a anotaçaõ @TextIndexed e criar index (db.artigo.createIndex({texto:"text"})).
        TextCriteria criteria = TextCriteria
                .forDefaultLanguage()
                .matchingPhrase(searchTerm);

        Query query = TextQuery
                .queryText(criteria)
                .sortByScore();

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {

        TypedAggregation<Artigo> aggregation =
                Aggregation
                        .newAggregation(
                                Artigo.class,
                                Aggregation
                                        .group("status")
                                        .count()
                                        .as("quantidade"),
                                Aggregation
                                        .project("quantidade")
                                        .and("status")
                                        .previousOperation()
                        );
        AggregationResults<ArtigoStatusCount> result =
                mongoTemplate.aggregate(
                        aggregation,
                        ArtigoStatusCount.class
                );

        return result.getMappedResults();
    }

}
