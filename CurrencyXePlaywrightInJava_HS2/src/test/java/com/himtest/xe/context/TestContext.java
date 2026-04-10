package com.himtest.xe.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public final class TestContext {
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> BROWSER_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private TestContext() {
    }

    public static Playwright getPlaywright() {
        return PLAYWRIGHT.get();
    }

    public static void setPlaywright(Playwright playwright) {
        PLAYWRIGHT.set(playwright);
    }

    public static Browser getBrowser() {
        return BROWSER.get();
    }

    public static void setBrowser(Browser browser) {
        BROWSER.set(browser);
    }

    public static BrowserContext getBrowserContext() {
        return BROWSER_CONTEXT.get();
    }

    public static void setBrowserContext(BrowserContext browserContext) {
        BROWSER_CONTEXT.set(browserContext);
    }

    public static Page getPage() {
        return PAGE.get();
    }

    public static void setPage(Page page) {
        PAGE.set(page);
    }

    public static void clear() {
        PAGE.remove();
        BROWSER_CONTEXT.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
    }
}