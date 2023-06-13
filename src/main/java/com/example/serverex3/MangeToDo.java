package com.example.serverex3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.serverex3.RequestCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@RestController

public class MangeToDo  {
    private List<ToDO> TodoList = new ArrayList<>();
    private Map<Integer, ToDO> todoMap = new HashMap<>();
    private int nextId = 1;
    private int requestCounter = 0;
    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("request-logger");
    private static final Logger TODO_LOGGER = LoggerFactory.getLogger("todo-logger");

        @GetMapping("/todo/health")
        public String health() {
            Logging.logRequestCounter();
            return "OK";
        }

        @PostMapping("/todo")
        public ResponseEntity<String> createTodo(@RequestBody ToDORequest request) {
            // Check if a todo with the same title already exists
            Logging.logRequestCounter();
            boolean titleExists = TodoList.stream().anyMatch(t -> t.getTitle().equals(request.getTitle()));
            if (titleExists) {
                String errorMessage = "Error: TODO with the title [" + request.getTitle() + "] already exists in the system";
                Logging.logTodoCreation(request.getTitle());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
            }

            // Check if the due date is in the future
            long currentTimeMillis = System.currentTimeMillis();
            if (request.getDueDate() < currentTimeMillis) {
                String errorMessage = "Error: Can’t create new TODO that its due date is in the past";
                Logging.logTodoCreation(request.getTitle());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
            }

            // Create the new todo
            ToDO newTodo = new ToDO();
            newTodo.setId(nextId++);
            newTodo.setTitle(request.getTitle());
            newTodo.setContent(request.getContent());
            newTodo.setDueDate(request.getDueDate());
            newTodo.setStatus(Status.PENDING);
            TodoList.add(newTodo);

            //addtolog
            Logging.logTodoCreation(newTodo.getId());
            Logging.logTotalExistingTodos(TodoList.size() - 1, newTodo.getId());


            // Return the newly assigned TODO number
            return ResponseEntity.ok(String.valueOf(newTodo.getId()));
        }

    @GetMapping("/todo/size")
    public ResponseEntity<String> getTodoCount(@RequestParam(value = "status") String status) {
        Logging.logRequestCounter();
        int count;
        if (status.equals("ALL")) {
            count = TodoList.size(); // assuming todoList is a list of all TODO items
        } else if (status.equals("PENDING")) {
            count = (int) TodoList.stream().filter(todo -> todo.getStatus() == Status.PENDING).count();
        } else if (status.equals("LATE")) {
            count = (int) TodoList.stream().filter(todo -> todo.getStatus() == Status.LATE).count();
        } else if (status.equals("DONE")) {
            count = (int) TodoList.stream().filter(todo -> todo.getStatus() == Status.DONE).count();
        } else {
            Logging.logGetTodoCount(status, 0);
            return ResponseEntity.badRequest().body("Invalid filter provided.");
        }
        Logging.logGetTodoCount(status, count);
        return ResponseEntity.ok(String.format("Total number of TODOs with status %s: %d", status, count));
    }


    @GetMapping("/todo/content")
    public ResponseEntity<List<ToDO>> getTodoContent(
            @RequestParam(value = "status") String status,
            @RequestParam(value = "sortBy", required = false, defaultValue = "ID") String sortBy) {
        Logging.logRequestCounter();

        List<ToDO> todos;
        if(status.equals("ALL"))
            todos=this.TodoList;
        else
            todos = this.ListByStatus(status);

        switch(sortBy) {
            case "ID":
                todos.sort(Comparator.comparing(ToDO::getId));
                break;
            case "DUE_DATE":
                todos.sort(Comparator.comparing(ToDO::getDueDate));
                break;
            case "TITLE":
                todos.sort(Comparator.comparing(ToDO::getTitle));
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        Logging.logRequestCounter();
        return ResponseEntity.ok(todos);
    }




    @PutMapping("/todo")
    public ResponseEntity<String> updateTodoStatus(@RequestParam("id") int id, @RequestParam("status") String status) {
        Logging.logRequestCounter();
            todoMap = fromListToMap();
        ToDO todo = todoMap.get(id);
        if (todo == null) {
            Logging.logUpdateTodoStatus(id, status);
            return ResponseEntity.notFound().build();
        }

        if (!status.equals("PENDING") && !status.equals("LATE") && !status.equals("DONE")) {
            Logging.logUpdateTodoStatus(id, status);
            return ResponseEntity.badRequest().build();
        }

        String oldStatus = todo.getStatus().toString();

        todo.setStatus(status);
        Logging.logTodoUpdate(id, status);
        Logging.logTodoStateChange(id, oldStatus, status);

        return ResponseEntity.ok(oldStatus);
    }

    @DeleteMapping("/todo")
    public ResponseEntity<?> deleteTodoById(@RequestParam("id") int id) {
        Logging.logRequestCounter();
        // Check if a todo with the given id exists
        ToDO todo = todoMap.get(id);
        if (todo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no such TODO with id " + id);
        }

        // Delete the todo
        TodoList.remove(todo);

        // Return the number of remaining todos
        int numTodos = TodoList.size();
        Logging.logTodoDeletion(id);
        return ResponseEntity.ok("Number of TODOs left: " + numTodos);
    }


    private Map<Integer, ToDO> fromListToMap()
    {
        Logging.logRequestCounter();
        Map<Integer, ToDO> map = new HashMap<>();
        for (ToDO toDO :TodoList ) {
            map.put(toDO.getId(),toDO);
        }
        return map;
    }

    private List<ToDO> ListByStatus(String status)
    {
        Logging.logRequestCounter();
        List<ToDO>  collect = TodoList.stream()
                .filter(todo->todo.getStatus().toString().equals(status))
                .collect(Collectors.toList());

        return collect;
    }
    @GetMapping("/logs/level")
    public ResponseEntity<String> getLoggerLevel(@RequestParam("logger-name") String loggerName) {
        // TODO: Implement logic to get the logger level
        return ResponseEntity.ok("Logger level");
    }

    @PutMapping("/logs/level")
    public ResponseEntity<String> setLoggerLevel(@RequestParam("logger-name") String loggerName, @RequestParam("logger-level") String loggerLevel) {
        // TODO: Implement logic to set the logger level
        return ResponseEntity.ok("Updated logger level");
    }
    public void handleRequest(int requestNumber, String resource, String httpVerb) {
        String logMessage = String.format("Incoming request | #%d | resource: %s | HTTP Verb %s | request #%d",
                requestNumber, resource, httpVerb, requestNumber);
        REQUEST_LOGGER.info(logMessage);
    }
}

