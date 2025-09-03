package com.example.todoapi.service;

import com.example.todoapi.dto.TaskCreateDTO;
import com.example.todoapi.dto.TaskUpdateDTO;
import com.example.todoapi.model.Task;
import com.example.todoapi.model.Task.EstadoTarea;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio para gestión de tareas
 */
public interface TaskService {

    /**
     * Obtiene todas las tareas
     * @return Lista de todas las tareas
     */
    List<Task> obtenerTodasLasTareas();

    /**
     * Obtiene tareas por estado
     * @param estado El estado de las tareas a buscar
     * @return Lista de tareas con el estado especificado
     */
    List<Task> obtenerTareasPorEstado(EstadoTarea estado);

    /**
     * Obtiene una tarea por su ID
     * @param id El ID de la tarea
     * @return Optional con la tarea si existe
     */
    Optional<Task> obtenerTareaPorId(Long id);

    /**
     * Crea una nueva tarea
     * @param taskCreateDTO DTO con los datos de la nueva tarea
     * @return La tarea creada
     */
    Task crearTarea(TaskCreateDTO taskCreateDTO);

    /**
     * Actualiza una tarea existente
     * @param id El ID de la tarea a actualizar
     * @param taskUpdateDTO DTO con los nuevos datos
     * @return La tarea actualizada
     * @throws RuntimeException Si la tarea no existe
     */
    Task actualizarTarea(Long id, TaskUpdateDTO taskUpdateDTO);

    /**
     * Cambia el estado de una tarea
     * @param id El ID de la tarea
     * @param nuevoEstado El nuevo estado
     * @return La tarea actualizada
     * @throws RuntimeException Si la tarea no existe
     */
    Task cambiarEstadoTarea(Long id, EstadoTarea nuevoEstado);

    /**
     * Elimina una tarea
     * @param id El ID de la tarea a eliminar
     * @throws RuntimeException Si la tarea no existe
     */
    void eliminarTarea(Long id);
}