document.addEventListener('DOMContentLoaded', () => {
  applyTheme(state.currentTheme);
  initCommonUI();
  initPage();
});

function initCommonUI() {
  const sidebarToggle = document.getElementById('sidebarToggle');
  const darkModeToggle = document.getElementById('darkModeToggle');

  sidebarToggle?.addEventListener('click', () => {
    document.body.classList.toggle('sidebar-collapsed');
    document.getElementById('sidebar')?.classList.toggle('show');
  });

  darkModeToggle?.addEventListener('click', () => {
    const nextTheme = document.documentElement.dataset.theme === 'dark' ? 'light' : 'dark';
    applyTheme(nextTheme);
    showToast(`Mode ${nextTheme === 'dark' ? 'gelap' : 'terang'} aktif`, 'success');
  });

  document.querySelectorAll('a[href="index.html"]').forEach((link) => {
    link.addEventListener('click', () => {
      sessionStorage.removeItem('role');
    });
  });
}

function applyTheme(theme) {
  document.documentElement.dataset.theme = theme;
  state.currentTheme = theme;
  localStorage.setItem('theme', theme);
}

function initPage() {
  const page = document.body.dataset.page || 'login';

  if (page !== 'login') {
    enforceRoleAccess(page);
    filterSidebarByRole();
  }

  if (page === 'login') initLoginPage();
  if (page === 'dashboard') initDashboardPage();
  if (page === 'products') initProductsPage();
  if (page === 'categories') initCategoriesPage();
  if (page === 'transactions') initTransactionsPage();
  if (page === 'transaction-history') initTransactionHistoryPage();
}

function getCurrentRole() {
  const role = sessionStorage.getItem('role');
  return role === 'admin' || role === 'kasir' ? role : null;
}

function getDefaultPageByRole(role) {
  return role === 'kasir' ? 'kasir/transactions.html' : 'admin/dashboard.html';
}

function enforceRoleAccess(page) {
  const role = getCurrentRole();
  if (!role) {
    window.location.href = 'index.html';
    return;
  }

  const allowedPages = ROLE_ACCESS[role] || [];
  if (!allowedPages.includes(page)) {
    window.location.href = getDefaultPageByRole(role);
  }
}

function filterSidebarByRole() {
  const role = getCurrentRole();
  if (!role) return;

  const allowedPages = ROLE_ACCESS[role] || [];
  document.querySelectorAll('.sidebar-nav .nav-link').forEach((link) => {
    const href = link.getAttribute('href') || '';
    if (href === 'index.html') return;

    const pageName = Object.keys(PAGE_ROUTE).find((key) => PAGE_ROUTE[key].endsWith(href));
    if (!pageName) return;

    link.style.display = allowedPages.includes(pageName) ? '' : 'none';
  });
}

function initLoginPage() {
  const form = document.getElementById('loginForm');
  const toggleButton = document.getElementById('togglePassword');
  const passwordInput = document.getElementById('password');
  const usernameInput = document.getElementById('username');
  const usernameError = document.getElementById('usernameError');
  const passwordError = document.getElementById('passwordError');

  const userAccounts = {
    admin: { password: 'admin123', role: 'admin' },
    kasir: { password: 'kasir123', role: 'kasir' }
  };

  toggleButton?.addEventListener('click', () => {
    const isHidden = passwordInput.type === 'password';
    passwordInput.type = isHidden ? 'text' : 'password';
    toggleButton.innerHTML = `<i class="fa-regular fa-${isHidden ? 'eye-slash' : 'eye'}"></i>`;
  });

  form?.addEventListener('submit', (event) => {
    event.preventDefault();
    let valid = true;
    const normalizedUsername = usernameInput.value.trim().toLowerCase();
    const password = passwordInput.value.trim();

    if (!normalizedUsername) {
      usernameInput.classList.add('is-invalid');
      usernameError.textContent = 'Masukkan username yang valid.';
      valid = false;
    } else {
      usernameInput.classList.remove('is-invalid');
    }

    if (password.length < 6) {
      passwordInput.classList.add('is-invalid');
      passwordError.textContent = 'Password minimal 6 karakter.';
      valid = false;
    } else {
      passwordInput.classList.remove('is-invalid');
      passwordError.textContent = '';
    }

    if (valid) {
      const account = userAccounts[normalizedUsername];
      if (!account || account.password !== password) {
        passwordInput.classList.add('is-invalid');
        passwordError.textContent = 'Username atau password tidak sesuai.';
        showToast('Login gagal. Cek username/password akun Anda.', 'danger');
        return;
      }

      sessionStorage.setItem('role', account.role);
      const targetPage = getDefaultPageByRole(account.role);
      showToast(`Login ${account.role} berhasil. Mengarahkan...`, 'success');
      setTimeout(() => window.location.href = targetPage, 700);
    }
  });
}

function initReportsPage() {
  document.querySelectorAll('button').forEach((button) => {
    const label = button.textContent?.toLowerCase() || '';
    if (label.includes('export')) {
      button.addEventListener('click', () => showToast(`${button.textContent.trim()} diklik`, 'success'));
    }
  });
}

function showToast(message, type = 'success') {
  const toastContainer = document.getElementById('toastContainer');
  if (!toastContainer) {
    alert(message);
    return;
  }

  const toastId = `toast-${Date.now()}`;
  const bgClass = type === 'danger' ? 'border-danger' : type === 'warning' ? 'border-warning' : 'border-success';
  toastContainer.insertAdjacentHTML('beforeend', `
    <div id="${toastId}" class="toast align-items-center ${bgClass} border-2" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">${escapeHTML(message)}</div>
        <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast"></button>
      </div>
    </div>
  `);

  const toastElement = document.getElementById(toastId);
  const toast = bootstrap.Toast.getOrCreateInstance(toastElement, { delay: 2600 });
  toast.show();
  toastElement.addEventListener('hidden.bs.toast', () => toastElement.remove());
}
