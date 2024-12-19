package com.example.testprep_carte.network;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskRunner {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            try {
                R result = callable.call();

                handler.post(() -> callback.runResultOnUIThread(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
