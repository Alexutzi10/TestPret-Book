package com.example.testprep_carte.network;

public interface Callback<R> {
    void runResultOnUIThread(R result);
}
