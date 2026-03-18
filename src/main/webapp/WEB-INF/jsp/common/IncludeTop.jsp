<%--

       Copyright 2010-2026 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          https://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/css/jpetstore.css" type="text/css"
media="screen" />
<meta name="generator"
content="HTML Tidy for Linux/x86 (vers 1st November 2002), see www.w3.org" />
<title>JPetStore Demo</title>
<meta content="text/html; charset=windows-1252"
http-equiv="Content-Type" />
<meta http-equiv="Cache-Control" content="max-age=0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="Pragma" content="no-cache" />
</head>

<body>

<div id="Header">

<div id="Logo">
<div id="LogoContent"><a href="${pageContext.request.contextPath}/catalog"><img src="${pageContext.request.contextPath}/images/logo-topbar.gif" /></a></div>
</div>

<div id="Menu">
<div id="MenuContent">
<a href="${pageContext.request.contextPath}/cart"><img align="middle" name="img_cart" src="${pageContext.request.contextPath}/images/cart.gif" /></a>
<img align="middle" src="${pageContext.request.contextPath}/images/separator.gif" />
<c:choose>
  <c:when test="${sessionScope.accountBean == null || !sessionScope.accountBean.authenticated}">
    <a href="${pageContext.request.contextPath}/account">Sign In</a>
  </c:when>
  <c:otherwise>
    <a href="${pageContext.request.contextPath}/account/signoff">Sign Out</a>
    <img align="middle" src="${pageContext.request.contextPath}/images/separator.gif" />
    <a href="${pageContext.request.contextPath}/account/edit">My Account</a>
  </c:otherwise>
</c:choose>
<img align="middle" src="${pageContext.request.contextPath}/images/separator.gif" />
<a href="${pageContext.request.contextPath}/help.html">?</a>
</div>
</div>

<div id="Search">
<div id="SearchContent">
<form method="get" action="${pageContext.request.contextPath}/catalog/searchProducts">
    <input type="text" name="keyword" size="14" />
    <input type="submit" name="searchProducts" value="Search" />
</form>
</div>
</div>

<div id="QuickLinks">
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=FISH"><img src="${pageContext.request.contextPath}/images/sm_fish.gif" /></a>
<img src="${pageContext.request.contextPath}/images/separator.gif" />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=DOGS"><img src="${pageContext.request.contextPath}/images/sm_dogs.gif" /></a>
<img src="${pageContext.request.contextPath}/images/separator.gif" />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=REPTILES"><img src="${pageContext.request.contextPath}/images/sm_reptiles.gif" /></a>
<img src="${pageContext.request.contextPath}/images/separator.gif" />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=CATS"><img src="${pageContext.request.contextPath}/images/sm_cats.gif" /></a>
<img src="${pageContext.request.contextPath}/images/separator.gif" />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=BIRDS"><img src="${pageContext.request.contextPath}/images/sm_birds.gif" /></a>
</div>

</div>

<div id="Content">
<c:if test="${not empty message}">
<ul class="messages"><li>${message}</li></ul>
</c:if>
