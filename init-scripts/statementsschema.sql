CREATE TABLE Statement (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    transactionId UUID NOT NULL,                        -- Identificador da transação
    amount DECIMAL(18,2) NOT NULL,                      -- Valor da transação
    accountNumber INT NOT NULL,                         -- Número da conta associada
    accountAgency INT NOT NULL,                         -- Agência da conta associada
    referenceDate TIMESTAMP with time zone NOT NULL,    -- Data de referência da transação
    description VARCHAR(255) NOT NULL,                  -- Descrição da transação
    operationType VARCHAR(64) NOT NULL,                 -- Tipo da operação
    balance DECIMAL(18,2) NOT NULL,                     -- Saldo atual 
    CONSTRAINT "PK_Statement" PRIMARY KEY (Id),
    CONSTRAINT "UQ_Statement" UNIQUE (transactionId, accountNumber, accountAgency)
);

CREATE INDEX idx_statement_account_time 
ON Statement ("accountnumber", "accountagency", "referencedate");
