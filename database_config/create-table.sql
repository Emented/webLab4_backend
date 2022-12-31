CREATE TABLE IF NOT EXISTS s336189.users
(
    id SERIAL,
    email             VARCHAR(255) NOT NULL UNIQUE ,
    enabled           BOOLEAN      NOT NULL,
    password          VARCHAR(255) NOT NULL,
    verification_code VARCHAR(64),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS s336189.hits (
    id SERIAL,
    user_id INTEGER NOT NULL ,
    x DOUBLE PRECISION NOT NULL,
    y DOUBLE PRECISION NOT NULL,
    r DOUBLE PRECISION NOT NULL,
    check_date TIMESTAMP WITH TIME ZONE NOT NULL,
    execution_time BIGINT NOT NULL,
    status BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES s336189.users(id)
);

