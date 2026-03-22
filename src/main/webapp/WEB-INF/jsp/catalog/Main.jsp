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
<%@ include file="../common/IncludeTop.jsp"%>

<div id="Welcome">
<div id="WelcomeContent"><c:if
test="${sessionScope.accountBean != null }">
<c:if test="${sessionScope.accountBean.authenticated}">
        Welcome ${sessionScope.accountBean.account.firstName}!
      </c:if>
</c:if></div>
</div>

<div id="Main">
<div id="Sidebar">
<div id="SidebarContent">
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=FISH"><img src="${pageContext.request.contextPath}/images/fish_icon.gif" /></a> <br />
Saltwater, Freshwater <br />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=DOGS"><img src="${pageContext.request.contextPath}/images/dogs_icon.gif" /></a> <br />
Various Breeds <br />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=CATS"><img src="${pageContext.request.contextPath}/images/cats_icon.gif" /></a> <br />
Various Breeds, Exotic Varieties <br />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=REPTILES"><img src="${pageContext.request.contextPath}/images/reptiles_icon.gif" /></a> <br />
Lizards, Turtles, Snakes <br />
<a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=BIRDS"><img src="${pageContext.request.contextPath}/images/birds_icon.gif" /></a> <br />
Exotic Varieties</div>
</div>

<div id="MainImage">
<div id="MainImageContent">
  <map name="estoremap">
<area alt="Birds" coords="72,2,280,250"
href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=BIRDS" shape="RECT" />
<area alt="Fish" coords="2,180,72,250"
href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=FISH" shape="RECT" />
<area alt="Dogs" coords="60,250,130,320"
href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=DOGS" shape="RECT" />
<area alt="Reptiles" coords="140,270,210,340"
href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=REPTILES" shape="RECT" />
<area alt="Cats" coords="225,240,295,310"
href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=CATS" shape="RECT" />
<area alt="Birds" coords="280,180,350,250"
href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=BIRDS" shape="RECT" />
  </map>
  <img height="355" src="${pageContext.request.contextPath}/images/splash.gif" align="middle"
usemap="#estoremap" width="350" /></div>
</div>

<div id="Separator">&nbsp;</div>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
