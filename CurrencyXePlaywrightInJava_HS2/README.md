# XE Currency Converter - Java + Cucumber + Playwright

This project is a Java/Cucumber conversion of the original Python repository:
`CurrencyXePlaywrightInPython`.

## What it keeps from the Python repo

- Reads conversion test data from CSV
- Opens the XE Currency Converter page
- Enters amount and currencies
- Captures the displayed rate/result
- Calculates the expected conversion value
- Compares expected and actual results

## Stack

- Java 17
- Maven
- Playwright for Java
- Cucumber JVM
- JUnit 5 platform engine
- AssertJ

## Project structure

```text
src
└── test
    ├── java
    │   └── com/sanjquant/xe
    │       ├── context
    │       ├── hooks
    │       ├── pages
    │       ├── runner
    │       ├── steps
    │       └── utils
    └── resources
        ├── config
        ├── features
        └── testdata
```

## Setup

### 1) Install dependencies

```bash
mvn clean test-compile
```

### 2) Install Playwright browsers

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"
```

On Windows, you can usually run:

```powershell
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

## Run tests

Run the smoke suite:

```bash
mvn test
```

Run headed mode:

```bash
mvn test -Dheadless=false
```

Point to a different environment URL:

```bash
mvn test -Dbase.url=https://www.xe.com/currencyconverter/
```

## Notes

- The XE DOM can change, so selectors may need minor maintenance.
- This version keeps the Python repo's page-object and CSV-driven idea, but expresses it in Cucumber steps.
- The feature currently reads from `src/test/resources/testdata/curr_pair.csv`.
