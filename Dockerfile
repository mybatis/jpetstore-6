FROM tomcat:8.0-alpine
COPY /home/vsts/work/1/s/target/jpetstore.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
