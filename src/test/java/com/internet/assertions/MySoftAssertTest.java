package com.internet.assertions;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MySoftAssertTest {
    @Test
    public void verifiesAllCollectorsAndClearsTheirRegistrations() {
        MySoftAssert firstCollector = new MySoftAssert();
        firstCollector.assertThat(1).isEqualTo(2);

        MySoftAssert secondCollector = new MySoftAssert();
        secondCollector.assertThat(2).isEqualTo(2);

        try {
            AssertionError failure = Assert.expectThrows(
                    AssertionError.class, firstCollector::assertAll);

            Assert.assertTrue(failure.getMessage().contains("1 soft assertion(s)"));
            Assert.assertNotSame(MySoftAssert.getInstance(), secondCollector);
        } finally {
            MySoftAssert.getInstance().assertAll();
        }
    }
}
