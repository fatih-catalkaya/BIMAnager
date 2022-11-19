CREATE TABLE room (
    room_id TEXT,
    room_name TEXT UNIQUE NOT NULL,
    PRIMARY KEY(room_id)
);

CREATE TABLE person (
    person_id TEXT,
    person_name TEXT NOT NULL,
    person_email TEXT NOT NULL UNIQUE ,
    person_password TEXT NOT NULL,
    person_room_name TEXT,
    person_is_administrator INT NOT NULL,
    PRIMARY KEY (person_id)
);

CREATE TABLE product (
    product_id TEXT,
    product_name TEXT,
    product_price REAL,
    PRIMARY KEY (product_id)
);

CREATE TABLE purchase (
    purchase_id TEXT,
    purchase_datetime TEXT NOT NULL,
    purchase_product_name TEXT NOT NULL,
    purchase_product_price REAL NOT NULL,
    purchase_product_amount INT NOT NULL,
    PRIMARY KEY (purchase_id)
);

CREATE TABLE deposit (
    deposit_id TEXT,
    deposit_datetime TEXT NOT NULL,
    deposit_amount REAL NOT NULL,
    deposit_method TEXT,
    PRIMARY KEY (deposit_id)
);

CREATE TABLE session (
    session_id TEXT,
    session_auth_key TEXT NOT NULL,
    session_person_id TEXT NOT NULL,
    session_start_datetime TEXT NOT NULL,
    PRIMARY KEY (session_id)
);

-- INSERT DEFAULT ADMINISTRATOR
INSERT INTO person VALUES ('c893a566-4470-4e94-a6f3-607c16753c3a', 'Administrator', 'admin@istrator.de', 'admin123', NULL, 1);