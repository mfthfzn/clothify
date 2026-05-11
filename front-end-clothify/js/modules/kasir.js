function initTransactionsPage() {
  const searchInput = document.getElementById('cashierSearch');
  const productList = document.getElementById('cashierProductList');
  const cartList = document.getElementById('cartList');
  const subtotalAmount = document.getElementById('subtotalAmount');
  const totalAmount = document.getElementById('totalAmount');

  searchInput?.addEventListener('input', renderCashierProducts);
  document.getElementById('checkoutBtn')?.addEventListener('click', checkoutCart);
  productList?.addEventListener('click', handleCashierActions);
  cartList?.addEventListener('click', handleCartActions);

  renderCashierProducts();
  renderCart();

  function handleCashierActions(event) {
    const button = event.target.closest('button[data-add]');
    if (!button) return;
    const productId = Number(button.dataset.add);
    const product = APP_DATA.products.find((item) => item.id === productId);
    if (!product) return;
    const existing = state.cart.find((item) => item.id === productId);
    if (existing) existing.qty += 1;
    else state.cart.push({ id: product.id, name: product.name, price: product.price, qty: 1, image: product.image });
    renderCart();
    showToast(`${product.name} ditambahkan ke keranjang`, 'success');
  }

  function handleCartActions(event) {
    const button = event.target.closest('button[data-cart-action]');
    if (!button) return;
    const productId = Number(button.dataset.id);

    if (button.dataset.cartAction === 'remove') state.cart = state.cart.filter((item) => item.id !== productId);
    if (button.dataset.cartAction === 'increase') state.cart = state.cart.map((item) => item.id === productId ? { ...item, qty: item.qty + 1 } : item);
    if (button.dataset.cartAction === 'decrease') state.cart = state.cart.map((item) => item.id === productId ? { ...item, qty: Math.max(1, item.qty - 1) } : item);

    renderCart();
  }

  function renderCashierProducts() {
    const query = searchInput.value.toLowerCase().trim();
    const items = APP_DATA.products.filter((product) => product.name.toLowerCase().includes(query) || product.category.toLowerCase().includes(query));
    productList.innerHTML = items.map((product) => `
      <div class="product-card">
        <div class="d-flex align-items-center gap-3">
          <div class="product-thumb"><img src="${escapeHTML(product.image)}" alt="${escapeHTML(product.name)}"></div>
          <div class="product-meta"><div class="fw-semibold">${escapeHTML(product.name)}</div><small class="text-muted">${escapeHTML(product.category)} • Stok ${product.stock}</small><span class="product-price">${rupiah(product.price)}</span></div>
        </div>
        <button class="btn btn-outline-primary btn-sm" data-add="${product.id}"><i class="fa-solid fa-plus"></i></button>
      </div>
    `).join('') || '<div class="text-muted">Produk tidak ditemukan.</div>';
  }

  function renderCart() {
    cartList.innerHTML = state.cart.map((item) => `
      <div class="cart-item">
        <div class="d-flex align-items-center gap-3">
          <div class="product-thumb" style="width:56px;height:56px"><img src="${escapeHTML(item.image)}" alt="${escapeHTML(item.name)}"></div>
          <div><div class="fw-semibold">${escapeHTML(item.name)}</div><small class="text-muted">${rupiah(item.price)}</small></div>
        </div>
        <div class="text-end">
          <div class="qty-control mb-2">
            <button class="btn btn-light" data-cart-action="decrease" data-id="${item.id}">-</button>
            <strong>${item.qty}</strong>
            <button class="btn btn-light" data-cart-action="increase" data-id="${item.id}">+</button>
          </div>
          <button class="btn btn-link text-danger p-0" data-cart-action="remove" data-id="${item.id}">Hapus</button>
        </div>
      </div>
    `).join('') || '<div class="text-muted py-4">Keranjang kosong.</div>';

    const subtotal = state.cart.reduce((sum, item) => sum + (item.price * item.qty), 0);
    subtotalAmount.textContent = rupiah(subtotal);
    totalAmount.textContent = rupiah(subtotal);
  }

  function checkoutCart() {
    if (!state.cart.length) return showToast('Keranjang masih kosong', 'danger');
    Swal.fire({ title: 'Checkout berhasil', text: 'Transaksi simulasi selesai.', icon: 'success' });
    state.cart = [];
    renderCart();
  }
}

function initTransactionHistoryPage() {
  const searchInput = document.getElementById('transactionSearch');
  searchInput?.addEventListener('input', renderTransactionsTable);
  document.getElementById('transactionTable')?.addEventListener('click', handleTransactionActions);
  renderTransactionsTable();

  function renderTransactionsTable() {
    const query = searchInput.value.toLowerCase().trim();
    const filtered = APP_DATA.transactions.filter((transaction) => 
      transaction.id.toLowerCase().includes(query) || 
      transaction.date.toLowerCase().includes(query) ||
      transaction.status.toLowerCase().includes(query)
    );

    document.querySelector('#transactionTable tbody').innerHTML = filtered.map((transaction) => `
      <tr>
        <td class="fw-semibold">${escapeHTML(transaction.id)}</td>
        <td>${escapeHTML(transaction.date)}</td>
        <td>${rupiah(transaction.total)}</td>
        <td><span class="badge bg-success">${escapeHTML(transaction.status)}</span></td>
        <td>
          <button class="btn btn-light btn-icon" data-action="view" data-id="${transaction.id}" title="Lihat Detail"><i class="fa-solid fa-eye"></i></button>
          <button class="btn btn-light btn-icon" data-action="print" data-id="${transaction.id}" title="Cetak"><i class="fa-solid fa-print"></i></button>
        </td>
      </tr>
    `).join('') || '<tr><td colspan="5" class="text-center py-5 text-muted">Riwayat transaksi tidak ditemukan</td></tr>';
  }

  function handleTransactionActions(event) {
    const button = event.target.closest('button[data-action]');
    if (!button) return;
    const transactionId = button.dataset.id;
    const transaction = APP_DATA.transactions.find((item) => item.id === transactionId);
    if (!transaction) return;

    if (button.dataset.action === 'view') {
      Swal.fire({
        title: `Detail Transaksi ${transaction.id}`,
        html: `
          <div class="text-start">
            <p><strong>Tanggal:</strong> ${escapeHTML(transaction.date)}</p>
            <p><strong>Total:</strong> ${rupiah(transaction.total)}</p>
            <p><strong>Status:</strong> ${escapeHTML(transaction.status)}</p>
            <p><strong>Jumlah Item:</strong> ${transaction.items}</p>
          </div>
        `,
        icon: 'info',
        confirmButtonText: 'Tutup'
      });
    }

    if (button.dataset.action === 'print') {
      printTransactionDetail(transaction);
    }
  }
}

function printTransactionDetail(transaction) {
  const printContent = `
    <!DOCTYPE html>
    <html lang="id">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Struk Transaksi ${transaction.id}</title>
      <style>
        * {
          margin: 0;
          padding: 0;
          box-sizing: border-box;
        }
        body {
          font-family: 'Courier New', monospace;
          background-color: #f5f5f5;
          padding: 20px;
        }
        .receipt {
          background-color: white;
          width: 80mm;
          margin: 0 auto;
          padding: 20px;
          border: 1px solid #ddd;
        }
        .header {
          text-align: center;
          border-bottom: 2px solid #333;
          padding-bottom: 10px;
          margin-bottom: 15px;
        }
        .store-name {
          font-size: 12px;
          font-weight: bold;
          margin-bottom: 3px;
        }
        .receipt-title {
          font-size: 11px;
          color: #666;
          margin-bottom: 5px;
        }
        .divider {
          border-bottom: 1px dashed #333;
          margin: 10px 0;
        }
        .info-row {
          display: flex;
          justify-content: space-between;
          padding: 4px 0;
          font-size: 11px;
        }
        .info-label {
          font-weight: bold;
        }
        .total-section {
          background-color: #f0f0f0;
          padding: 10px;
          text-align: center;
          margin: 10px 0;
          border: 1px solid #ddd;
        }
        .total-label {
          font-size: 10px;
          color: #666;
        }
        .total-amount {
          font-size: 20px;
          font-weight: bold;
          color: #2563eb;
          margin-top: 5px;
        }
        .status-box {
          text-align: center;
          padding: 8px;
          background-color: #d4edda;
          color: #155724;
          border-radius: 3px;
          margin: 10px 0;
          font-weight: bold;
          font-size: 12px;
        }
        .footer {
          text-align: center;
          font-size: 10px;
          color: #999;
          margin-top: 15px;
          border-top: 1px solid #ddd;
          padding-top: 10px;
        }
        .footer-text {
          margin: 3px 0;
        }
        @media print {
          body {
            background-color: white;
            padding: 0;
          }
          .receipt {
            border: none;
            box-shadow: none;
            margin: 0;
            padding: 0;
            width: 100%;
          }
        }
      </style>
    </head>
    <body>
      <div class="receipt">
        <div class="header">
          <div class="store-name">♦ CLOTHIFY ♦</div>
          <div class="receipt-title">TOKO PAKAIAN</div>
        </div>
        
        <div class="divider"></div>
        
        <div style="margin-bottom: 10px;">
          <div class="info-row">
            <span class="info-label">NO. TRANSAKSI</span>
            <span>${escapeHTML(transaction.id)}</span>
          </div>
          <div class="info-row">
            <span class="info-label">TANGGAL</span>
            <span>${escapeHTML(transaction.date)}</span>
          </div>
          <div class="info-row">
            <span class="info-label">JUMLAH ITEM</span>
            <span>${transaction.items}</span>
          </div>
        </div>
        
        <div class="divider"></div>
        
        <div class="total-section">
          <div class="total-label">TOTAL PEMBAYARAN</div>
          <div class="total-amount">${rupiah(transaction.total)}</div>
        </div>
        
        <div class="status-box">
          ✓ ${escapeHTML(transaction.status)}
        </div>
        
        <div class="divider"></div>
        
        <div class="footer">
          <div class="footer-text">Terima kasih telah berbelanja</div>
          <div class="footer-text">di Clothify</div>
          <div class="footer-text" style="margin-top: 8px; font-size: 9px;">Printed: ${new Date().toLocaleString('id-ID')}</div>
        </div>
      </div>

      <script>
        window.onload = function() {
          setTimeout(function() {
            window.print();
          }, 500);
        };
      </script>
    </body>
    </html>
  `;

  const printWindow = window.open('', '_blank', 'width=400,height=600');
  printWindow.document.write(printContent);
  printWindow.document.close();
}
