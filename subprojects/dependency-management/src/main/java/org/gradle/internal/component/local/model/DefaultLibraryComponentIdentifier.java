/*
 * Copyright 2015 the original author or authors.
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
package org.gradle.internal.component.local.model;

import com.google.common.base.Objects;
import org.gradle.api.artifacts.component.LibraryComponentIdentifier;

public class DefaultLibraryComponentIdentifier implements LibraryComponentIdentifier {
    private final String projectPath;
    private final String libraryName;
    private final String displayName;
    private final String variant;

    public String getDisplayName() {
        return displayName;
    }

    public DefaultLibraryComponentIdentifier(String projectPath, String libraryName, String variant) {
        assert projectPath != null : "project path cannot be null";
        assert libraryName != null : "library name cannot be null";
        assert variant != null : "variant cannot be null";
        this.projectPath = projectPath;
        this.libraryName = libraryName;
        this.variant = variant;
        this.displayName = String.format("project '%s' library '%s' variant '%s'", projectPath, libraryName, variant);
    }

    @Override
    public String getProjectPath() {
        return projectPath;
    }

    @Override
    public String getLibraryName() {
        return libraryName;
    }

    @Override
    public String getVariant() {
        return variant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultLibraryComponentIdentifier that = (DefaultLibraryComponentIdentifier) o;
        return Objects.equal(projectPath, that.projectPath)
            && Objects.equal(libraryName, that.libraryName)
            && Objects.equal(variant, that.variant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectPath, libraryName, variant);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
