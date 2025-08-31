#
#    Copyright 2010-2025 the original author or authors.
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#       https://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

# Use OpenJDK 21 as the base image
FROM openjdk:21

# Copy all files from the current directory into /usr/src/myapp in the container
COPY . /usr/src/myapp

# Specify the directory to work in within the container
WORKDIR /usr/src/myapp

# Compile & package the project using Maven Wrapper
RUN ./mvnw clean package

# Run the application using Maven Cargo plugin with the 'tomcat90' profile
CMD ./mvnw cargo:run -P tomcat90

