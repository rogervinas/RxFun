package com.rogervinas.rxfun;

import rx.Observable;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ConcatObservables {

    public static <T extends Comparable<T>> Observable<T> concat(
        Observable<T> observable1,
        Duration timeout1,
        Observable<T> observable2
    ) {
        return Observable.create(subscriber -> {
            MutableValue<T> lastValue = new MutableValue<>();
            System.out.println("SUBSCRIPTION BEGINS " + subscriber);
            observable1
                .timeout(timeout1.toMillis(), TimeUnit.MILLISECONDS)
                .doOnNext(value -> lastValue.set(value))
                .doOnNext(value -> System.out.println("NEXT ON OBSERVABLE #1 " + value))
                .doOnCompleted(() -> System.out.println("COMPLETED OBSERVABLE #1"))
                .onErrorResumeNext(error -> {
                    System.out.println("ERROR ON OBSERVABLE #1 " + error);
                    return Observable.empty();
                })
                .concatWith(observable2.skipWhile(v -> lastValue.get().map(lv -> v.compareTo(lv) <= 0).orElse(false)))
                .doOnNext(value -> System.out.println("NEXT ON OBSERVABLE #2 " + value))
                .doOnCompleted(() -> System.out.println("COMPLETED OBSERVABLE #2"))
                .subscribe(subscriber);
            System.out.println("SUBSCRIPTION ENDS");
        });
    }

    private static class MutableValue<T> {
        private T value = null;

        public Optional<T> get() {
            return Optional.ofNullable(value);
        }

        public void set(T value) {
            this.value = value;
        }
    }
}
