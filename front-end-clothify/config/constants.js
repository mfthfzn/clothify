const APP_DATA = {
  products: [
    { id: 1, name: 'Kaos Oversize Core', category: 'Pakaian Atas', size: 'L', color: 'Hitam', price: 149000, stock: 42, image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=600&q=80', description: 'Kaos oversize premium dengan bahan cotton combed.' },
    { id: 2, name: 'Kemeja Linen Casual', category: 'Pakaian Atas', size: 'M', color: 'Beige', price: 229000, stock: 18, image: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=600&q=80', description: 'Kemeja santai untuk tampilan clean dan rapi.' },
    { id: 3, name: 'Celana Chino Slim', category: 'Pakaian Bawah', size: 'XL', color: 'Navy', price: 279000, stock: 25, image: 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?auto=format&fit=crop&w=600&q=80', description: 'Celana chino slim fit dengan stretch ringan.' },
    { id: 4, name: 'Topi Baseball Urban', category: 'Pakaian Atas', size: 'All Size', color: 'Putih', price: 99000, stock: 60, image: 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=600&q=80', description: 'Aksesoris casual untuk gaya harian.' },
    { id: 5, name: 'Hoodie Minimal Premium', category: 'Pakaian Atas', size: 'L', color: 'Abu', price: 339000, stock: 15, image: 'https://images.unsplash.com/photo-1503341504253-dff4815485f1?auto=format&fit=crop&w=600&q=80', description: 'Hoodie tebal dengan nuansa minimalis modern.' },
    { id: 6, name: 'Rok Plisket Elegan', category: 'Pakaian Bawah', size: 'M', color: 'Cream', price: 189000, stock: 22, image: 'https://images.unsplash.com/photo-1572804013427-4d7ca7268217?auto=format&fit=crop&w=600&q=80', description: 'Rok stylish untuk koleksi wanita.' }
  ],
  categories: [
    { id: 1, name: 'Pakaian Atas', description: 'Kaos, kemeja, hoodie, dan outerwear.', count: 124 },
    { id: 2, name: 'Pakaian Bawah', description: 'Celana, rok, dan jeans.', count: 78 }
  ],
  cart: [
    { id: 1, name: 'Kaos Oversize Core', price: 149000, qty: 2, image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=600&q=80' },
    { id: 4, name: 'Topi Baseball Urban', price: 99000, qty: 1, image: 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=600&q=80' }
  ],
  transactions: [
    { id: 'TXN001', date: '2026-05-08 10:30', total: 447000, status: 'Berhasil', items: 3 },
    { id: 'TXN002', date: '2026-05-07 14:15', total: 279000, status: 'Berhasil', items: 1 },
    { id: 'TXN003', date: '2026-05-07 09:45', total: 697000, status: 'Berhasil', items: 2 },
    { id: 'TXN004', date: '2026-05-06 16:20', total: 538000, status: 'Berhasil', items: 2 },
    { id: 'TXN005', date: '2026-05-06 11:00', total: 149000, status: 'Berhasil', items: 1 }
  ]
};

const PAGE_ROUTE = {
  dashboard: '/admin/dashboard.html',
  products: '/admin/products.html',
  categories: '/admin/categories.html',
  transactions: '/kasir/transactions.html',
  'transaction-history': '/kasir/transaction-history.html'
};

const ROLE_ACCESS = {
  admin: ['dashboard', 'products', 'categories'],
  kasir: ['transactions', 'transaction-history']
};

const state = {
  products: [...APP_DATA.products],
  filteredProducts: [...APP_DATA.products],
  currentPage: 1,
  pageSize: 4,
  categories: [...APP_DATA.categories],
  cart: [...APP_DATA.cart],
  editingProductId: null,
  editingCategoryId: null,
  currentTheme: localStorage.getItem('theme') || 'light'
};
