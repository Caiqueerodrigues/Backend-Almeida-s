# Almeidas Cortes — Backend

Backend da aplicação Almeidas Cortes, desenvolvido com Java e Spring Boot.

## Executar o projeto

No terminal, acesse a pasta do backend e execute:

```bash
mvn spring-boot:run
```

Esse comando utiliza o Maven para:

- baixar as dependências necessárias, caso ainda não estejam disponíveis;
- compilar o código da aplicação;
- executar as migrations pendentes do Flyway;
- iniciar o servidor Spring Boot local.

Por padrão, a API fica disponível em:

```text
http://localhost:3003
```

O backend utiliza as configurações de banco definidas nas variáveis de ambiente. No ambiente local, ele deve apontar para o MySQL em `localhost:3306` e para o schema `almeidas_cortes`.

Para encerrar a aplicação, pressione `Ctrl+C` no terminal em que ela está sendo executada.
