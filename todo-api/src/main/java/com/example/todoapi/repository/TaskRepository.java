package com.example.todoapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapi.model.Task;
import com.example.todoapi.model.Task.EstadoTarea;

/**
 * Repositorio para operaciones de base de datos con la entidad Task
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Busca todas las tareas por estado
     * @param estado El estado de las tareas a buscar
     * @return Lista de tareas con el estado especificado
     */
    List<Task> findByEstado(EstadoTarea estado);

    /**
     * Busca tareas por título (búsqueda parcial, case insensitive)
     * @param titulo El título o parte del título a buscar
     * @return Lista de tareas que contienen el título especificado
     */
    List<Task> findByTituloContainingIgnoreCase(String titulo);
}