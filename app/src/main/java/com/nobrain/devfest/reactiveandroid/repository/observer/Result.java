package com.nobrain.devfest.reactiveandroid.repository.observer;


public class Result<T> {
    public T data;
    private Status status;

    private Result(T data, Status status) {
        this.data = data;
        this.status = status;
    }

    public static <T> Result<T> added(T t) {
        return new Result<>(t, Status.ADDED);
    }
    public static <T> Result<T> updated(T t) {
        return new Result<>(t, Status.UPDATED);
    }
    public static <T> Result<T> deleted(T t) {
        return new Result<>(t, Status.DELETED);
    }

    public boolean added() { return status == Status.ADDED;}

    public boolean updated() { return status == Status.UPDATED;}

    public boolean deleted() { return status == Status.DELETED;}

    private enum Status {
        ADDED, UPDATED, DELETED, UNKNOWN
    }
}
