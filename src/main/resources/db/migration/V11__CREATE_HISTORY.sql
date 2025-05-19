ALTER TABLE tab_pedidos 
ADD COLUMN id_User INT NOT NULL;

UPDATE tab_pedidos SET id_User = 2 WHERE id_User = 0;

ALTER TABLE tab_pedidos 
ADD CONSTRAINT fk_tab_pedidos_user FOREIGN KEY (id_User) REFERENCES tab_users(id);

CREATE TABLE tab_history_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_Pedido INT NOT NULL,
    operation VARCHAR(100) NOT NULL,
    update_AT DATETIME NOT NULL,
    id_User INT NOT NULL,
    
    FOREIGN KEY (id_Pedido) REFERENCES tab_pedidos(id),
    FOREIGN KEY (id_User) REFERENCES tab_users(id)
);