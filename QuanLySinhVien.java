package connectdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class QuanLySinhVien {

    private ArrayList<SinhVien> danhSachSV;
    private SinhVienDAO dao = new SinhVienDAO();

    public QuanLySinhVien() {
        // Lấy danh sách từ DB
        this.danhSachSV = new ArrayList<>(dao.getAll());
    }

    public ArrayList<SinhVien> getDanhSachSV() {
        return danhSachSV;
    }

    public void setDanhSachSV(ArrayList<SinhVien> danhSachSV) {
        this.danhSachSV = danhSachSV;
    }

    // ================== CRUD (THÊM / SỬA / XÓA) ==================

    public boolean themSinhVien(SinhVien sv) {
        if (sv == null) return false;
        
        // Kiểm tra trùng trong DB
        if (dao.findByMssv(sv.getMssv()) != null) {
            return false;
        }
        
        // Insert vào DB thành công thì mới thêm vào List
        if (dao.insert(sv)) {
            danhSachSV.add(sv);
            return true;
        }
        return false;
    }

    public boolean suaSinhVien(String mssvCu, SinhVien svMoi) {
        if (dao.update(mssvCu, svMoi)) {
            // Update thành công thì load lại danh sách cho chuẩn
            this.danhSachSV = new ArrayList<>(dao.getAll());
            return true;
        }
        return false;
    }

    public boolean xoaSinhVien(String mssv) {
        if (dao.delete(mssv)) {
            // Xóa trong List bằng vòng lặp
            for (int i = 0; i < danhSachSV.size(); i++) {
                if (danhSachSV.get(i).getMssv().equals(mssv)) {
                    danhSachSV.remove(i);
                    break;
                }
            }
            return true;
        }
        return false;
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

    // ================== SẮP XẾP (ĐÃ SỬA LẠI ĐÚNG LOGIC CỦA BẠN) ==================

    // 1. Sắp xếp theo tên (A-Z)
    public void sapXepTheoTen() {
        Collections.sort(this.danhSachSV, new Comparator<SinhVien>() {
            @Override
            public int compare(SinhVien sv1, SinhVien sv2) {
                return sv1.getHoTen().compareToIgnoreCase(sv2.getHoTen());
            }
        });
    }

    // 2. Sắp xếp theo điểm TB (Giảm dần)
    public void sapXepTheoDiemTB() {
        Collections.sort(this.danhSachSV, new Comparator<SinhVien>() {
            @Override
            public int compare(SinhVien sv1, SinhVien sv2) {
                // GỌI HÀM CÓ SẴN TRONG FILE SINHVIEN CỦA BẠN
                double dtb1 = sv1.tinhDiemTrungBinh();
                double dtb2 = sv2.tinhDiemTrungBinh();

                // So sánh giảm dần (Ai điểm cao xếp trước)
                return Double.compare(dtb2, dtb1);
            }
        });
    }

    // ================== TOP & THỐNG KÊ ==================

    public void layTopSinhVien(int soLuong) {
        // Sắp xếp trước
        sapXepTheoDiemTB();
        
        // Lấy top N
        if (this.danhSachSV.size() > soLuong) {
            ArrayList<SinhVien> topList = new ArrayList<>();
            for(int i = 0; i < soLuong; i++) {
                topList.add(this.danhSachSV.get(i));
            }
            this.danhSachSV = topList;
        }
    }

    public Map<String, Integer> thongKeTheoGioiTinh() {
        return dao.thongKeTheoGioiTinh();
    }
}
