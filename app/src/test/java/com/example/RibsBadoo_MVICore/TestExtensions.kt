package com.example.RibsBadoo_MVICore

import io.reactivex.observers.TestObserver

fun <T> TestObserver<T>.onNextEvents() =
        events[0]
