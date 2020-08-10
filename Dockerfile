FROM tomcat:8.0-alpine
RUN rm -f /usr/local/tomcat/webapps/*
COPY target/jpetstore.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
