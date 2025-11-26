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

<h3>Account Information</h3>

<table>
	<tr>
		<td>First name:</td>
		<td><stripes:text name="account.firstName" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><stripes:text name="account.lastName" /></td>
	</tr>
	<tr>
		<td>Email:</td>
		<td><stripes:text size="40" name="account.email" /></td>
	</tr>
	<tr>
		<td>Phone:</td>
		<td><stripes:text name="account.phone" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><stripes:text size="40" name="account.address1" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><stripes:text size="40" name="account.address2" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><stripes:text name="account.city" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><stripes:text size="4" name="account.state" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><stripes:text size="10" name="account.zip" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><stripes:text size="15" name="account.country" /></td>
	</tr>
</table>

<h3>Profile Information</h3>

<table>
	<tr>
		<td>Language Preference:</td>
		<td><stripes:select name="account.languagePreference">
			<stripes:options-collection collection="${actionBean.languages}" />
		</stripes:select></td>
	</tr>
	<tr>
		<td>Favourite Category:</td>
		<td><stripes:select name="account.favouriteCategoryId">
			<stripes:options-collection collection="${actionBean.categories}" />
		</stripes:select></td>
	</tr>
	<tr>
		<td>Enable MyList</td>
		<td><stripes:checkbox name="account.listOption" /></td>
	</tr>
	<tr>
		<td>Enable MyBanner</td>
		<td><stripes:checkbox name="account.bannerOption" /></td>
	</tr>

</table>

<h3>Lifestyle Information (for AI Recommendation)</h3>

<table>
	<tr>
		<td>Age:</td>
		<td><stripes:text name="account.age" size="10" /></td>
	</tr>
	<tr>
		<td>Occupation:</td>
		<td><stripes:text name="account.occupation" size="30" /></td>
	</tr>
	<tr>
		<td>Daily Home Hours:</td>
		<td>
			<stripes:select name="account.homeHours">
				<stripes:option value="">선택하세요</stripes:option>
				<stripes:option value="LESS_THAN_2">2시간 미만</stripes:option>
				<stripes:option value="TWO_TO_SIX">2-6시간</stripes:option>
				<stripes:option value="MORE_THAN_6">6시간 이상</stripes:option>
			</stripes:select>
		</td>
	</tr>
	<tr>
		<td>Housing Type:</td>
		<td>
			<stripes:select name="account.housingType">
				<stripes:option value="">선택하세요</stripes:option>
				<stripes:option value="STUDIO">원룸</stripes:option>
				<stripes:option value="APARTMENT">아파트</stripes:option>
				<stripes:option value="HOUSE">단독주택</stripes:option>
			</stripes:select>
		</td>
	</tr>
	<tr>
		<td>Monthly Pet Budget:</td>
		<td>
			<stripes:select name="account.monthlyBudget">
				<stripes:option value="">선택하세요</stripes:option>
				<stripes:option value="UNDER_100K">10만원 미만</stripes:option>
				<stripes:option value="BETWEEN_100K_300K">10-30만원</stripes:option>
				<stripes:option value="OVER_300K">30만원 이상</stripes:option>
			</stripes:select>
		</td>
	</tr>
	<tr>
		<td>Animal Fur Allergy:</td>
		<td>
			<stripes:radio name="account.hasAllergy" value="true" /> Yes
			<stripes:radio name="account.hasAllergy" value="false" checked="checked" /> No
		</td>
	</tr>

</table>
