-- Datos iniciales para la base de datos H2
INSERT INTO tasks (titulo, descripcion, estado, fecha_creacion, fecha_actualizacion) VALUES 
('Tarea de ejemplo 1', 'Esta es una tarea de ejemplo para probar la API', 'PENDIENTE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tarea de ejemplo 2', 'Otra tarea de ejemplo', 'COMPLETADA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Estudiar Spring Boot', 'Aprender los conceptos básicos de Spring Boot', 'PENDIENTE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);