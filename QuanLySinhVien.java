<<<<<<< HEAD
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class QuanLySinhVien implements IFileHandler, IThongKe {

    private ArrayList<SinhVien> danhSachSV;

    // --- Constructor ---
    public QuanLySinhVien() {
        this.danhSachSV = new ArrayList<>();
    }

    // --- Getter ---
    public ArrayList<SinhVien> getDanhSachSV() {
        return danhSachSV;
    }

    // ====================== 1. THÊM SINH VIÊN ======================
    public boolean themSinhVien(SinhVien sv) {
        if (sv == null || timSinhVienTheoMSSV(sv.getMSSV()) != null) {
            return false; // Không thêm nếu trùng MSSV
        }
        danhSachSV.add(sv);
        return true;
    }

    // ====================== 2. SỬA SINH VIÊN ======================
    public boolean suaSinhVien(String mssv, SinhVien svMoi) {
        SinhVien svCu = timSinhVienTheoMSSV(mssv);
        if (svCu == null) {
            return false;
        }
        // Cập nhật thông tin
        svCu.setHoTen(svMoi.getHoTen());
        svCu.setNgaySinh(svMoi.getNgaySinh());
        svCu.setGioiTinh(svMoi.getGioiTinh());
        svCu.setNganhHoc(svMoi.getNganhHoc());
        svCu.setDanhSachDiem(new ArrayList<>(svMoi.getDanhSachDiem()));
        return true;
    }

    // ====================== 3. XÓA SINH VIÊN ======================
    public boolean xoaSinhVien(String mssv) {
        SinhVien sv = timSinhVienTheoMSSV(mssv);
        if (sv == null) {
            return false;
        }
        danhSachSV.remove(sv);
        return true;
    }

    // ====================== 4. HIỂN THỊ DANH SÁCH ======================
    public void hienThiDanhSach() {
        if (danhSachSV.isEmpty()) {
            System.out.println("Danh sách sinh viên trống!");
            return;
        }
        System.out.println("\n=== DANH SÁCH SINH VIÊN ===");
        for (SinhVien sv : danhSachSV) {
            sv.hienThiThongTin();
        }
        System.out.println("================================\n");
    }

    // ====================== TÌM KIẾM ======================
    public SinhVien timSinhVienTheoMSSV(String mssv) {
        for (SinhVien sv : danhSachSV) {
            if (sv.getMSSV().equalsIgnoreCase(mssv.trim())) {
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
            if (sv.getNganhHoc().equalsIgnoreCase(nganh.trim())) {
                ketQua.add(sv);
            }
        }
        return ketQua;
    }

    // ====================== SẮP XẾP ======================
    public void sapXepTheoTen() {
        Collections.sort(danhSachSV, Comparator.comparing(SinhVien::getHoTen));
    }

    public void sapXepTheoDiemTB() {
        Collections.sort(danhSachSV, (sv1, sv2) -> 
            Double.compare(sv2.tinhDiemTrungBinh(), sv1.tinhDiemTrungBinh()));
    }

    // ====================== GHI FILE ======================
    @Override
    public void ghiFile(ArrayList<SinhVien> ds, String tenFile) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tenFile))) {
            oos.writeObject(ds);
        }
    }

    public void ghiFile(String tenFile) throws Exception {
        ghiFile(danhSachSV, tenFile);
    }

    // ====================== ĐỌC FILE ======================
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<SinhVien> docFile(String tenFile) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tenFile))) {
            danhSachSV = (ArrayList<SinhVien>) ois.readObject();
            return danhSachSV;
        }
    }

    // ====================== THỐNG KÊ ======================
    @Override
    public void thongKeTheoNganh(ArrayList<SinhVien> ds) {
        // Dùng để hiển thị trong GUI
    }

    @Override
    public void thongKeTheoGioiTinh(ArrayList<SinhVien> ds) {
        // Dùng để hiển thị trong GUI
    }

    @Override
    public void timTopSinhVien(ArrayList<SinhVien> ds) {
        // Dùng để hiển thị trong GUI
    }

    // === Các phương thức hỗ trợ GUI (sẽ gọi từ Giaodienqlsv) ===
    public String thongKeNganh() {
        StringBuilder sb = new StringBuilder("THỐNG KÊ THEO NGÀNH\n");
        sb.append("-------------------\n");
        ArrayList<String> nganhList = new ArrayList<>();
        for (SinhVien sv : danhSachSV) {
            if (!nganhList.contains(sv.getNganhHoc())) {
                nganhList.add(sv.getNganhHoc());
            }
        }
        for (String nganh : nganhList) {
            int count = 0;
            for (SinhVien sv : danhSachSV) {
                if (sv.getNganhHoc().equals(nganh)) count++;
            }
            sb.append(String.format("%-20s : %d sinh viên\n", nganh, count));
        }
        return sb.toString();
    }

    public String thongKeGioiTinh() {
        int nam = 0, nu = 0;
        for (SinhVien sv : danhSachSV) {
            if (sv.getGioiTinh().equalsIgnoreCase("Nam")) nam++;
            else if (sv.getGioiTinh().equalsIgnoreCase("Nu")) nu++;
        }
        return String.format("THỐNG KÊ GIỚI TÍNH\n-------------------\nNam: %d\nNữ: %d\n", nam, nu);
    }

    public String xepHangDiemTB() {
        if (danhSachSV.isEmpty()) return "Chưa có dữ liệu!";
        sapXepTheoDiemTB();
        StringBuilder sb = new StringBuilder("XẾP HẠNG ĐIỂM TB\n");
        sb.append("-------------------\n");
        for (int i = 0; i < danhSachSV.size(); i++) {
            SinhVien sv = danhSachSV.get(i);
            sb.append(String.format("%d. %s - %.2f\n", (i+1), sv.getHoTen(), sv.tinhDiemTrungBinh()));
        }
        return sb.toString();
    }

    public SinhVien timSVCoDiemCaoNhat() {
        if (danhSachSV.isEmpty()) return null;
        return Collections.max(danhSachSV, Comparator.comparingDouble(SinhVien::tinhDiemTrungBinh));
    }

    public SinhVien timSVCoDiemThapNhat() {
        if (danhSachSV.isEmpty()) return null;
        return Collections.min(danhSachSV, Comparator.comparingDouble(SinhVien::tinhDiemTrungBinh));
    }
=======
import java.util.ArrayList;
import java.util.Collections;

public class QuanLySinhVien {
    private ArrayList<SinhVien> danhSachSV;

    public QuanLySinhVien() {
        this.danhSachSV = new ArrayList<>();
    }

    // --- Getter & Setter cho danh sách (Để FileService có thể lấy/gán dữ liệu) ---
    public ArrayList<SinhVien> getDanhSachSV() {
        return danhSachSV;
    }

    public void setDanhSachSV(ArrayList<SinhVien> danhSachSV) {
        this.danhSachSV = danhSachSV;
    }

    // ================== 1. CHỨC NĂNG THÊM ==================
    public boolean themSinhVien(SinhVien sv) {
        if (sv == null || timSinhVienTheoMSSV(sv.getMssv()) != null) {
            return false; // Trùng MSSV hoặc null
        }
        danhSachSV.add(sv);
        return true;
    }

    // ================== 2. CHỨC NĂNG SỬA ==================
    public boolean suaSinhVien(String mssv, SinhVien svMoi) {
        SinhVien svCu = timSinhVienTheoMSSV(mssv);
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

    // ================== 3. CHỨC NĂNG XÓA ==================
    public boolean xoaSinhVien(String mssv) {
        SinhVien sv = timSinhVienTheoMSSV(mssv);
        if (sv == null) {
            return false;
        }
        danhSachSV.remove(sv);
        return true;
    }

    // ================== 4. CHỨC NĂNG TÌM KIẾM ==================
    public SinhVien timSinhVienTheoMSSV(String mssv) {
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

    // ================== 5. SẮP XẾP (Hỗ trợ hiển thị) ==================
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
>>>>>>> ff4cd85 (update)
}