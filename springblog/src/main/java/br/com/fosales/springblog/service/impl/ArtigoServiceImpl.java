package br.com.fosales.springblog.service.impl;

import br.com.fosales.springblog.model.Artigo;
import br.com.fosales.springblog.model.ArtigoStatusCount;
import br.com.fosales.springblog.model.Autor;
import br.com.fosales.springblog.model.AutorTotalArtigo;
import br.com.fosales.springblog.repository.ArtigoRepository;
import br.com.fosales.springblog.repository.AutorRepository;
import br.com.fosales.springblog.service.ArtigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    @Override
    public Artigo obterPorCodigo(String codigo) {

        return artigoRepository
                .findById(codigo)
                .orElseThrow(
                        () -> new IllegalArgumentException("Artigo não existe!")
                );
    }

    @Transactional
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

        Artigo artigoRetorno = null;
        try {
            artigoRetorno = artigoRepository.save(artigo);
        } catch (OptimisticLockingFailureException ex) {

            // desenvolver a estratégia

            // 1. recuperar o documento mais recente do banco de dados(coleção Artigo)
            Artigo atualizado = artigoRepository
                    .findById(artigo.getCodigo())
                    .orElse(null);

            if (atualizado != null) {
                // atualizar os campos desejados
                atualizado.setTitulo(artigo.getTitulo());
                atualizado.setTexto(artigo.getTexto());
                atualizado.setStatus(artigo.getStatus());

                // incrementar a versão
                atualizado.setVersion(atualizado.getVersion() + 1);

                // tentar salvar
                artigoRetorno = artigoRepository.save(atualizado);
            } else {
                // se o documento não for encontrado tratar o erro
                throw new RuntimeException("Artigo não encontrado. codigo: " + artigo.getCodigo());
            }
        }

        return artigoRetorno;
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

    @Transactional
    @Override
    public Artigo atualizar(Artigo artigo) {
        return artigoRepository.save(artigo);
    }

    @Transactional
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

    @Transactional
    @Override
    public void deleteById(String id) {
        artigoRepository.deleteById(id);
    }

    @Transactional
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

    public List<AutorTotalArtigo> calularTotalArtigosAutorPorPeriodo(LocalDate dataInicio,
                                                                     LocalDate dataFim) {
        TypedAggregation<Artigo> aggregation =
                Aggregation
                        .newAggregation(
                                Artigo.class,
                                Aggregation
                                        .match(
                                                Criteria
                                                        .where("data").gte(
                                                                dataInicio.atStartOfDay()
                                                        )
                                                        .lt(
                                                                dataFim.plusDays(1).atStartOfDay()
                                                        )
                                        ),
                                Aggregation
                                        .group("autor")
                                        .count()
                                        .as("totalArtigos"),
                                Aggregation
                                        .project("totalArtigos")
                                        .and("autor")
                                        .previousOperation()

                        );

        AggregationResults<AutorTotalArtigo> result =
                mongoTemplate.aggregate(
                        aggregation,
                        AutorTotalArtigo.class
                );

        return result.getMappedResults();
    }

}
