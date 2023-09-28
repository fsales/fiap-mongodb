package br.com.fosales.springblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;


@Document // anotação do mongo
@Data
public class Artigo implements Serializable {

    @Id
    private String codigo;

    private String titulo;

    private LocalDateTime data;

    private String texto;

    private String url;

    private Integer status;

    @DBRef
    private Autor autor;
}
