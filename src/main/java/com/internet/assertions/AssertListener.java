package com.internet.assertions;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public final class AssertListener implements IInvokedMethodListener {
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) {
            return;
        }

        try {
            MySoftAssert.assertAllPending();
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
