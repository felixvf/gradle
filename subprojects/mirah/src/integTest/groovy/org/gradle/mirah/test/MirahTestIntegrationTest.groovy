/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.mirah.test

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.DefaultTestExecutionResult
import org.gradle.integtests.fixtures.ForkMirahCompileInDaemonModeFixture
import org.gradle.integtests.fixtures.TestResources
import org.junit.Rule

class MirahTestIntegrationTest extends AbstractIntegrationSpec {
    @Rule TestResources resources = new TestResources(temporaryFolder)
    @Rule public final ForkMirahCompileInDaemonModeFixture forkMirahCompileInDaemonModeFixture = new ForkMirahCompileInDaemonModeFixture(executer, temporaryFolder)

    def executesTestsWithMultiLineDescriptions() {
        file("build.gradle") << """
apply plugin: 'mirah'

repositories {
    mavenCentral()
}

dependencies {
//  compile "org.mirah:mirah-library:0.1.5-SNAPSHOT"
    testCompile "org.mirahtest:mirahtest_2.11:2.1.5"
    testCompile "junit:junit:4.12"
}
        """

        when:
        file("src/test/mirah/MultiLineNameTest.mirah") << """
package org.gradle

import org.mirahtest.FunSuite
import org.junit.runner.RunWith
import org.mirahtest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MultiLineSuite extends FunSuite {
    test("This test method name\\nspans many\\nlines") {
        assert(1 === 1)
    }
}
        """

        then:
        succeeds("test")

        def result = new DefaultTestExecutionResult(testDirectory)
        result.assertTestClassesExecuted("org.gradle.MultiLineSuite")
	    result.testClass("org.gradle.MultiLineSuite").assertTestPassed("This test method name\nspans many\nlines")
    }
}
