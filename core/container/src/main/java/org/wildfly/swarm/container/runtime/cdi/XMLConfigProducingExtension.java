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
package org.wildfly.swarm.container.runtime.cdi;

import java.net.URL;
import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.wildfly.swarm.bootstrap.performance.Performance;
import org.wildfly.swarm.container.runtime.xmlconfig.XMLConfig;
import org.wildfly.swarm.spi.api.cdi.CommonBean;
import org.wildfly.swarm.spi.api.cdi.CommonBeanBuilder;

/**
 * Produces any explicitly-set XML configuration URL (standalone.xml)
 *
 * @author Bob McWhirter
 */
public class XMLConfigProducingExtension implements Extension {

    private final Optional<URL> xmlConfig;

    public XMLConfigProducingExtension(Optional<URL> xmlConfig) {
        this.xmlConfig = xmlConfig;
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager beanManager) throws Exception {
        try (AutoCloseable handle = Performance.time("XMLConfigProducingExtension.afterBeanDiscovery")) {
            CommonBean<URL> urlBean = CommonBeanBuilder.newBuilder(URL.class)
                    .beanClass(XMLConfigProducingExtension.class)
                    .scope(Dependent.class)
                    .addQualifier(XMLConfig.Literal.INSTANCE)
                    .createSupplier(this::getXMLConfig)
                    .addType(URL.class)
                    .addType(Object.class).build();
            abd.addBean(urlBean);
        }
    }

    private URL getXMLConfig() {
        if (this.xmlConfig.isPresent()) {
            return this.xmlConfig.get();
        }
        return null;
    }

}
