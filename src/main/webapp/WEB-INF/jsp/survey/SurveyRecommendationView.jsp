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
<%@ include file="../common/IncludeTop.jsp"%>

<div id="Catalog">

  <h2>Survey Recommendations</h2>

  <table class="table table-striped">
    <thead>
      <tr>
        <th>ID</th>
        <th>Residence Env</th>
        <th>Care Period</th>
        <th>Pet Color Pref</th>
        <th>Pet Size Pref</th>
        <th>Activity Time</th>
        <th>Diet Management</th>
        <th>Recommended JSON Data</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="rec" items="${actionBean.surveyRecommendations}">
        <tr>
          <td>${rec.surveyRecommendationId}</td>
          <td>${rec.residenceEnv}</td>
          <td>${rec.carePeriod}</td>
          <td>${rec.petColorPref}</td>
          <td>${rec.petSizePref}</td>
          <td>${rec.activityTime}</td>
          <td>${rec.dietManagement}</td>
          <td><pre>${rec.recommendedJsonData}</pre></td>
        </tr>
      </c:forEach>
      <c:if test="${empty actionBean.surveyRecommendations}">
        <tr>
          <td colspan="8">No survey recommendations found.</td>
        </tr>
      </c:if>
    </tbody>
  </table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>