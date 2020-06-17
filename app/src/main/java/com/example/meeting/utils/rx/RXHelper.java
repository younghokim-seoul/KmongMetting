package com.example.meeting.utils.rx;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RXHelper {

    public static Disposable delay(long delayTime, @NonNull RxCall<Long> call) {
        return delay(delayTime, TimeUnit.MILLISECONDS, call);
    }

    public static Disposable delay(long delayTime, TimeUnit unit, @NonNull RxCall<Long> call) {
        return delay(delayTime, unit, call, null);
    }

    public static Disposable delay(long delayTime, TimeUnit unit, @NonNull final RxCall<Long> call, @NonNull Consumer<Throwable> errorConsumer) {
        return Flowable.timer(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(call::onCall, call::onError);
    }

    public static <T> Disposable runOnBackground(@NonNull final RxTaskCall<T> call) {
        return Observable.create((ObservableOnSubscribe<T>) emitter -> {
            T result = call.doInBackground();
            if (result == null) {
                call.onError(new NullPointerException());
                emitter.onComplete();
            } else {
                emitter.onNext(result);
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> call.onResult(t));

    }
}
