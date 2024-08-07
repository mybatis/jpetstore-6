# This app works on jdk 17 and tomcat 9
FROM tomcat:9.0.93-jdk17

# Set the environment variable for the Tomcat home directory
ENV CATALINA_HOME /usr/local/tomcat

# Copy the WAR file from the intermediate stage to the webapps directory
COPY jpetstore.war $CATALINA_HOME/webapps/jpetstore.war

# Expose the port where Tomcat will run
EXPOSE 8080

# Start Tomcat
ENTRYPOINT ["catalina.sh", "run"]