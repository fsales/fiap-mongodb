package br.com.fosales.springblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Data
public class Autor implements Serializable {

    @Id
    private String codigo;

    private String nome;

    private String biografia;

    private String imagem;
}
