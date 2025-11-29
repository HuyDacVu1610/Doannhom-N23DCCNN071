/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectdb;

import java.util.ArrayList;
import java.util.Map;

public class QuanLySinhVien {

    private ArrayList<SinhVien> danhSachSV;
    private SinhVienDAO dao = new SinhVienDAO();

    public QuanLySinhVien() {
        // Lấy danh sách ban đầu từ DB
        this.danhSachSV = new ArrayList<>(dao.getAll());
    }

    public ArrayList<SinhVien> getDanhSachSV() {
        return danhSachSV;
    }

    public void setDanhSachSV(ArrayList<SinhVien> danhSachSV) {
        this.danhSachSV = danhSachSV;
    }

    // ================== CRUD ==================

    public boolean themSinhVien(SinhVien sv) {
        if (sv == null) {
            return false;
        }
        // kiểm tra trùng MSSV trên DB
        if (dao.findByMssv(sv.getMssv()) != null) {
            return false;
        }
        // insert DB
        if (!dao.insert(sv)) {
            return false;
        }
        // refresh list từ DB
        this.danhSachSV = new ArrayList<>(dao.getAll());
        return true;
    }

    public boolean suaSinhVien(String mssv, SinhVien svMoi) {
        // kiểm tra tồn tại trên DB
        if (dao.findByMssv(mssv) == null) {
            return false;
        }
        // update DB
        if (!dao.update(mssv, svMoi)) {
            return false;
        }
        // refresh list từ DB
        this.danhSachSV = new ArrayList<>(dao.getAll());
        return true;
    }

    public boolean xoaSinhVien(String mssv) {
        // kiểm tra tồn tại trên DB
        if (dao.findByMssv(mssv) == null) {
            return false;
        }
        // xóa DB
        if (!dao.delete(mssv)) {
            return false;
        }
        // refresh list từ DB
        this.danhSachSV = new ArrayList<>(dao.getAll());
        return true;
    }

    // ================== TÌM KIẾM ==================

    public SinhVien timSinhVienTheoMssv(String mssv) {
        return dao.findByMssv(mssv);
    }

    public ArrayList<SinhVien> timSinhVienTheoTen(String ten) {
        return new ArrayList<>(dao.searchByName(ten));
    }

    public ArrayList<SinhVien> timSinhVienTheoNganh(String nganh) {
        return new ArrayList<>(dao.searchByNganh(nganh));
    }

    // ================== SẮP XẾP ==================

    public void sapXepTheoTen() {
        // Lấy lại list từ DB đã sort sẵn theo tên
        this.danhSachSV = new ArrayList<>(dao.getAllSortedByName());
    }

    public void sapXepTheoDiemTB() {
        // Lấy lại list từ DB đã sort sẵn theo điểm trung bình
        this.danhSachSV = new ArrayList<>(dao.getAllSortedByDiemTB());
    }

    // ================== TOP & THỐNG KÊ ==================

    // Lấy TOP N sinh viên điểm cao (danhSachSV chỉ chứa top N)
    public void layTopSinhVien(int soLuong) {
        this.danhSachSV = new ArrayList<>(dao.getTopSinhVien(soLuong));
    }

    // Thống kê số lượng SV theo giới tính (Nam/Nữ/...)
    public Map<String, Integer> thongKeTheoGioiTinh() {
        return dao.thongKeTheoGioiTinh();
    }
}
