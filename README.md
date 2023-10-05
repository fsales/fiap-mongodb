# Aula de MongoDB

## Entrar no mongoshel

```shell
mongosh
```

## Trocar Banco

```shell
use workshop
```

## Criar coleção

```shell
db.createCollection("customers")
```

## Listar coleção

```shell
show collections
```

## Verificar quantidade de documentos

```shell
db.customers.countDocuments()
```

## Listar documentos

```shell
db.customers.find({})
```

## Insert documentos

```shell
db.customers.insertOne({
    nome: "João",
    idade: 46
})
```

## buscar documentos

```shell
db.customers.find({}, {"_id":0}).pretty()
```

## Atualizar documento

```shell
db.customers.replaceOne(
{
    nome: "João"
},
{
    nome: "João",
    idade: 46,
    uf: "DF"
})
```

## Listar colleções

```shell
show collections
```

## operador

> $unset = remove a field

> $inc = faz incremento do valor

> $push = inclusão de itens de documentos dentro de um array de um documento.

> $pull = exclusão de itens de documentos dentro de um array de um documento.

> $unset = remover as field de um documento


## Habilitar suporte a transações

1. adicinar no pom.xml

> spring-boot-starter-data-mongodb-reactive

2. adicionar no model a versão

```java
    @Version
    private Long version;
```
3. adicionar o anotação @Transactional

4. apartir da versão 4.0 o mongodb suporta transações para banco de dados duplicados.