package br.com.fosales.springblog.repository;

import br.com.fosales.springblog.model.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AutorRepository extends MongoRepository<Autor, String> {
}
