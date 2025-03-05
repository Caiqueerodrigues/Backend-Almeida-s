CREATE TABLE tab_clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    razao_Social VARCHAR(100),
    telefone VARCHAR(25) NOT NULL,
    ativo boolean NOT NULL DEFAULT TRUE,
    obs VARCHAR(255)
);

CREATE TABLE tab_models (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_Client INT NOT NULL,
    tipo_Calcado VARCHAR(100) NOT NULL,
    preco DOUBLE NOT NULL,
    qtd_Pecas_Par INT NOT NULL,
    ref_Ordem VARCHAR(100),
    fotos VARCHAR(100),
    qtd_Faca INT NOT NULL,
    rendimento VARCHAR(255),
    cronometragem VARCHAR(255),
    obs VARCHAR(255),
    FOREIGN KEY (id_Client) REFERENCES tab_clients(id)
);

CREATE TABLE tab_anexos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_Modelo INT,
    nome_File VARCHAR(100) NOT NULL,
    nome_Peca VARCHAR(100) NOT NULL,
    qtd_Par INT NOT NULL DEFAULT 2,
    propriedade_Faca VARCHAR(100) NOT NULL,
    preco_Faca DOUBLE NOT NULL,
    obs VARCHAR(255),
    FOREIGN KEY (id_Modelo) REFERENCES tab_models(id)
);

CREATE TABLE tab_pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_Client INT NOT NULL,
    modelo INT NOT NULL,
    data_Pedido DATE NOT NULL,
    relatorio_Cliente VARCHAR(100),
    total_Dinheiro DOUBLE NOT NULL,
    total_Pares INT NOT NULL,
    total_Pecas INT NOT NULL,
    grade TEXT NOT NULL,
    obs VARCHAR(255),
    data_Pagamento DATE,
    metragem_Recebido TEXT NOT NULL,
    tipo_Recebido TEXT NOT NULL,
    metragem_Finalizado TEXT,
    rendimento_Pares_Metro VARCHAR(255),
    FOREIGN KEY (id_Client) REFERENCES tab_clients(id),
    FOREIGN KEY (modelo) REFERENCES tab_models(id)
);
