package com.rogervinas.rxfun;

import org.junit.Test;
import rx.Observable;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcatObservablesTest {

    private List<Integer> valuesOneToTen = Observable.range(1, 10).toList().toBlocking().first();

    private static final Duration oneSecond = Duration.ofSeconds(1);
    private static final Duration twoSeconds = Duration.ofSeconds(2);
    private static final Duration fourSeconds = Duration.ofSeconds(4);

    @Test
    public void sync_timeout_completed_concat_with_sync_timeout_completed() {
        List<Integer> values = ConcatObservables.concat(
            Observables.syncObservable(1, 5, oneSecond, fourSeconds, true),
            twoSeconds,
            Observables.syncObservable(6, 10, oneSecond, fourSeconds, true)
        ).toList().toBlocking().first();
        assertThat(values).isEqualTo(valuesOneToTen);
    }

}
