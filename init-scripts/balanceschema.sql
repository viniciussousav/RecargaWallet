CREATE TABLE Balance (
    Id BIGINT GENERATED ALWAYS AS IDENTITY,
    Amount DECIMAL(18,2) NOT NULL,
    AccountNumber INT NOT NULL,
    AccountAgency INT NOT NULL,
    OverdraftLimit DECIMAL (18,2) NOT NULL,
    CONSTRAINT "PK_Balance" PRIMARY KEY (Id),
    CONSTRAINT "UQ_Balance_Account" UNIQUE (AccountNumber, AccountAgency)
);