--liquibase formatted sql

--changeset Jules:1
--comment: Create inventory_items table
CREATE TABLE inventory_items (
    id BIGINT AUTO_INCREMENT NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT PK_INVENTORY_ITEMS PRIMARY KEY (id),
    CONSTRAINT UQ_INVENTORY_ITEMS_PRODUCT_ID UNIQUE (product_id)
);
