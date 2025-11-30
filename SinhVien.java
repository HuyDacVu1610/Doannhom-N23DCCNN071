package doanhaha;
import java.io.Serializable;
import java.util.ArrayList;

public class SinhVien extends Nguoi  {

    private String mssv;
    private String nganhHoc;
    private ArrayList<Double> danhSachDiem;

    // 1. Constructor mặc định
    public SinhVien() {
        super();
        this.danhSachDiem = new ArrayList<>();
    }

    // 2. Constructor full: map đúng 1 dòng trong DB
    public SinhVien(String mssv,
                    String hoTen,
                    String ngaySinh,
                    String gioiTinh,
                    String nganhHoc,
                    double diem1,
                    double diem2,
                    double diem3) {

        // gọi Nguoi(hoTen, ngaySinh, gioiTinh)
        super(hoTen, ngaySinh, gioiTinh);

        this.mssv = mssv;
        this.nganhHoc = nganhHoc;

        this.danhSachDiem = new ArrayList<>();
        this.danhSachDiem.add(diem1);
        this.danhSachDiem.add(diem2);
        this.danhSachDiem.add(diem3);
    }

    // Logic nghiệp vụ tính điểm trung bình
    public double tinhDiemTrungBinh() {
        if (danhSachDiem == null || danhSachDiem.isEmpty()) {
            return 0.0;
        }
        double tong = 0;
        for (Double d : danhSachDiem) {
            tong += d;
        }
        return tong / danhSachDiem.size();
    }

    // Thêm điểm mới vào danh sách
    public void themDiem(double diem) {
        if (this.danhSachDiem == null) {
            this.danhSachDiem = new ArrayList<>();
        }
        this.danhSachDiem.add(diem);
    }

    // Getters & Setters
    public String getHoTen() {
    return super.getHoTen();
    }
    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getNganhHoc() {
        return nganhHoc;
    }

    public void setNganhHoc(String nganhHoc) {
        this.nganhHoc = nganhHoc;
    }

    public ArrayList<Double> getDanhSachDiem() {
        return danhSachDiem;
    }

    public void setDanhSachDiem(ArrayList<Double> danhSachDiem) {
        this.danhSachDiem = danhSachDiem;
    }

    public String getDiemString() {
        if (danhSachDiem == null || danhSachDiem.isEmpty()) {
            return "";
        }
        return danhSachDiem.toString().replace("[", "").replace("]", "");
    }

    @Override
    public String toString() {
        return "SinhVien{" +
                "mssv='" + mssv + '\'' +
                ", nganhHoc='" + nganhHoc + '\'' +
                ", diem=" + getDiemString() +
                '}';
    }
}
