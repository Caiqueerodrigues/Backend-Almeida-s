CREATE TABLE tab_exit (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    data_Registro DATETIME NOT NULL,
    data_Compra DATE NOT NULL,
    valor_Compra DECIMAL(10, 2) NOT NULL,
    tipo_Servico ENUM('Corte', 'Dublagem', 'Debruagem') NOT NULL,
    anotacoes VARCHAR(150) NOT NULL
);

ALTER TABLE tab_models
ADD COLUMN ativo BOOLEAN DEFAULT 1;