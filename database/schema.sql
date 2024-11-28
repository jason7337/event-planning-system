-- Tabla Usuarios
CREATE TABLE Usuarios (
    idUsuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correoElectronico VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    tipoUsuario VARCHAR(20) CHECK (tipoUsuario IN ('organizador', 'gestor')) NOT NULL,
    fechaRegistro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla TipoInvitado
CREATE TABLE TipoInvitado (
    idTipoInvitado SERIAL PRIMARY KEY,
    nombreTipo VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- Tabla Invitados
CREATE TABLE Invitados (
    idInvitado SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correoElectronico VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    telefono VARCHAR(15)

    telefono VARCHAR(15),
    idTipoInvitado INT NOT NULL,
    CONSTRAINT fk_tipoInvitado FOREIGN KEY (idTipoInvitado) REFERENCES TipoInvitado (idTipoInvitado) ON DELETE CASCADE

);

-- Tabla TiposEvento
CREATE TABLE TiposEvento (
    idTipoEvento SERIAL PRIMARY KEY,
    nombreTipo VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- Tabla Eventos
CREATE TABLE Eventos (
    idEvento SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fechaInicio TIMESTAMP NOT NULL,
    fechaFin TIMESTAMP,
    ubicacion VARCHAR(255),
    idOrganizador INT NOT NULL,
    idTipoEvento INT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('activo', 'cancelado')) DEFAULT 'activo',
    estado VARCHAR(20) CHECK (estado IN ('borrador', 'publicado', 'activo', 'en_progreso', 'finalizado', 'pospuesto', 'cancelado')) DEFAULT 'borrador',
    CONSTRAINT fk_organizador FOREIGN KEY (idOrganizador) REFERENCES Usuarios (idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_tipoEvento FOREIGN KEY (idTipoEvento) REFERENCES TiposEvento (idTipoEvento) ON DELETE CASCADE
);

-- Tabla ParticipantesEvento
CREATE TABLE ParticipantesEvento (
    idParticipante SERIAL PRIMARY KEY,
    idEvento INT NOT NULL,
    idInvitado INT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('confirmado', 'pendiente', 'rechazado')) DEFAULT 'pendiente',
    CONSTRAINT fk_evento FOREIGN KEY (idEvento) REFERENCES Eventos (idEvento) ON DELETE CASCADE,
    CONSTRAINT fk_invitado FOREIGN KEY (idInvitado) REFERENCES Invitados (idInvitado) ON DELETE CASCADE
);

-- Tabla Invitaciones
CREATE TABLE Invitaciones (
    idInvitacion SERIAL PRIMARY KEY,
    idEvento INT NOT NULL,
    idInvitado INT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('pendiente', 'aceptada', 'rechazada')) DEFAULT 'pendiente',
    fechaRespuesta TIMESTAMP,
    CONSTRAINT fk_eventoInvitacion FOREIGN KEY (idEvento) REFERENCES Eventos (idEvento) ON DELETE CASCADE,
    CONSTRAINT fk_invitadoInvitacion FOREIGN KEY (idInvitado) REFERENCES Invitados (idInvitado) ON DELETE CASCADE
);

-- Tabla Mensajes
CREATE TABLE Mensajes (
    idMensaje SERIAL PRIMARY KEY,
    idEvento INT NOT NULL,
    idUsuario INT NOT NULL,
    idInvitado INT NOT NULL,
    mensaje TEXT NOT NULL,
    fechaEnvio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eventoMensaje FOREIGN KEY (idEvento) REFERENCES Eventos (idEvento) ON DELETE CASCADE,
    CONSTRAINT fk_usuarioMensaje FOREIGN KEY (idUsuario) REFERENCES Usuarios (idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_invitadoMensaje FOREIGN KEY (idInvitado) REFERENCES Invitados (idInvitado) ON DELETE CASCADE
);

-- Tabla Correos
CREATE TABLE Correos (
    idCorreo SERIAL PRIMARY KEY,
    idEvento INT NOT NULL,
    idUsuario INT NOT NULL,
    idInvitado INT NOT NULL,
    asunto VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    fechaEnvio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eventoCorreo FOREIGN KEY (idEvento) REFERENCES Eventos (idEvento) ON DELETE CASCADE,
    CONSTRAINT fk_usuarioCorreo FOREIGN KEY (idUsuario) REFERENCES Usuarios (idUsuario) ON DELETE CASCADE,
    CONSTRAINT fk_invitadoCorreo FOREIGN KEY (idInvitado) REFERENCES Invitados (idInvitado) ON DELETE CASCADE
);
