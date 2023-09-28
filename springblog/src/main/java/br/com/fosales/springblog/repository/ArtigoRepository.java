package br.com.fosales.springblog.repository;

import br.com.fosales.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {

    void deleteById(String id);

}
