ALTER TABLE tab_exit
MODIFY COLUMN anotacoes VARCHAR(2000) NOT NULL,
MODIFY COLUMN tipo_Servico ENUM('Corte', 'Dublagem', 'Debruagem', 'Geral') NOT NULL;
