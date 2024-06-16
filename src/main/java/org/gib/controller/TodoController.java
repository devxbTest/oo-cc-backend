package org.gib.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.gib.dto.ResponseDto;
import org.gib.dto.TodoDTO;
import org.gib.model.TodoEntity;
import org.gib.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(
        @AuthenticationPrincipal String userId,
        @RequestBody TodoDTO todoDTO
    ) {
        try {
            TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);
            todoEntity.setUserId(userId);
            Optional<TodoEntity> saved = todoService.create(todoEntity);

            List<TodoDTO> todoDTOList = saved.stream().map(TodoDTO::new).toList();

            ResponseDto<TodoDTO> response = ResponseDto.<TodoDTO>builder().data(todoDTOList).build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDTO> response = ResponseDto.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
        @AuthenticationPrincipal String userId
    ) {
        List<TodoEntity> entities = todoService.retrieve(userId);
        List<TodoDTO> todoDTOList = entities.stream().map(TodoDTO::new).toList();

        ResponseDto<TodoDTO> response = ResponseDto.<TodoDTO>builder().data(todoDTOList).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(userId);

        Optional<TodoEntity> entities = todoService.update(entity);

        List<TodoDTO> todoDTOList = entities.stream().map(TodoDTO::new).toList();

        ResponseDto<TodoDTO> response = ResponseDto.<TodoDTO>builder().data(todoDTOList).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(userId);

        Optional<TodoEntity> entities = todoService.updateTodo(entity);

        List<TodoDTO> todoDTOList = entities.stream().map(TodoDTO::new).toList();

        ResponseDto<TodoDTO> response = ResponseDto.<TodoDTO>builder().data(todoDTOList).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
        @AuthenticationPrincipal String userId,
        @RequestBody TodoDTO dto
    ) {
        try {
            List<String> message = new ArrayList<>();

            String msg = todoService.delete(dto.getId());
            message.add(msg);
            ResponseDto<String> responseDto = ResponseDto.<String>builder().data(message).build();
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDTO> response = ResponseDto.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
