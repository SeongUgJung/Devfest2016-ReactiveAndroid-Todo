package com.nobrain.devfest.reactiveandroid.repository.observer;


import rx.Observable;
import rx.Subscription;

public interface Notify<T> {
    Subscription update(Observable<Result<T>> observable);
}
