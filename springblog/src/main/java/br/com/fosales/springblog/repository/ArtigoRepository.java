package br.com.fosales.springblog.repository;

import br.com.fosales.springblog.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {

    void deleteById(String id);

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    List<Artigo> findByStatusEquals(Integer status);

    @Query("{$and: [{'data': {$gte: ?0 }}, {'data': {$lte: ?1}}]}")
    List<Artigo> obeterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);


    Page<Artigo> findAll(Pageable pageable);

    List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    @Query(value = "{ 'status': {$eq: ?0} }", sort = "{'titulo':  1}")
    List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status);

}
