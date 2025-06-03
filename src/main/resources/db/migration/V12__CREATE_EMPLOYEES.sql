CREATE TABLE tab_employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_Registro DATETIME NOT NULL,
    nome_Funcionario VARCHAR(50) NOT NULL,
    data DATE NOT NULL,
    horarios VARCHAR(255) NOT NULL,
    status_Pagamento BOOLEAN DEFAULT 0,
    update_AT DATETIME,
    id_User INT NOT NULL,
    
    FOREIGN KEY (id_User) REFERENCES tab_users(id)
)