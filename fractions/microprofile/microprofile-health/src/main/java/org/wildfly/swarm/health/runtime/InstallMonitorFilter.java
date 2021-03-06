/**
 * Copyright 2015-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.health.runtime;

import org.jboss.shrinkwrap.api.Archive;
import org.wildfly.swarm.spi.api.DeploymentProcessor;
import org.wildfly.swarm.spi.runtime.annotations.DeploymentScoped;
import org.wildfly.swarm.undertow.WARArchive;

import javax.inject.Inject;

/**
 * @author Ken Finnigan
 */
@DeploymentScoped
public class InstallMonitorFilter implements DeploymentProcessor {

    @Inject
    public InstallMonitorFilter(Archive archive) {
        this.archive = archive;
    }

    @Override
    public void process() throws Exception {
        try {
            WARArchive warArchive = archive.as(WARArchive.class);
            /*warArchive.addDependency("org.wildfly.swarm:health-api:jar:" + SwarmInfo.VERSION);
            warArchive.addDependency("org.eclipse.microprofile.apis:microprofile-health-api:jar:" + MP_HEALTH_VERSION);*/
            warArchive.findWebXmlAsset().setContextParam("resteasy.scan", "true");
        } catch (Exception e) {
            throw new RuntimeException("Failed to install health processor", e);
        }
    }

    private final Archive archive;
}
