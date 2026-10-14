package com.internet.tests;

import com.internet.assertions.MyHardAssert;
import com.internet.webdriver.DriverProvider;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ParallelTests extends BaseTest {
    private final CountDownLatch bothTestsRunning = new CountDownLatch(2);
    private final AtomicReference<WebDriver> firstDriver = new AtomicReference<>();
    private final AtomicReference<WebDriver> secondDriver = new AtomicReference<>();
    private final AtomicReference<Thread> firstThread = new AtomicReference<>();
    private final AtomicReference<Thread> secondThread = new AtomicReference<>();

    @Test
    public void firstTestUsesItsOwnDriverDuringParallelExecution() throws InterruptedException {
        verifyParallelDriver(firstDriver, firstThread);
    }

    @Test
    public void secondTestUsesItsOwnDriverDuringParallelExecution() throws InterruptedException {
        verifyParallelDriver(secondDriver, secondThread);
    }

    private void verifyParallelDriver(AtomicReference<WebDriver> driverSlot,
                                     AtomicReference<Thread> threadSlot) throws InterruptedException {
        WebDriver driver = DriverProvider.getWebDriver();
        MyHardAssert.assertThat(driver != null).isTrue();
        MyHardAssert.assertThat(driverSlot.compareAndSet(null, driver)).isTrue();
        threadSlot.set(Thread.currentThread());
        bothTestsRunning.countDown();

        MyHardAssert.assertThat(bothTestsRunning.await(30, TimeUnit.SECONDS)).isTrue();
        MyHardAssert.assertThat(DriverProvider.getWebDriver() == driver).isTrue();
        MyHardAssert.assertThat(firstDriver.get() != secondDriver.get()).isTrue();
        MyHardAssert.assertThat(firstThread.get() != secondThread.get()).isTrue();
    }
}
