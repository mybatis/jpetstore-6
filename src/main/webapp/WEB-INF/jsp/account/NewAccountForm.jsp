<%@ include file="../common/IncludeTop.jsp"%>

<div id="Catalog"><stripes:form
	beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
	focus="">

	<h3>User Information</h3>

	<table>
		<tr>
			<td>User ID:</td>
			<td><stripes:text name="username" /></td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><stripes:text name="password" /></td>
		</tr>
		<tr>
			<td>Repeat password:</td>
			<td><stripes:text name="repeatedPassword" /></td>
		</tr>
	</table>

	<%@ include file="IncludeAccountFields.jsp"%>

	<stripes:submit name="newAccount" value="Save Account Information" />

</stripes:form></div>

<%@ include file="../common/IncludeBottom.jsp"%>