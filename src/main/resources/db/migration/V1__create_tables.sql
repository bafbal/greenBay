CREATE TABLE IF NOT EXISTS users
(
    Id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    username          VARCHAR(255) UNIQUE NOT NULL,
    password          VARCHAR(255) NOT NULL,
    balance           BIGINT NOT NULL
);
CREATE TABLE IF NOT EXISTS items
(
    Id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    description       VARCHAR(255) UNIQUE NOT NULL,
    photo_url         VARCHAR(255) NOT NULL,
    start_price       BIGINT NOT NULL,
    purchase_price    BIGINT,
    buyer_id          BIGINT,
    seller_id         BIGINT NOT NULL
    );