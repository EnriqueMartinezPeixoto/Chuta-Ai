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
                         pontosTotal INT DEFAULT 0,
                         is_admin BOOLEAN DEFAULT FALSE
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

-- 6 - cria a tabela principal do Bolão privado
CREATE TABLE bolao (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL,
                       codigoAcesso VARCHAR(10) NOT NULL UNIQUE,
                       dono_id BIGINT NOT NULL,
                       FOREIGN KEY (dono_id) REFERENCES usuario(id)
);

-- 7 - Cria a tabela de ligação (Quem está participando de qual bolão)
CREATE TABLE bolao_participantes (
                                     bolao_id BIGINT NOT NULL,
                                     usuario_id BIGINT NOT NULL,
                                     PRIMARY KEY (bolao_id, usuario_id),
                                     FOREIGN KEY (bolao_id) REFERENCES bolao(id),
                                     FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);