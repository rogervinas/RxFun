package com.rogervinas.rxfun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rx.Observable;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ConcatObservablesTest {

    private static final Duration duration1 = Duration.ofMillis(500);
    private static final Duration duration2 = duration1.multipliedBy(2);
    private static final Duration duration4 = duration1.multipliedBy(4);

    public static final int VALUE_FROM_1 = 1;
    public static final int VALUE_TO_1 = 5;
    private static final Observable<Integer> observable1_sync_timeout_completed = Observables.syncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration4, true);
    private static final Observable<Integer> observable1_sync_timeout_no_completed = Observables.syncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration4, false);
    private static final Observable<Integer> observable1_sync_no_timeout_completed = Observables.syncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration1, true);
    private static final Observable<Integer> observable1_sync_no_timeout_no_completed = Observables.syncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration1, false);

    public static final int VALUE_FROM_2 = 3;
    public static final int VALUE_TO_2 = 10;
    private static final Observable<Integer> observable2_sync_timeout_completed = Observables.syncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration4, true);
    private static final Observable<Integer> observable2_sync_timeout_no_completed = Observables.syncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration4, false);
    private static final Observable<Integer> observable2_sync_no_timeout_completed = Observables.syncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration1, true);
    private static final Observable<Integer> observable2_sync_no_timeout_no_completed = Observables.syncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration1, false);

    private static final Observable<Integer> observable1_async_timeout_completed = Observables.asyncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration4, true);
    private static final Observable<Integer> observable1_async_timeout_no_completed = Observables.asyncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration4, false);
    private static final Observable<Integer> observable1_async_no_timeout_completed = Observables.asyncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration1, true);
    private static final Observable<Integer> observable1_async_no_timeout_no_completed = Observables.asyncObservable(VALUE_FROM_1, VALUE_TO_1, duration1, duration1, false);

    private static final Observable<Integer> observable2_async_timeout_completed = Observables.asyncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration4, true);
    private static final Observable<Integer> observable2_async_timeout_no_completed = Observables.asyncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration4, false);
    private static final Observable<Integer> observable2_async_no_timeout_completed = Observables.asyncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration1, true);
    private static final Observable<Integer> observable2_async_no_timeout_no_completed = Observables.asyncObservable(VALUE_FROM_2, VALUE_TO_2, duration1, duration1, false);

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        Collection<Observable<Integer>> observables1 = Arrays.asList(
            observable1_sync_timeout_completed,
            observable1_sync_timeout_no_completed,
            observable1_sync_no_timeout_completed,
            observable1_sync_no_timeout_no_completed,
            observable1_async_timeout_completed,
            observable1_async_timeout_no_completed,
            observable1_async_no_timeout_completed,
            observable1_async_no_timeout_no_completed
        );
        Collection<Observable<Integer>> observables2 = Arrays.asList(
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
        // Both empty
        List<Integer> valuesEmpty = Collections.emptyList();
        observables.add(new Object[] { Observable.empty(), Observable.empty(), valuesEmpty});
        // First non-empty
        List<Integer> valuesFrom_1_To_1 = IntStream.rangeClosed(VALUE_FROM_1, VALUE_TO_1).boxed().collect(Collectors.toList());
        for(Observable<Integer> observable1 : observables1) {
            observables.add(new Object[] { observable1, Observable.empty(), valuesFrom_1_To_1});
        }
        // Second non-empty
        List<Integer> valuesFrom_2_To_2 = IntStream.rangeClosed(VALUE_FROM_2, VALUE_TO_2).boxed().collect(Collectors.toList());
        for(Observable<Integer> observable2 : observables2) {
            observables.add(new Object[] { Observable.empty(), observable2, valuesFrom_2_To_2});
        }
        // Both non-empty
        List<Integer> valuesFrom_1_To_2 = IntStream.rangeClosed(VALUE_FROM_1, VALUE_TO_2).boxed().collect(Collectors.toList());
        for(Observable<Integer> observable1 : observables1) {
            for(Observable<Integer> observable2 : observables2) {
                observables.add(new Object[] { observable1, observable2, valuesFrom_1_To_2});
            }
        }
        return observables;
    }

    @Parameterized.Parameter(0)
    public Observable<Integer> observable1;

    @Parameterized.Parameter(1)
    public Observable<Integer> observable2;

    @Parameterized.Parameter(2)
    public List<Integer> expectedResult;

    @Test(timeout = 15_000)
    public void test() {
        List<Integer> values = ConcatObservables.concat(
            observable1,
            duration2,
            observable2
        ).take(expectedResult.size()).toList().toBlocking().first();
        assertThat(values).isEqualTo(expectedResult);
    }
}
