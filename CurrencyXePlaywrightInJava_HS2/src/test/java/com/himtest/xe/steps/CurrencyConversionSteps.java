package com.himtest.xe.steps;

import com.himtest.xe.context.TestContext;
import com.himtest.xe.pages.CurrencyConverterPage;
import com.himtest.xe.utils.CsvDataLoader;
import com.himtest.xe.utils.NumberFormatter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import java.math.BigDecimal;

public class CurrencyConversionSteps {
    private List<Map<String, String>> records;

    @Given("the XE Currency Converter site is opened")
    public void theXeCurrencyConverterSiteIsOpened() {
        String baseUrl = System.getProperty("base.url", "https://www.xe.com/");
        CurrencyConverterPage page = new CurrencyConverterPage(TestContext.getPage());
        page.navigateToXeCurrencyConverter(baseUrl);
        page.verifyPageTitle();
        page.acceptCookiesIfPresent();
    }

    @When("I load the currency conversion records from {string}")
    public void iLoadTheCurrencyConversionRecordsFrom(String csvResource) {
        records = CsvDataLoader.load(csvResource);
        assertThat(records).isNotEmpty();
    }

    @Then("each record should produce the expected converted value")
    public void eachRecordShouldProduceTheExpectedConvertedValue() {

        assertThat(records).isNotNull().isNotEmpty();
        CurrencyConverterPage page = new CurrencyConverterPage(TestContext.getPage());

        for (Map<String, String> record : records) {

            String amount = record.get("Amount");
            String fromCurrency = record.get("FromCurrency");
            String toCurrency = record.get("ToCurrency");

            // 🔥 IMPORTANT FIX: reset state before each iteration
            TestContext.getPage().navigate("https://www.xe.com/");

            page.acceptCookiesIfPresent();
            page.enterAmount(amount);
            page.fromCurrency(fromCurrency);
            page.toCurrency(toCurrency);

            page.clickConvertButton();
            page.acceptFirstAlertIfPresent();
            page.waitForResultToRender();

            String convertedAmount = page.fetch_and_return_converted_amount();
            String bigRateText = page.fetch_and_return_expected_rate();
            String bigRate = NumberFormatter.extractAndFormatNumber(bigRateText);

            String validateConvertedAmount =
                    NumberFormatter.multiplyRateWithAmount(bigRate, Double.parseDouble(amount));

            BigDecimal actual = new BigDecimal(convertedAmount.replaceAll(",", ""));
            BigDecimal expected = new BigDecimal(validateConvertedAmount.replaceAll(",", ""));

            boolean passed = false;
            try {
                assertThat(actual)
                        .as("Converted value for %s -> %s", fromCurrency, toCurrency)
                        .isCloseTo(expected, withPercentage(1));

                passed = true;

            } catch (AssertionError e) {
                passed = false;
            }

// Print result
            System.out.printf(
                    "Test %s -> %s - Amount: %s | Expected: %.2f | Actual: %.2f | Result: %s%n",
                    fromCurrency,
                    toCurrency,
                    amount,
                    expected,
                    actual,
                    passed ? "PASS" : "FAIL"
            );
        }
    }
}