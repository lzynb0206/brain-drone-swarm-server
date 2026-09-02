CREATE TABLE app_users (
    id UUID PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    display_name VARCHAR(80) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(32) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_app_users_role CHECK (
        role IN ('SOLDIER', 'COMMANDER', 'RESEARCHER', 'ENGINEER', 'ADMIN')
    )
);

CREATE INDEX idx_app_users_email ON app_users (email);
