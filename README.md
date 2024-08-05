# Projeto Ajudae

## Descrição

O Ajudae é uma aplicação para gerenciar projetos de financiamento coletivo. Ele permite criar e apoiar projetos, além de gerenciar contas associadas a usuários e projetos. A aplicação também garante que um projeto é automaticamente excluído da base de dados quando sua meta é atingida e que um usuário só pode ser excluído da base de dados se não tiver projetos vinculados a ele e sua conta não possuir saldo.

## Funcionalidades

- **Usuário:**
  - Criar usuário com conta associada
  - Listar usuários
  - Consulta saldo do usuário
  - Transações em conta (depósito e saque)
  - Deletar usuário (somente se não tiver projetos vinculados e o saldo da conta for igual a zero)

- **Projeto:**
  - Criar projeto
  - Listar projetos
  - Apoiar projeto (transfere valor da conta do usuário que deseja patrocinar um projeto para a conta do projeto e, de forma automática e simultânea, para a conta do criador do projeto)
  - Deletar projeto (automaticamente quando a meta é atingida)

## Endpoints

### Usuário

- **Criar Usuário:**
  - **URL:** `/usuarios`
  - **Método:** `POST`
  - **Body:**
    ```json
    {
      "nome": "Gandalf the Wise",
      "cpf": "32860684549",
      "dataNascimento": "1981-10-14",
      "email": "gandalfwise@sauron.com"
    }
    ```

- **Listar Usuários:**
  - **URL:** `/usuarios`
  - **Método:** `GET`

- **Consultar Saldo:**
  - **URL:** `/usuarios/saldo`
  - **Método:** `POST`
  - **Body:**
    ```json
    {
      "usuarioId": 1
    }
    ```

- **Deletar Usuário:**
  - **URL:** `/usuarios/deletar`
  - **Método:** `DELETE`
  - **Body:**
    ```json
    {
      "usuarioId": 1
    }
    ```

### Projeto

- **Criar Projeto:**
  - **URL:** `/projetos`
  - **Método:** `POST`
  - **Body:**
    ```json
    {
      "nome": "Reunião de Formação da Sociedade do Anel",
      "descricao": "Arrecadação de fundos para financiar o primeiro encontro de raças da Terra Média unidas para combater Saruman",
      "meta": 15000.00,
      "usuarioId": 1
    }
    ```

- **Listar Projetos:**
  - **URL:** `/projetos`
  - **Método:** `GET`

- **Apoiar Projeto:**
  - **URL:** `/projetos/apoiar`
  - **Método:** `POST`
  - **Body:**
    ```json
    {
      "usuarioId": 1,
      "projetoId": 1,
      "valor": 10000.0
    }
    ```
    
### Conta

- **Depositar:**
  - **URL:** `/contas/depositar`
  - **Método:** `POST`
  - **Body:**
    ```json
    {
      "contaId": 1,
      "valor": 1000.0
    }
    ```

- **Sacar:**
  - **URL:** `/contas/sacar`
  - **Método:** `POST`
  - **Body:**
    ```json
    {
      "contaId": 1,
      "valor": 500.0
    }
    ```
    

## Estrutura do Projeto

  - `controller/`: Contém os controladores da aplicação que lidam com as requisições HTTP.
  - `dao/`: Contém os Data Access Objects que se comunicam com o banco de dados.
  - `dto/`: Contém os Data Transfer Objects usados para transferir dados entre as camadas.
  - `exception/`: Contém as classes de exceção personalizadas.
  - `model/`: Contém as classes de modelo que representam as entidades do banco de dados.
  - `usecase/`: Contém os serviços que implementam a lógica de negócio.
  - `utils/`: Contém as classes utilitárias para validações e outras funções auxiliares.

## Configuração do Banco de Dados

- **Tabelas:**
  - `usuario`: id, nome, cpf, data_nascimento, email, conta_id
  - `projeto`: id, nome, descricao, meta, usuario_id, conta_id
  - `conta`: id, numero, saldo, usuario_id, projeto_id

- **Chaves Estrangeiras:**
  - `conta.usuario_id` referencia `usuario.id` com `ON DELETE CASCADE`
  - `conta.projeto_id` referencia `projeto.id` com `ON DELETE CASCADE`
  - `projeto.usuario_id` referencia `usuario.id` com `ON DELETE CASCADE`
  - `projeto.conta_id` referencia `conta.id` com `ON DELETE CASCADE`

