import java.io.*;
import java.util.ArrayList;

public class FileService implements IFileHandler {
    @Override
    public void ghiFile(ArrayList<SinhVien> ds, String tenFile) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tenFile))) {
            for (SinhVien sv : ds) {
                StringBuilder sb = new StringBuilder();
                sb.append(sv.getMssv()).append(",");
                sb.append(sv.getHoTen()).append(",");
                sb.append(sv.getNgaySinh()).append(",");
                sb.append(sv.getGioiTinh()).append(",");
                sb.append(sv.getNganhHoc()).append(",");
                
                ArrayList<Double> diem = sv.getDanhSachDiem();
                if (diem != null && !diem.isEmpty()) {
                    for (int i = 0; i < diem.size(); i++) {
                        sb.append(diem.get(i));
                        if (i < diem.size() - 1) sb.append("|");
                    }
                } else {
                    sb.append("0.0");
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    @Override
    public ArrayList<SinhVien> docFile(String tenFile) throws Exception {
        ArrayList<SinhVien> list = new ArrayList<>();
        File f = new File(tenFile);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    SinhVien sv = new SinhVien(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    if (parts.length > 5 && !parts[5].isEmpty()) {
                        String[] diemParts = parts[5].split("\\|");
                        ArrayList<Double> dsDiem = new ArrayList<>();
                        for (String d : diemParts) {
                            try { dsDiem.add(Double.parseDouble(d)); } catch (Exception e) {}
                        }
                        sv.setDanhSachDiem(dsDiem);
                    }
                    list.add(sv);
                }
            }
        }
        return list;
    }
}