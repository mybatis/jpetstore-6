#
#    Copyright 2010-2020 the original author or authors.
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

FROM tomcat:9

MAINTAINER Jerome Loisel

ENV JAVA_OPTS="${JAVA_OPTS} -javaagent:${CATALINA_HOME}/dd-java-agent.jar"

ADD https://dtdg.co/latest-java-tracer ${CATALINA_HOME}/dd-java-agent.jar
RUN rm -rf ${CATALINA_HOME}/webapps/ ${CATALINA_HOME}/work/Catalina/localhost
ADD target/jpetstore.war ${CATALINA_HOME}/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
