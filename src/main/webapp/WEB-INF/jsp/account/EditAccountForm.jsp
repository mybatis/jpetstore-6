<%--

       Copyright 2010-2025 the original author or authors.

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
<%@ page language="java"
		 contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>

<%@ include file="../common/IncludeTop.jsp"%>

<div id="Catalog">
	<!-- Stripes 메시지 표시 -->
	<stripes:messages/>
	<stripes:errors/>

	<stripes:form
	beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
	focus="" id="editAccountForm">

	<h3>User Information</h3>

	<table>
		<tr>
			<td>User ID:</td>
			<td>${actionBean.username}</td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><stripes:password name="password" id="password" /></td>
		</tr>
		<tr>
			<td>Repeat password:</td>
			<td><stripes:password name="repeatedPassword" id="repeatedPassword" /></td>
		</tr>
	</table>
	<%@ include file="IncludeAccountFields.jsp"%>

	<stripes:submit name="editAccount" value="Save Account Information" onclick="return validateEditAccountForm();" />

</stripes:form> <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.OrderActionBean"
	event="listOrders">My Orders</stripes:link></div>

<script type="text/javascript">
function validateEditAccountForm() {
	var firstName = document.getElementById('account.firstName').value.trim();
	var lastName = document.getElementById('account.lastName').value.trim();
	var email = document.getElementById('account.email').value.trim();
	var phone = document.getElementById('account.phone').value.trim();
	var address1 = document.getElementById('account.address1').value.trim();
	var city = document.getElementById('account.city').value.trim();
	var state = document.getElementById('account.state').value.trim();
	var zip = document.getElementById('account.zip').value.trim();
	var country = document.getElementById('account.country').value.trim();

	if (!firstName || !lastName || !email || !phone || !address1 || !city || !state || !zip || !country) {
		alert('모든 필수 정보를 입력해주세요.');
		return false;
	}

	var password = document.getElementById('password').value.trim();
	var repeatedPassword = document.getElementById('repeatedPassword').value.trim();

	if (password || repeatedPassword) {
		if (password !== repeatedPassword) {
			alert('비밀번호가 일치하지 않습니다.');
			return false;
		}
	}

	return true;
}
</script>

<%@ include file="../common/IncludeBottom.jsp"%>
