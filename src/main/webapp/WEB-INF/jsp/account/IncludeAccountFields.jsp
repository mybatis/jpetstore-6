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

    <!--한글깨짐현상으로 인해 설문지 내용 한글에서 영어로 변경
   	<tr>
   		<td>1. 현재 당신의 거주 환경은 어떤가요?</td>
   		<td><stripes:select name="account.residenceEnv">
   			<stripes:option value="">-- 선택 --</stripes:option>
   			<stripes:option value="실내 위주(아파트, 원룸 등)">실내 위주(아파트, 원룸 등)</stripes:option>
   			<stripes:option value="실외 공간 포함(마당, 베란다 등)">실외 공간 포함(마당, 베란다 등)</stripes:option>
   			<stripes:option value="습한 환경 (강가*호수 근처)">습한 환경 (강가*호수 근처)</stripes:option>
   			<stripes:option value="건조한 환경">건조한 환경</stripes:option>
   			<stripes:option value="특수공간 유지 가능(어항, 테라리움 등)">특수공간 유지 가능(어항, 테라리움 등)</stripes:option>
   		</stripes:select></td>
   	</tr>
   	<tr>
   		<td>2. 당신이 선호하거나 감당 가능한 반려동물의 돌봄 기간은 어느 정도인가요?</td>
   		<td><stripes:select name="account.carePeriod">
   			<stripes:option value="">-- 선택 --</stripes:option>
   			<stripes:option value="5년 이하">5년 이하</stripes:option>
   			<stripes:option value="5~10년">5~10년</stripes:option>
   			<stripes:option value="10년 이상">10년 이상</stripes:option>
   			<stripes:option value="상관없음">상관없음</stripes:option>
   		</stripes:select></td>
   	</tr>
   	<tr>
   		<td>3. 당신의 주거 공간 분위기나 인테리어 색상과 가장 잘 어울리는 반려동물의 색감은 무엇인가요?</td>
   		<td><stripes:select name="account.petColorPref">
   			<stripes:option value="">-- 선택 --</stripes:option>
   			<stripes:option value="밝은색(화이트 | 아이보리톤)">밝은색(화이트 | 아이보리톤)</stripes:option>
   			<stripes:option value="따뜻한색(갈색 | 금색톤)">따뜻한색(갈색 | 금색톤)</stripes:option>
   			<stripes:option value="어두운색(블랙 | 그레이톤)">어두운색(블랙 | 그레이톤)</stripes:option>
   			<stripes:option value="혼합색">혼합색</stripes:option>
   			<stripes:option value="상관없음">상관없음</stripes:option>
   		</stripes:select></td>
   	</tr>
   	<tr>
   		<td>4. 당신의 거주 공간과 생활 패턴을 고려했을 때, 반려동물의 크기는 어느 정도가 적합하다고 생각하시나요?</td>
   		<td><stripes:select name="account.petSizePref">
   			<stripes:option value="">-- 선택 --</stripes:option>
   			<stripes:option value="소형(원룸 및 소형 아파트 적합)">소형(원룸 및 소형 아파트 적합)</stripes:option>
   			<stripes:option value="중형(일반 가정, 일정 공간 여유 있음)">중형(일반 가정, 일정 공간 여유 있음)</stripes:option>
   			<stripes:option value="대형(마당 및 넓은 실내공간 가능)">대형(마당 및 넓은 실내공간 가능)</stripes:option>
   		</stripes:select></td>
   	</tr>
   	<tr>
   		<td>5. 당신은 주로 언제 활동하나요?</td>
   		<td><stripes:select name="account.activityTime">
   			<stripes:option value="">-- 선택 --</stripes:option>
   			<stripes:option value="낮">낮</stripes:option>
   			<stripes:option value="밤">밤</stripes:option>
   			<stripes:option value="일정하지 않음">일정하지 않음</stripes:option>
   		</stripes:select></td>
   	</tr>
   	<tr>
   		<td>6. 당신이 반려동물의 식단을 관리할 때, 어떤 정도의 관리가 가능할 것 같나요?</td>
   		<td><stripes:select name="account.dietManagement">
   			<stripes:option value="">-- 선택 --</stripes:option>
   			<stripes:option value="단순 사료 위주(육식, 잡식형)">단순 사료 위주(육식, 잡식형)</stripes:option>
   			<stripes:option value="다양한 식단 제공 가능(채식, 혼합형 가능)">다양한 식단 제공 가능(채식, 혼합형 가능)</stripes:option>
   			<stripes:option value="특수식단 관리 가능(곤충식, 건조먹이 등)">특수식단 관리 가능(곤충식, 건조먹이 등)</stripes:option>
   		</stripes:select></td>
   	</tr>
   한글깨짐현상으로 인해 설문지 내용 한글에서 영어로 변경-->

	<tr>
		<td>1. What is your current living environment like?</td>
		<td><stripes:select name="account.residenceEnv">
			<stripes:option value="">-- Select --</stripes:option>
			<stripes:option value="Indoor-focused (apartment, studio, etc.)">Indoor-focused (apartment, studio, etc.)</stripes:option>
			<stripes:option value="Includes outdoor space (yard, balcony, etc.)">Includes outdoor space (yard, balcony, etc.)</stripes:option>
			<stripes:option value="Humid environment (near river/lake)">Humid environment (near river/lake)</stripes:option>
			<stripes:option value="Dry environment">Dry environment</stripes:option>
			<stripes:option value="Can maintain special spaces (aquarium, terrarium, etc.)">Can maintain special spaces (aquarium, terrarium, etc.)</stripes:option>
		</stripes:select></td>
	</tr>
	<tr>
		<td>2. What is your preferred or manageable pet care period?</td>
		<td><stripes:select name="account.carePeriod">
			<stripes:option value="">-- Select --</stripes:option>
			<stripes:option value="5 years or less">5 years or less</stripes:option>
			<stripes:option value="5-10 years">5-10 years</stripes:option>
			<stripes:option value="10 years or more">10 years or more</stripes:option>
			<stripes:option value="Doesn't matter">Doesn't matter</stripes:option>
		</stripes:select></td>
	</tr>
	<tr>
		<td>3. What pet color best matches your living space's atmosphere or interior colors?</td>
		<td><stripes:select name="account.petColorPref">
			<stripes:option value="">-- Select --</stripes:option>
			<stripes:option value="Light colors (white | ivory tones)">Light colors (white | ivory tones)</stripes:option>
			<stripes:option value="Warm colors (brown | gold tones)">Warm colors (brown | gold tones)</stripes:option>
			<stripes:option value="Dark colors (black | gray tones)">Dark colors (black | gray tones)</stripes:option>
			<stripes:option value="Mixed colors">Mixed colors</stripes:option>
			<stripes:option value="Doesn't matter">Doesn't matter</stripes:option>
		</stripes:select></td>
	</tr>
	<tr>
		<td>4. Considering your living space and lifestyle, what pet size do you think is appropriate?</td>
		<td><stripes:select name="account.petSizePref">
			<stripes:option value="">-- Select --</stripes:option>
			<stripes:option value="Small (suitable for studio/small apartment)">Small (suitable for studio/small apartment)</stripes:option>
			<stripes:option value="Medium (regular home, some space available)">Medium (regular home, some space available)</stripes:option>
			<stripes:option value="Large (yard and spacious indoor area possible)">Large (yard and spacious indoor area possible)</stripes:option>
		</stripes:select></td>
	</tr>
	<tr>
		<td>5. When are you most active?</td>
		<td><stripes:select name="account.activityTime">
			<stripes:option value="">-- Select --</stripes:option>
			<stripes:option value="Day">Day</stripes:option>
			<stripes:option value="Night">Night</stripes:option>
			<stripes:option value="Irregular">Irregular</stripes:option>
		</stripes:select></td>
	</tr>
	<tr>
		<td>6. When managing your pet's diet, what level of care can you provide?</td>
		<td><stripes:select name="account.dietManagement">
			<stripes:option value="">-- Select --</stripes:option>
			<stripes:option value="Simple kibble-focused (carnivore, omnivore)">Simple kibble-focused (carnivore, omnivore)</stripes:option>
			<stripes:option value="Can provide diverse diet (vegetarian, mixed possible)">Can provide diverse diet (vegetarian, mixed possible)</stripes:option>
			<stripes:option value="Can manage special diets (insect-based, dried food, etc.)">Can manage special diets (insect-based, dried food, etc.)</stripes:option>
		</stripes:select></td>
	</tr>

</table>
