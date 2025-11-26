/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SinhVienDAO extends BaseDAO {

    // 1. Lấy toàn bộ danh sách
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
                SinhVien sv = new SinhVien(
                    rs.getString("id"), 
                    rs.getString("ten"), 
                    rs.getDouble("diem")
                );
                list.add(sv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs); // Dùng hàm ông viết trong BaseDAO
        }
        return list;
    }

    // 2. Thêm
    public boolean insert(SinhVien sv) {
        Connection conn = getConnection();
        String sql = "INSERT INTO sinhvien(id, ten, diem) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sv.getId());
            ps.setString(2, sv.getTen());
            ps.setDouble(3, sv.getDiem());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps, null);
        }
    }

    // 3. Sửa
    public boolean update(SinhVien sv) {
        Connection conn = getConnection();
        String sql = "UPDATE sinhvien SET ten=?, diem=? WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, sv.getTen());
            ps.setDouble(2, sv.getDiem());
            ps.setString(3, sv.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps, null);
        }
    }

    // 4. Xóa
    public boolean delete(String id) {
        Connection conn = getConnection();
        String sql = "DELETE FROM sinhvien WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps, null);
        }
    }

    // 5. Tìm kiếm (Theo tên gần đúng)
    public List<SinhVien> search(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        Connection conn = getConnection();
        String sql = "SELECT * FROM sinhvien WHERE ten LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SinhVien(rs.getString("id"), rs.getString("ten"), rs.getDouble("diem")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return list;
    }
    public List<SinhVien> getSortedList() {
        List<SinhVien> list = getAll();
   
        Collections.sort(list, (sv1, sv2) -> Double.compare(sv2.getDiem(), sv1.getDiem())); 
        return list;
    }
}