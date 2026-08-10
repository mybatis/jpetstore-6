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

<div id="Catalog">
<form method="post" action="${pageContext.request.contextPath}/account/signon">

<p>Please enter your username and password.</p>
<p>Username:<input type="text" name="username" value="j2ee" /> <br />
Password:<input type="password" name="password" value="j2ee" /></p>
<input type="submit" name="signon" value="Login" />

</form> Need a user name and password? <a href="${pageContext.request.contextPath}/account/new">Register Now!</a></div>

<%@ include file="../common/IncludeBottom.jsp"%>
