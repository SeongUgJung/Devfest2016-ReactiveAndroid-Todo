package com.nobrain.devfest.reactiveandroid.repository;


import com.nobrain.devfest.reactiveandroid.repository.domain.Todo;
import com.nobrain.devfest.reactiveandroid.repository.observer.DataObserver;
import com.nobrain.devfest.reactiveandroid.repository.observer.Result;

import java.util.ArrayList;
import java.util.List;

public class TodoRepository {
    private static TodoRepository repository;
    private List<Todo> todos;

    private TodoRepository() {
        todos = new ArrayList<>();
    }

    public static TodoRepository getRepository() {
        if (repository == null) {
            repository = new TodoRepository();
        }
        return repository;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void addTodo(long id, String content) {
        Todo item = new Todo(id);
        item.content = content;
        todos.add(item);
        DataObserver.getInstance().notify(Result.added(item));

    }

    public void updateTodo(long id, String content) {
        for (Todo todo : todos) {
            if (todo.id == id) {
                todo.content = content;
                DataObserver.getInstance().notify(Result.updated(todo));
                break;
            }
        }
    }

    public void remove(long id) {
        for (int i = 0, todosSize = todos.size(); i < todosSize; i++) {
            Todo todo = todos.get(i);
            if (todo.id == id) {
                todos.remove(todo);
                DataObserver.getInstance().notify(Result.deleted(todo));
                break;
            }
        }
    }

    public Todo get(long id) {
        for (Todo todo : todos) {
            if (todo.id == id) {
                return todo;
            }
        }
        return new Todo(id);
    }
}
