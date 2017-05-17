package com.example.alwayslike.jihuo.http;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class ErrorResponseCodeException extends Exception {
    public ErrorResponseCodeException() {
        super();
    }

    public ErrorResponseCodeException(String message) {
        super(message);
    }
}
