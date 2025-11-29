/*
 * SinhVienDAO – MySQL
 */
package connectdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinhVienDAO extends BaseDAO {

    // ================== HELPER ==================

    // Lấy điểm thứ index trong danhSachDiem, thiếu thì trả 0
    private double getDiemAt(SinhVien sv, int index) {
        if (sv.getDanhSachDiem() == null || sv.getDanhSachDiem().size() <= index) {
            return 0.0;
        }
        Double d = sv.getDanhSachDiem().get(index);
        return d != null ? d : 0.0;
    }

    // Map 1 dòng ResultSet -> SinhVien
    private SinhVien mapRowToSinhVien(ResultSet rs) throws SQLException {
        return new SinhVien(
                rs.getString("mssv"),
                rs.getString("hoTen"),
                rs.getString("ngaySinh"),
                rs.getString("gioiTinh"),
                rs.getString("nganhHoc"),
                rs.getDouble("diem1"),
                rs.getDouble("diem2"),
                rs.getDouble("diem3")
        );
    }

    // Đóng connection (dùng hàm có sẵn của BaseDAO)
    private void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        closeConnection(conn, ps, rs);
    }

    // ================== SELECT LIST ==================

    // Lấy toàn bộ sinh viên
    public List<SinhVien> getAll() {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSinhVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    // Lấy tất cả sinh viên sắp xếp theo tên (A-Z)
    public List<SinhVien> getAllSortedByName() {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien ORDER BY hoTen ASC";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSinhVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    // Lấy tất cả sinh viên sắp xếp theo điểm trung bình giảm dần
    public List<SinhVien> getAllSortedByDiemTB() {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien " +
                     "ORDER BY (diem1 + diem2 + diem3) / 3 DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSinhVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    // Lấy TOP N sinh viên điểm TB cao nhất
    public List<SinhVien> getTopSinhVien(int soLuong) {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien " +
                     "ORDER BY (diem1 + diem2 + diem3) / 3 DESC " +
                     "LIMIT ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, soLuong);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSinhVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    // ================== SEARCH ==================

    // Tìm 1 sinh viên theo MSSV
    public SinhVien findByMssv(String mssv) {
        SinhVien sv = null;
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien WHERE mssv = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, mssv);
            rs = ps.executeQuery();
            if (rs.next()) {
                sv = mapRowToSinhVien(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return sv;
    }

    // Tìm sinh viên theo tên (gần đúng)
    public List<SinhVien> searchByName(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien WHERE hoTen LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSinhVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    // Tìm sinh viên theo ngành (gần đúng)
    public List<SinhVien> searchByNganh(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien WHERE nganhHoc LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSinhVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return list;
    }

    // ================== THỐNG KÊ ==================

    // Thống kê số lượng sinh viên theo giới tính
    // key: "Nam", "Nữ", ...  value: số lượng
    public Map<String, Integer> thongKeTheoGioiTinh() {
        Map<String, Integer> kq = new HashMap<>();
        Connection conn = getConnection();
        String sql = "SELECT gioiTinh, COUNT(*) AS soLuong " +
                     "FROM sinhvien GROUP BY gioiTinh";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String gt = rs.getString("gioiTinh");
                int soLuong = rs.getInt("soLuong");
                kq.put(gt, soLuong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return kq;
    }

    // ================== INSERT / UPDATE / DELETE ==================

    // Thêm sinh viên
    public boolean insert(SinhVien sv) {
        Connection conn = getConnection();
        String sql = "INSERT INTO sinhvien " +
                "(mssv, hoTen, ngaySinh, gioiTinh, nganhHoc, diem1, diem2, diem3) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sv.getMssv());
            ps.setString(2, sv.getHoTen());
            ps.setString(3, sv.getNgaySinh());
            ps.setString(4, sv.getGioiTinh());
            ps.setString(5, sv.getNganhHoc());
            ps.setDouble(6, getDiemAt(sv, 0));
            ps.setDouble(7, getDiemAt(sv, 1));
            ps.setDouble(8, getDiemAt(sv, 2));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(conn, ps, null);
        }
    }

    // Cập nhật sinh viên (mssv cũ -> sv mới)
    public boolean update(String mssvCu, SinhVien svMoi) {
        Connection conn = getConnection();
        String sql = "UPDATE sinhvien SET " +
                "mssv = ?, " +
                "hoTen = ?, " +
                "ngaySinh = ?, " +
                "gioiTinh = ?, " +
                "nganhHoc = ?, " +
                "diem1 = ?, " +
                "diem2 = ?, " +
                "diem3 = ? " +
                "WHERE mssv = ?";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, svMoi.getMssv());
            ps.setString(2, svMoi.getHoTen());
            ps.setString(3, svMoi.getNgaySinh());
            ps.setString(4, svMoi.getGioiTinh());
            ps.setString(5, svMoi.getNganhHoc());
            ps.setDouble(6, getDiemAt(svMoi, 0));
            ps.setDouble(7, getDiemAt(svMoi, 1));
            ps.setDouble(8, getDiemAt(svMoi, 2));
            ps.setString(9, mssvCu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(conn, ps, null);
        }
    }

    // Xóa sinh viên
    public boolean delete(String mssv) {
        Connection conn = getConnection();
        String sql = "DELETE FROM sinhvien WHERE mssv = ?";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, mssv);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(conn, ps, null);
        }
    }
}
