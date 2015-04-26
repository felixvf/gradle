/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.tasks.mirah;

import org.gradle.api.file.FileTree;
import org.gradle.language.base.internal.compile.Compiler;
import org.gradle.api.internal.tasks.compile.JavaCompileSpec;
import org.gradle.api.tasks.WorkResult;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;

public class DefaultMirahJavaJointCompiler implements Compiler<MirahJavaJointCompileSpec> {
    private final Compiler<MirahCompileSpec> mirahCompiler;
    private final Compiler<JavaCompileSpec> javaCompiler;

    public DefaultMirahJavaJointCompiler(Compiler<MirahCompileSpec> mirahCompiler, Compiler<JavaCompileSpec> javaCompiler) {
        this.mirahCompiler = mirahCompiler;
        this.javaCompiler = javaCompiler;
    }

    public WorkResult execute(MirahJavaJointCompileSpec spec) {
        return mirahCompiler.execute(spec);
    }
}
