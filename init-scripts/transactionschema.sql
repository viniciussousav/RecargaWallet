CREATE TABLE Credit (
    id UUID PRIMARY KEY,                                -- Identificador único do crédito (UUID)
    amount DECIMAL(18,2) NOT NULL,                      -- Valor do crédito
    accountNumber INT NOT NULL,                         -- Número da conta associada
    accountAgency INT NOT NULL,                         -- Agência da conta associada
    referenceDate TIMESTAMP with time zone NOT NULL,    -- Data de referência do crédito
    description VARCHAR(255),                           -- Descrição do crédito
    status VARCHAR(64)
);

CREATE TABLE Debit (
    id UUID PRIMARY KEY,                                -- Identificador único do crédito (UUID)
    amount DECIMAL(18,2) NOT NULL,                      -- Valor do crédito
    accountNumber INT NOT NULL,                         -- Número da conta associada
    accountAgency INT NOT NULL,                         -- Agência da conta associada
    referenceDate TIMESTAMP with time zone NOT NULL,    -- Data de referência do crédito
    description VARCHAR(255),                           -- Descrição do crédito
    status VARCHAR(64)
);

CREATE TABLE Transfer (
    id UUID PRIMARY KEY,                                -- Identificador único da transferência (UUID)
    creditid UUID NOT NULL,                             -- Chave estrangeira referenciando a tabela Credit
    debitid UUID NOT NULL,                              -- Chave estrangeira referenciando a tabela Debit
    amount DECIMAL(18,2) NOT NULL,                      -- Valor da transferência
    referencedate TIMESTAMP with time zone NOT NULL,    -- Data de referência da transferência
    status VARCHAR(64),
    CONSTRAINT fk_credit FOREIGN KEY (creditid) REFERENCES Credit (id) ON DELETE CASCADE,
    CONSTRAINT fk_debit FOREIGN KEY (debitid) REFERENCES Debit (id) ON DELETE CASCADE
);