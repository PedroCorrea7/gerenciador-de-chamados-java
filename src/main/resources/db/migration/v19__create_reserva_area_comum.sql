-- 1. Criação das tabelas de área comum.
CREATE TABLE area_comum (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

--2 Criação da tabela de reservas
CREATE TABLE reserva (
    id UUID PRIMARY KEY DEFAULT gen_random_uuido(),
    area_comum_id UUID NOT NULL,
    morador_id UUID NOT NULL,
    data_reserva DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'SOLICITADO',
    motivo_negacao VARCHAR(255),
    admin_id UUID,
    criado_em TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reserva_area_comum FOREIGN KEY (area_comum_id) REFERENCES area_comum_id,
    CONSTRAINT fk_reserva_morador FOREIGN KEY (morador_id)  REFERENCES moradores (id),
    CONSTRAINT fk_reserva_admin FOREIGN KEY (admin_id) REFERENCES administradores (id),
    CONSTRAINT chk_horario_valido CHECK ( hora_fim > hora_inicio )
);

-- Indices de apoio a consultas e relacionamentos
CREATE INDEX idx_reserva_area_comum_id ON reserva (area_comum_id);
CREATE INDEX idx_reserva_morador_id ON reserva (morador_id);
CREATE INDEX idx_reserva_admin_id ON reserva (admin_id;)