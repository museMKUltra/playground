ALTER TABLE playground.products
    ADD `description` VARCHAR(255) NULL;

ALTER TABLE playground.products
    MODIFY `description` VARCHAR(255) NOT NULL;