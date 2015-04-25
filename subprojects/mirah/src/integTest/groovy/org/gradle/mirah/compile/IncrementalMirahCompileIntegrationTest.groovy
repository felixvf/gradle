/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.mirah.compile

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.ForkMirahCompileInDaemonModeFixture
import org.gradle.integtests.fixtures.TestResources
import org.junit.Rule
import spock.lang.Ignore
import spock.lang.Issue

class IncrementalMirahCompileIntegrationTest extends AbstractIntegrationSpec {

    @Rule TestResources resources = new TestResources(temporaryFolder)
    @Rule public final ForkMirahCompileInDaemonModeFixture daemonModeFixture = new ForkMirahCompileInDaemonModeFixture(executer, temporaryFolder)

    def recompilesSourceWhenPropertiesChange() {
        expect:
        run('compileMirah').assertTasksSkipped(':compileJava')

        when:
        file('build.gradle').text += '''
            compileMirah.options.debug = false
'''
        then:
        run('compileMirah').assertTasksSkipped(':compileJava')

        run('compileMirah').assertTasksSkipped(':compileJava', ':compileMirah')
    }

    def recompilesDependentClasses() {
        given:
        run("classes")

        when: // Update interface, compile should fail
        file('src/main/mirah/IPerson.mirah').assertIsFile().copyFrom(file('NewIPerson.mirah'))

        then:
        runAndFail("classes").assertHasDescription("Execution failed for task ':compileMirah'.")
    }

    @Issue("GRADLE-2548")
    @Ignore
    def recompilesMirahWhenJavaChanges() {
        file("build.gradle") << """
            apply plugin: 'mirah'

            repositories {
                mavenCentral()
            }

            dependencies {
//              compile 'org.mirah:mirah-library:0.1.5-SNAPSHOT'
            }
        """

        file("src/main/java/Person.java") << "public interface Person { String getName(); }"

        file("src/main/mirah/DefaultPerson.mirah") << """class DefaultPerson(name: String) extends Person {
    def getName(): String = name
}"""
        when:
        run('classes') //makes everything up-to-date

        //change the java interface
        file("src/main/java/Person.java").text = "public interface Person { String fooBar(); }"

        then:
        //the build should fail because the interface the mirah class needs has changed
        runAndFail("classes").assertHasDescription("Execution failed for task ':compileMirah'.")
    }
}
