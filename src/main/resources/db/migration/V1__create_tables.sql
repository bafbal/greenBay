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
    item_name         VARCHAR(255) NOT NULL,
    description       VARCHAR(255) NOT NULL,
    photo_url         VARCHAR(255) NOT NULL,
    start_price       BIGINT NOT NULL,
    purchase_price    BIGINT,
    buyer_id          BIGINT,
    seller_id         BIGINT NOT NULL,
    last_bid          BIGINT,
    sold              BIT
);