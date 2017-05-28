package com.rogervinas.rxfun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rx.Observable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ConcatObservablesTest {

    private final List<Integer> valuesOneToTen = Observable.range(1, 10).toList().toBlocking().first();

    private static final Duration duration1 = Duration.ofMillis(500);
    private static final Duration duration2 = duration1.multipliedBy(2);
    private static final Duration duration4 = duration1.multipliedBy(4);

    private static final Observable<Integer> observable1_sync_timeout_completed = Observables.syncObservable(1, 5, duration1, duration4, true);
    private static final Observable<Integer> observable1_sync_timeout_no_completed = Observables.syncObservable(1, 5, duration1, duration4, false);
    private static final Observable<Integer> observable1_sync_no_timeout_completed = Observables.syncObservable(1, 5, duration1, duration1, true);
    private static final Observable<Integer> observable1_sync_no_timeout_no_completed = Observables.syncObservable(1, 5, duration1, duration1, false);

    private static final Observable<Integer> observable2_sync_timeout_completed = Observables.syncObservable(6, 10, duration1, duration4, true);
    private static final Observable<Integer> observable2_sync_timeout_no_completed = Observables.syncObservable(6, 10, duration1, duration4, false);
    private static final Observable<Integer> observable2_sync_no_timeout_completed = Observables.syncObservable(6, 10, duration1, duration1, true);
    private static final Observable<Integer> observable2_sync_no_timeout_no_completed = Observables.syncObservable(6, 10, duration1, duration1, false);

    private static final Observable<Integer> observable1_async_timeout_completed = Observables.asyncObservable(1, 5, duration1, duration4, true);
    private static final Observable<Integer> observable1_async_timeout_no_completed = Observables.asyncObservable(1, 5, duration1, duration4, false);
    private static final Observable<Integer> observable1_async_no_timeout_completed = Observables.asyncObservable(1, 5, duration1, duration1, true);
    private static final Observable<Integer> observable1_async_no_timeout_no_completed = Observables.asyncObservable(1, 5, duration1, duration1, false);

    private static final Observable<Integer> observable2_async_timeout_completed = Observables.asyncObservable(6, 10, duration1, duration4, true);
    private static final Observable<Integer> observable2_async_timeout_no_completed = Observables.asyncObservable(6, 10, duration1, duration4, false);
    private static final Observable<Integer> observable2_async_no_timeout_completed = Observables.asyncObservable(6, 10, duration1, duration1, true);
    private static final Observable<Integer> observable2_async_no_timeout_no_completed = Observables.asyncObservable(6, 10, duration1, duration1, false);

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        Collection<Object> observables1 = Arrays.asList(
            observable1_sync_timeout_completed,
            observable1_sync_timeout_no_completed,
            observable1_sync_no_timeout_completed,
            observable1_sync_no_timeout_no_completed,
            observable1_async_timeout_completed,
            observable1_async_timeout_no_completed,
            observable1_async_no_timeout_completed,
            observable1_async_no_timeout_no_completed
        );
        Collection<Object> observables2 = Arrays.asList(
            observable2_sync_timeout_completed,
            observable2_sync_timeout_no_completed,
            observable2_sync_no_timeout_completed,
            observable2_sync_no_timeout_no_completed,
            observable2_async_timeout_completed,
            observable2_async_timeout_no_completed,
            observable2_async_no_timeout_completed,
            observable2_async_no_timeout_no_completed
        );
        Collection<Object[]> observables = new ArrayList<>();
        for(Object observable1 : observables1) {
            for(Object observable2 : observables2) {
                observables.add(new Object[] { observable1, observable2});
            }
        }
        return observables;
    }

    @Parameterized.Parameter(0)
    public Observable<Integer> observable1;

    @Parameterized.Parameter(1)
    public Observable<Integer> observable2;

    @Test(timeout = 15_000)
    public void test() {
        List<Integer> values = ConcatObservables.concat(
            observable1,
            duration2,
            observable2
        ).take(10).toList().toBlocking().first();
        assertThat(values).isEqualTo(valuesOneToTen);
    }
}
