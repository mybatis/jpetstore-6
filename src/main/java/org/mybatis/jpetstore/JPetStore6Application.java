/**
 *    Copyright 2010-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore;

import static javax.servlet.DispatcherType.REQUEST;

import net.sourceforge.stripes.controller.StripesFilter;

import org.mybatis.jpetstore.config.DataSourceConfig;
import org.mybatis.jpetstore.config.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import java.util.EnumSet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@SpringBootApplication
public class JPetStore6Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(new Class[]{JPetStore6Application.class}, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(
        DataSourceConfig.class,
        ServiceConfig.class);
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {

    servletContext.setInitParameter(
        "javax.servlet.jsp.jstl.fmt.localizationContext", "StripesResources");

    FilterRegistration.Dynamic stripesFilter = servletContext.addFilter(
        "stripesFilter", StripesFilter.class);
    stripesFilter.setInitParameter("ActionResolver.Packages", "org.mybatis.jpetstore.web");
    stripesFilter.setInitParameter(
        "Interceptor.Classes", "net.sourceforge.stripes.integration.spring.SpringInterceptor");
    stripesFilter.addMappingForServletNames(EnumSet.of(REQUEST), false, "stripesDispatcher");

    ServletRegistration.Dynamic stripesDispatcher = servletContext.addServlet(
        "stripesDispatcher", new net.sourceforge.stripes.controller.DispatcherServlet());
    stripesDispatcher.setLoadOnStartup(1);
    stripesDispatcher.addMapping("*.action");
    super.onStartup(servletContext);
  }
}
