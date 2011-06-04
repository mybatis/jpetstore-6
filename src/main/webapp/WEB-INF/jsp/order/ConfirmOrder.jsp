<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
	Return to Main Menu
	</stripes:link></div>

<div id="Catalog">Please confirm the information below and then
press continue...

<table>
	<tr>
		<th align="center" colspan="2"><font size="4"><b>Order</b></font>
		<br />
		<font size="3"><b> <fmt:formatDate
			value="${actionBean.order.orderDate}" pattern="yyyy/MM/dd hh:mm:ss" /></b></font>
		</th>
	</tr>

	<tr>
		<th colspan="2">Billing Address</th>
	</tr>
	<tr>
		<td>First name:</td>
		<td><c:out value="${actionBean.order.billToFirstName}" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><c:out value="${actionBean.order.billToLastName}" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><c:out value="${actionBean.order.billAddress1}" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><c:out value="${actionBean.order.billAddress2}" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><c:out value="${actionBean.order.billCity}" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><c:out value="${actionBean.order.billState}" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><c:out value="${actionBean.order.billZip}" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><c:out value="${actionBean.order.billCountry}" /></td>
	</tr>
	<tr>
		<th colspan="2">Shipping Address</th>
	</tr>
	<tr>
		<td>First name:</td>
		<td><c:out value="${actionBean.order.shipToFirstName}" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><c:out value="${actionBean.order.shipToLastName}" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><c:out value="${actionBean.order.shipAddress1}" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><c:out value="${actionBean.order.shipAddress2}" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><c:out value="${actionBean.order.shipCity}" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><c:out value="${actionBean.order.shipState}" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><c:out value="${actionBean.order.shipZip}" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><c:out value="${actionBean.order.shipCountry}" /></td>
	</tr>

</table>

<stripes:link class="Button"
	beanclass="org.mybatis.jpetstore.web.actions.OrderActionBean"
	event="newOrder">
	<stripes:param name="confirmed" value="true" />
Confirm
</stripes:link></div>

<%@ include file="../common/IncludeBottom.jsp"%>





