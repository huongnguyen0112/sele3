package com.internet.tests;

import org.testng.annotations.Test;

import com.internet.reports.ReportProvider;

public class SearchTests extends BaseTest {

    @Test 
    public void testSearch() {
        // Implement search test logic here
        ReportProvider.step("1. Step 1: Open search page");
        ReportProvider.screenshot("Search page opened");
        ReportProvider.step("2. Step 2: Enter search query");
        ReportProvider.log("Search query entered");
        ReportProvider.step("3. Step 3: Submit search");    
        ReportProvider.screenshot("Search submitted");
    }
    
}
