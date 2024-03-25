CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    tenantId  VARCHAR(255),
    firstName VARCHAR(255)        NOT NULL,
    lastName  VARCHAR(255),
    document   VARCHAR(255) UNIQUE,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255),
    type       VARCHAR(50),
    balance    DECIMAL(19, 2) DEFAULT 0,
    createdAt TIMESTAMPTZ    DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMPTZ    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions
(
    id         BIGSERIAL PRIMARY KEY,
    buyerId   BIGINT,
    sellerId  BIGINT,
    amount     DECIMAL(19, 2),
    createdAt TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN     DEFAULT false,
    FOREIGN KEY (buyerId) REFERENCES users (id),
    FOREIGN KEY (sellerId) REFERENCES users (id)
);



drop table transactions;
drop table users;

