'use client';

import { useState, useEffect } from 'react';

interface Category {
  categoryId: string;
  name: string;
  description: string;
}

interface Product {
  productId: string;
  name: string;
  description: string;
  categoryId: string;
  categoryName: string;
}

export default function Home() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>('');
  const [loading, setLoading] = useState(true);

  const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081/api';

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/catalog/categories`);
      const data = await response.json();
      setCategories(data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching categories:', error);
      setLoading(false);
    }
  };

  const fetchProductsByCategory = async (categoryId: string) => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/catalog/categories/${categoryId}/products`);
      const data = await response.json();
      setProducts(data);
      setSelectedCategory(categoryId);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching products:', error);
      setLoading(false);
    }
  };

  if (loading && categories.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl">Loading JPetStore...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-blue-600 text-white p-6">
        <h1 className="text-3xl font-bold">JPetStore - Modern Edition</h1>
        <p className="text-blue-100 mt-2">Your favorite pet store, now with a modern API and frontend</p>
      </header>

      <main className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Categories Sidebar */}
          <div className="md:col-span-1">
            <h2 className="text-2xl font-semibold mb-4">Categories</h2>
            <div className="space-y-2">
              {categories.map((category) => (
                <button
                  key={category.categoryId}
                  onClick={() => fetchProductsByCategory(category.categoryId)}
                  className={`w-full text-left p-3 rounded-lg border transition-colors ${
                    selectedCategory === category.categoryId
                      ? 'bg-blue-100 border-blue-300'
                      : 'bg-white border-gray-200 hover:bg-gray-50'
                  }`}
                >
                  <div className="font-medium">{category.name}</div>
                  <div className="text-sm text-gray-600">{category.description}</div>
                </button>
              ))}
            </div>
          </div>

          {/* Products Area */}
          <div className="md:col-span-2">
            {selectedCategory ? (
              <>
                <h2 className="text-2xl font-semibold mb-4">
                  Products in {categories.find(c => c.categoryId === selectedCategory)?.name}
                </h2>
                {loading ? (
                  <div className="text-center py-8">Loading products...</div>
                ) : (
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    {products.map((product) => (
                      <div key={product.productId} className="bg-white p-4 rounded-lg border border-gray-200 shadow-sm">
                        <h3 className="font-semibold text-lg">{product.name}</h3>
                        <p className="text-gray-600 text-sm mt-1">{product.description}</p>
                        <div className="mt-3">
                          <span className="inline-block bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">
                            {product.productId}
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </>
            ) : (
              <div className="text-center py-16">
                <h2 className="text-2xl font-semibold text-gray-600 mb-4">Welcome to JPetStore</h2>
                <p className="text-gray-500">Select a category from the sidebar to browse products</p>
              </div>
            )}
          </div>
        </div>
      </main>

      <footer className="bg-gray-800 text-white p-6 mt-16">
        <div className="container mx-auto text-center">
          <p>&copy; 2024 JPetStore - Modernized with Spring Boot API and Next.js Frontend</p>
        </div>
      </footer>
    </div>
  );
}