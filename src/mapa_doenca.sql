
-- Tabela de Usuários anônimos
CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  apelido VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabela de Doenças
CREATE TABLE IF NOT EXISTS doencas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  gravidade ENUM('Leve','Moderada','Grave') NOT NULL,
  UNIQUE KEY uq_doenca_nome_gravidade (nome, gravidade)
) ENGINE=InnoDB;

-- Tabela de Sintomas
CREATE TABLE IF NOT EXISTS sintomas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabela de associação N:N entre Doenças e Sintomas
CREATE TABLE IF NOT EXISTS doenca_sintoma (
  doenca_id INT NOT NULL,
  sintoma_id INT NOT NULL,
  PRIMARY KEY (doenca_id, sintoma_id),
  KEY idx_ds_sintoma (sintoma_id),
  CONSTRAINT fk_ds_doenca FOREIGN KEY (doenca_id) REFERENCES doencas(id) ON DELETE CASCADE,
  CONSTRAINT fk_ds_sintoma FOREIGN KEY (sintoma_id) REFERENCES sintomas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela de Locais
CREATE TABLE IF NOT EXISTS locais (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabela de Relatos
CREATE TABLE IF NOT EXISTS relatos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  doenca_id INT NOT NULL,
  local_id INT NOT NULL,
  data DATETIME NOT NULL,
  KEY idx_rel_usuario (usuario_id),
  KEY idx_rel_doenca (doenca_id),
  KEY idx_rel_local (local_id),
  CONSTRAINT fk_rel_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  CONSTRAINT fk_rel_doenca FOREIGN KEY (doenca_id) REFERENCES doencas(id) ON DELETE CASCADE,
  CONSTRAINT fk_rel_local FOREIGN KEY (local_id) REFERENCES locais(id) ON DELETE CASCADE
) ENGINE=InnoDB;


-- 1) Populando LOCAIS
INSERT INTO locais (nome) VALUES
  ('São Paulo'),
  ('Rio de Janeiro'),
  ('Belo Horizonte'),
  ('Porto Alegre');

-- 2) Populando DOENÇAS
INSERT INTO doencas (nome, gravidade) VALUES
  ('Gripe',       'Leve'),
  ('Dengue',      'Moderada'),
  ('Febre Amarela','Grave'),
  ('Covid-19',    'Moderada');

-- 3) (Opcional) Populando SINTOMAS
INSERT INTO sintomas (nome) VALUES
  ('febre'),
  ('dor de cabeça'),
  ('tosse seca'),
  ('manchas vermelhas');

-- 4) (Opcional) Associações N:N doença ↔ sintoma
INSERT INTO doenca_sintoma (doenca_id, sintoma_id) VALUES
  (1,1),(1,3),       -- Gripe: febre, tosse seca
  (2,1),(2,2),(2,4), -- Dengue: febre, dor de cabeça, manchas
  (4,3);             -- Covid-19: tosse secadoenca_sintomadoenca_idsintoma_id



