CREATE TABLE tab_tables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_Client INT NOT NULL,
    tabelas JSON NOT NULL,
    created_At DATETIME,
    updated_At DATETIME,
    
    CONSTRAINT fk_client_tables_client
        FOREIGN KEY (id_Client)
        REFERENCES tab_clients(id)
        ON DELETE CASCADE
);