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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>

<head>
<link rel="StyleSheet" href="../css/jpetstore.css" type="text/css"
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

<!-- ★★★ 추가됨 START: 비교 팝업 HTML (CSS) ★★★ -->
 <style>
    .modal {
      display: none;
      position: fixed;
      z-index: 2000;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.4);
    }

    .modal-content {
      background-color: #fefefe;
      margin: 5% auto;
      padding: 20px;
      border: 1px solid #888;
      width: 85%;
      max-width: 1000px;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      max-height: 90vh;
      display: flex;
      flex-direction: column;
    }

    .modal-header {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 20px;
      border-bottom: 2px solid #008CBA;
      padding-bottom: 10px;
      position: relative;
    }

    .close-x {
      position: absolute;
      right: 0;
      top: 0;
      font-size: 35px;
      font-weight: bold;
      color: #aaa;
      cursor: pointer;
      transition: color 0.2s;
      line-height: 1;
    }

    .close-x:hover,
    .close-x:focus {
      color: #000;
    }

    .modal-body {
      margin-bottom: 20px;
      max-height: none;
      overflow-y: scroll !important;
      flex: 1;
    }

    .modal-footer {
      text-align: right;
      position: sticky;
      bottom: 0;
      background-color: #fefefe;
      padding-top: 15px;
      z-index: 10;
    }

    .close-btn {
      background-color: #f44336;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }

    .close-btn:hover {
      background-color: #da190b;
    }
 </style>
 <!-- ★★★ 추가됨 END: 비교 팝업 HTML (CSS) ★★★ -->

<style>
  /* 비교하기 버튼 - 우측 상단에 고정 */
  .compare-btn-fixed {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 12px 24px;
    background-color: #008CBA;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    font-weight: bold;
    z-index: 1000;
    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
  }

  .compare-btn-fixed:hover {
    background-color: #007399;
  }

  .compare-btn-fixed:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }

  .checkbox-cell {
    text-align: center;
    width: 50px;
  }
</style>

<!-- ★★★ 추가됨 START: 비교 기능 JavaScript ★★★ -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const tables = document.querySelectorAll('table.itemList');

    tables.forEach(table => {
      const rows = table.querySelectorAll('tr');

      if (rows.length > 0) {
        const headerCell = document.createElement('th');
        headerCell.className = 'checkbox-cell';
        headerCell.textContent = 'Select';
        rows[0].insertBefore(headerCell, rows[0].firstChild);

        for (let i = 1; i < rows.length; i++) {
          const checkbox = document.createElement('input');
          checkbox.type = 'checkbox';
          checkbox.name = 'selectedItems';
          checkbox.className = 'item-checkbox';
          <!--fix-->
          checkbox.value = rows[i].cells[0]?.innerText.trim();
          checkbox.onchange = updateCompareButton;

          const cell = document.createElement('td');
          cell.className = 'checkbox-cell';
          cell.appendChild(checkbox);
          rows[i].insertBefore(cell, rows[i].firstChild);
        }
      }
    });
  });

  function updateCompareButton() {
    const checkedBoxes = document.querySelectorAll('input[name="selectedItems"]:checked');
    const compareBtn = document.getElementById('compareBtn');

    if (compareBtn) {
      if (checkedBoxes.length === 2) {
        compareBtn.disabled = false;
      } else {
        compareBtn.disabled = true;
      }
    }
  }

  function openComparisonPopup() {
    const checkedBoxes = document.querySelectorAll('input[name="selectedItems"]:checked');

    if (checkedBoxes.length !== 2) {
      alert('Please select exactly 2 products.');
      return;
    }

    const selectedItems = [];
    checkedBoxes.forEach(checkbox => {
      selectedItems.push({
        itemId: checkbox.value
      });
    });

    showComparisonPopup(selectedItems);
  }

  function showComparisonPopup(items) {
    console.log('Selected products:', items);

    // 로딩 표시
    document.getElementById('comparisonContent').innerHTML = '<p>Analyzing products... Please wait.</p>';
    document.getElementById('comparisonModal').style.display = 'block';

     // 백엔드 API 호출
     fetch('/jpetstore/actions/Comparison.action', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'itemId1=' + items[0].itemId + '&itemId2=' + items[1].itemId
     })
      .then(response => {
          console.log('Response status:', response.status);
          console.log('Response ok:', response.ok);
          console.log('Response Content-Type:', response.headers.get('Content-Type'));
          return response.text();
      })
      .then(responseText => {
         console.log('Response text:', responseText);

         // 수동으로 JSON 파싱
         try {
             const responseData = JSON.parse(responseText);
             console.log('GPT Response (parsed):', responseData);
             console.log('Type of responseData:', typeof responseData);
             console.log('Keys:', Object.keys(responseData));

             displayComparisonResult(items, responseData);
         } catch (e) {
             console.error('JSON parse error:', e);
             console.error('Failed text:', responseText);
             document.getElementById('comparisonContent').innerHTML = '<p>Error parsing response.</p>';
         }
     })
      .catch(error => {
        console.error('Error:', error);
        console.error('Error stack:', error.stack);
        document.getElementById('comparisonContent').innerHTML = '<p>Error occurred during analysis.</p>';
      });
  }

  function displayComparisonResult(items, responseData) {
      console.log('========================================');
          console.log('=== displayComparisonResult START ===');
          console.log('========================================');
          console.log('items:', items);
          console.log('responseData:', responseData);
          console.log('typeof responseData:', typeof responseData);

          // 상품명
          const productName1 = responseData.item1_name || items[0].itemId || 'Product 1';
          const productName2 = responseData.item2_name || items[1].itemId || 'Product 2';
          console.log('productName1:', productName1);
          console.log('productName2:', productName2);

          // 가격
          const item1Price = responseData.item1_price || 'N/A';
          const item2Price = responseData.item2_price || 'N/A';
          console.log('item1Price:', item1Price);
          console.log('item2Price:', item2Price);

          // 사용자 정보
          const livingEnvUser = responseData.user_living_environment || 'Not specified';
          const petSizeUser = responseData.user_pet_size || 'Not specified';
          const activityTimeUser = responseData.user_activity_time || 'Not specified';
          console.log('user info:', livingEnvUser, petSizeUser, activityTimeUser);

          // gpt_analysis 안전하게 접근
          let gptAnalysis = responseData.gpt_analysis;
          console.log('gpt_analysis (raw):', gptAnalysis);
          console.log('typeof gpt_analysis:', typeof gptAnalysis);

          if (typeof gptAnalysis === 'string') {
            try {
              gptAnalysis = JSON.parse(gptAnalysis);
              console.log('gpt_analysis parsed successfully');
            } catch (e) {
              console.error('Failed to parse gptAnalysis:', e);
              gptAnalysis = {};
            }
          }

          if (!gptAnalysis || typeof gptAnalysis !== 'object') {
              console.error('gpt_analysis is not an object!');
              gptAnalysis = {};
          }



          // GPT 분석 데이터 추출
          const activityTimeAnalysis = gptAnalysis.activity_time || 'No analysis available';
          const livingEnvAnalysis = gptAnalysis.living_environment || 'No analysis available';
          const petSizeAnalysis = gptAnalysis.pet_size || 'No analysis available';
          const priceAnalysis = gptAnalysis.price || 'No analysis available';
          const product1Better = gptAnalysis.product1_better || 'No recommendation available';
          const product2Better = gptAnalysis.product2_better || 'No recommendation available';
          const recommendation = gptAnalysis.recommendation || 'No recommendation available';



          // HTML 생성 (옵션 2: 3열 구조)
          const comparisonContent = `
            <div style="margin-bottom: 20px; text-align: center;">
                    <h2>\${productName1} vs \${productName2}</h2>
          </div>
                  <!-- Product Comparison: 가격만 -->
                  <div style="margin-bottom: 20px; padding: 15px; background-color: #f9f9f9; border-radius: 4px;">
                    <h3>Product Comparison</h3>
                    <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
                      <tr style="background-color: #e8f4f8;">
                        <th style="border: 1px solid #ddd; padding: 10px; text-align: left; width: 30%;">Category</th>
                        <th style="border: 1px solid #ddd; padding: 10px; text-align: left; width: 35%;">\${productName1}</th>
                        <th style="border: 1px solid #ddd; padding: 10px; text-align: left; width: 35%;">\${productName2}</th>
                      </tr>
                      <tr>
                        <td style="border: 1px solid #ddd; padding: 10px; font-weight: bold;">Price</td>
                        <td style="border: 1px solid #ddd; padding: 10px;">\${item1Price}</td>
                        <td style="border: 1px solid #ddd; padding: 10px;">\${item2Price}</td>
                      </tr>
                    </table>
                  </div>

                  <!-- Your Preferences: 별도 박스 -->
                  <div style="margin-bottom: 20px; padding: 15px; background-color: #fff9e6; border-radius: 4px; border-left: 4px solid #ffc107;">
                    <h3>Your Preferences</h3>
                    <p style="margin: 5px 0; line-height: 1.6;"><strong>Living Environment:</strong> \${livingEnvUser}</p>
                    <p style="margin: 5px 0; line-height: 1.6;"><strong>Pet Size Preference:</strong> \${petSizeUser}</p>
                    <p style="margin: 5px 0; line-height: 1.6;"><strong>Activity Time:</strong> \${activityTimeUser}</p>
                  </div>

                  <!-- AI Analysis -->
                  <div style="margin-bottom: 20px; padding: 15px; background-color: #e3f2fd; border-radius: 4px;">
                    <h3>AI Analysis</h3>
                    <div style="margin-bottom: 10px;">
                      <p><strong>Price:</strong> \${priceAnalysis}</p>
                    </div>
                    <div style="margin-bottom: 10px;">
                      <p><strong>Living Environment:</strong> \${livingEnvAnalysis}</p>
                    </div>
                    <div style="margin-bottom: 10px;">
                      <p><strong>Pet Size:</strong> \${petSizeAnalysis}</p>
                    </div>
                    <div style="margin-bottom: 10px;">
                      <p><strong>Activity Time:</strong> \${activityTimeAnalysis}</p>
                    </div>
                  </div>

                  <!-- Recommendations -->
                  <div style="padding: 15px; background-color: #f1f8e9; border-radius: 4px;">
                    <h3>Recommendations</h3>

                    <div style="margin-bottom: 15px;">
                      <strong>\${productName1} is better when:</strong>
                      <p style="margin: 5px 0 0 0; line-height: 1.6;">\${product1Better}</p>
                    </div>

                    <div style="margin-bottom: 15px;">
                      <strong>\${productName2} is better when:</strong>
                      <p style="margin: 5px 0 0 0; line-height: 1.6;">\${product2Better}</p>
                    </div>

                    <div style="margin-top: 15px; padding: 15px; background-color: #fff3cd; border-left: 4px solid #ffc107; border-radius: 4px;">
                      <strong style="font-size: 16px;">📌 Final Recommendation</strong>
                      <p style="margin: 10px 0 0 0; line-height: 1.6;">\${recommendation}</p>
                    </div>
                  </div>
                `;


      document.getElementById('comparisonContent').innerHTML = comparisonContent;

  }

  function closeComparisonModal() {
          document.getElementById('comparisonModal').style.display = 'none';
  }

</script>
<!-- ★★★ 추가됨 END: 비교 기능 JavaScript ★★★ -->
</head>

<body>


  <div id="comparisonModal" class="modal">
    <div class="modal-content">
      <div class="modal-header">
        Product Comparison
        <span class="close-x" onclick="closeComparisonModal()">&times;</span>
      </div>
      <div class="modal-body" id="comparisonContent">
        <!-- 여기에 비교 내용이 들어갑니다 -->
      </div>
      <div class="modal-footer">
        <button class="close-btn" onclick="closeComparisonModal()">Close</button>
      </div>
    </div>
  </div>

<div id="Header">

<div id="Logo">
<div id="LogoContent"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
	<img src="../images/logo-topbar.gif" />
</stripes:link></div>
</div>

<div id="Menu">
<div id="MenuContent"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CartActionBean"
	event="viewCart">
	<img align="middle" name="img_cart" src="../images/cart.gif" />
</stripes:link> <img align="middle" src="../images/separator.gif" /> <c:if
	test="${sessionScope.accountBean == null}">
	<stripes:link
		beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
		event="signonForm">
          Sign In
	    </stripes:link>
</c:if> <c:if test="${sessionScope.accountBean != null}">
	<c:if test="${!sessionScope.accountBean.authenticated}">
		<stripes:link
			beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
			event="signonForm">
            Sign In
	      </stripes:link>
	</c:if>
</c:if> <c:if test="${sessionScope.accountBean != null}">
	<c:if test="${sessionScope.accountBean.authenticated}">
		<stripes:link
			beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
			event="signoff">
            Sign Out
	      </stripes:link>
		<img align="middle" src="../images/separator.gif" />
		<stripes:link
			beanclass="org.mybatis.jpetstore.web.actions.AccountActionBean"
			event="editAccountForm">
            My Account
	      </stripes:link>
		<img align="middle" src="../images/separator.gif" />
		<stripes:link
			beanclass="org.mybatis.jpetstore.web.actions.LiveRecommendationActionBean">
            AI Recommendation
	      </stripes:link>
	</c:if>
</c:if> <img align="middle" src="../images/separator.gif" /> <a
	href="../help.html">?</a></div>
</div>

<div id="Search">
<div id="SearchContent"><stripes:form
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
	<stripes:text name="keyword" size="14" />
	<stripes:submit name="searchProducts" value="Search" />
</stripes:form></div>
</div>

<div id="QuickLinks">
    <stripes:link
            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
            event="viewCategory">
        <stripes:param name="categoryId" value="ALL" />
        <font color="black" style="font-weight:bold; font-size:1.0em; vertical-align:middle;">[ ALL ]</font>
    </stripes:link>
    <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="FISH" />
	<img src="../images/sm_fish.gif" />
</stripes:link> <img src="../images/separator.gif" /> <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="DOGS" />
	<img src="../images/sm_dogs.gif" />
</stripes:link> <img src="../images/separator.gif" /> <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="REPTILES" />
	<img src="../images/sm_reptiles.gif" />
</stripes:link> <img src="../images/separator.gif" /> <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="CATS" />
	<img src="../images/sm_cats.gif" />
</stripes:link> <img src="../images/separator.gif" /> <stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="BIRDS" />
	<img src="../images/sm_birds.gif" />
</stripes:link></div>

</div>

<div id="Content"><stripes:messages />
