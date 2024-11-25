-- Insertar usuarios iniciales (Todos usando la contraseña "password123")
INSERT INTO Usuarios (nombre, correoElectronico, contraseña, telefono, tipoUsuario)
VALUES
    ('Admin Usuario', 'admin@example.com', '$2a$10$XQnYKN4gQD3W4JiF7wQlz.vKy4EQSXSb1wTW6odUnf4HleVJ1wGNy', '1234567890', 'gestor'),
    ('Organizador Ejemplo', 'organizador@example.com', '$2a$10$XQnYKN4gQD3W4JiF7wQlz.vKy4EQSXSb1wTW6odUnf4HleVJ1wGNy', '0987654321', 'organizador');

-- Insertar tipos de invitado
INSERT INTO TipoInvitado (nombreTipo, descripcion)
VALUES
    ('VIP', 'Invitado con acceso especial'),
    ('Regular', 'Invitado estándar'),
    ('Prensa', 'Representante de medios de comunicación');

-- Insertar tipos de evento
INSERT INTO TiposEvento (nombreTipo, descripcion)
VALUES
    ('Conferencia', 'Evento de presentaciones y charlas'),
    ('Taller', 'Sesión práctica de aprendizaje'),
    ('Networking', 'Evento para establecer contactos profesionales');

-- Insertar un evento de ejemplo
INSERT INTO Eventos (titulo, descripcion, fechaInicio, fechaFin, ubicacion, idOrganizador, idTipoEvento, estado)
VALUES
    ('Conferencia de Tecnología 2024', 'Una conferencia sobre las últimas tendencias tecnológicas', 
     '2024-12-15 09:00:00', '2024-12-16 18:00:00', 'Centro de Convenciones Ciudad Ejemplo', 
     (SELECT idUsuario FROM Usuarios WHERE correoElectronico = 'organizador@example.com'),
     (SELECT idTipoEvento FROM TiposEvento WHERE nombreTipo = 'Conferencia'),
     'publicado');

-- Insertar algunos invitados de ejemplo (Todos usando la contraseña "password123")
INSERT INTO Invitados (nombre, correoElectronico, contraseña, telefono, idTipoInvitado)
VALUES
    ('Juan Pérez', 'juan@example.com', '$2a$10$XQnYKN4gQD3W4JiF7wQlz.vKy4EQSXSb1wTW6odUnf4HleVJ1wGNy', '1122334455', (SELECT idTipoInvitado FROM TipoInvitado WHERE nombreTipo = 'VIP')),
    ('María García', 'maria@example.com', '$2a$10$XQnYKN4gQD3W4JiF7wQlz.vKy4EQSXSb1wTW6odUnf4HleVJ1wGNy', '5544332211', (SELECT idTipoInvitado FROM TipoInvitado WHERE nombreTipo = 'Regular'));

-- Crear algunas invitaciones para el evento de ejemplo
INSERT INTO Invitaciones (idEvento, idInvitado, estado)
VALUES
    ((SELECT idEvento FROM Eventos WHERE titulo = 'Conferencia de Tecnología 2024'),
     (SELECT idInvitado FROM Invitados WHERE correoElectronico = 'juan@example.com'),
     'pendiente'),
    ((SELECT idEvento FROM Eventos WHERE titulo = 'Conferencia de Tecnología 2024'),
     (SELECT idInvitado FROM Invitados WHERE correoElectronico = 'maria@example.com'),
     'pendiente');