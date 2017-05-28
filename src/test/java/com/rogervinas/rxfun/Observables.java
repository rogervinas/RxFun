package com.rogervinas.rxfun;

import rx.Observable;

import java.time.Duration;

public class Observables {

    public static Observable<Integer> syncObservable(
        int valueFrom,
        int valueTo,
        Duration sleepBeforeOnNext,
        Duration sleepBeforeOnCompleted,
        boolean callOnCompleted
    ) {
        return Observable.create(subscriber -> {
            int value = valueFrom;
            while (!subscriber.isUnsubscribed() && value <= valueTo) {
                sleep(sleepBeforeOnNext);
                subscriber.onNext(value++);
            }
            sleep(sleepBeforeOnCompleted);
            if(callOnCompleted) {
                subscriber.onCompleted();
            }
        });
    }

    private static void sleep(Duration sleep) {
        try {
            Thread.sleep(sleep.toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
