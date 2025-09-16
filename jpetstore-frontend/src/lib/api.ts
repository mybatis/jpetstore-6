// API configuration
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081';

// Helper function for API calls
async function fetchAPI(endpoint: string, options: RequestInit = {}) {
  const url = `${API_URL}${endpoint}`;
  console.log('API_URL:', API_URL);
  console.log('Full URL:', url);
  
  const defaultOptions: RequestInit = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    credentials: 'include', // Important for session cookies
    ...options,
  };

  const response = await fetch(url, defaultOptions);
  
  if (!response.ok && response.status !== 401) {
    throw new Error(`API call failed: ${response.statusText}`);
  }
  
  return response.json();
}

// Auth API
export const authAPI = {
  login: async (username: string, password: string) => {
    return fetchAPI('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });
  },
  
  register: async (userData: {
    username: string;
    password: string;
    email: string;
    firstName: string;
    lastName: string;
    phone?: string;
    address1?: string;
    address2?: string;
    city?: string;
    state?: string;
    zip?: string;
    country?: string;
  }) => {
    return fetchAPI('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  },
  
  logout: async () => {
    return fetchAPI('/api/auth/logout', {
      method: 'POST',
    });
  },
  
  getCurrentUser: async () => {
    return fetchAPI('/api/auth/me');
  },
};

// Catalog API
export const catalogAPI = {
  getCategories: async () => {
    return fetchAPI('/api/catalog/categories');
  },
  
  getCategory: async (categoryId: string) => {
    return fetchAPI(`/api/catalog/categories/${categoryId}`);
  },
  
  getProductsByCategory: async (categoryId: string) => {
    return fetchAPI(`/api/catalog/categories/${categoryId}/products`);
  },
  
  getProduct: async (productId: string) => {
    return fetchAPI(`/api/catalog/products/${productId}`);
  },
  
  searchProducts: async (keyword: string) => {
    return fetchAPI(`/api/catalog/products/search?keyword=${encodeURIComponent(keyword)}`);
  },
  
  getItemsByProduct: async (productId: string) => {
    return fetchAPI(`/api/catalog/products/${productId}/items`);
  },
  
  getItem: async (itemId: string) => {
    return fetchAPI(`/api/catalog/items/${itemId}`);
  },
};

// Cart API
export const cartAPI = {
  getCart: async () => {
    return fetchAPI('/api/cart');
  },
  
  addToCart: async (item: Partial<CartItem>) => {
    return fetchAPI('/api/cart/add', {
      method: 'POST',
      body: JSON.stringify(item),
    });
  },
  
  updateCartItem: async (itemId: string, quantity: number) => {
    return fetchAPI(`/api/cart/update/${itemId}?quantity=${quantity}`, {
      method: 'PUT',
    });
  },
  
  removeFromCart: async (itemId: string) => {
    return fetchAPI(`/api/cart/remove/${itemId}`, {
      method: 'DELETE',
    });
  },
  
  clearCart: async () => {
    return fetchAPI('/api/cart/clear', {
      method: 'DELETE',
    });
  },
  
  getCartCount: async () => {
    return fetchAPI('/api/cart/count');
  },
};

// Types
export interface User {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  address1?: string;
  address2?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
}

export interface Category {
  categoryId: string;
  name: string;
  description?: string;
}

export interface Product {
  productId: string;
  categoryId: string;
  name: string;
  description?: string;
}

export interface Item {
  itemId: string;
  productId: string;
  listPrice: number;
  unitCost: number;
  supplierId?: number;
  status?: string;
  attribute1?: string;
  attribute2?: string;
  attribute3?: string;
  attribute4?: string;
  attribute5?: string;
}

export interface CartItem {
  itemId: string;
  productId: string;
  name: string;
  description?: string;
  imageUrl?: string;
  price: number;
  quantity: number;
  total: number;
  category?: string;
  breed?: string;
}

export interface Cart {
  items: CartItem[];
  subtotal: number;
  tax: number;
  total: number;
  itemCount: number;
}
