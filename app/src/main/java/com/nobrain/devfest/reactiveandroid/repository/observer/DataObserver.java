package com.nobrain.devfest.reactiveandroid.repository.observer;


import java.util.HashMap;
import java.util.Map;

import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

public class DataObserver {

    private static DataObserver instance;

    private Subject<Result, Result> notifier;
    Map<Object, CompositeSubscription> observer;

    private DataObserver() {
        notifier = PublishSubject.create();
        observer = new HashMap<>();
    }


    synchronized public static DataObserver getInstance() {
        if (instance == null) {
            instance = new DataObserver();
        }
        return instance;
    }

    synchronized public <DATA> void register(Object register, Class<DATA> domain, Notify<DATA> notify) {
        if (!observer.containsKey(register)) {
            observer.put(register, new CompositeSubscription());
        }

        CompositeSubscription compositeSubscription = observer.get(register);
        compositeSubscription.add(notify.update(notifier.filter(result -> result.data.getClass() == domain).map(it -> it)));
    }

    synchronized public void unregister(Object register) {
        if (observer.containsKey(register)) {
            observer.remove(register).unsubscribe();
        }
    }

    synchronized public void notify(Result result) {
        notifier.onNext(result);
    }
}
