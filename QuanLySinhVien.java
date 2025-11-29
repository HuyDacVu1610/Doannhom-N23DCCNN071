/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectdb;
import java.util.ArrayList;
import java.util.Collections;

public class QuanLySinhVien {
    private ArrayList<SinhVien> danhSachSV;
    private SinhVienDAO dao = new SinhVienDAO();

    public QuanLySinhVien() {
        this.danhSachSV = new ArrayList<>(dao.getAll());
    }

    public ArrayList<SinhVien> getDanhSachSV() {
        return danhSachSV;
    }

    public void setDanhSachSV(ArrayList<SinhVien> danhSachSV) {
        this.danhSachSV = danhSachSV;
    }

    public boolean themSinhVien(SinhVien sv) {
        if (sv == null || timSinhVienTheoMssv(sv.getMssv()) != null) {
            return false;
        }
        danhSachSV.add(sv);
        dao.insert(sv);
        return true;
    }

    public boolean suaSinhVien(String mssv, SinhVien svMoi) {
        SinhVien svCu = timSinhVienTheoMssv(mssv);
        if (svCu == null) {
            return false;
        }
        dao.update(mssv, svMoi);
        svCu.setHoTen(svMoi.getHoTen());
        svCu.setNgaySinh(svMoi.getNgaySinh());
        svCu.setGioiTinh(svMoi.getGioiTinh());
        svCu.setNganhHoc(svMoi.getNganhHoc());
        svCu.setDanhSachDiem(new ArrayList<>(svMoi.getDanhSachDiem()));
        return true;
    }

    public boolean xoaSinhVien(String mssv) {
        SinhVien sv = timSinhVienTheoMssv(mssv);
        if (sv == null) {
            return false;
        }
        danhSachSV.remove(sv);
        dao.delete(mssv);
        return true;
    }

    public SinhVien timSinhVienTheoMssv(String mssv) {
        for (SinhVien sv : danhSachSV) {
            if (sv.getMssv().equalsIgnoreCase(mssv.trim())) {
                return sv;
            }
        }
        return null;
    }

    public ArrayList<SinhVien> timSinhVienTheoTen(String ten) {
        ArrayList<SinhVien> ketQua = new ArrayList<>();
        for (SinhVien sv : danhSachSV) {
            if (sv.getHoTen().toLowerCase().contains(ten.toLowerCase())) {
                ketQua.add(sv);
            }
        }
        return ketQua;
    }

    public ArrayList<SinhVien> timSinhVienTheoNganh(String nganh) {
        ArrayList<SinhVien> ketQua = new ArrayList<>();
        for (SinhVien sv : danhSachSV) {
            if (sv.getNganhHoc().toLowerCase().contains(nganh.toLowerCase())) {
                ketQua.add(sv);
            }
        }
        return ketQua;
    }

public void sapXepTheoTen() {
    // Lấy lại list từ DB đã sort sẵn theo tên
    this.danhSachSV = new ArrayList<>(dao.getAllSortedByName());
}

public void sapXepTheoDiemTB() {
    // Lấy lại list từ DB đã sort sẵn theo điểm trung bình
    this.danhSachSV = new ArrayList<>(dao.getAllSortedByDiemTB());
}

}
