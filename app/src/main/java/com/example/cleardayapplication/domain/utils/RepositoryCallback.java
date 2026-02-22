package com.example.cleardayapplication.domain.utils;

/**
 * A generic callback interface used to handle the results
 * of asynchronous repository operations.
 * @param <T> The type of the result data expected on success.
 */
public interface RepositoryCallback<T> {
    void onSuccess(T result);
    void onError(Exception exception);
}
