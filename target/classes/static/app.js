// ==========================================
// 🌐 ĐỊNH NGHĨA HẰNG SỐ API (ĐỒNG BỘ VỚI BACKEND)
// ==========================================
const API_BOOKS = "http://localhost:8080/api/books";
const API_CATEGORIES = "http://localhost:8080/api/categories";
const API_URL = "http://localhost:8080/api/users";
const API_BORROWS = "http://localhost:8080/api/borrows"; // CHUẨN HÓA: Đổi từ /borrows thành /borrow theo đúng Controller
const API_THONGKE = "http://localhost:8080/api/thongke/tongquan";
const API_AUTH_LOGIN = "http://localhost:8080/api/auth/login";
let isEditMode = false;
let isCatEditMode = false;
let isUserEditMode = false;
let isBorrowEditMode = false;

document.addEventListener("DOMContentLoaded", function () {
  const formLogin = document.getElementById("form-login");
  if (formLogin) {
    formLogin.addEventListener("submit", function (e) {
      e.preventDefault();

      const loginError = document.getElementById("login-error-msg");
      loginError.classList.add("d-none"); // Ẩn thông báo lỗi cũ nếu có

      const loginData = {
        username: document.getElementById("login-username").value.trim(),
        password: document.getElementById("login-password").value,
      };

      // Gửi dữ liệu đăng nhập lên API_AUTH_LOGIN của Backend
      fetch(API_AUTH_LOGIN, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData),
      })
        .then(async (res) => {
          // Nếu Backend trả về status 200 OK (ResponseEntity.ok)
          if (res.ok) {
            return res.json();
          }
          // Nếu Backend trả về 401 Unauthorized (.status(HttpStatus.UNAUTHORIZED))
          const errorText = await res.text();
          throw new Error(errorText || "Đăng nhập thất bại!");
        })
        .then((account) => {
          alert(`🎉 Đăng nhập thành công! Chào mừng ${account.username}`);

          // Lưu trữ thông tin định danh và phân quyền vào localStorage của trình duyệt
          localStorage.setItem("userToken", account.id);
          localStorage.setItem("userRole", account.role);
          localStorage.setItem("username", account.username);

          // Ẩn form đăng nhập và tải dữ liệu ứng dụng
          document.getElementById("login-container").classList.add("d-none");

          // Áp dụng bộ lọc phân quyền hiển thị giao diện
          applyRoleAuthorization(account.role);

          // Kích hoạt load dữ liệu cũ của bạn
          loadCategories();
          loadBooks();
          taiThongKeTongQuan();
        })
        .catch((error) => {
          // Bắt chuỗi thông báo lỗi trả về từ .body("...") của Backend và hiển thị lên UI
          loginError.innerText = "❌ " + error.message;
          loginError.classList.remove("d-none");
        });
    });
  }

  // ==========================================
  // 🧭 ĐIỀU HƯỚNG TABS SIDEBAR
  // ==========================================
  document.getElementById("menu-books").addEventListener("click", function (e) {
    e.preventDefault();
    switchPage("panel-books", this);
  });

  document
    .getElementById("menu-categories")
    .addEventListener("click", function (e) {
      e.preventDefault();
      switchPage("panel-categories", this);
    });

  const menuUsers = document.getElementById("menu-users");
  if (menuUsers) {
    menuUsers.addEventListener("click", function (e) {
      e.preventDefault();
      switchPage("panel-users", this);
      loadUsers();
    });
  }

  const menuBorrows = document.getElementById("menu-borrows");
  if (menuBorrows) {
    menuBorrows.addEventListener("click", function (e) {
      e.preventDefault();
      switchPage("panel-borrows", this);
      loadBorrows();
      syncBorrowBooks();
      initBorrowDates();
    });
  }
  // xử lý đăng nhập
  // Hàm kiểm tra xem người dùng đã đăng nhập chưa khi F5 lại trang
  function checkLoginStatus() {
    const token = localStorage.getItem("userToken");
    const role = localStorage.getItem("userRole");
    const loginContainer = document.getElementById("login-container");

    if (token && role) {
      // Đã đăng nhập trước đó -> Ẩn form đăng nhập ngay lập tức
      if (loginContainer) loginContainer.classList.add("d-none");
      applyRoleAuthorization(role);
    } else {
      // Chưa đăng nhập -> Hiện form đăng nhập, chặn không cho xem dữ liệu
      if (loginContainer) loginContainer.classList.remove("d-none");
    }
  }

  // Hàm phân quyền: Ẩn bớt chức năng dựa trên quyền hạn của tài khoản
  function applyRoleAuthorization(role) {
    document.getElementById("add-book-section").style.display = "block";
    document.getElementById("statistics-section").classList.remove("d-none");
    document.getElementById("add-category-section").style.display = "block";
    document.getElementById("add-borrow-section").style.display = "block";
    const menuUsers = document.getElementById("menu-users");

    const btnAddBook = document.getElementById("btn-add-book");
    const btnAddCategory = document.getElementById("btn-add-category");
    const btnAddUser = document.getElementById("btn-add-user");
    const btnAddBorrow = document.getElementById("btn-add-borrow");

    // MEMBER (Độc giả)
    if (role === "MEMBER") {
      document.getElementById("add-book-section").style.display = "none";
      document.getElementById("statistics-section").style.display = "none";
      document.getElementById("add-category-section").style.display = "none";
      document.getElementById("add-borrow-section").style.display = "none";
      if (menuUsers) menuUsers.style.display = "none";

      if (btnAddBook) btnAddBook.style.display = "none";

      if (btnAddCategory) btnAddCategory.style.display = "none";

      if (btnAddUser) btnAddUser.style.display = "none";

      if (btnAddBorrow) btnAddBorrow.style.display = "none";
    }

    // LIBRARIAN (Thủ thư)
  }
  // ==========================================
  // 🔄 NÚT LÀM MỚI DỮ LIỆU (REFRESH)
  // ==========================================
  document
    .getElementById("btn-refresh-books")
    .addEventListener("click", loadBooks);
  document
    .getElementById("btn-refresh-categories")
    .addEventListener("click", loadCategories);

  const btnRefreshUsers = document.getElementById("btn-refresh-users");
  if (btnRefreshUsers) btnRefreshUsers.addEventListener("click", loadUsers);

  const btnRefreshBorrows = document.getElementById("btn-refresh-borrows");
  if (btnRefreshBorrows) {
    btnRefreshBorrows.addEventListener("click", function () {
      loadBorrows();
      syncBorrowBooks();
    });
  }

  // ==========================================
  // 📚 1. XỬ LÝ FORM SÁCH (BOOK)
  // ==========================================
  document
    .getElementById("form-add-book")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const bookId = document.getElementById("book-id").value;
      const bookData = {
        title: document.getElementById("book-title").value.trim(),
        author: document.getElementById("book-author").value.trim(),
        isbn: document.getElementById("book-isbn").value.trim(),
        totalQuantity:
          parseInt(document.getElementById("book-quantity").value) || 0,
        categoryId: parseInt(document.getElementById("book-cat-id").value) || 0,
      };

      if (isEditMode) {
        bookData.id = parseInt(bookId);
        bookData.availableQuantity =
          parseInt(document.getElementById("book-quantity").value) || 0;
      }

      const method = isEditMode ? "PUT" : "POST";
      fetch(API_BOOKS, {
        method: method,
        headers: {
          "Content-Type": "application/json",
          User_Role: localStorage.getItem("userRole"),
        },
        body: JSON.stringify(bookData),
      })
        .then((res) => res.json())
        .then((data) => {
          if (data.success) {
            alert("thành công " + data.message);
            resetFormState();
            loadBooks();
          } else {
            alert("thất bại " + data.message);
          }
        })
        .catch(() =>
          alert("Lỗi hệ thống: Không thể kết nối tới Backend Sách!")
        );
    });

  // ==========================================
  // 🏷️ 2. XỬ LÝ FORM THỂ LOẠI (CATEGORY)
  // ==========================================
  document
    .getElementById("form-add-category")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const catId = document.getElementById("category-id").value;
      const catData = {
        name: document.getElementById("category-name").value.trim(),
        description: document.getElementById("category-desc").value.trim(),
      };

      if (isCatEditMode) catData.id = parseInt(catId);
      const method = isCatEditMode ? "PUT" : "POST";

      fetch(API_CATEGORIES, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(catData),
      })
        .then((res) => res.json())
        .then((data) => {
          if (data.success) {
            alert("thành công " + data.message);
            resetCatFormState();
            loadCategories();
            loadBooks();
          } else {
            alert("thất bại " + data.message);
          }
        })
        .catch(() => alert("Lỗi kết nối API Thể loại!"));
    });

  // ==========================================
  // 👥 3. XỬ LÝ FORM NGƯỜI DÙNG (USER)
  // ==========================================
  const formUser = document.getElementById("form-add-user");
  if (formUser) {
    formUser.addEventListener("submit", function (e) {
      e.preventDefault();
      const userId = document.getElementById("user-id").value;
      const userData = {
        username: document.getElementById("user-username").value.trim(),
        password: document.getElementById("user-password").value,
        fullname: document.getElementById("user-fullname").value.trim(),
        email: document.getElementById("user-email").value.trim(),
        role: document.getElementById("user-role").value,
        status: parseInt(document.getElementById("user-status").value),
      };

      if (isUserEditMode) userData.id = parseInt(userId);
      const method = isUserEditMode ? "PUT" : "POST";

      fetch(API_URL, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData),
      })
        .then((res) => res.json())
        .then((data) => {
          if (data.success) {
            alert("thành công " + data.message);
            resetUserFormState();
            loadUsers();
          } else {
            alert("thất bại " + data.message);
          }
        })

        .catch(() => alert("Lỗi kết nối API Người dùng!"));
    });
  }

  // ==========================================
  // 📑 4. XỬ LÝ FORM MƯỢN TRẢ (BORROW)
  // ==========================================
  const formBorrow = document.getElementById("form-add-borrow");
  if (formBorrow) {
    formBorrow.addEventListener("submit", function (e) {
      e.preventDefault();
      const borrowId = document.getElementById("borrow-id").value;
      const borrowData = {
        userId: parseInt(document.getElementById("borrow-user-id").value),
        bookId: parseInt(document.getElementById("borrow-book-id").value),
        quantity:
          parseInt(document.getElementById("borrow-quantity").value) || 1,
        borrowDate: document.getElementById("borrow-date").value,
        dueDate: document.getElementById("borrow-due-date").value,
      };

      if (isBorrowEditMode) {
        borrowData.id = parseInt(borrowId);
        borrowData.status = document.getElementById("borrow-status").value;
      } else {
        borrowData.status = "BORROWING";
      }

      const method = isBorrowEditMode ? "PUT" : "POST";
      fetch(API_BORROWS, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(borrowData),
      })
        .then((res) => res.json())

        .then((data) => {
          // Xử lý dữ liệu trả về thông qua Object Map chứa isSuccess và message từ Backend
          console.log("PUT response:", data);
          if (data.success) {
            alert("🎉 " + data.message);
            resetBorrowFormState();
            loadBorrows();
            loadBooks();
          } else {
            alert("❌ " + data.message);
          }
        })
        .catch(() =>
          alert("Lỗi hệ thống: Không thể kết nối tới Backend Mượn Trả!")
        );
    });
  }
  //search book
  const searchInput = document.getElementById("searchBook");

  searchInput.addEventListener("input", async function () {
    const keyword = this.value.trim();

    let url = "http://localhost:8080/api/books";

    if (keyword !== "") {
      url = `http://localhost:8080/api/books/search?keyword=${encodeURIComponent(
        keyword
      )}`;
    }

    const response = await fetch(url);
    const books = await response.json();

    renderBooks(books);
  });
  //search thể loại
  const searchCategory = document.getElementById("searchCategory");

  if (searchCategory) {
    searchCategory.addEventListener("input", async function () {
      const keyword = this.value.trim();

      let url = API_CATEGORIES;

      if (keyword !== "") {
        url = `${API_CATEGORIES}/search?keyword=${encodeURIComponent(keyword)}`;
      }

      const response = await fetch(url);
      const categories = await response.json();

      renderCategories(categories);
    });
  }
  //search người dùng
  const searchUser = document.getElementById("searchUser");

  if (searchUser) {
    searchUser.addEventListener("input", async function () {
      const keyword = this.value.trim();

      let url = API_URL;

      if (keyword !== "") {
        url = `${API_URL}/search?keyword=${encodeURIComponent(keyword)}`;
      }

      const response = await fetch(url);
      const users = await response.json();

      renderUsers(users);
    });
  }

  //tìm kiếm phiếu mượn
  const searchBorrow = document.getElementById("searchBorrow");

  if (searchBorrow) {
    searchBorrow.addEventListener("input", async function () {
      const keyword = this.value.trim();

      let url = API_BORROWS;

      if (keyword !== "") {
        url = `${API_BORROWS}/search?keyword=${encodeURIComponent(keyword)}`;
      }

      const response = await fetch(url);
      const borrows = await response.json();

      renderBorrows(borrows);
    });
  }
  // Tải dữ liệu ban đầu cho ứng dụng
  loadCategories();
  loadBooks();
  taiThongKeTongQuan();
  // Kiểm tra trạng thái đăng nhập ngay khi mở trang
  checkLoginStatus();
  const btnLogout = document.getElementById("btn-logout");
  const modal = document.getElementById("logout-modal");

  const btnCancel = document.getElementById("btn-cancel");
  const btnConfirm = document.getElementById("btn-confirm");

  // Mở hộp xác nhận
  btnLogout.addEventListener("click", () => {
    modal.style.display = "flex";
  });

  // Nhấn Hủy
  btnCancel.addEventListener("click", () => {
    modal.style.display = "none";
  });

  // Nhấn Đăng xuất
  btnConfirm.addEventListener("click", () => {
    localStorage.clear();

    window.location.href = "index.html";
  });
});
function renderUsers(users) {
  console.log("Đang render", users);
  const tbody = document.getElementById("table-user-body");
  console.log("tbody =", tbody);
  tbody.innerHTML = "";

  users.forEach((u) => {
    const userJson = JSON.stringify(u).replace(/"/g, "&quot;");

    const statusBadge =
      u.status === 1
        ? `<span class="badge bg-success-subtle text-success px-2 py-1">Hoạt động</span>`
        : `<span class="badge bg-danger-subtle text-danger px-2 py-1">Bị khóa</span>`;

    tbody.innerHTML += `
      <tr>
          <td>${u.id}</td>
          <td>${u.username}</td>
          <td>${u.fullName || u.fullname}</td>
          <td>${u.email}</td>
          <td>${u.role}</td>
          <td>${statusBadge}</td>
          <td class="text-center">
              <button class="btn btn-sm btn-outline-warning me-1"
                  onclick="handleUserEdit('${userJson}')">
                  <i class="fa-solid fa-pen"></i>
              </button>

              <button class="btn btn-sm btn-outline-danger"
                  onclick="handleUserDelete(${u.id}, '${u.username}')">
                  <i class="fa-solid fa-trash"></i>
              </button>
          </td>
      </tr>`;
  });
}
//hàm thực hiện chức năng mướn trả
function renderBorrows(borrows) {
  const tbody = document.getElementById("table-borrow-body");
  const role = localStorage.getItem("userRole");
  const actionHeader = document.getElementById("borrow-action-header");
  actionHeader.style.display = role === "ADMIN" ? "" : "none";
  tbody.innerHTML = "";

  if (borrows.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="8" class="text-center text-muted py-3">
          Không tìm thấy phiếu mượn
        </td>
      </tr>`;
    return;
  }

  borrows.forEach((br) => {
    const borrowJson = JSON.stringify(br).replace(/"/g, "&quot;");

    const today = new Date();
    const dueDate = new Date(br.dueDate);
    const isOverdue = !br.returnDate && today > dueDate;

    let statusBadge = "";
    let rowClass = "";
    let actionButton = "";

    if (br.returnDate) {
      statusBadge = `
        <span class="badge bg-success-subtle text-success px-2 py-1">
          <i class="fa-solid fa-circle-check me-1"></i>Đã trả
        </span>`;

      actionButton = `
        <button class="btn btn-sm btn-outline-secondary me-1" disabled>
          <i class="fa-solid fa-check"></i> Hoàn tất
        </button>`;
    } else if (isOverdue) {
      const timeDiff = today.getTime() - dueDate.getTime();
      const daysOverdue = Math.ceil(timeDiff / (1000 * 3600 * 24));

      statusBadge = `
        <span class="badge bg-danger text-white px-2 py-1">
          <i class="fa-solid fa-triangle-exclamation me-1"></i>
          Trễ ${daysOverdue} ngày
        </span>`;

      rowClass = "table-danger";

      actionButton = `
        <button class="btn btn-sm btn-danger me-1"
          onclick="handleReturnBook(${br.id})">
          <i class="fa-solid fa-rotate-left me-1"></i>
          Thu phạt & Trả
        </button>`;
    } else {
      statusBadge = `
        <span class="badge bg-warning-subtle text-warning px-2 py-1">
          <i class="fa-solid fa-clock me-1"></i>Đang giữ
        </span>`;

      actionButton = `
        <button class="btn btn-sm btn-success me-1"
          onclick="handleReturnBook(${br.id})">
          <i class="fa-solid fa-rotate-left me-1"></i>
          Trả sách
        </button>`;
    }

    tbody.innerHTML += `
      <tr class="${rowClass}">
        <td class="text-center fw-bold text-secondary">#${br.ticketId}</td>

        <td class="text-center font-monospace text-muted small">
          ${br.id}
        </td>

        <td>
          <div class="fw-bold text-dark">
            Mã Độc Giả: ${br.userId}
          </div>

          <small class="text-muted">
            <i class="fa-regular fa-user me-1"></i>
            ${br.username || "Chưa cập nhật"}
          </small>
        </td>

        <td>
          <div class="fw-semibold text-primary">
            ${br.bookTitle || "Mã sách: " + br.bookId}
          </div>

          <small class="text-muted">
            SL: <b>${br.quantity}</b> cuốn
          </small>
        </td>

        <td>
          <div class="small">
            📅 Mượn: ${br.borrowDate}
          </div>

          <div class="small text-danger">
            ⏳ Hạn hẹn: <b>${br.dueDate}</b>
          </div>
        </td>

        <td>
          <div class="small">
            🔄 Trả: ${br.returnDate || "<i>Chưa trả</i>"}
          </div>

          <div class="small text-danger-emphasis fw-bold">
            💸 Phạt:
            ${
              br.fineAmount ? br.fineAmount.toLocaleString("vi-VN") + "đ" : "0đ"
            }
          </div>
        </td>
            

        <td class="text-center">
          ${statusBadge}
        </td>

        ${
          role === "ADMIN"
            ? `
          <td class="text-center">
              ${actionButton}
          
              <button class="btn btn-sm btn-outline-warning"
                  onclick="handleBorrowEdit('${borrowJson}')"
                  title="Sửa phiếu">
                  <i class="fa-solid fa-pen"></i>
              </button>
          </td>
          `
            : ""
        }
      </tr>`;
  });
}

// ==========================================
// 📚 CHỨC NĂNG QUẢN LÝ SÁCH
// ==========================================
function loadBooks() {
  const tbody = document.getElementById("table-book-body");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted"><div class="spinner-border spinner-border-sm text-primary me-2"></div>Đang quét kho sách...</td></tr>`;

  fetch(API_BOOKS)
    .then((res) => res.json())
    .then((books) => {
      tbody.innerHTML = "";
      if (books.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted py-3">Kho sách trống!</td></tr>`;
        return;
      }
      renderBooks(books);
    })
    .catch(() => {
      tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger py-3">Lỗi kết nối Server Backend!</td></tr>`;
    });
}
function renderBooks(books) {
  const tbody = document.getElementById("table-book-body");
  const role = localStorage.getItem("userRole");
  const actionHeader = document.getElementById("action-header");
  console.log(localStorage.getItem("userRole"));
  tbody.innerHTML = "";

  if (books.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="7" class="text-center text-muted py-3">
          Không tìm thấy sách
        </td>
      </tr>`;
    return;
  }
  actionHeader.style.display =
    role === "ADMIN" || role === "LIBRARIAN" ? "" : "none";

  books.forEach((b) => {
    const bookJson = JSON.stringify(b).replace(/"/g, "&quot;");
    let actionHtml = "";
    if (role === "ADMIN" || role === "LIBRARIAN") {
      actionHtml = `
        <button class="btn btn-sm btn-outline-warning me-1"
          onclick="handleEdit('${bookJson}')">
          <i class="fa-solid fa-pen"></i>
        </button>

        <button class="btn btn-sm btn-outline-danger"
          onclick="handleDelete(${b.id}, '${b.title}')">
          <i class="fa-solid fa-trash"></i>
        </button>
      `;
    }

    tbody.innerHTML += `
      <tr>
        <td class="text-center fw-bold text-secondary">${b.id}</td>

        <td>
          <div class="fw-bold text-dark">${b.title}</div>
          <small class="text-muted">
            <i class="fa-regular fa-user me-1"></i>${b.author}
          </small>
        </td>

        <td>${b.isbn}</td>

        <td class="text-center">${b.totalQuantity}</td>

        <td class="text-center">
          ${b.availableQuantity}
        </td>

        <td class="text-center">${b.categoryId}</td>
       
        ${role !== "MEMBER" ? `<td class="text-center">${actionHtml}</td>` : ""}
      </tr>
    `;
  });
}
function handleEdit(bookStr) {
  const book = JSON.parse(bookStr);
  isEditMode = true;
  document.getElementById("book-id").value = book.id;
  document.getElementById("book-title").value = book.title;
  document.getElementById("book-author").value = book.author;
  document.getElementById("book-isbn").value = book.isbn;
  document.getElementById("book-quantity").value = book.totalQuantity;
  document.getElementById("book-cat-id").value = book.categoryId;
  document.getElementById("form-action-buttons").innerHTML = `
    <div class="row g-2 mt-2">
      <div class="col-8">
        <button type="submit" class="btn btn-warning w-100 fw-bold py-2 text-dark"><i class="fa-solid fa-pen-to-square me-2"></i>Cập Nhật Sách</button>
      </div>
      <div class="col-4">
        <button type="button" class="btn btn-secondary w-100 fw-bold py-2" onclick="resetFormState()">Hủy</button>
      </div>
    </div>`;
  document
    .getElementById("form-add-book")
    .scrollIntoView({ behavior: "smooth" });
}

function handleDelete(id, title) {
  if (
    confirm(`⚠️ Bạn có chắc chắn muốn xóa cuốn sách "${title}" khỏi hệ thống?`)
  ) {
    fetch(`${API_BOOKS}/${id}`, {
      method: "DELETE",
      headers: { User_Role: localStorage.getItem("userRole") },
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          alert("thành công " + data.message);
          loadBooks();
          if (isEditMode) resetFormState();
        } else {
          alert("thất bại " + data.message);
        }
      })
      .catch(() => alert("Lỗi kết nối tới hệ thống!"));
  }
}

function resetFormState() {
  isEditMode = false;
  document.getElementById("form-add-book").reset();
  document.getElementById("book-id").value = "";
  document.getElementById("form-action-buttons").innerHTML = `
    <button type="submit" class="btn btn-success w-100 fw-bold py-2 mt-2" id="btn-submit-form">
      <i class="fa-solid fa-cloud-arrow-up me-2"></i>Lưu Vào Cơ Sở Dữ Liệu
    </button>`;
}

// ==========================================
// 🏷️ CHỨC NĂNG QUẢN LÝ THỂ LOẠI
// ==========================================
function loadCategories() {
  const tbody = document.getElementById("table-category-body");
  const bookCatSelect = document.getElementById("book-cat-id");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="4" class="text-center text-muted">Đang quét danh mục...</td></tr>`;
  fetch(API_CATEGORIES)
    .then((res) => res.json())
    .then((categories) => {
      tbody.innerHTML = "";
      if (categories.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="text-center text-muted py-3">Không có thể loại nào!</td></tr>`;
      } else {
        renderCategories(categories);
      }
      if (bookCatSelect) {
        const currentSelectedValue = bookCatSelect.value;
        bookCatSelect.innerHTML = `<option value="" disabled selected>-- Chọn thể loại --</option>`;
        categories.forEach((c) => {
          bookCatSelect.innerHTML += `<option value="${c.id}">[ID: ${c.id}] - ${c.name}</option>`;
        });
        if (currentSelectedValue) bookCatSelect.value = currentSelectedValue;
      }
    })
    .catch((err) => console.error("Lỗi đồng bộ danh mục thể loại:", err));
}

function handleCatEdit(catStr) {
  const cat = JSON.parse(catStr);
  isCatEditMode = true;
  document.getElementById("category-id").value = cat.id;
  document.getElementById("category-name").value = cat.name;
  document.getElementById("category-desc").value = cat.description
    ? cat.description
    : "";
  document.getElementById("category-form-buttons").innerHTML = `
    <div class="row g-2">
      <div class="col-8">
        <button type="submit" class="btn btn-warning w-100 fw-bold text-dark"><i class="fa-solid fa-pen-to-square me-1"></i>Cập Nhật</button>
      </div>
      <div class="col-4">
        <button type="button" class="btn btn-secondary w-100 fw-bold" onclick="resetCatFormState()">Hủy</button>
      </div>
    </div>`;
}

function handleCatDelete(id, name) {
  if (
    confirm(
      `⚠️ Bạn có chắc chắn muốn xóa thể loại "${name}"?\nLưu ý: Hành động này có thể lỗi nếu có sách đang thuộc thể loại này.`
    )
  ) {
    fetch(`${API_CATEGORIES}/${id}`, { method: "DELETE" })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          alert("thành công " + data.message);
          loadCategories();
          loadBooks();
          if (isCatEditMode) resetCatFormState();
        } else {
          alert("thất bại " + data.message);
        }
      })
      .catch(() => alert("Lỗi kết nối hệ thống khi xóa!"));
  }
}

function resetCatFormState() {
  isCatEditMode = false;
  document.getElementById("form-add-category").reset();
  document.getElementById("category-id").value = "";
  document.getElementById("category-form-buttons").innerHTML = `
    <button type="submit" class="btn btn-primary w-100 fw-bold py-2" id="btn-submit-cat">
      <i class="fa-solid fa-plus me-2"></i>Thêm Thể Loại
    </button>`;
}

// ==========================================
// 👥 CHỨC NĂNG QUẢN LÝ NGƯỜI DÙNG
// ==========================================
function loadUsers() {
  const tbody = document.getElementById("table-user-body");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted"><div class="spinner-border spinner-border-sm text-warning me-2"></div>Đang tải danh sách tài khoản...</td></tr>`;

  fetch(API_URL)
    .then((res) => res.json())
    .then((users) => {
      console.log(users);
      tbody.innerHTML = "";
      if (users.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-muted py-3">Danh sách trống!</td></tr>`;
        return;
      }
      renderUsers(users);
    })
    .catch(() => {
      tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger py-3">Lỗi kết nối Server Backend!</td></tr>`;
    });
}

function handleUserEdit(userStr) {
  const user = JSON.parse(userStr);
  isUserEditMode = true;
  document.getElementById("user-id").value = user.id;
  document.getElementById("user-username").value = user.username;
  document.getElementById("user-username").disabled = true;
  document.getElementById("user-password").value = "********";
  document.getElementById("user-password").disabled = true;
  document.getElementById("user-fullname").value =
    user.full_name || user.fullname;
  document.getElementById("user-email").value = user.email;
  document.getElementById("user-role").value = user.role;
  document.getElementById("user-status").value = user.status;
  document.getElementById(
    "user-form-title"
  ).innerHTML = `<i class="fa-solid fa-user-pen text-warning me-2"></i>Cập Nhật Tài Khoản #${user.id}`;
  document.getElementById("user-form-buttons").innerHTML = `
    <div class="row g-2 w-100 m-0">
      <div class="col-8 p-0 pe-1">
        <button type="submit" class="btn btn-warning w-100 fw-bold py-2 text-dark"><i class="fa-solid fa-pen-to-square me-2"></i>Cập Nhật</button>
      </div>
      <div class="col-4 p-0 ps-1">
        <button type="button" class="btn btn-secondary w-100 fw-bold py-2" onclick="resetUserFormState()">Hủy</button>
      </div>
    </div>`;
  document
    .getElementById("form-add-user")
    .scrollIntoView({ behavior: "smooth" });
}

function handleUserDelete(id, username) {
  if (
    confirm(
      `⚠️ Bạn chắc chắn muốn xóa tài khoản "${username}" ra khỏi hệ thống chứ?`
    )
  ) {
    fetch(`${API_URL}/${id}`, { method: "DELETE" })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          alert("thành công " + data.message);
          loadUsers();
          if (isUserEditMode) resetUserFormState();
        } else {
          alert("thất bại " + data.message);
        }
      })
      .catch(() => alert("Lỗi kết nối hệ thống khi thực hiện xóa!"));
  }
}

function resetUserFormState() {
  isUserEditMode = false;
  document.getElementById("form-add-user").reset();
  document.getElementById("user-id").value = "";
  document.getElementById("user-username").disabled = false;
  document.getElementById("user-password").disabled = false;
  document.getElementById(
    "user-form-title"
  ).innerHTML = `<i class="fa-solid fa-user-plus text-success me-2"></i>Thêm Tài Khoản Mới`;
  document.getElementById("user-form-buttons").innerHTML = `
    <button type="submit" class="btn btn-success w-100 fw-bold py-2 mt-2" id="btn-submit-user">
      <i class="fa-solid fa-cloud-arrow-up me-2"></i>Lưu Dữ Liệu
    </button>`;
}

// ==========================================
// 🔄 ĐIỀU HƯỚNG PANEL VIEW
// ==========================================
function switchPage(panelId, menuElement) {
  document
    .querySelectorAll(".page-panel")
    .forEach((p) => p.classList.remove("active"));
  document
    .querySelectorAll(".sidebar .nav-link")
    .forEach((link) => link.classList.remove("active"));
  const targetPanel = document.getElementById(panelId);
  if (targetPanel) targetPanel.classList.add("active");
  if (menuElement) menuElement.classList.add("active");
}

// ==========================================
// 📑 CHỨC NĂNG QUẢN LÝ MƯỢN TRẢ SÁCH
// ==========================================
function loadBorrows() {
  const tbody = document.getElementById("table-borrow-body");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="8" class="text-center text-muted"><div class="spinner-border spinner-border-sm text-primary me-2"></div>Đang quét nhật ký mượn trả chi tiết...</td></tr>`;

  fetch(API_BORROWS)
    .then((res) => res.json())
    .then((borrows) => {
      tbody.innerHTML = "";
      if (borrows.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="text-center text-muted py-3">Chưa có lượt mượn trả nào trong hệ thống!</td></tr>`;
        return;
      }

      renderBorrows(borrows);
    })
    .catch(() => {
      tbody.innerHTML = `<tr><td colspan="8" class="text-center text-danger py-3">Lỗi kết nối đồng bộ danh sách mượn trả!</td></tr>`;
    });
}
function syncBorrowBooks() {
  const borrowBookSelect = document.getElementById("borrow-book-id");
  if (!borrowBookSelect) return;

  // SỬA: Thay vì fix cứng URL chuỗi, ta gọi đồng bộ qua hằng số định nghĩa ở đầu file
  fetch(API_BOOKS)
    .then((res) => res.json())
    .then((books) => {
      borrowBookSelect.innerHTML = `<option value="" disabled selected>-- Chọn cuốn sách --</option>`;
      books.forEach((b) => {
        const disabledAttr = b.availableQuantity <= 0 ? "disabled" : "";
        const noteText =
          b.availableQuantity <= 0
            ? "(Hết sách)"
            : `(Còn ${b.availableQuantity} cuốn)`;
        borrowBookSelect.innerHTML += `<option value="${b.id}" ${disabledAttr}>${b.title} ${noteText}</option>`;
      });
    })
    .catch((err) => console.error("Lỗi đồng bộ kho sách mượn:", err));
}

function initBorrowDates() {
  const today = new Date();
  const dueDate = new Date();
  dueDate.setDate(today.getDate() + 14); // Mặc định hạn mượn sách là 14 ngày
  document.getElementById("borrow-date").value = today
    .toISOString()
    .split("T")[0];
  document.getElementById("borrow-due-date").value = dueDate
    .toISOString()
    .split("T")[0];
}

function handleReturnBook(id) {
  console.log("ID =", id);
  if (
    confirm(
      `Xác nhận độc giả đã mang trả sách hoàn tất cho phiếu mượn mã số #${id}?`
    )
  ) {
    fetch(`${API_BORROWS}/${id}/return`, { method: "POST" })
      .then((res) => res.json())
      .then((data) => {
        console.log("PUT response:", data);
        if (data.success) {
          alert("🎉 " + data.message);
          loadBorrows();
          loadBooks();
        } else {
          alert("❌ " + data.message);
        }
      })
      .catch(() => alert("Lỗi hệ thống khi thực hiện trả sách nhanh!"));
  }
}

function handleBorrowEdit(borrowStr) {
  const br = JSON.parse(borrowStr);
  isBorrowEditMode = true;
  document.getElementById("borrow-id").value = br.id;
  document.getElementById("borrow-user-id").value = br.userId;
  document.getElementById("borrow-book-id").value = br.bookId;
  document.getElementById("borrow-quantity").value = br.quantity || 1;
  document.getElementById("borrow-date").value = br.borrowDate || "";
  document.getElementById("borrow-due-date").value = br.dueDate || "";
  document.getElementById("container-borrow-status").classList.remove("d-none");
  document.getElementById("borrow-status").value = br.status;
  document.getElementById(
    "borrow-form-title"
  ).innerHTML = `<i class="fa-solid fa-pen-to-square text-warning me-2"></i>Cập Nhật Phiếu #${br.id}`;
  document.getElementById("borrow-form-buttons").innerHTML = `
    <div class="row g-2 w-100 m-0">
      <div class="col-8 p-0 pe-1">
        <button type="submit" class="btn btn-warning w-100 fw-bold py-2 text-dark"><i class="fa-solid fa-check me-2"></i>Cập Nhật</button>
      </div>
      <div class="col-4 p-0 ps-1">
        <button type="button" class="btn btn-secondary w-100 fw-bold py-2" onclick="resetBorrowFormState()">Hủy</button>
      </div>
    </div>`;
  document
    .getElementById("form-add-borrow")
    .scrollIntoView({ behavior: "smooth" });
}

function resetBorrowFormState() {
  console.log("resetBorrowFormState called");
  isBorrowEditMode = false;
  document.getElementById("form-add-borrow").reset();
  document.getElementById("borrow-id").value = "";
  document.getElementById("container-borrow-status").classList.add("d-none");
  document.getElementById(
    "borrow-form-title"
  ).innerHTML = `<i class="fa-solid fa-file-invoice text-success me-2"></i>Tạo Phiếu Mượn Mới`;
  document.getElementById("borrow-form-buttons").innerHTML = `
    <button type="submit" class="btn btn-success w-100 fw-bold py-2 mt-2" id="btn-submit-borrow">
      <i class="fa-solid fa-floppy-disk me-2"></i>Lập Phiếu Mượn
    </button>`;
  initBorrowDates();
}

function taiThongKeTongQuan() {
  fetch(API_THONGKE)
    .then((response) => {
      console.log("Status:", response.status);
      return response.json();
    })
    .then((data) => {
      console.log("DATA API:", data);
      console.log("Thong ke:", data);
      console.log("tong sach:", document.getElementById("lbl-tong-sach"));
      console.log("dang muon:", document.getElementById("lbl-dang-muon"));
      console.log("tien phat:", document.getElementById("lbl-tien-phat"));
      console.log("doc gia:", document.getElementById("lbl-doc-gia"));

      document.getElementById("lbl-tong-sach").innerText = data.tongSoSach;
      document.getElementById("lbl-dang-muon").innerText = data.soSachDangMuon;
      document.getElementById("lbl-tien-phat").innerText = data.tongTienPhat;
      document.getElementById("lbl-doc-gia").innerText = data.tongSoDocGia;
    })
    .catch((error) => {
      console.error("Lỗi:", error);
    });
}
//tìm kiếm thể loại
function renderCategories(categories) {
  const tbody = document.getElementById("table-category-body");
  const role = localStorage.getItem("userRole");
  const actionHeader = document.getElementById("category-action-header");

  actionHeader.style.display =
    role === "ADMIN" || role === "LIBRARIAN" ? "" : "none";
  tbody.innerHTML = "";

  categories.forEach((c) => {
    const catJson = JSON.stringify(c).replace(/"/g, "&quot;");
    let actionHtml = "";

    if (role === "ADMIN" || role === "LIBRARIAN") {
      actionHtml = `
    <button class="btn btn-sm btn-outline-warning me-1"
      onclick="handleCategoryEdit('${catJson}')">
      <i class="fa-solid fa-pen"></i>
    </button>

    <button class="btn btn-sm btn-outline-danger"
      onclick="handleCategoryDelete(${c.id}, '${c.name}')">
      <i class="fa-solid fa-trash"></i>
    </button>
  `;
    }
    tbody.innerHTML += `
      <tr>
        <td>${c.id}</td>
        <td>${c.name}</td>
        <td>${c.description || ""}</td>
        
          ${
            role !== "MEMBER"
              ? `
    <td class="text-center">
      ${actionHtml}
    </td>
    `
              : ""
          }
      </tr>
    `;
  });
}
const menuProfile = document.getElementById("menu-profile");

if (menuProfile) {
  menuProfile.addEventListener("click", function (e) {
    e.preventDefault();
    switchPage("panel-profile", this);

    loadProfile();
  });
}
//hàm thông tin người dùng
function loadProfile() {
  const userId = localStorage.getItem("userToken");

  fetch(`http://localhost:8080/api/users/${userId}`)
    .then((res) => res.json())
    .then((user) => {
      document.getElementById("profile-id").innerText = user.id;

      document.getElementById("profile-username").innerText = user.username;

      document.getElementById("profile-fullname").innerText = user.fullname;

      document.getElementById("profile-email").innerText = user.email;

      document.getElementById("profile-role").innerText = user.role;

      document.getElementById("profile-status").innerText =
        user.status == 1 ? "Hoạt động" : "Bị khóa";
    });
}
