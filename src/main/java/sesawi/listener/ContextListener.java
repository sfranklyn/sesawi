/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
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
package sesawi.listener;

import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import sesawi.ejb.service.ConfigsServiceBean;
import sesawi.jpa.Configs;
import sesawi.jsf.beans.AppBean;

/**
 * Web application life cycle listener.
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
public class ContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ContextListener.class.getName());
    @Inject
    private AppBean appBean;
    @Inject
    private ConfigsServiceBean configsServiceBean;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext sc = sce.getServletContext();
            LOG.log(Level.INFO, "sesawi: Context initialized {0}", sc.getContextPath());

            Configs configs = configsServiceBean.getByKey(ConfigsServiceBean.PASSWORD_EXPIRE_DAYS);
            if (configs == null) {
                configs = new Configs();
                configs.setConfigKey(ConfigsServiceBean.PASSWORD_EXPIRE_DAYS);
                configs.setConfigDesc("Password expire in days");
                configs.setConfigType("java.lang.Integer");
                configs.setConfigValue("90");
                configsServiceBean.saveCreate(configs, Locale.getDefault());
            }

            appBean.setComputerName(ManagementFactory.getRuntimeMXBean().
                    getName().split("@")[1]);
        } catch (Exception ex) {
            LOG.severe(ex.toString());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.log(Level.INFO, "Context destroyed {0}", sce.getServletContext().getContextPath());
    }

}
