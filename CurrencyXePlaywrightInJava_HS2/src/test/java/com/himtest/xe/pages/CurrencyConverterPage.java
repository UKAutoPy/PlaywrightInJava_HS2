package com.himtest.xe.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CurrencyConverterPage {
    private static final Pattern RATE_VALUE_PATTERN = Pattern.compile("^\\s*[A-Z]{3}\\s*=\\s*([0-9.]+)\\s*[A-Z]{3}\\s*$");

    private final Page page;
    private final Locator amountTextBox;
    private final Locator fromCurrencyDropdown;
    private final Locator toCurrencyDropdown;
    private final Locator convertButton;

    public CurrencyConverterPage(Page page) {
        this.page = page;
        this.amountTextBox = page.getByLabel("Amount");
        this.fromCurrencyDropdown = page.locator("#midmarketFromCurrency").getByPlaceholder("Type to search...");
        this.toCurrencyDropdown = page.locator("#midmarketToCurrency").getByPlaceholder("Type to search...");
        this.convertButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Convert").setExact(true));
    }

    public void navigateToXeCurrencyConverter(String websiteUrl) {
        page.navigate(websiteUrl);
        page.waitForLoadState();
        page.waitForTimeout(2000);
    }

    public void verifyPageTitle() {
        assertThat(page).hasTitle("Xe: Currency Exchange Rates and International Money Transfers");
    }

    public void acceptCookiesIfPresent() {
        // Accept cookies if shown
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept"))
                    .first()
                    .click(new Locator.ClickOptions().setTimeout(3000));
        } catch (PlaywrightException ignored) {
            // Cookie banner not present → ignore
        }

        // Close promotional popup if shown
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close promotional message"))
                    .first()
                    .click(new Locator.ClickOptions().setTimeout(3000));
        } catch (PlaywrightException ignored) {
            // Promo popup not present → ignore
        }
    }

    public void enterAmount(String amount) {
        amountTextBox.fill("");// Clear the textbox first
        amountTextBox.fill(amount);
        page.waitForTimeout(2000); // wait for 2 seconds
        amountTextBox.press("Tab");
    }

    public void fromCurrency(String fromCurrency) {
        selectCurrency(fromCurrency, fromCurrencyDropdown, 0, "#midmarketFromCurrency-listbox");
        page.waitForTimeout(2000); // wait for 2 seconds
    }

    public void toCurrency(String toCurrency) {
        selectCurrency(toCurrency, toCurrencyDropdown, 1, "#midmarketToCurrency-listbox");
        page.waitForTimeout(2000); // wait for 2 seconds
    }

    public void clickConvertButton() {
        convertButton.click();
        page.waitForTimeout(3000); // wait for 3 seconds
    }

    public void acceptFirstAlertIfPresent() {
        Locator acceptButton = page.locator("button", new Page.LocatorOptions().setHasText("Accept"));
        try {
            if (acceptButton.count() > 0 && acceptButton.first().isVisible(new Locator.IsVisibleOptions().setTimeout(2_000))) {
                acceptButton.first().click();
            }
        } catch (PlaywrightException ignored) {
        }
    }

/**
    public String getExchangeRate(String fromCurrency, String toCurrency) {
        //Locator locator = page.locator("span")
        Locator locator = page.getByLabel("Receiving amount").nth(1)
                .filter(new Locator.FilterOptions().setHasText(fromCurrency))
                .filter(new Locator.FilterOptions().setHasText(toCurrency))
                .first();

        String text = locator.innerText();
        Matcher matcher = RATE_VALUE_PATTERN.matcher(text);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not extract rate from text: " + text);
        }
        return matcher.group(1);
    }
*/
public String fetch_and_return_converted_amount() {
    // Returns the converted 'To' amount.
    // Get the second "Receiving amount" field (nth(1)) = TO converted amount
    Locator toField = page.getByLabel("Receiving amount").nth(1);

    // Wait for it to be visible (8 seconds timeout)
    toField.waitFor(new Locator.WaitForOptions().setTimeout(8000));

    // Return the value of the input field, trimmed
    return toField.inputValue().trim();
}
    public String fetch_and_return_expected_rate() {
    //Returns a list containing the expected rate string. Example: ['GBP = 1.31848966 USD']
    //Locator values = page.locator("p[class*='result__BigRate']");
        Locator values = page.locator("text=/^\\d+(\\.\\d+)? [A-Z]{3} = \\d+(\\.\\d+)? [A-Z]{3}$/").first();
        if (values.count() == 0) {
            throw new IllegalStateException("No conversion result found on the page");
        }
        return values.first().innerText().trim();

    }

    public void waitForResultToRender() {
        //page.locator("p[class*='result__BigRate']").first()
        page.locator("text=/^\\d+(\\.\\d+)? [A-Z]{3} = \\d+(\\.\\d+)? [A-Z]{3}$/").first()
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15_000));
    }

    private void selectCurrency(String currency, Locator dropdown, int searchBoxIndex, String listboxSelector) {
        String currencyCode = currency.trim().toLowerCase();

        dropdown.click();
        Locator searchBox = page.getByPlaceholder("Type to search...").nth(searchBoxIndex);
        assertThat(searchBox).isVisible();
        searchBox.click();
        searchBox.fill("");
        searchBox.pressSequentially(currencyCode, new Locator.PressSequentiallyOptions().setDelay(150));

        Locator option = page.locator(listboxSelector);
        Locator optionItem = option.locator(String.format("img[alt='%s']", currencyCode)).first();
        optionItem.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10_000));
        optionItem.scrollIntoViewIfNeeded();
        optionItem.click();
    }
}