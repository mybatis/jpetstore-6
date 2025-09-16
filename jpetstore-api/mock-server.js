const express = require('express');
const cors = require('cors');
const app = express();
const port = 8081;

// Middleware
app.use(cors({
  origin: ['http://localhost:3000', 'http://localhost:3001'],
  credentials: true
}));
app.use(express.json());

// Session simulation
const sessions = {};
const users = {
  'demo:password': {
    username: 'demo',
    email: 'demo@jpetstore.com',
    firstName: 'Demo',
    lastName: 'User',
    phone: '555-0100',
    address1: '123 Demo Street',
    city: 'Demo City',
    state: 'CA',
    zip: '12345',
    country: 'USA'
  },
  'john:john123': {
    username: 'john',
    email: 'john@example.com',
    firstName: 'John',
    lastName: 'Doe',
    phone: '555-0101',
    address1: '456 Main Street',
    city: 'Springfield',
    state: 'IL',
    zip: '62701',
    country: 'USA'
  },
  'admin:admin': {
    username: 'admin',
    email: 'admin@jpetstore.com',
    firstName: 'Admin',
    lastName: 'User',
    phone: '555-0000',
    address1: '1 Admin Plaza',
    city: 'Admin City',
    state: 'CA',
    zip: '00000',
    country: 'USA'
  }
};

// Mock data
const categories = [
  { categoryId: 'DOGS', name: 'Dogs', description: 'Various breeds of dogs' },
  { categoryId: 'CATS', name: 'Cats', description: 'Various breeds of cats' },
  { categoryId: 'BIRDS', name: 'Birds', description: 'Various species of birds' },
  { categoryId: 'FISH', name: 'Fish', description: 'Various species of fish' },
  { categoryId: 'REPTILES', name: 'Reptiles', description: 'Various species of reptiles' }
];

const products = {
  DOGS: [
    { productId: 'K9-BD-01', name: 'Bulldog', categoryId: 'DOGS', description: 'Friendly dog from England' },
    { productId: 'K9-PO-02', name: 'Poodle', categoryId: 'DOGS', description: 'Cute dog from France' },
    { productId: 'K9-DL-01', name: 'Dalmation', categoryId: 'DOGS', description: 'Great dog for a Fire Station' },
    { productId: 'K9-RT-01', name: 'Golden Retriever', categoryId: 'DOGS', description: 'Great family dog' },
    { productId: 'K9-RT-02', name: 'Labrador Retriever', categoryId: 'DOGS', description: 'Great hunting dog' },
    { productId: 'K9-CW-01', name: 'Chihuahua', categoryId: 'DOGS', description: 'Great companion dog' }
  ],
  CATS: [
    { productId: 'FL-DSH-01', name: 'Manx', categoryId: 'CATS', description: 'Great for reducing mouse populations' },
    { productId: 'FL-DLH-02', name: 'Persian', categoryId: 'CATS', description: 'Friendly house cat, doubles as a princess' }
  ],
  BIRDS: [
    { productId: 'AV-CB-01', name: 'Amazon Parrot', categoryId: 'BIRDS', description: 'Great companion for up to 75 years' },
    { productId: 'AV-SB-02', name: 'Finch', categoryId: 'BIRDS', description: 'Great stress reliever' }
  ],
  FISH: [
    { productId: 'FI-SW-01', name: 'Angelfish', categoryId: 'FISH', description: 'Salt Water fish from Australia' },
    { productId: 'FI-SW-02', name: 'Tiger Shark', categoryId: 'FISH', description: 'Salt Water fish from Australia' },
    { productId: 'FI-FW-01', name: 'Koi', categoryId: 'FISH', description: 'Fresh Water fish from Japan' },
    { productId: 'FI-FW-02', name: 'Goldfish', categoryId: 'FISH', description: 'Fresh Water fish from China' }
  ],
  REPTILES: [
    { productId: 'RP-SN-01', name: 'Rattlesnake', categoryId: 'REPTILES', description: 'Doubles as a watch dog' },
    { productId: 'RP-LI-02', name: 'Iguana', categoryId: 'REPTILES', description: 'Friendly green friend' }
  ]
};

// Cart storage
const carts = {};

// Auth endpoints
app.post('/api/auth/login', (req, res) => {
  const { username, password } = req.body;
  const key = `${username}:${password}`;
  const user = users[key];
  
  if (user) {
    const sessionId = Math.random().toString(36).substring(7);
    sessions[sessionId] = user;
    res.json({
      success: true,
      user: user,
      message: 'Login successful'
    });
  } else {
    res.status(401).json({
      success: false,
      message: 'Invalid username or password'
    });
  }
});

app.post('/api/auth/register', (req, res) => {
  const { username, password, ...userData } = req.body;
  const key = `${username}:${password}`;
  
  if (users[key]) {
    res.status(400).json({
      success: false,
      message: 'Username already exists'
    });
  } else {
    const newUser = { username, ...userData };
    users[key] = newUser;
    res.json({
      success: true,
      user: newUser,
      message: 'Registration successful'
    });
  }
});

app.post('/api/auth/logout', (req, res) => {
  res.json({
    success: true,
    message: 'Logout successful'
  });
});

app.get('/api/auth/me', (req, res) => {
  // For demo, return first user
  res.json(users['demo:password']);
});

// Catalog endpoints
app.get('/api/catalog/categories', (req, res) => {
  res.json(categories);
});

app.get('/api/catalog/categories/:categoryId', (req, res) => {
  const category = categories.find(c => c.categoryId === req.params.categoryId);
  if (category) {
    res.json(category);
  } else {
    res.status(404).json({ error: 'Category not found' });
  }
});

app.get('/api/catalog/categories/:categoryId/products', (req, res) => {
  const categoryProducts = products[req.params.categoryId] || [];
  res.json(categoryProducts);
});

app.get('/api/catalog/products/:productId', (req, res) => {
  let product = null;
  for (const categoryProducts of Object.values(products)) {
    product = categoryProducts.find(p => p.productId === req.params.productId);
    if (product) break;
  }
  
  if (product) {
    res.json(product);
  } else {
    res.status(404).json({ error: 'Product not found' });
  }
});

app.get('/api/catalog/products/search', (req, res) => {
  const keyword = req.query.keyword?.toLowerCase() || '';
  const results = [];
  
  for (const categoryProducts of Object.values(products)) {
    const matches = categoryProducts.filter(p => 
      p.name.toLowerCase().includes(keyword) || 
      p.description.toLowerCase().includes(keyword)
    );
    results.push(...matches);
  }
  
  res.json(results);
});

// Cart endpoints
app.get('/api/cart', (req, res) => {
  const sessionId = req.headers['session-id'] || 'default';
  const cart = carts[sessionId] || {
    items: [],
    subtotal: 0,
    tax: 0,
    total: 0,
    itemCount: 0
  };
  res.json(cart);
});

app.post('/api/cart/add', (req, res) => {
  const sessionId = req.headers['session-id'] || 'default';
  const item = req.body;
  
  if (!carts[sessionId]) {
    carts[sessionId] = {
      items: [],
      subtotal: 0,
      tax: 0,
      total: 0,
      itemCount: 0
    };
  }
  
  const cart = carts[sessionId];
  const existingItem = cart.items.find(i => i.itemId === item.itemId);
  
  if (existingItem) {
    existingItem.quantity += item.quantity;
    existingItem.total = existingItem.price * existingItem.quantity;
  } else {
    item.total = item.price * item.quantity;
    cart.items.push(item);
  }
  
  // Recalculate totals
  cart.subtotal = cart.items.reduce((sum, i) => sum + i.total, 0);
  cart.tax = cart.subtotal * 0.08;
  cart.total = cart.subtotal + cart.tax;
  cart.itemCount = cart.items.reduce((sum, i) => sum + i.quantity, 0);
  
  res.json(cart);
});

app.get('/api/cart/count', (req, res) => {
  const sessionId = req.headers['session-id'] || 'default';
  const cart = carts[sessionId];
  const count = cart ? cart.itemCount : 0;
  res.json({ count });
});

app.delete('/api/cart/clear', (req, res) => {
  const sessionId = req.headers['session-id'] || 'default';
  delete carts[sessionId];
  res.json({ success: true, message: 'Cart cleared successfully' });
});

// Start server
app.listen(port, () => {
  console.log(`Mock JPetStore API server running at http://localhost:${port}`);
  console.log('Available endpoints:');
  console.log('  POST /api/auth/login');
  console.log('  POST /api/auth/register');
  console.log('  POST /api/auth/logout');
  console.log('  GET  /api/auth/me');
  console.log('  GET  /api/catalog/categories');
  console.log('  GET  /api/catalog/categories/:id/products');
  console.log('  GET  /api/cart');
  console.log('  POST /api/cart/add');
  console.log('  GET  /api/cart/count');
  console.log('\nDemo credentials:');
  console.log('  Username: demo, Password: password');
  console.log('  Username: john, Password: john123');
  console.log('  Username: admin, Password: admin');
});
