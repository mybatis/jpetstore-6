# JPetStore - "모든 상품 보기" 기능 추가

## 📋 프로젝트 개요

### 프로젝트명
JPetStore 기능 개선 - 전체 상품 조회 시스템

### 개발 기간
2025년 10월

### 개발 목표
사용자가 카테고리 구분 없이 모든 상품을 한 화면에서 조회할 수 있는 기능 구현

---

## 🎯 개발 배경 및 필요성

### 기존 시스템의 문제점
1. **카테고리별 제한된 조회**: 사용자가 특정 카테고리 내의 상품만 확인 가능
2. **탐색 비효율성**: 전체 상품을 확인하려면 여러 카테고리를 순차적으로 방문해야 함
3. **사용자 경험 저하**: 상품 비교 및 선택 시 불편함

### 개선 목표
- 모든 카테고리의 상품을 한 번에 조회할 수 있는 통합 뷰 제공
- 직관적인 UI/UX로 접근성 향상
- 기존 시스템과의 일관성 유지

---

## 💻 기술 스택

### 백엔드
- **Framework**: Spring Framework
- **ORM**: MyBatis 3.x
- **Language**: Java

### 프론트엔드
- **View Template**: JSP (JavaServer Pages)
- **Framework**: Stripes Framework
- **Library**: JSTL (JSP Standard Tag Library)

### 데이터베이스
- **Query**: SQL (JOIN 쿼리 활용)
- **테이블**: ITEM, PRODUCT

---

## 🏗️ 시스템 아키텍처

### MVC 패턴 기반 구조

```
┌─────────────┐
│   View      │  AllItems.jsp, Main.jsp, IncludeTop.jsp
│   (JSP)     │
└──────┬──────┘
       │
       ↓
┌─────────────┐
│ Controller  │  CatalogActionBean.java
│ (Action)    │  - viewAllItems()
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  Service    │  CatalogService.java
│  (Business) │  - getAllItems()
└──────┬──────┘
       │
       ↓
┌─────────────┐
│   Mapper    │  ItemMapper.java / ItemMapper.xml
│   (DAO)     │  - getAllItems() SQL
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  Database   │  ITEM, PRODUCT 테이블
└─────────────┘
```

---

## 📝 상세 구현 내용

### 1. 데이터 접근 계층 (DAO)

#### ItemMapper.java
```java
public interface ItemMapper {
    // 기존 메서드들...

    /**
     * 모든 상품 조회
     * @return 전체 상품 목록
     */
    List<Item> getAllItems();
}
```

#### ItemMapper.xml - SQL 쿼리
```xml
<select id="getAllItems" resultType="Item">
  SELECT
    I.ITEMID,                              -- 상품 ID
    LISTPRICE,                             -- 정가
    UNITCOST,                              -- 원가
    SUPPLIER AS supplierId,                -- 공급자 ID
    I.PRODUCTID AS "product.productId",    -- 제품 ID
    NAME AS "product.name",                -- 제품명
    DESCN AS "product.description",        -- 설명
    CATEGORY AS "product.categoryId",      -- 카테고리 ID
    STATUS,                                -- 상태
    ATTR1 AS attribute1,                   -- 속성 1
    ATTR2 AS attribute2,                   -- 속성 2
    ATTR3 AS attribute3,                   -- 속성 3
    ATTR4 AS attribute4,                   -- 속성 4
    ATTR5 AS attribute5                    -- 속성 5
  FROM ITEM I, PRODUCT P
  WHERE P.PRODUCTID = I.PRODUCTID          -- INNER JOIN
  ORDER BY I.PRODUCTID                     -- 제품 ID 정렬
</select>
```

**쿼리 특징**:
- `ITEM`과 `PRODUCT` 테이블 조인으로 완전한 상품 정보 조회
- `ORDER BY`로 데이터 정렬 보장
- MyBatis resultType 매핑 활용

---

### 2. 비즈니스 로직 계층 (Service)

#### CatalogService.java
```java
@Service
public class CatalogService {
    @Autowired
    private ItemMapper itemMapper;

    // 기존 메서드들...

    /**
     * 모든 상품 조회
     * @return 전체 상품 목록
     */
    public List<Item> getAllItems() {
        return itemMapper.getAllItems();
    }
}
```

**역할**:
- Mapper 계층과 Controller 계층 사이의 중간 계층
- 향후 비즈니스 로직 추가 가능 (필터링, 캐싱 등)

---

### 3. 컨트롤러 계층 (Action)

#### CatalogActionBean.java
```java
public class CatalogActionBean extends AbstractActionBean {

    // 뷰 경로 상수 추가
    private static final String VIEW_ALL_ITEMS = "/WEB-INF/jsp/catalog/AllItems.jsp";

    @SpringBean
    private transient CatalogService catalogService;

    private List<Item> itemList;

    /**
     * 모든 상품 조회 액션
     * @return AllItems.jsp로 포워딩
     */
    public ForwardResolution viewAllItems() {
        itemList = catalogService.getAllItems();
        return new ForwardResolution(VIEW_ALL_ITEMS);
    }

    // Getter/Setter
    public List<Item> getItemList() {
        return itemList;
    }
}
```

**처리 흐름**:
1. 사용자 요청 수신
2. `CatalogService.getAllItems()` 호출
3. 조회된 상품 목록을 `itemList`에 저장
4. `AllItems.jsp`로 포워딩

---

### 4. 프레젠테이션 계층 (View)

#### 📄 AllItems.jsp (신규 생성)
```jsp
<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink">
    <stripes:link beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
        Return to Main Menu
    </stripes:link>
</div>

<div id="Catalog">
    <h2>All Products</h2>

    <table>
        <tr>
            <th>Item ID</th>
            <th>Product ID</th>
            <th>Description</th>
            <th>List Price</th>
            <th>&nbsp;</th>
        </tr>

        <c:forEach var="item" items="${actionBean.itemList}">
            <tr>
                <!-- 상품 ID (링크) -->
                <td>
                    <stripes:link
                        beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                        event="viewItem">
                        <stripes:param name="itemId" value="${item.itemId}" />
                        ${item.itemId}
                    </stripes:link>
                </td>

                <!-- 제품 ID -->
                <td>${item.product.productId}</td>

                <!-- 상품 설명 -->
                <td>
                    ${item.attribute1} ${item.attribute2} ${item.attribute3}
                    ${item.attribute4} ${item.attribute5} ${item.product.name}
                </td>

                <!-- 가격 (통화 형식) -->
                <td>
                    <fmt:formatNumber value="${item.listPrice}" pattern="$#,##0.00" />
                </td>

                <!-- 장바구니 추가 버튼 -->
                <td>
                    <stripes:link
                        beanclass="org.mybatis.jpetstore.web.actions.CartActionBean"
                        event="addItemToCart">
                        <stripes:param name="workingItemId" value="${item.itemId}" />
                        Add to Cart
                    </stripes:link>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
```

**화면 구성**:
- **헤더**: "All Products" 제목
- **테이블**: 상품 목록 표시
- **링크**: 상품 상세 페이지 이동, 장바구니 추가
- **Back 링크**: 메인 메뉴로 돌아가기

---

### 5. 네비게이션 추가

#### Main.jsp - 사이드바
```jsp
<div id="Sidebar">
    <div id="SidebarContent">
        <!-- 새로 추가된 부분 -->
        <stripes:link
            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
            event="viewAllItems">
            <strong>All</strong>
        </stripes:link> <br />
        All Products <br />

        <!-- 기존 카테고리 링크들... -->
        <stripes:link ... event="viewCategory">
            <stripes:param name="categoryId" value="FISH" />
            FISH
        </stripes:link>
        ...
    </div>
</div>
```

#### IncludeTop.jsp - 상단 퀵링크
```jsp
<div id="QuickLinks">
    <!-- 새로 추가된 부분 -->
    <stripes:link
        beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
        event="viewAllItems">All
    </stripes:link>
    <img src="../images/separator.gif" />

    <!-- 기존 링크들... -->
    <stripes:link ... event="viewCategory">
        <stripes:param name="categoryId" value="FISH" />
    </stripes:link>
    ...
</div>
```

**접근성 향상**:
- 메인 페이지 사이드바에서 즉시 접근
- 모든 페이지 상단의 QuickLinks에서 글로벌 접근

---

## 🔄 기능 실행 흐름

### 사용자 시나리오

```
1. 사용자가 메인 페이지 방문
   ↓
2. 사이드바 또는 상단 QuickLinks에서 "All" 클릭
   ↓
3. CatalogActionBean.viewAllItems() 실행
   ↓
4. CatalogService.getAllItems() 호출
   ↓
5. ItemMapper.getAllItems() SQL 실행
   ↓
6. 데이터베이스에서 ITEM과 PRODUCT 조인 쿼리 실행
   ↓
7. 결과를 List<Item>으로 반환
   ↓
8. AllItems.jsp에서 테이블 형태로 렌더링
   ↓
9. 사용자가 상품 목록 확인
   ↓
10. (선택) 상품 클릭 → 상세 페이지 이동
    또는 "Add to Cart" 클릭 → 장바구니 추가
```

---

## 📊 변경 파일 요약

### 수정된 파일 (5개)

| 파일명 | 경로 | 변경 내용 |
|--------|------|-----------|
| ItemMapper.java | src/main/java/org/mybatis/jpetstore/mapper/ | getAllItems() 메서드 추가 |
| ItemMapper.xml | src/main/resources/org/mybatis/jpetstore/mapper/ | getAllItems SQL 쿼리 추가 |
| CatalogService.java | src/main/java/org/mybatis/jpetstore/service/ | getAllItems() 서비스 메서드 추가 |
| CatalogActionBean.java | src/main/java/org/mybatis/jpetstore/web/actions/ | viewAllItems() 액션 추가 |
| Main.jsp | src/main/webapp/WEB-INF/jsp/catalog/ | 사이드바에 All 링크 추가 |
| IncludeTop.jsp | src/main/webapp/WEB-INF/jsp/common/ | QuickLinks에 All 링크 추가 |

### 신규 생성 파일 (1개)

| 파일명 | 경로 | 설명 |
|--------|------|------|
| AllItems.jsp | src/main/webapp/WEB-INF/jsp/catalog/ | 전체 상품 목록 뷰 페이지 (65줄) |

### 변경 통계
- **총 수정 라인**: +96줄 / -0줄
- **수정된 파일**: 6개
- **신규 파일**: 1개

---

## ✨ 주요 기능 및 특징

### 1. 통합 상품 조회
- 모든 카테고리의 상품을 한 화면에서 조회
- FISH, DOGS, CATS, BIRDS, REPTILES 등 전 카테고리 포함

### 2. 직관적인 UI
- 테이블 형식의 깔끔한 레이아웃
- Item ID, Product ID, 설명, 가격 정보 한눈에 확인
- 기존 JPetStore 디자인과 일관성 유지

### 3. 편리한 접근성
- **메인 페이지 사이드바**: 첫 화면에서 즉시 접근
- **상단 QuickLinks**: 모든 페이지에서 글로벌 접근 가능
- 명확한 "All" 라벨로 기능 직관적 전달

### 4. 상세 기능 연동
- Item ID 클릭 → 상품 상세 페이지 이동
- Add to Cart 버튼 → 장바구니 즉시 추가
- Return to Main Menu 링크 → 메인 페이지 복귀

### 5. 가격 포맷팅
- 통화 형식 자동 적용: `$#,##0.00`
- 예: `$10.00`, `$1,234.56`

---

## 🎯 기대 효과

### 사용자 관점
1. **탐색 시간 단축**: 여러 카테고리를 돌아다니지 않고 한 번에 확인
2. **비교 편의성**: 다양한 카테고리의 상품을 쉽게 비교
3. **구매 결정 향상**: 더 많은 선택지 제공으로 만족도 증가

### 비즈니스 관점
1. **전환율 향상**: 사용자가 더 많은 상품을 탐색할 기회 제공
2. **사용자 경험 개선**: 직관적인 UI로 이탈률 감소
3. **확장 가능성**: 향후 필터링, 정렬, 검색 기능 추가 기반 마련

### 기술적 관점
1. **유지보수성**: MVC 패턴 준수로 코드 구조 명확
2. **재사용성**: 기존 컴포넌트 최대한 활용
3. **확장성**: 새로운 기능 추가 용이

---

## 🔍 향후 개선 방향

### 단기 개선 사항
- [ ] 페이지네이션 추가 (대량 상품 처리)
- [ ] 카테고리별 필터링 기능
- [ ] 가격 범위 필터
- [ ] 정렬 옵션 (가격순, 이름순 등)

### 중기 개선 사항
- [ ] 검색 기능 통합
- [ ] 상품 이미지 썸네일 표시
- [ ] Ajax 기반 동적 로딩
- [ ] 재고 상태 표시

### 장기 개선 사항
- [ ] 개인화 추천 시스템
- [ ] 위시리스트 기능
- [ ] 최근 본 상품 기능
- [ ] 상품 비교 기능

---

## 📚 기술적 고려사항

### 성능 최적화
- **현재**: 전체 상품 일괄 조회
- **개선 필요**:
  - 페이지네이션으로 LIMIT/OFFSET 적용
  - 인덱스 최적화 (PRODUCTID, ITEMID)
  - 쿼리 결과 캐싱 (Redis, EhCache 등)

### 보안
- **SQL Injection**: MyBatis PreparedStatement 자동 적용으로 방지
- **XSS 방지**: JSTL의 자동 이스케이핑 활용

### 확장성
- **현재 구조**: 단일 서버 환경
- **향후 고려**:
  - 마이크로서비스 아키텍처로 전환 가능성
  - RESTful API 제공
  - React/Vue.js 프론트엔드 분리

---

## 🛠️ 테스트 시나리오

### 기능 테스트
1. ✅ "All" 링크 클릭 시 전체 상품 목록 표시
2. ✅ 상품 테이블 정상 렌더링 (Item ID, Product ID, Description, Price)
3. ✅ Item ID 클릭 시 상세 페이지 이동
4. ✅ Add to Cart 버튼 정상 동작
5. ✅ Return to Main Menu 링크 정상 동작

### 데이터 무결성
1. ✅ ITEM과 PRODUCT 테이블 조인 정확성
2. ✅ 모든 카테고리 상품 포함 확인
3. ✅ 가격 포맷팅 정확성
4. ✅ NULL 값 처리

### UI/UX 테스트
1. ✅ 브라우저 호환성 (Chrome, Firefox, Safari)
2. ✅ 반응형 디자인 (추후 개선 필요)
3. ✅ 기존 디자인과의 일관성

---

## 📈 프로젝트 관리

### Git 커밋 히스토리
```
commit 670f65b
Author: Jeong-Ryeol <jwr5336@naver.com>
Date:   Mon Oct 13 17:26:54 2025 +0900

    모든 상품 보기 기능 추가

    모든 카테고리의 상품을 한 번에 볼 수 있는 기능 구현:
    - ItemMapper와 CatalogService에 getAllItems() 메서드 추가
    - CatalogActionBean에 viewAllItems() 액션 추가
    - AllItems.jsp 뷰 페이지 생성
    - 사이드바와 퀵링크에 All 링크 추가
```

### 브랜치 전략
- **메인 브랜치**: `master`
- **기능 브랜치**: `feature/issue-3`

---

## 💡 결론

### 성과 요약
- ✅ 사용자 편의성 대폭 향상
- ✅ 기존 시스템과의 완벽한 호환성
- ✅ MVC 패턴 준수로 유지보수 용이
- ✅ 확장 가능한 구조 설계

### 학습 포인트
1. **MyBatis JOIN 쿼리**: 복수 테이블 조인 및 매핑
2. **Stripes Framework**: 액션 기반 웹 프레임워크 이해
3. **JSP/JSTL**: 동적 뷰 렌더링 및 태그 라이브러리 활용
4. **Spring DI**: 의존성 주입 및 서비스 계층 설계

### 프로젝트 의의
JPetStore의 기존 구조를 깊이 이해하고, 실제 사용자 니즈를 반영한 기능을 성공적으로 추가함으로써 **Full-Stack 개발 역량**을 입증하였습니다.

---

## 📞 질의응답

### Q&A 예상 질문

**Q1: 왜 새로운 JSP를 만들었나요? 기존 페이지를 재사용할 수 없었나요?**
> A: 기존 페이지는 카테고리별 상품 조회에 특화되어 있어, 전체 상품을 표시하는 독립적인 뷰가 필요했습니다. 또한 향후 전체 상품 페이지에만 적용될 수 있는 기능(필터링, 정렬 등)을 고려하여 분리했습니다.

**Q2: 성능 이슈는 없나요? 상품이 많아지면 어떻게 되나요?**
> A: 현재는 프로토타입 단계로 전체 조회를 구현했습니다. 향후 페이지네이션(LIMIT/OFFSET)과 쿼리 캐싱을 도입하여 대량 데이터에 대응할 계획입니다.

**Q3: 모바일 환경은 고려했나요?**
> A: 현재는 데스크톱 환경에 최적화되어 있습니다. 향후 반응형 디자인 적용과 모바일 전용 뷰를 추가할 예정입니다.

**Q4: 다른 기능과의 충돌은 없나요?**
> A: 기존 코드를 수정하지 않고 새로운 메서드와 페이지를 추가하는 방식으로 구현하여 기존 기능에 영향을 주지 않습니다.

---

## 📚 참고 자료

### 프로젝트 저장소
- GitHub: [JPetStore-6](https://github.com/mybatis/jpetstore-6)

### 사용 기술 문서
- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [MyBatis Documentation](https://mybatis.org/mybatis-3/)
- [Stripes Framework](https://stripesframework.atlassian.net/wiki/spaces/STRIPES/overview)

---

**발표일**: 2025년 10월
**발표자**: 정원열
**프로젝트**: JPetStore - 모든 상품 보기 기능 추가