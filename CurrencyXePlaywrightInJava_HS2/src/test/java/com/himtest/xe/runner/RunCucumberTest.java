package com.himtest.xe.runner;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.himtest.xe")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report.html, json:target/cucumber-report.json")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@smoketest")
public class RunCucumberTest {
}