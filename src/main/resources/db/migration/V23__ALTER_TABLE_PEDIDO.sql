ALTER TABLE tab_pedidos
MODIFY COLUMN `obs` VARCHAR(2000),
ADD COLUMN `excluido` BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN `data_Exclusao` DATETIME NULL,
ADD COLUMN `quem_Excluiu` INT DEFAULT NULL,
ADD CONSTRAINT `fk_tab_pedidos_quem_excluiu` FOREIGN KEY (`quem_Excluiu`) REFERENCES tab_users(id);