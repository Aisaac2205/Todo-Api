package com.example.todoapi.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todoapi.dto.TaskCreateDTO;
import com.example.todoapi.dto.TaskUpdateDTO;
import com.example.todoapi.model.Task;
import com.example.todoapi.model.Task.EstadoTarea;
import com.example.todoapi.repository.TaskRepository;

/**
 * Implementación del servicio para gestión de tareas
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> obtenerTodasLasTareas() {
        log.info("Obteniendo todas las tareas");
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> obtenerTareasPorEstado(EstadoTarea estado) {
        log.info("Obteniendo tareas por estado: {}", estado);
        return taskRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> obtenerTareaPorId(Long id) {
        log.info("Obteniendo tarea por ID: {}", id);
        return taskRepository.findById(id);
    }

    @Override
    public Task crearTarea(TaskCreateDTO taskCreateDTO) {
        log.info("Creando nueva tarea: {}", taskCreateDTO.getTitulo());
        
        Task nuevaTarea = new Task();
        nuevaTarea.setTitulo(taskCreateDTO.getTitulo());
        nuevaTarea.setDescripcion(taskCreateDTO.getDescripcion());
        nuevaTarea.setEstado(EstadoTarea.PENDIENTE);
        
        Task tareaGuardada = taskRepository.save(nuevaTarea);
        log.info("Tarea creada exitosamente con ID: {}", tareaGuardada.getId());
        
        return tareaGuardada;
    }

    @Override
    public Task actualizarTarea(Long id, TaskUpdateDTO taskUpdateDTO) {
        log.info("Actualizando tarea con ID: {}", id);
        
        Task tareaExistente = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));
        
        tareaExistente.setTitulo(taskUpdateDTO.getTitulo());
        tareaExistente.setDescripcion(taskUpdateDTO.getDescripcion());
        
        if (taskUpdateDTO.getEstado() != null) {
            tareaExistente.setEstado(taskUpdateDTO.getEstado());
        }
        
        Task tareaActualizada = taskRepository.save(tareaExistente);
        log.info("Tarea actualizada exitosamente con ID: {}", id);
        
        return tareaActualizada;
    }

    @Override
    public Task cambiarEstadoTarea(Long id, EstadoTarea nuevoEstado) {
        log.info("Cambiando estado de tarea con ID: {} a {}", id, nuevoEstado);
        
        Task tareaExistente = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));
        
        tareaExistente.setEstado(nuevoEstado);
        
        Task tareaActualizada = taskRepository.save(tareaExistente);
        log.info("Estado de tarea cambiado exitosamente con ID: {}", id);
        
        return tareaActualizada;
    }

    @Override
    public void eliminarTarea(Long id) {
        log.info("Eliminando tarea con ID: {}", id);
        
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada con ID: " + id);
        }
        
        taskRepository.deleteById(id);
        log.info("Tarea eliminada exitosamente con ID: {}", id);
    }
}