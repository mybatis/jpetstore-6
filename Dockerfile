FROM tomcat:9

MAINTAINER Jerome Loisel

RUN rm -rf ${CATALINA_HOME}/webapps/ ${CATALINA_HOME}/work/Catalina/localhost
ADD target/jpetstore.war ${CATALINA_HOME}/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
