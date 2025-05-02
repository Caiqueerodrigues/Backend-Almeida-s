ALTER TABLE tab_materials DROP COLUMN ativo;

ALTER TABLE tab_pedidos 
    DROP COLUMN metragem_Recebido, 
    DROP COLUMN metragem_Finalizado;