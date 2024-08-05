CREATE TABLE conta (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(100) NOT NULL UNIQUE,
    saldo NUMERIC(15, 2) NOT NULL
);

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    conta_id INTEGER NOT NULL,
    FOREIGN KEY (conta_id) REFERENCES conta(id)
);

CREATE TABLE projeto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    meta NUMERIC(15, 2) NOT NULL,
    usuario_id INTEGER NOT NULL,
    conta_id INTEGER NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (conta_id) REFERENCES conta(id)
);

----------------------------------------------------------------------------------------------------------
FUNÇÕES E PROCEDIMENTOS PARA CONTA

-- Função para inserir uma nova conta
CREATE OR REPLACE FUNCTION inserir_conta(p_numero VARCHAR, p_saldo NUMERIC)
RETURNS INTEGER AS $$
DECLARE
    v_conta_id INTEGER;
BEGIN
    INSERT INTO conta (numero, saldo) VALUES (p_numero, p_saldo) RETURNING id INTO v_conta_id;
    RETURN v_conta_id;
END;
$$ LANGUAGE plpgsql;

-- Função para atualizar uma conta existente
CREATE OR REPLACE FUNCTION atualizar_conta(p_conta_id INTEGER, p_saldo NUMERIC)
RETURNS VOID AS $$
BEGIN
    UPDATE conta SET saldo = p_saldo WHERE id = p_conta_id;
END;
$$ LANGUAGE plpgsql;

-- Função para buscar uma conta pelo ID
CREATE OR REPLACE FUNCTION buscar_conta_por_id(p_conta_id INTEGER)
RETURNS TABLE(id INTEGER, numero VARCHAR, saldo NUMERIC) AS $$
BEGIN
    RETURN QUERY SELECT id, numero, saldo FROM conta WHERE id = p_conta_id;
END;
$$ LANGUAGE plpgsql;

--------------------------------------------------------------------------------------------------------
FUNÇÕES E PROCEDIMENTOS PARA USUÁRIO

-- Função para inserir um novo usuário
CREATE OR REPLACE FUNCTION inserir_usuario(p_nome VARCHAR, p_cpf VARCHAR, p_data_nascimento DATE, p_email VARCHAR, p_conta_id INTEGER)
RETURNS INTEGER AS $$
DECLARE
    v_usuario_id INTEGER;
BEGIN
    INSERT INTO usuario (nome, cpf, data_nascimento, email, conta_id) VALUES (p_nome, p_cpf, p_data_nascimento, p_email, p_conta_id) RETURNING id INTO v_usuario_id;
    RETURN v_usuario_id;
END;
$$ LANGUAGE plpgsql;

-- Função para atualizar um usuário existente
CREATE OR REPLACE FUNCTION atualizar_usuario(p_usuario_id INTEGER, p_nome VARCHAR, p_cpf VARCHAR, p_data_nascimento DATE, p_email VARCHAR)
RETURNS VOID AS $$
BEGIN
    UPDATE usuario SET nome = p_nome, cpf = p_cpf, data_nascimento = p_data_nascimento, email = p_email WHERE id = p_usuario_id;
END;
$$ LANGUAGE plpgsql;

-- Função para buscar um usuário pelo ID
CREATE OR REPLACE FUNCTION buscar_usuario_por_id(p_usuario_id INTEGER)
RETURNS TABLE(id INTEGER, nome VARCHAR, cpf VARCHAR, data_nascimento DATE, email VARCHAR, conta_id INTEGER) AS $$
BEGIN
    RETURN QUERY SELECT id, nome, cpf, data_nascimento, email, conta_id FROM usuario WHERE id = p_usuario_id;
END;
$$ LANGUAGE plpgsql;

-----------------------------------------------------------------------------------------------------------------
FUNÇÕES E PROCEDIMENTOS PARA PROJETO

-- Função para inserir um novo projeto
CREATE OR REPLACE FUNCTION inserir_projeto(p_nome VARCHAR, p_descricao TEXT, p_meta NUMERIC, p_usuario_id INTEGER, p_conta_id INTEGER)
RETURNS INTEGER AS $$
DECLARE
    v_projeto_id INTEGER;
BEGIN
    INSERT INTO projeto (nome, descricao, meta, usuario_id, conta_id) VALUES (p_nome, p_descricao, p_meta, p_usuario_id, p_conta_id) RETURNING id INTO v_projeto_id;
    RETURN v_projeto_id;
END;
$$ LANGUAGE plpgsql;

-- Função para atualizar um projeto existente
CREATE OR REPLACE FUNCTION atualizar_projeto(p_projeto_id INTEGER, p_nome VARCHAR, p_descricao TEXT, p_meta NUMERIC)
RETURNS VOID AS $$
BEGIN
    UPDATE projeto SET nome = p_nome, descricao = p_descricao, meta = p_meta WHERE id = p_projeto_id;
END;
$$ LANGUAGE plpgsql;

-- Função para buscar um projeto pelo ID
CREATE OR REPLACE FUNCTION buscar_projeto_por_id(p_projeto_id INTEGER)
RETURNS TABLE(id INTEGER, nome VARCHAR, descricao TEXT, meta NUMERIC, usuario_id INTEGER, conta_id INTEGER) AS $$
BEGIN
    RETURN QUERY SELECT id, nome, descricao, meta, usuario_id, conta_id FROM projeto WHERE id = p_projeto_id;
END;
$$ LANGUAGE plpgsql;

