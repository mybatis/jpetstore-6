FROM tomcat:8.0-alpine
ADD /home/vsts/work/1/a/target/jpetstore.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
