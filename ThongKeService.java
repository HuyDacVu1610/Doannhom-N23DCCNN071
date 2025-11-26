import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThongKeService implements IThongKe {
    @Override
    public String thongKeNganh(ArrayList<SinhVien> ds) {
        if (ds.isEmpty()) return "Danh sách trống.";
        StringBuilder sb = new StringBuilder("=== THỐNG KÊ THEO NGÀNH ===\n");
        List<String> listNganh = new ArrayList<>();
        for (SinhVien sv : ds) {
            if (!listNganh.contains(sv.getNganhHoc())) listNganh.add(sv.getNganhHoc());
        }
        for (String nganh : listNganh) {
            int count = 0;
            for (SinhVien sv : ds) if (sv.getNganhHoc().equals(nganh)) count++;
            sb.append(String.format("- %s: %d sinh viên\n", nganh, count));
        }
        return sb.toString();
    }

    @Override
    public String thongKeGioiTinh(ArrayList<SinhVien> ds) {
        if (ds.isEmpty()) return "Danh sách trống.";
        int nam = 0, nu = 0, khac = 0;
        for (SinhVien sv : ds) {
            if (sv.getGioiTinh().equalsIgnoreCase("Nam")) nam++;
            else if (sv.getGioiTinh().equalsIgnoreCase("Nữ")) nu++;
            else khac++;
        }
        return String.format("=== THỐNG KÊ GIỚI TÍNH ===\n- Nam: %d\n- Nữ: %d\n- Khác: %d", nam, nu, khac);
    }

    @Override
    public String topSinhVien(ArrayList<SinhVien> ds) {
        if (ds.isEmpty()) return "Danh sách trống.";
        SinhVien max = Collections.max(ds, Comparator.comparingDouble(SinhVien::tinhDiemTrungBinh));
        SinhVien min = Collections.min(ds, Comparator.comparingDouble(SinhVien::tinhDiemTrungBinh));
        
        return String.format("=== TOP SINH VIÊN ===\nCao nhất: %s (%.2f)\nThấp nhất: %s (%.2f)", 
                             max.getHoTen(), max.tinhDiemTrungBinh(), 
                             min.getHoTen(), min.tinhDiemTrungBinh());
    }
}