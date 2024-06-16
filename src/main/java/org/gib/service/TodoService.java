package org.gib.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.gib.model.TodoEntity;
import org.gib.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Optional<TodoEntity> create(final TodoEntity todoEntity) {
        todoRepository.save(todoEntity);
        return todoRepository.findById(todoEntity.getId());
    }

    public void validate(final TodoEntity entity){
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if (entity.getId() == null) {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> retrieve(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    public Optional<TodoEntity> update(final TodoEntity entity) {
        validate(entity);
        if (!todoRepository.existsById(entity.getId())) {
            throw new RuntimeException("Unknown id");
        }
        todoRepository.save(entity);

        return todoRepository.findById(entity.getId());
    }

    public Optional<TodoEntity> updateTodo(final TodoEntity entity) {
        validate(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            todoRepository.save(todo);
            }
        );

        return todoRepository.findById(entity.getId());
    }

    public String delete(final String id) {
        if(todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("id does not exist");
        }

        return "Deleted!";
    }

}
