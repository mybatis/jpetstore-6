# JPetStore Modernization Plan

## Stripes → Spring Boot + JSP → Next.js Migration

### 🎯 Project Overview

**Goal**: Modernize JPetStore application by migrating from Stripes framework to Spring Boot and from JSP views to Next.js frontend

**Timeline**: 7-8 hours (Hackathon Implementation)

**Team Size**: 3 Developers

**Migration Strategy**: Parallel development with gradual cutover using feature toggles

---

## 📊 Current vs Target Architecture

### Current State

- **Framework**: Stripes 1.6.0 MVC
- **Frontend**: JSP + JSTL + Server-side rendering
- **Data**: MyBatis 3.5.19 + HSQLDB
- **Deployment**: Single WAR file
- **Request Flow**: Browser → Stripes Filter → ActionBean → JSP

### Target State

- **Backend**: Spring Boot 3.x with REST APIs
- **Frontend**: Next.js with SSR/SSG capabilities
- **Data**: Spring Data JPA + H2/PostgreSQL
- **Deployment**: Separate backend and frontend deployments
- **Request Flow**: Browser → Next.js → REST API → JSON Response

---

## 👥 Team Distribution

### 👤 Developer 1: Spring Boot Backend Migration (2.5-3 hours)

**Focus**: Convert Stripes ActionBeans to Spring REST Controllers

### 👤 Developer 2: Next.js Frontend Development (2.5-3 hours)

**Focus**: Convert JSP pages to Next.js pages and components

### 👤 Developer 3: Integration & Routing (2.5-3 hours)

**Focus**: Proxy setup, feature toggles, and system integration

---

## 🚀 Implementation Plan

### Phase 1: Project Setup (30 minutes each developer)

#### Developer 1 - Spring Boot Setup

```bash
# Create new Spring Boot project
mkdir jpetstore-api
cd jpetstore-api

# Initialize with Maven
mvn archetype:generate \
  -DgroupId=com.jpetstore \
  -DartifactId=jpetstore-api \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

**Required Dependencies:**

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

#### Developer 2 - Next.js Setup

```bash
# Create Next.js application
npx create-next-app@latest jpetstore-frontend --typescript --tailwind --eslint --app
cd jpetstore-frontend

# Install additional dependencies
npm install axios @types/node
```

#### Developer 3 - Integration Setup

```bash
# Set up proxy server for development
npm install -g http-proxy-middleware express
```

### Phase 2: Core Migration (90 minutes each developer)

#### Developer 1 Tasks: Backend Migration

**Priority Order**: Catalog → Account → Cart → Order

**Task 1A: Convert CatalogActionBean to REST Controller**

```java
@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "http://localhost:3000")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        // Implementation
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @PathVariable String categoryId) {
        // Implementation
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(
            @RequestParam String keyword) {
        // Implementation
    }
}
```

**Task 1B: Create DTOs for API Responses**

```java
public class CategoryDto {
    private String categoryId;
    private String name;
    private String description;
    // getters and setters
}

public class ProductDto {
    private String productId;
    private String name;
    private String description;
    private String categoryId;
    // getters and setters
}
```

**Task 1C: Configure Application**

```yaml
# application.yml
server:
  port: 8081
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:h2:mem:jpetstore
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true
```

#### Developer 2 Tasks: Frontend Migration

**Task 2A: Create API Service Layer**

```typescript
// lib/api.ts
import axios from "axios";

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_URL || "http://localhost:8081/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export interface Category {
  categoryId: string;
  name: string;
  description: string;
}

export interface Product {
  productId: string;
  name: string;
  description: string;
  categoryId: string;
}

export const catalogAPI = {
  getCategories: () => api.get<Category[]>("/catalog/categories"),
  getProductsByCategory: (categoryId: string) =>
    api.get<Product[]>(`/catalog/categories/${categoryId}/products`),
  searchProducts: (keyword: string) =>
    api.get<Product[]>(`/catalog/search?keyword=${keyword}`),
};
```

**Task 2B: Convert JSP to Next.js Pages and Components**

```typescript
// app/page.tsx (Home page with categories)
import { catalogAPI, Category } from "@/lib/api";
import CategoryGrid from "@/components/CategoryGrid";

async function getCategories(): Promise<Category[]> {
  try {
    const response = await catalogAPI.getCategories();
    return response.data;
  } catch (error) {
    console.error("Error fetching categories:", error);
    return [];
  }
}

export default async function HomePage() {
  const categories = await getCategories();

  return (
    <main className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold text-center mb-8">
        Welcome to JPetStore
      </h1>
      <CategoryGrid categories={categories} />
    </main>
  );
}
```

```typescript
// components/CategoryGrid.tsx
"use client";
import Link from "next/link";
import Image from "next/image";
import { Category } from "@/lib/api";

interface CategoryGridProps {
  categories: Category[];
}

export default function CategoryGrid({ categories }: CategoryGridProps) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {categories.map((category) => (
        <Link
          key={category.categoryId}
          href={`/categories/${category.categoryId}`}
          className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-6"
        >
          <div className="text-center">
            <Image
              src={`/images/${category.categoryId}.gif`}
              alt={category.name}
              width={100}
              height={100}
              className="mx-auto mb-4"
            />
            <h3 className="text-xl font-semibold mb-2">{category.name}</h3>
            <p className="text-gray-600">{category.description}</p>
          </div>
        </Link>
      ))}
    </div>
  );
}
```

**Task 2C: Create Dynamic Routes**

```typescript
// app/categories/[categoryId]/page.tsx
import { catalogAPI, Product } from "@/lib/api";
import ProductGrid from "@/components/ProductGrid";

interface CategoryPageProps {
  params: {
    categoryId: string;
  };
}

async function getProducts(categoryId: string): Promise<Product[]> {
  try {
    const response = await catalogAPI.getProductsByCategory(categoryId);
    return response.data;
  } catch (error) {
    console.error("Error fetching products:", error);
    return [];
  }
}

export default async function CategoryPage({ params }: CategoryPageProps) {
  const products = await getProducts(params.categoryId);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8 capitalize">
        {params.categoryId} Products
      </h1>
      <ProductGrid products={products} />
    </div>
  );
}
```

```typescript
// components/ProductGrid.tsx
"use client";
import Link from "next/link";
import Image from "next/image";
```
