package org.hydev.mcpm.client.database.fetcher;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * This class is an implementation of FutureCallback that does nothing on all callbacks.
 * Apache's Async Http Client has some features we want to take advantage of (streamed HttpEntities).
 * Unfortunately, we don't want to change our architecture to be asynchronous (at this moment).
 * So we will ignore async callbacks and use the blocking versions of methods.
 *
 * @param <T> FutureCallback backing type. Passed to FutureCallback.
 */
class EmptyFutureCallback<T> implements FutureCallback<T> {
    @Override
    public void completed(T result) { /* empty */ }

    @Override
    public void failed(Exception ex) { /* empty */ }

    @Override
    public void cancelled() { /* empty */ }
}
