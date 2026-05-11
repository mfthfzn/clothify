function initDashboardPage() {
  renderSalesChart();
  initQuickActions();
}

function initQuickActions() {
  document.querySelectorAll('[data-quick-action]').forEach((button) => {
    button.addEventListener('click', () => {
      const action = button.dataset.quickAction;

      if (action === 'products') {
        window.location.href = 'products.html';
        return;
      }

      if (action === 'categories') {
        window.location.href = 'categories.html';
        return;
      }

      if (action === 'report') {
        downloadTransactionReport();
      }
    });
  });
}

function downloadTransactionReport() {
  const rows = [
    ['ID', 'Tanggal', 'Total', 'Status', 'Jumlah Item'],
    ...APP_DATA.transactions.map((transaction) => [
      transaction.id,
      transaction.date,
      transaction.total,
      transaction.status,
      transaction.items
    ])
  ];

  const csvContent = rows
    .map((row) => row.map((value) => `"${String(value).replace(/"/g, '""')}"`).join(','))
    .join('\n');

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = `laporan-transaksi-${new Date().toISOString().slice(0, 10)}.csv`;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
  showToast('Laporan transaksi sedang diunduh', 'success');
}

function renderSalesChart() {
  const canvas = document.getElementById('salesChart');
  if (!canvas || typeof Chart === 'undefined') return;

  new Chart(canvas, {
    type: 'line',
    data: {
      labels: ['Sen', 'Sel', 'Rab', 'Kam', 'Jum', 'Sab', 'Min'],
      datasets: [{
        label: 'Penjualan',
        data: [12, 18, 15, 24, 20, 28, 33],
        borderColor: '#2563eb',
        backgroundColor: 'rgba(37, 99, 235, 0.15)',
        fill: true,
        tension: 0.35,
        pointRadius: 4,
        pointBackgroundColor: '#2563eb'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { grid: { display: false } },
        y: { grid: { color: 'rgba(148, 163, 184, 0.15)' } }
      }
    }
  });
}

function initProductsPage() {
  const searchInput = document.getElementById('productSearch');
  const filterSelect = document.getElementById('categoryFilter');
  const saveButton = document.getElementById('saveProductBtn');
  const imageInput = document.getElementById('productImage');
  const imagePreview = document.getElementById('imagePreview');

  const modal = document.getElementById('productModal');
  modal?.addEventListener('show.bs.modal', (event) => {
    if (state.editingProductId) return;
    resetProductForm();
    if (event.relatedTarget?.dataset?.bsTarget) {
      state.editingProductId = null;
    }
  });
  modal?.addEventListener('hidden.bs.modal', () => {
    state.editingProductId = null;
  });

  searchInput?.addEventListener('input', renderProductsTable);
  filterSelect?.addEventListener('change', renderProductsTable);
  imageInput?.addEventListener('change', handleImagePreview);
  saveButton?.addEventListener('click', saveProduct);

  document.getElementById('productTable')?.addEventListener('click', handleProductActions);
  renderProductsTable();

  function handleImagePreview(event) {
    const file = event.target.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => { imagePreview.src = reader.result; };
    reader.readAsDataURL(file);
  }

  function saveProduct() {
    const formValues = getProductFormValues();
    if (!validateProductForm(formValues)) return;

    const imageFile = document.getElementById('productImage').files?.[0];
    const imageUrl = imagePreview?.src || APP_DATA.products[0].image;

    if (state.editingProductId) {
      state.products = state.products.map((product) => product.id === state.editingProductId ? { ...product, ...formValues, price: Number(formValues.price), stock: Number(formValues.stock), image: imageFile ? imageUrl : product.image } : product);
      showToast('Produk berhasil diperbarui', 'success');
    } else {
      const newProduct = { id: Date.now(), ...formValues, price: Number(formValues.price), stock: Number(formValues.stock), image: imageUrl };
      state.products.unshift(newProduct);
      showToast('Produk berhasil ditambahkan', 'success');
    }

    bootstrap.Modal.getInstance(modal)?.hide();
    renderProductsTable();
  }

  function getProductFormValues() {
    return {
      name: document.getElementById('productName').value.trim(),
      category: document.getElementById('productCategory').value.trim(),
      size: document.getElementById('productSize').value.trim(),
      color: document.getElementById('productColor').value.trim(),
      price: document.getElementById('productPrice').value,
      stock: document.getElementById('productStock').value,
      description: document.getElementById('productDescription').value.trim()
    };
  }

  function validateProductForm(values) {
    const required = Object.values(values).every((value) => String(value).length > 0);
    if (!required) {
      showToast('Lengkapi semua field produk', 'danger');
      return false;
    }
    return true;
  }

  function resetProductForm(product = null) {
    state.editingProductId = product?.id || null;
    document.getElementById('productModalTitle').textContent = product ? 'Edit Produk' : 'Tambah Produk';
    document.getElementById('productId').value = product?.id || '';
    document.getElementById('productName').value = product?.name || '';
    document.getElementById('productCategory').value = product?.category || '';
    document.getElementById('productSize').value = product?.size || '';
    document.getElementById('productColor').value = product?.color || '';
    document.getElementById('productPrice').value = product?.price || '';
    document.getElementById('productStock').value = product?.stock || '';
    document.getElementById('productDescription').value = product?.description || '';
    imagePreview.src = product?.image || 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent('<svg xmlns="http://www.w3.org/2000/svg" width="300" height="300"><rect width="100%" height="100%" rx="24" fill="#e2e8f0"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#64748b" font-family="Arial" font-size="18">Preview</text></svg>');
    document.getElementById('productImage').value = '';
  }

  function handleProductActions(event) {
    const button = event.target.closest('button[data-action]');
    if (!button) return;
    const productId = Number(button.dataset.id);
    const product = state.products.find((item) => item.id === productId);
    if (!product) return;

    if (button.dataset.action === 'edit') {
      resetProductForm(product);
      bootstrap.Modal.getOrCreateInstance(modal).show();
    }

    if (button.dataset.action === 'delete') {
      Swal.fire({ title: 'Hapus produk?', text: product.name, icon: 'warning', showCancelButton: true, confirmButtonText: 'Hapus' }).then((result) => {
        if (result.isConfirmed) {
          state.products = state.products.filter((item) => item.id !== productId);
          renderProductsTable();
          showToast('Produk dihapus', 'success');
        }
      });
    }
  }

  function renderProductsTable() {
    const query = searchInput.value.toLowerCase().trim();
    const category = filterSelect.value;
    const filtered = state.products.filter((product) => {
      const matchText = [product.name, product.category, product.size, product.color].join(' ').toLowerCase().includes(query);
      const matchCategory = category === 'all' || product.category === category;
      return matchText && matchCategory;
    });

    state.filteredProducts = filtered;
    const totalPages = Math.max(1, Math.ceil(filtered.length / state.pageSize));
    if (state.currentPage > totalPages) state.currentPage = totalPages;
    const start = (state.currentPage - 1) * state.pageSize;
    const visible = filtered.slice(start, start + state.pageSize);

    const tbody = document.querySelector('#productTable tbody');
    tbody.innerHTML = visible.map((product) => `
      <tr>
        <td><div class="product-thumb"><img src="${escapeHTML(product.image)}" alt="${escapeHTML(product.name)}"></div></td>
        <td><div class="fw-semibold">${escapeHTML(product.name)}</div><small class="text-muted">${escapeHTML(product.description)}</small></td>
        <td>${escapeHTML(product.category)}</td>
        <td>${escapeHTML(product.size)}</td>
        <td>${escapeHTML(product.color)}</td>
        <td>${rupiah(product.price)}</td>
        <td><span class="badge bg-${product.stock > 20 ? 'success' : product.stock > 10 ? 'warning text-dark' : 'danger'}">${product.stock}</span></td>
        <td>
          <div class="d-flex gap-2">
            <button class="btn btn-light btn-icon" data-action="edit" data-id="${product.id}"><i class="fa-solid fa-pen"></i></button>
            <button class="btn btn-light btn-icon text-danger" data-action="delete" data-id="${product.id}"><i class="fa-solid fa-trash"></i></button>
          </div>
        </td>
      </tr>
    `).join('') || '<tr><td colspan="8" class="text-center py-5 text-muted">Produk tidak ditemukan</td></tr>';

    document.getElementById('productTableInfo').textContent = `${filtered.length} produk ditemukan`;
    renderPagination(totalPages);
  }

  function renderPagination(totalPages) {
    const pagination = document.getElementById('productPagination');
    pagination.innerHTML = `
      <li class="page-item ${state.currentPage === 1 ? 'disabled' : ''}"><button class="page-link" data-page="prev">Prev</button></li>
      ${Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => `<li class="page-item ${state.currentPage === page ? 'active' : ''}"><button class="page-link" data-page="${page}">${page}</button></li>`).join('')}
      <li class="page-item ${state.currentPage === totalPages ? 'disabled' : ''}"><button class="page-link" data-page="next">Next</button></li>
    `;
    pagination.querySelectorAll('button').forEach((button) => button.addEventListener('click', () => {
      if (button.dataset.page === 'prev') state.currentPage = Math.max(1, state.currentPage - 1);
      else if (button.dataset.page === 'next') state.currentPage = Math.min(totalPages, state.currentPage + 1);
      else state.currentPage = Number(button.dataset.page);
      renderProductsTable();
    }));
  }
}

function initCategoriesPage() {
  const searchInput = document.getElementById('categorySearch');
  const saveButton = document.getElementById('saveCategoryBtn');
  const modal = document.getElementById('categoryModal');

  modal?.addEventListener('show.bs.modal', () => {
    if (state.editingCategoryId) return;
    resetCategoryForm();
  });
  modal?.addEventListener('hidden.bs.modal', () => {
    state.editingCategoryId = null;
  });
  searchInput?.addEventListener('input', renderCategoriesTable);
  saveButton?.addEventListener('click', saveCategory);
  document.getElementById('categoryTable')?.addEventListener('click', handleCategoryActions);
  renderCategoriesTable();

  function resetCategoryForm(category = null) {
    state.editingCategoryId = category?.id || null;
    document.querySelector('#categoryModal .modal-title').textContent = category ? 'Edit Kategori' : 'Tambah Kategori';
    document.getElementById('categoryId').value = category?.id || '';
    document.getElementById('categoryName').value = category?.name || '';
    document.getElementById('categoryDescription').value = category?.description || '';
  }

  function saveCategory() {
    const name = document.getElementById('categoryName').value.trim();
    const description = document.getElementById('categoryDescription').value.trim();
    if (!name || !description) return showToast('Isi nama dan deskripsi kategori', 'danger');

    if (state.editingCategoryId) {
      state.categories = state.categories.map((category) => category.id === state.editingCategoryId ? { ...category, name, description } : category);
      showToast('Kategori diperbarui', 'success');
    } else {
      state.categories.unshift({ id: Date.now(), name, description, count: 0 });
      showToast('Kategori ditambahkan', 'success');
    }
    bootstrap.Modal.getInstance(modal)?.hide();
    renderCategoriesTable();
  }

  function handleCategoryActions(event) {
    const button = event.target.closest('button[data-action]');
    if (!button) return;
    const categoryId = Number(button.dataset.id);
    const category = state.categories.find((item) => item.id === categoryId);
    if (!category) return;

    if (button.dataset.action === 'edit') {
      resetCategoryForm(category);
      bootstrap.Modal.getOrCreateInstance(modal).show();
    }

    if (button.dataset.action === 'delete') {
      Swal.fire({ title: 'Hapus kategori?', text: category.name, icon: 'warning', showCancelButton: true, confirmButtonText: 'Hapus' }).then((result) => {
        if (result.isConfirmed) {
          state.categories = state.categories.filter((item) => item.id !== categoryId);
          renderCategoriesTable();
          showToast('Kategori dihapus', 'success');
        }
      });
    }
  }

  function renderCategoriesTable() {
    const query = searchInput.value.toLowerCase().trim();
    const filtered = state.categories.filter((category) => [category.name, category.description].join(' ').toLowerCase().includes(query));
    document.querySelector('#categoryTable tbody').innerHTML = filtered.map((category) => `
      <tr>
        <td class="fw-semibold">${escapeHTML(category.name)}</td>
        <td>${escapeHTML(category.description)}</td>
        <td><span class="badge bg-primary">${category.count}</span></td>
        <td>
          <button class="btn btn-light btn-icon me-2" data-action="edit" data-id="${category.id}"><i class="fa-solid fa-pen"></i></button>
          <button class="btn btn-light btn-icon text-danger" data-action="delete" data-id="${category.id}"><i class="fa-solid fa-trash"></i></button>
        </td>
      </tr>
    `).join('') || '<tr><td colspan="4" class="text-center py-5 text-muted">Kategori tidak ditemukan</td></tr>';
  }
}
