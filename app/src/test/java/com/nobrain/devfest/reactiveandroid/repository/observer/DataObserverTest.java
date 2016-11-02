package com.nobrain.devfest.reactiveandroid.repository.observer;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

public class DataObserverTest {

    @Test
    public void test() throws Exception {

        TestSubscriber<Result<Mock>> subscriber = TestSubscriber.create();

        // register
        DataObserver.getInstance().register(this, Mock.class, observable -> observable.subscribe(subscriber));

        assertThat(DataObserver.getInstance().observer).hasSize(1);

        Mock t = new Mock();
        t.value = "find!!";

        // onNext
        DataObserver.getInstance().notify(Result.added(t));


        // assertion
        subscriber.awaitValueCount(1, 500, TimeUnit.MILLISECONDS);

        subscriber.assertValueCount(1);
        Result<Mock> result = subscriber.getOnNextEvents().get(0);
        assertThat(result).isNotNull();
        assertThat(result.added()).isTrue();
        assertThat(result.data.value).isEqualTo("find!!");


        DataObserver.getInstance().unregister(this);
        assertThat(DataObserver.getInstance().observer).hasSize(0);

    }

    static class Mock {
        String value;
    }
}