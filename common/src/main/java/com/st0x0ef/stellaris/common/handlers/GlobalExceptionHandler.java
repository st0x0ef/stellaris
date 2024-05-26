package com.st0x0ef.stellaris.common.handlers;

import com.st0x0ef.stellaris.Stellaris;

import java.util.NoSuchElementException;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof NoSuchElementException) {
            System.err.println("Handled NoSuchElementException: " + e.getMessage());
        } else {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, e);
        }
    }
}