CREATE SCHEMA forumfilme;
USE forumfilme;
CREATE TABLE conteudo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    lancamento INT NOT NULL,
    diretor VARCHAR(255),
	classificacao INT
);


CREATE TABLE serie (
    conteudo_id INT PRIMARY KEY,
    episodios INT NOT NULL,
    CONSTRAINT fk_conteudo FOREIGN KEY (conteudo_id) REFERENCES conteudo(id) ON DELETE CASCADE
);



CREATE TABLE filme (
    conteudo_id INT PRIMARY KEY,
    duracao INT NOT NULL,
    CONSTRAINT fk_conteudo_filme FOREIGN KEY (conteudo_id) REFERENCES conteudo(id) ON DELETE CASCADE
);

CREATE TABLE genero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_genero VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE usuario (
    id INT auto_increment PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    conteudo_id INT NOT NULL REFERENCES conteudo(id) ON DELETE CASCADE,
    nota FLOAT CHECK (nota >= 0 AND nota <= 10),
    titulo VARCHAR(255),
    comentario TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE usuario_conteudo_adicionado (
    usuario_id INT REFERENCES usuario(id),
    conteudo_id INT REFERENCES conteudo(id),
    PRIMARY KEY (usuario_id, conteudo_id)
);

CREATE TABLE usuario_favoritos (
    usuario_id INT REFERENCES usuario(id),
    conteudo_id INT REFERENCES conteudo(id),
    PRIMARY KEY (usuario_id, conteudo_id)
);

CREATE TABLE usuario_seguidores (
    seguidor_id INT REFERENCES usuario(id) ON DELETE CASCADE,
    seguido_id INT REFERENCES usuario(id) ON DELETE CASCADE,
    PRIMARY KEY (seguidor_id, seguido_id),
    CHECK (seguidor_id <> seguido_id)
);



CREATE TABLE conteudo_genero (
    conteudo_id INT REFERENCES conteudo(id) ON DELETE CASCADE,
    genero_id INT REFERENCES genero(id) ON DELETE CASCADE,
    PRIMARY KEY (conteudo_id, genero_id)
);
