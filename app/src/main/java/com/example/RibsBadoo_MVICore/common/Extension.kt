package com.example.RibsBadoo_MVICore.common

import android.os.Bundle
import com.badoo.binder.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import io.reactivex.subjects.BehaviorSubject

fun androidx.lifecycle.Lifecycle.createDestroyLifecycle(): Lifecycle {
    val subject = BehaviorSubject.create<Lifecycle.Event>()

    this.subscribe(
        onCreate = {
            subject.onNext(Lifecycle.Event.BEGIN)
        },
        onDestroy = {
            subject.onNext(Lifecycle.Event.END)
            subject.onComplete()
        }
    )
    return Lifecycle.wrap(subject)
}

inline fun <reified T : Enum<T>> Bundle.getEnum(key: String, default: T) =
    getInt(key).let { if (it >= 0) enumValues<T>()[it] else default }

fun <T : Enum<T>> Bundle.putEnum(key: String, value: T?) =
    putInt(key, value?.ordinal ?: -1)