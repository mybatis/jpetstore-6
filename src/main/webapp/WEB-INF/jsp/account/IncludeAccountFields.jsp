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
<h3>Account Information</h3>

<table>
<tr>
<td>First name:</td>
<td><input type="text" name="firstName" value="${account.firstName}" /></td>
</tr>
<tr>
<td>Last name:</td>
<td><input type="text" name="lastName" value="${account.lastName}" /></td>
</tr>
<tr>
<td>Email:</td>
<td><input type="text" size="40" name="email" value="${account.email}" /></td>
</tr>
<tr>
<td>Phone:</td>
<td><input type="text" name="phone" value="${account.phone}" /></td>
</tr>
<tr>
<td>Address 1:</td>
<td><input type="text" size="40" name="address1" value="${account.address1}" /></td>
</tr>
<tr>
<td>Address 2:</td>
<td><input type="text" size="40" name="address2" value="${account.address2}" /></td>
</tr>
<tr>
<td>City:</td>
<td><input type="text" name="city" value="${account.city}" /></td>
</tr>
<tr>
<td>State:</td>
<td><input type="text" size="4" name="state" value="${account.state}" /></td>
</tr>
<tr>
<td>Zip:</td>
<td><input type="text" size="10" name="zip" value="${account.zip}" /></td>
</tr>
<tr>
<td>Country:</td>
<td><input type="text" size="15" name="country" value="${account.country}" /></td>
</tr>
</table>

<h3>Profile Information</h3>

<table>
<tr>
<td>Language Preference:</td>
<td><select name="languagePreference">
<c:forEach var="lang" items="${languages}">
<option value="${lang}" <c:if test="${account.languagePreference == lang}">selected</c:if>>${lang}</option>
</c:forEach>
</select></td>
</tr>
<tr>
<td>Favourite Category:</td>
<td><select name="favouriteCategoryId">
<c:forEach var="cat" items="${categories}">
<option value="${cat}" <c:if test="${account.favouriteCategoryId == cat}">selected</c:if>>${cat}</option>
</c:forEach>
</select></td>
</tr>
<tr>
<td>Enable MyList</td>
<td><input type="checkbox" name="listOption" value="true" <c:if test="${account.listOption}">checked</c:if> /></td>
</tr>
<tr>
<td>Enable MyBanner</td>
<td><input type="checkbox" name="bannerOption" value="true" <c:if test="${account.bannerOption}">checked</c:if> /></td>
</tr>

</table>
