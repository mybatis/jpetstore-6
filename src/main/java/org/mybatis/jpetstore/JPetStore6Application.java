package org.mybatis.jpetstore;

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

import static javax.servlet.DispatcherType.REQUEST;

@SpringBootApplication
public class JPetStore6Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(new Class[] {JPetStoreDemo6SpringBootApplication.class}, args);
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
