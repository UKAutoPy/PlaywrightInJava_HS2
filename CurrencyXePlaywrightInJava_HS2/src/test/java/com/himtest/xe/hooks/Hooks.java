package com.himtest.xe.hooks;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.himtest.xe.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(headless)
        );
        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();

        page.onDialog(dialog -> dialog.dismiss());

        TestContext.setPlaywright(playwright);
        TestContext.setBrowser(browser);
        TestContext.setBrowserContext(browserContext);
        TestContext.setPage(page);

        scenario.log("Started browser for scenario: " + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        Page page = TestContext.getPage();

        if (scenario.isFailed() && page != null) {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }

        BrowserContext browserContext = TestContext.getBrowserContext();
        Browser browser = TestContext.getBrowser();
        Playwright playwright = TestContext.getPlaywright();

        if (page != null) {
            page.close();
        }
        if (browserContext != null) {
            browserContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }

        TestContext.clear();
    }
}