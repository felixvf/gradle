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

package org.gradle.api.internal.artifacts;

import org.gradle.api.Action;
import org.gradle.api.artifacts.ResolveException;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.artifacts.result.ResolutionResult;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.oldresult.ResolvedArtifactsBuilder;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.oldresult.ResolvedGraphResults;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.oldresult.TransientConfigurationResultsBuilder;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.projectresult.ResolvedLocalComponentsResult;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.projectresult.ResolvedProjectConfiguration;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.ResolvedArtifactsContainer;

public class DefaultResolverResults implements ResolverResults {
    private ResolvedConfiguration resolvedConfiguration;
    private ResolutionResult resolutionResult;
    private ResolveException fatalFailure;
    private ResolvedLocalComponentsResult resolvedLocalComponentsResult;
    private TransientConfigurationResultsBuilder transientConfigurationResultsBuilder;
    private ResolvedGraphResults graphResults;
    private ResolvedArtifactsBuilder artifactResults;

    @Override
    public boolean hasError() {
        if (fatalFailure != null) {
            return true;
        }
        if (graphResults != null && graphResults.hasError()) {
            return true;
        }
        if (resolvedConfiguration != null && resolvedConfiguration.hasError()) {
            return true;
        }
        return false;
    }

    //old model, slowly being replaced by the new model
    @Override
    public ResolvedConfiguration getResolvedConfiguration() {
        assertHasArtifacts();
        return resolvedConfiguration;
    }

    //new model
    @Override
    public ResolutionResult getResolutionResult() {
        assertHasResult();
        if (fatalFailure != null) {
            throw fatalFailure;
        }
        return resolutionResult;
    }

    // TODO:DAZ Remove this
    public void eachResolvedProject(Action<ResolvedProjectConfiguration> action) {
        assertHasResult();
        if (fatalFailure != null) {
            throw fatalFailure;
        }
        for (ResolvedProjectConfiguration resolvedProjectConfiguration : resolvedLocalComponentsResult.getResolvedProjectConfigurations()) {
            action.execute(resolvedProjectConfiguration);
        }
    }

    @Override
    public ResolvedLocalComponentsResult getResolvedLocalComponents() {
        assertHasResult();
        if (fatalFailure != null) {
            throw fatalFailure;
        }
        return resolvedLocalComponentsResult;
    }

    @Override
    public ResolvedArtifactsContainer getResolvedArtifacts() {
        return artifactResults.resolve();
    }

    private void assertHasResult() {
        if (resolutionResult == null && fatalFailure == null) {
            throw new IllegalStateException("Resolution result has not been attached.");
        }
    }

    private void assertHasArtifacts() {
        if (resolvedConfiguration == null) {
            throw new IllegalStateException("Resolution artifacts have not been attached.");
        }
    }

    public void resolved(ResolutionResult resolutionResult, ResolvedLocalComponentsResult resolvedLocalComponentsResult) {
        this.resolutionResult = resolutionResult;
        this.resolvedLocalComponentsResult = resolvedLocalComponentsResult;
        this.fatalFailure = null;
    }

    public void failed(ResolveException failure) {
        this.resolutionResult = null;
        this.fatalFailure = failure;
    }

    public void retainState(ResolvedGraphResults graphResults, ResolvedArtifactsBuilder artifactResults, TransientConfigurationResultsBuilder transientConfigurationResultsBuilder) {
        this.graphResults = graphResults;
        this.artifactResults = artifactResults;
        this.transientConfigurationResultsBuilder = transientConfigurationResultsBuilder;
    }

    public void withResolvedConfiguration(ResolvedConfiguration resolvedConfiguration) {
        this.resolvedConfiguration = resolvedConfiguration;
        this.graphResults = null;
        this.transientConfigurationResultsBuilder = null;
        this.artifactResults = null;
    }

    public ResolvedGraphResults getGraphResults() {
        return graphResults;
    }

    public ResolvedArtifactsBuilder getArtifactsBuilder() {
        return artifactResults;
    }

    public TransientConfigurationResultsBuilder getTransientConfigurationResultsBuilder() {
        return transientConfigurationResultsBuilder;
    }
}
