import java.util.ArrayList;
import java.util.Collections;

public class QuanLySinhVien {
    private ArrayList<SinhVien> danhSachSV;

    public QuanLySinhVien() {
        this.danhSachSV = new ArrayList<>();
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
        return true;
    }

    public boolean suaSinhVien(String mssv, SinhVien svMoi) {
        SinhVien svCu = timSinhVienTheoMssv(mssv);
        if (svCu == null) {
            return false;
        }
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

    // Thêm nếu dùng btnTimNganh
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
        Collections.sort(danhSachSV, (sv1, sv2) -> {
            String ten1 = sv1.getHoTen().substring(sv1.getHoTen().lastIndexOf(" ") + 1);
            String ten2 = sv2.getHoTen().substring(sv2.getHoTen().lastIndexOf(" ") + 1);
            return ten1.compareToIgnoreCase(ten2);
        });
    }
    
    public void sapXepTheoDiemTB() {
        Collections.sort(danhSachSV, (sv1, sv2) -> 
            Double.compare(sv2.tinhDiemTrungBinh(), sv1.tinhDiemTrungBinh())
        );
    }
}