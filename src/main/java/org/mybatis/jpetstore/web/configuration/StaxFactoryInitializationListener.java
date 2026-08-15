/*
 *    Copyright 2010-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.web.configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StaxFactoryInitializationListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // Force Java 17 / Payara to explicitly use the JDK's built-in StAX factory
    // instead of searching via the broken OSGi/HK2 service loader context.
    System.setProperty("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl");
    System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
    System.setProperty("javax.xml.stream.XMLEventFactory", "com.sun.xml.internal.stream.events.XMLEventFactoryImpl");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // Cleanup if necessary
  }
}
