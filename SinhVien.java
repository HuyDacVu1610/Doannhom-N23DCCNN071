import java.io.Serializable;
import java.util.ArrayList;

public class SinhVien extends Nguoi implements Serializable {
    private String mssv;
    private String nganhHoc;
    private ArrayList<Double> danhSachDiem;

    public SinhVien() {
        super();
        this.danhSachDiem = new ArrayList<>();
    }

    public SinhVien(String mssv, String hoTen, String ngaySinh, String gioiTinh, String nganhHoc) {
        super(hoTen, ngaySinh, gioiTinh);
        this.mssv = mssv;
        this.nganhHoc = nganhHoc;
        this.danhSachDiem = new ArrayList<>();
    }

    // Logic nghiệp vụ tính điểm vẫn giữ ở đây
    public double tinhDiemTrungBinh() {
        if (danhSachDiem == null || danhSachDiem.isEmpty()) return 0.0;
        double tong = 0;
        for (Double d : danhSachDiem) tong += d;
        return tong / danhSachDiem.size();
    }

    // Getters & Setters
    public String getMssv() { return mssv; }
    public void setMssv(String mssv) { this.mssv = mssv; }
    public String getNganhHoc() { return nganhHoc; }
    public void setNganhHoc(String nganhHoc) { this.nganhHoc = nganhHoc; }
    public ArrayList<Double> getDanhSachDiem() { return danhSachDiem; }
    public void setDanhSachDiem(ArrayList<Double> danhSachDiem) { this.danhSachDiem = danhSachDiem; }
    
    // Helper để hiển thị chuỗi điểm ra bảng
    public String getDiemString() {
        if (danhSachDiem.isEmpty()) return "";
        return danhSachDiem.toString().replace("[", "").replace("]", "");
    }
}