package com.internet.assertions;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public final class AssertListener implements IInvokedMethodListener {
    /**
     * Flushes any collected soft assertions after a TestNG test method finishes.
     *
     * @param method the invoked method metadata from TestNG
     * @param testResult the current TestNG result for the invocation
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) {
            return;
        }

        try {
            MySoftAssert.assertAll();
        } catch (AssertionError failure) {
            Throwable testFailure = testResult.getThrowable();
            if (testFailure == null) {
                testResult.setThrowable(failure);
            } else {
                testFailure.addSuppressed(failure);
            }
            testResult.setStatus(ITestResult.FAILURE);
        }
    }
}
