document.addEventListener('DOMContentLoaded', function () {
    // Khởi tạo các sự kiện khi trang load
    loadDangKyLamThem();

    document.getElementById('dangKyForm').addEventListener('submit', function (event) {
        event.preventDefault();
        saveDangKyLamThem();
    });

    document.getElementById('editForm').addEventListener('submit', function (event) {
        event.preventDefault();
        updateDangKyLamThem();
    });

    document.getElementById('addViecLamForm').addEventListener('submit', function (event) {
        event.preventDefault();
        addViecLam();
    });
});

// ========== CÁC HÀM CHÍNH ========== //

// 1. Load danh sách đăng ký
function loadDangKyLamThem() {
    fetch('/spmvc/api/dangky')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#dangKyTable tbody');
            tableBody.innerHTML = '';
            data.forEach(dangKy => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${dangKy.maDK}</td>
                    <td>${dangKy.maNV}</td>
                    <td>${dangKy.hoTen}</td>
                    <td>${dangKy.gioiTinh}</td>
                    <td>${dangKy.capBac}</td>
                    <td>${dangKy.viecLam.maVL}</td>
                    <td>${dangKy.soNgayCong}</td>
                    <td>${dangKy.tongTien}</td>
                    <td>
                        <button onclick="editDangKyLamThem('${dangKy.maDK}')">Edit</button>
                        <button onclick="deleteDangKyLamThem('${dangKy.maDK}')">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        });
}

// 2. Thêm đăng ký mới
function saveDangKyLamThem() {
    const soNgayCong = parseInt(document.getElementById('soNgayCong').value);

    if (soNgayCong <= 0 || soNgayCong > 20) {
        alert("Số ngày công phải từ 1 đến 20");
        return;
    }

    const formData = {
        maDK: document.getElementById("maDK").value,
        maNV: document.getElementById('maNV').value,
        hoTen: document.getElementById('hoTen').value,
        gioiTinh: document.getElementById('gioiTinh').value,
        capBac: parseInt(document.getElementById('capBac').value),
        viecLam: {maVL: document.getElementById('maVL').value},
        soNgayCong: soNgayCong,
        tongTien: 0
    };

    fetch('/spmvc/api/dangky/dkLamThem', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(() => {
            alert("Thêm đăng ký thành công!");
            document.getElementById('dangKyForm').reset();
            loadDangKyLamThem();
        })
        .catch(error => alert("Lỗi: " + error.message));
}

// 3. Chỉnh sửa đăng ký
function editDangKyLamThem(maDK) {
    fetch(`/spmvc/api/dangky/${maDK}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('maDKEdit').value = data.maDK;
            document.getElementById('maNVEdit').value = data.maNV;
            document.getElementById('hoTenEdit').value = data.hoTen;
            document.getElementById('gioiTinhEdit').value = data.gioiTinh;
            document.getElementById('capBacEdit').value = data.capBac;
            document.getElementById('maVLEdit').value = data.viecLam.maVL;
            document.getElementById('soNgayCongEdit').value = data.soNgayCong;

            document.getElementById('editForm').style.display = 'block';
        })
        .catch(error => alert("Lỗi khi tải dữ liệu: " + error.message));
}

function updateDangKyLamThem() {
    const formData = {
        maDK: document.getElementById('maDKEdit').value,
        maNV: document.getElementById('maNVEdit').value,
        hoTen: document.getElementById('hoTenEdit').value,
        gioiTinh: document.getElementById('gioiTinhEdit').value,
        capBac: parseInt(document.getElementById('capBacEdit').value),
        viecLam: {
            maVL: document.getElementById('maVLEdit').value
        },
        soNgayCong: parseInt(document.getElementById('soNgayCongEdit').value),
        tongTien: 0 // Sẽ được tính ở server
    };

    // Gửi request PUT
    fetch(`/spmvc/api/dangky/${formData.maDK}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(updatedData => {
            console.log("Dữ liệu sau khi update:", updatedData);
            loadDangKyLamThem();
        });
}

// 4. Xóa đăng ký
function deleteDangKyLamThem(maDK) {
    if (!confirm("Bạn có chắc muốn xóa đăng ký này?")) return;

    fetch(`/spmvc/api/dangky/${maDK}`, {
        method: 'DELETE'
    })
        .then(() => loadDangKyLamThem())
        .catch(error => alert("Lỗi khi xóa: " + error.message));
}

// 5. Thêm việc làm mới
function addViecLam() {
    const formData = {
        maVL: document.getElementById('maVL').value,
        moTa: document.getElementById('moTa').value,
        duAn: document.getElementById('duAn').value,
        ngonNgu: document.getElementById('ngonNgu').value,
        trangThai: document.getElementById('trangThai').value,
        ngayBD: document.getElementById('ngayBD').value,
        ngayKT: document.getElementById('ngayKT').value,
        tongNgayCong: document.getElementById('tongNgayCong').value
    };

    fetch('/spmvc/api/vieclam', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(() => {
            alert("Thêm việc làm thành công!");
            document.getElementById('addViecLamForm').reset();
        })
        .catch(error => {
            window.location.href = "/error.html?message=" + encodeURIComponent(error.message);
        });
}

// ========== CÁC HÀM HỖ TRỢ ========== //
function showAddViecLamForm() {
    document.getElementById('addViecLamForm').style.display = 'block';
}
// Hàm tìm kiếm
function searchDangKy() {
    const keyword = document.getElementById('searchInput').value;
    const searchType = document.getElementById('searchType').value;

    fetch(`/spmvc/api/dangky/search?keyword=${encodeURIComponent(keyword)}&searchType=${searchType}`)
        .then(response => response.json())
        .then(data => {
            renderDangKyTable(data);
        });
}

// Hàm hiển thị dữ liệu vào bảng
function renderDangKyTable(data) {
    const tableBody = document.querySelector('#dangKyTable tbody');
    tableBody.innerHTML = '';

    data.forEach(dangKy => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${dangKy.maDK}</td>
            <td>${dangKy.maNV}</td>
            <td>${dangKy.hoTen}</td>
            <td>${dangKy.gioiTinh}</td>
            <td>${dangKy.capBac}</td>
            <td>${dangKy.viecLam.maVL}</td>
            <td>${dangKy.soNgayCong}</td>
            <td>${dangKy.tongTien}</td>
            <td>
                <button onclick="editDangKyLamThem('${dangKy.maDK}')">Edit</button>
                <button onclick="deleteDangKyLamThem('${dangKy.maDK}')">Delete</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Load dữ liệu ban đầu
function loadDangKyLamThem() {
    fetch('/spmvc/api/dangky')
        .then(response => response.json())
        .then(data => renderDangKyTable(data));
}