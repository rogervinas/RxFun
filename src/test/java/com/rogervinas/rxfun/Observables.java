package com.rogervinas.rxfun;

import rx.Observable;
import rx.Subscriber;

import java.time.Duration;
import java.util.concurrent.Executors;

public class Observables {

    public static Observable<Integer> syncObservable(
        int valueFrom,
        int valueTo,
        Duration sleepBeforeOnNext,
        Duration sleepBeforeOnCompleted,
        boolean callOnCompleted
    ) {
        return Observable.create(subscriber -> {
            subscribe(valueFrom, valueTo, sleepBeforeOnNext, sleepBeforeOnCompleted, callOnCompleted, subscriber);
        });
    }

    public static Observable<Integer> asyncObservable(
        int valueFrom,
        int valueTo,
        Duration sleepBeforeOnNext,
        Duration sleepBeforeOnCompleted,
        boolean callOnCompleted
    ) {
        return Observable.create(subscriber -> {
            Executors.newSingleThreadExecutor().submit(() -> {
                subscribe(valueFrom, valueTo, sleepBeforeOnNext, sleepBeforeOnCompleted, callOnCompleted, subscriber);
            });
        });
    }

    private static void subscribe(
        int valueFrom,
        int valueTo,
        Duration sleepBeforeOnNext,
        Duration sleepBeforeOnCompleted,
        boolean callOnCompleted,
        Subscriber<? super Integer> subscriber
    ) {
        int value = valueFrom;
        while (!subscriber.isUnsubscribed() && value <= valueTo) {
            sleep(sleepBeforeOnNext);
            subscriber.onNext(value++);
        }
        sleep(sleepBeforeOnCompleted);
        if(callOnCompleted) {
            subscriber.onCompleted();
        }
    }

    private static void sleep(Duration sleep) {
        try {
            Thread.sleep(sleep.toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
