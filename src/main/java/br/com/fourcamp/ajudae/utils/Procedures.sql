----- CRIAÇÃO DE USUÁRIO COM CONTA -----
CREATE OR REPLACE PROCEDURE criar_usuario_com_conta(
    IN p_nome VARCHAR,
    IN p_cpf VARCHAR,
    IN p_data_nascimento DATE,
    IN p_email VARCHAR,
    IN p_numero_conta VARCHAR,
    IN p_saldo_conta NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_conta_id INTEGER;
    v_usuario_id INTEGER;
BEGIN
    -- Inserir Conta
    INSERT INTO conta (numero, saldo) VALUES (p_numero_conta, p_saldo_conta) RETURNING id INTO v_conta_id;

    -- Inserir Usuario
    INSERT INTO usuario (nome, cpf, data_nascimento, email, conta_id) VALUES (p_nome, p_cpf, p_data_nascimento, p_email, v_conta_id) RETURNING id INTO v_usuario_id;

    -- Retornar IDs
    RETURN;
END;
$$;

----- TRANSFERIR FUNDOS ENTRE CONTAS -----
CREATE OR REPLACE PROCEDURE transferir_fundos(
    IN p_conta_origem_id INTEGER,
    IN p_conta_destino_id INTEGER,
    IN p_valor NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_saldo_origem NUMERIC;
    v_saldo_destino NUMERIC;
BEGIN
    -- Obter saldo atual da conta de origem
    SELECT saldo INTO v_saldo_origem FROM conta WHERE id = p_conta_origem_id;

    -- Verificar se há saldo suficiente
    IF v_saldo_origem < p_valor THEN
        RAISE EXCEPTION 'Saldo insuficiente na conta de origem';
    END IF;

    -- Obter saldo atual da conta de destino
    SELECT saldo INTO v_saldo_destino FROM conta WHERE id = p_conta_destino_id;

    -- Atualizar saldos
    UPDATE conta SET saldo = v_saldo_origem - p_valor WHERE id = p_conta_origem_id;
    UPDATE conta SET saldo = v_saldo_destino + p_valor WHERE id = p_conta_destino_id;
END;
$$;


----- CRIAR PROJETO COM CONTA ASSOCIADA AO USUÁRIO -----
CREATE OR REPLACE PROCEDURE criar_projeto_com_conta(
    IN p_nome_projeto VARCHAR,
    IN p_descricao TEXT,
    IN p_meta NUMERIC,
    IN p_usuario_id INTEGER,
    IN p_numero_conta VARCHAR,
    IN p_saldo_conta NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_conta_id INTEGER;
    v_projeto_id INTEGER;
BEGIN
    -- Inserir Conta
    INSERT INTO conta (numero, saldo) VALUES (p_numero_conta, p_saldo_conta) RETURNING id INTO v_conta_id;

    -- Inserir Projeto
    INSERT INTO projeto (nome, descricao, meta, usuario_id, conta_id) VALUES (p_nome_projeto, p_descricao, p_meta, p_usuario_id, v_conta_id) RETURNING id INTO v_projeto_id;

    -- Retornar IDs
    RETURN;
END;
$$;

----- DEPÓSITO EM CONTA -----
CREATE OR REPLACE PROCEDURE depositar(
    IN p_conta_id INTEGER,
    IN p_valor NUMERIC
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE conta
    SET saldo = saldo + p_valor
    WHERE id = p_conta_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Conta não encontrada';
    END IF;
END;
$$;


----- SAQUE EM CONTA -----
CREATE OR REPLACE PROCEDURE sacar(
    IN p_conta_id INTEGER,
    IN p_valor NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_saldo NUMERIC;
BEGIN
    -- Obter saldo atual da conta
    SELECT saldo INTO v_saldo FROM conta WHERE id = p_conta_id;

    -- Verificar se há saldo suficiente
    IF v_saldo < p_valor THEN
        RAISE EXCEPTION 'Saldo insuficiente';
    END IF;

    -- Atualizar saldo
    UPDATE conta
    SET saldo = saldo - p_valor
    WHERE id = p_conta_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Conta não encontrada';
    END IF;
END;
$$;


