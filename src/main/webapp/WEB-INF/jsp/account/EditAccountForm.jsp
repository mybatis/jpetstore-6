<%@ include file="../common/IncludeTop.jsp"%>

<div id="Catalog"><stripes:form
	beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
	focus="">

	<h3>User Information</h3>

	<table>
		<tr>
			<td>User ID:</td>
			<td>${actionBean.username}</td>
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

	<stripes:submit name="editAccount" value="Save Account Information" />

</stripes:form> <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.OrderActionBean"
	event="listOrders">My Orders</stripes:link></div>

<%@ include file="../common/IncludeBottom.jsp"%>
