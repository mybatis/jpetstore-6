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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class StaxFactoryInitializationListenerTest.
 */
class StaxFactoryInitializationListenerTest {

  /**
   * Context initialized sets stax factory properties.
   */
  @Test
  void contextInitializedSetsStaxFactoryProperties() {
    String xmlInputFactory = System.getProperty("javax.xml.stream.XMLInputFactory");
    String xmlOutputFactory = System.getProperty("javax.xml.stream.XMLOutputFactory");
    String xmlEventFactory = System.getProperty("javax.xml.stream.XMLEventFactory");

    try {
      System.clearProperty("javax.xml.stream.XMLInputFactory");
      System.clearProperty("javax.xml.stream.XMLOutputFactory");
      System.clearProperty("javax.xml.stream.XMLEventFactory");

      new StaxFactoryInitializationListener().contextInitialized(null);

      Assertions.assertEquals("com.sun.xml.internal.stream.XMLInputFactoryImpl",
          System.getProperty("javax.xml.stream.XMLInputFactory"));
      Assertions.assertEquals("com.sun.xml.internal.stream.XMLOutputFactoryImpl",
          System.getProperty("javax.xml.stream.XMLOutputFactory"));
      Assertions.assertEquals("com.sun.xml.internal.stream.events.XMLEventFactoryImpl",
          System.getProperty("javax.xml.stream.XMLEventFactory"));
    } finally {
      restoreProperty("javax.xml.stream.XMLInputFactory", xmlInputFactory);
      restoreProperty("javax.xml.stream.XMLOutputFactory", xmlOutputFactory);
      restoreProperty("javax.xml.stream.XMLEventFactory", xmlEventFactory);
    }
  }

  /**
   * Context destroyed does nothing.
   */
  @Test
  void contextDestroyedDoesNothing() {
    String xmlInputFactory = System.getProperty("javax.xml.stream.XMLInputFactory");
    String xmlOutputFactory = System.getProperty("javax.xml.stream.XMLOutputFactory");
    String xmlEventFactory = System.getProperty("javax.xml.stream.XMLEventFactory");

    try {
      System.setProperty("javax.xml.stream.XMLInputFactory", "test-input");
      System.setProperty("javax.xml.stream.XMLOutputFactory", "test-output");
      System.setProperty("javax.xml.stream.XMLEventFactory", "test-event");

      new StaxFactoryInitializationListener().contextDestroyed(null);

      Assertions.assertEquals("test-input", System.getProperty("javax.xml.stream.XMLInputFactory"));
      Assertions.assertEquals("test-output", System.getProperty("javax.xml.stream.XMLOutputFactory"));
      Assertions.assertEquals("test-event", System.getProperty("javax.xml.stream.XMLEventFactory"));
    } finally {
      restoreProperty("javax.xml.stream.XMLInputFactory", xmlInputFactory);
      restoreProperty("javax.xml.stream.XMLOutputFactory", xmlOutputFactory);
      restoreProperty("javax.xml.stream.XMLEventFactory", xmlEventFactory);
    }
  }

  /**
   * Restore property.
   *
   * @param name
   *          the name
   * @param value
   *          the value
   */
  private static void restoreProperty(String name, String value) {
    if (value == null) {
      System.clearProperty(name);
    } else {
      System.setProperty(name, value);
    }
  }

}
