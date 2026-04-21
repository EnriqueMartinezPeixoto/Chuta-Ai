-- 1. Criação do Schema (Banco de Dados)
CREATE SCHEMA IF NOT EXISTS chutaai;

-- 2. Selecionar o banco recém-criado para uso
USE chutaai;

-- 3. Criação da tabela de Usuários
CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         nome VARCHAR(255) NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         pontosTotal INT DEFAULT 0
);

-- 4. Criação da tabela de Partidas
CREATE TABLE partida (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         timeCasa VARCHAR(255) NOT NULL,
                         timeFora VARCHAR(255) NOT NULL,
                         dataHora TIMESTAMP NOT NULL,
                         placarCasaReal INT,
                         placarForaReal INT,
                         fase VARCHAR(255) NOT NULL
);

-- 5. Criação da tabela de Palpites (com as chaves estrangeiras)
CREATE TABLE palpite (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         partida_id BIGINT NOT NULL,
                         palpiteCasa INT NOT NULL,
                         palpiteFora INT NOT NULL,
                         pontos INT DEFAULT 0,
                         CONSTRAINT fk_palpite_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
                         CONSTRAINT fk_palpite_partida FOREIGN KEY (partida_id) REFERENCES partida(id) ON DELETE CASCADE
);