/*
 * Copyright 2008 the original author or authors.
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
package org.gradle.execution;

import org.gradle.api.internal.GradleInternal;

/**
 * Selects and executes the tasks requested for a build.
 */
public interface BuildExecuter {

    /**
     * Selects the tasks to execute, if any. This method is called before any other methods on this executer.
     */
    void configure(GradleInternal gradle);

    /**
     * Returns the description of this executer. The result is used for log and error messages. Called after {@link
     * #configure(org.gradle.api.internal.GradleInternal)}.
     */
    String getDisplayName();

    /**
     * Executes the selected tasks. Called after {@link #configure(org.gradle.api.internal.GradleInternal)}.
     */
    void execute();
}
