CREATE TABLE IF NOT EXISTS s336189.users
(
    id SERIAL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    enabled           BOOLEAN      NOT NULL,
    password          VARCHAR(255) NOT NULL,
    verification_code VARCHAR(64),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS s336189.hits
(
    id SERIAL,
    user_id INTEGER NOT NULL,
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

CREATE TABLE IF NOT EXISTS s336189.roles
(
    id SERIAL,
    name            VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS s336189.roles_users_relation
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT fk_roles_users_relation_user_id
        FOREIGN KEY (user_id)
            REFERENCES s336189.users(id),
    CONSTRAINT fk_roles_users_relation_role_id
        FOREIGN KEY (role_id)
            REFERENCES s336189.roles(id),
    UNIQUE (user_id, role_id)
);

