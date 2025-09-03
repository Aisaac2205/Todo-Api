package com.example.todoapi.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapi.dto.TaskCreateDTO;
import com.example.todoapi.dto.TaskUpdateDTO;
import com.example.todoapi.model.Task;
import com.example.todoapi.model.Task.EstadoTarea;
import com.example.todoapi.service.TaskService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestión de tareas
 */
@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = "*") // Para desarrollo - en producción especificar dominios
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Obtiene todas las tareas
     * GET /api/v1/tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> obtenerTodasLasTareas(
            @RequestParam(required = false) EstadoTarea estado) {

        log.info("GET /api/v1/tasks - estado: {}", estado);

        List<Task> tasks;
        if (estado != null) {
            tasks = taskService.obtenerTareasPorEstado(estado);
        } else {
            tasks = taskService.obtenerTodasLasTareas();
        }

        return ResponseEntity.ok(tasks);
    }

    /**
     * Obtiene una tarea por ID
     * GET /api/v1/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> obtenerTareaPorId(@PathVariable Long id) {
        log.info("GET /api/v1/tasks/{}", id);

        return taskService.obtenerTareaPorId(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva tarea
     * POST /api/v1/tasks
     */
    @PostMapping
    public ResponseEntity<Task> crearTarea(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        log.info("POST /api/v1/tasks - {}", taskCreateDTO.getTitulo());

        try {
            Task nuevaTarea = taskService.crearTarea(taskCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
        } catch (Exception e) {
            log.error("Error al crear tarea: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza una tarea completa
     * PUT /api/v1/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> actualizarTarea(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {

        log.info("PUT /api/v1/tasks/{}", id);

        try {
            Task tareaActualizada = taskService.actualizarTarea(id, taskUpdateDTO);
            return ResponseEntity.ok(tareaActualizada);
        } catch (RuntimeException e) {
            log.error("Error al actualizar tarea {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al actualizar tarea {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cambia solo el estado de una tarea
     * PATCH /api/v1/tasks/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Task> cambiarEstadoTarea(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        log.info("PATCH /api/v1/tasks/{}/estado", id);

        try {
            String estadoStr = request.get("estado");
            EstadoTarea nuevoEstado = EstadoTarea.valueOf(estadoStr.toUpperCase());

            Task tareaActualizada = taskService.cambiarEstadoTarea(id, nuevoEstado);
            return ResponseEntity.ok(tareaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Estado inválido: {}", request.get("estado"));
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error al cambiar estado de tarea {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al cambiar estado de tarea {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina una tarea
     * DELETE /api/v1/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        log.info("DELETE /api/v1/tasks/{}", id);

        try {
            taskService.eliminarTarea(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error al eliminar tarea {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al eliminar tarea {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}