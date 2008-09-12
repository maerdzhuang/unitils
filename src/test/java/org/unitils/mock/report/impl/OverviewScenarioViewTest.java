/*
 * Copyright 2006-2007,  Unitils.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unitils.mock.report.impl;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.unitils.mock.Mock;
import org.unitils.mock.core.MockObject;
import org.unitils.mock.core.Scenario;

/**
 * Test for the creating an overview representation of a scenario.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 * @author Kenny Claes
 */
public class OverviewScenarioViewTest {

    /* class under test */
    private OverviewScenarioView overviewScenarioView;

    /* Test scenario */
    private Scenario scenario;

    /* Test mock that uses the scenario */
    private Mock<TestInterface> testMock;


    /** Initializes the test. */
    @Before
    public void setUp() {
        overviewScenarioView = new OverviewScenarioView();
        scenario = new Scenario();
        testMock = new MockObject<TestInterface>("testMock", TestInterface.class, false, scenario);
    }


    /** Test for creating a view containing 2 mock invocations. */
    @Test
    public void testCreateView() {
        TestInterface testProxy = testMock.getInstance();
        testProxy.testMethod1("value1");
        testProxy.testMethod2();

        String result = overviewScenarioView.createView(scenario);

        assertTrue(result.contains("testMethod1"));
        assertTrue(result.contains("testMethod2"));
    }


    /** Test for creating a view when there were no mock invocations. */
    @Test
    public void testCreateView_noInvocations() {
        String result = overviewScenarioView.createView(scenario);
        assertTrue(result.isEmpty());
    }


    /**
     * Test for creating a view when there is an argument value that is larger than 20 characters.
     * This value should have been replaced inline by string1 and then appended afterwards in a separate list.
     */
    @Test
    public void testCreateView_largeArgumentValue() {
        TestInterface testProxy = testMock.getInstance();
        testProxy.testMethod1("012345678901234567891");

        String result = overviewScenarioView.createView(scenario);
        assertTrue(result.contains("string1"));
        assertTrue(result.contains("012345678901234567891"));
    }


    /**
     * Test for creating a view when there is an result value that is larger than 20 characters.
     * This value should have been replaced inline by string1 and then appended afterwards in a separate list.
     */
    @Test
    public void testCreateView_largeResultValue() {
        testMock.returns("012345678901234567891").testMethod1(null);
        TestInterface testProxy = testMock.getInstance();
        testProxy.testMethod1(null);

        String result = overviewScenarioView.createView(scenario);
        assertTrue(result.contains("string1"));
        assertTrue(result.contains("012345678901234567891"));
    }


    /** Test interface that is going to be mocked. */
    public static interface TestInterface {

        public String testMethod1(String arg1);

        public void testMethod2();
    }

}