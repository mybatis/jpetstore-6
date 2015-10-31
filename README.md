MyBatis JPetStore
=================

[![Build Status](https://travis-ci.org/mybatis/jpetstore-6.svg?branch=master)](https://travis-ci.org/mybatis/jpetstore-6)
[![Coverage Status](https://coveralls.io/repos/mybatis/jpetstore-6/badge.svg?branch=master&service=github)](https://coveralls.io/github/mybatis/jpetstore-6?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/5619aafaa193340f320005fe/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5619aafaa193340f320005fe)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

![mybatis-jpetstore](http://mybatis.github.io/images/mybatis-logo.png)

JPetStore 6 is a full web application built on top of MyBatis 3, Spring 3 and Stripes. It is available for downloading in the downloads section of MyBatis project site. In this section we will walk through this sample to understand how is it built and learn how to run it.

See: http://www.mybatis.org/spring/sample.html

## Other versions that you may want to know about

- JPetstore on top of Spring, Spring MVC, MyBatis 3, and Spring Security https://github.com/making/spring-jpetstore
- JPetstore with Spring boot https://github.com/igor-baiborodine/jpetstore-6

## Running with Tomcat 7
Running JPetStore sample under Tomcat 7 (using maven).
- Clone this repository
- Open command prompt/shell and change to cloned directory
- Issue following command to run project using Tomcat 7

mvn clean tomcat7:run

- Run application in browser http://localhost:8080/jpetstore/ 
- Press Ctrl-C to stop the server.
