import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// ================== 1. FORM ĐĂNG NHẬP ==================
class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnExit;

    public LoginForm() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - getWidth()) / 2;
        int y = (screen.height - getHeight()) / 2;
        setLocation(x, y); // Căn giữa màn hình
        setLayout(new BorderLayout(10, 10));

        // Panel nhập liệu
        JPanel pnlCenter = new JPanel(new GridLayout(2, 2, 5, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        
        pnlCenter.add(new JLabel("Tài khoản:"));
        txtUser = new JTextField("admin"); // Mặc định cho nhanh test
        pnlCenter.add(txtUser);

        pnlCenter.add(new JLabel("Mật khẩu:"));
        txtPass = new JPasswordField("123");
        pnlCenter.add(txtPass);

        add(pnlCenter, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel pnlBottom = new JPanel(new FlowLayout());
        btnLogin = new JButton("Đăng nhập");
        btnExit = new JButton("Thoát");
        pnlBottom.add(btnLogin);
        pnlBottom.add(btnExit);

        add(pnlBottom, BorderLayout.SOUTH);

        // Xử lý sự kiện
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (user.equals("admin") && pass.equals("123")) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                new Giaodienqlsv().setVisible(true);
                dispose(); // Đóng form login
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}

// ================== 2. GIAO DIỆN CHÍNH ==================
public class Giaodienqlsv extends JFrame {
    
    // Khai báo các Service (Logic, File, Thống kê)
    private QuanLySinhVien quanLy; 
    private IFileHandler fileService; 
    private IThongKe thongKeService;

    // Components Giao diện
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMSSV, txtHoTen, txtNgaySinh, txtNganh, txtDiem;
    private JRadioButton male, female;
    private JTextArea txtThongKe;
    private JButton btnThem, btnXoa, btnSua, btnLamMoi;
    private JButton btnTimMSSV, btnTimTen, btnTimNganh; // Nút Tìm kiếm
    private JButton btnSapXepTen, btnSapXepTB, btnXepHang; // Nút Sắp xếp
    private JButton btnGhiFile, btnDocFile; // Nút File
    private JButton btnThongKeNganh, btnThongKeGioiTinh; // Nút Thống kê
    
    private JFileChooser fileChooser = new JFileChooser();

    public Giaodienqlsv() {
        // --- KHỞI TẠO CÁC SERVICE ---
        quanLy = new QuanLySinhVien();
        fileService = new FileService();       // Class thực thi IFileHandler
        thongKeService = new ThongKeService(); // Class thực thi IThongKe

        // --- CẤU HÌNH GIAO DIỆN ---
        setTitle("CHƯƠNG TRÌNH QUẢN LÝ SINH VIÊN");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - getWidth()) / 2;
        int y = (screen.height - getHeight()) / 2;
        setLocation(x, y); // Căn giữa màn hình

        // 1. FORM NHẬP LIỆU (NORTH)
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Sinh Viên"));

        txtMSSV = new JTextField();
        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField();
        txtNganh = new JTextField();
        txtDiem = new JTextField();

        male = new JRadioButton("Nam", true);
        female = new JRadioButton("Nữ");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(male);
        genderPanel.add(female);

        inputPanel.add(new JLabel("MSSV: *"));          inputPanel.add(txtMSSV);
        inputPanel.add(new JLabel("Họ Tên: *"));        inputPanel.add(txtHoTen);
        inputPanel.add(new JLabel("Ngày Sinh:"));       inputPanel.add(txtNgaySinh);
        inputPanel.add(new JLabel("Giới Tính:"));       inputPanel.add(genderPanel);
        inputPanel.add(new JLabel("Ngành Học:"));       inputPanel.add(txtNganh);
        inputPanel.add(new JLabel("Điểm (vd: 8, 9.5):")); inputPanel.add(txtDiem);

        add(inputPanel, BorderLayout.NORTH);

        // 2. BẢNG HIỂN THỊ (CENTER)
        String[] columns = {"MSSV", "Họ Tên", "Ngày Sinh", "Giới Tính", "Ngành", "Điểm Số", "Điểm TB"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sự kiện Click bảng -> Đổ dữ liệu lên form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doDuLieuLenForm();
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. CÁC NÚT CHỨC NĂNG (SOUTH)
        JPanel funcPanel = new JPanel(new GridLayout(3, 5, 10, 10));
        funcPanel.setBorder(BorderFactory.createTitledBorder("Bảng Điều Khiển"));

        btnThem = new JButton("Thêm Mới");
        btnSua = new JButton("Cập Nhật");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới Form");
        
        btnTimMSSV = new JButton("Tìm theo MSSV");
        btnTimTen = new JButton("Tìm theo Tên");
        btnTimNganh = new JButton("Tìm theo Ngành"); // Thêm nếu dùng
        
        btnSapXepTen = new JButton("Sắp Xếp Tên");
        btnSapXepTB = new JButton("Sắp Xếp Điểm");
        
        btnThongKeNganh = new JButton("TK Ngành");
        btnThongKeGioiTinh = new JButton("TK Giới Tính");
        btnXepHang = new JButton("Top SV");
        
        btnGhiFile = new JButton("Lưu File");
        btnDocFile = new JButton("Đọc File");

        // Add buttons (thêm btnTimNganh)
        funcPanel.add(btnThem); funcPanel.add(btnSua); funcPanel.add(btnXoa); funcPanel.add(btnLamMoi); funcPanel.add(new JLabel(""));
        funcPanel.add(btnTimMSSV); funcPanel.add(btnTimTen); funcPanel.add(btnTimNganh); funcPanel.add(btnSapXepTen); funcPanel.add(btnSapXepTB);
        funcPanel.add(btnGhiFile); funcPanel.add(btnDocFile); funcPanel.add(btnThongKeNganh); funcPanel.add(btnThongKeGioiTinh); funcPanel.add(btnXepHang);

        add(funcPanel, BorderLayout.SOUTH);

        // 4. KHUNG THỐNG KÊ (EAST)
        txtThongKe = new JTextArea();
        txtThongKe.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtThongKe.setEditable(false);
        JScrollPane statsScroll = new JScrollPane(txtThongKe);
        statsScroll.setBorder(BorderFactory.createTitledBorder("Báo Cáo Thống Kê"));
        statsScroll.setPreferredSize(new Dimension(300, 0));
        add(statsScroll, BorderLayout.EAST);

        // --- GÁN SỰ KIỆN (ACTIONS) ---
        ganSuKienChoCacNut();
    }

    private void ganSuKienChoCacNut() {
        btnThem.addActionListener(e -> xuLyThem());
        btnSua.addActionListener(e -> xuLySua());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> xoaTrangForm());
        btnTimMSSV.addActionListener(e -> xuLyTimKiemMSSV());
        btnTimTen.addActionListener(e -> xuLyTimKiemTen());
        // Thêm nếu dùng
        btnTimNganh.addActionListener(e -> xuLyTimKiemNganh());
        btnSapXepTen.addActionListener(e -> {
            quanLy.sapXepTheoTen();
            capNhatBang(quanLy.getDanhSachSV());
        });
        btnSapXepTB.addActionListener(e -> {
            quanLy.sapXepTheoDiemTB();
            capNhatBang(quanLy.getDanhSachSV());
        });
        btnGhiFile.addActionListener(e -> saveFile());
        btnDocFile.addActionListener(e -> loadFile());
        btnThongKeNganh.addActionListener(e -> {
            String kq = thongKeService.thongKeNganh(quanLy.getDanhSachSV());
            txtThongKe.setText(kq);
        });
        btnThongKeGioiTinh.addActionListener(e -> {
            String kq = thongKeService.thongKeGioiTinh(quanLy.getDanhSachSV());
            txtThongKe.setText(kq);
        });
        btnXepHang.addActionListener(e -> {
            String kq = thongKeService.topSinhVien(quanLy.getDanhSachSV());
            txtThongKe.setText(kq);
        });
    }

    private SinhVien taoSVTufrom() {
        String mssv = txtMSSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        
        if (mssv.isEmpty() || hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "MSSV và Tên không được để trống!");
            return null;
        }

        SinhVien sv = new SinhVien();
        sv.setMssv(mssv);
        sv.setHoTen(hoTen);
        sv.setNgaySinh(txtNgaySinh.getText().trim());
        sv.setGioiTinh(male.isSelected() ? "Nam" : "Nữ");
        sv.setNganhHoc(txtNganh.getText().trim());

        String strDiem = txtDiem.getText().trim();
        ArrayList<Double> listDiem = new ArrayList<>();
        if (!strDiem.isEmpty()) {
            try {
                String[] parts = strDiem.split(",");
                for (String p : parts) {
                    listDiem.add(Double.parseDouble(p.trim()));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Điểm nhập sai định dạng! (Ví dụ: 8.5, 9)");
                return null;
            }
        }
        sv.setDanhSachDiem(listDiem);
        return sv;
    }

    private void xuLyThem() {
        SinhVien sv = taoSVTufrom();
        if (sv != null) {
            if (quanLy.themSinhVien(sv)) {
                capNhatBang(quanLy.getDanhSachSV());
                xoaTrangForm();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Trùng MSSV hoặc lỗi!");
            }
        }
    }

    private void xuLySua() {
        String mssv = txtMSSV.getText().trim();
        if (mssv.isEmpty()) return;
        
        SinhVien svMoi = taoSVTufrom();
        if (svMoi != null) {
            if (quanLy.suaSinhVien(mssv, svMoi)) {
                capNhatBang(quanLy.getDanhSachSV());
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy MSSV để sửa!");
            }
        }
    }

    private void xuLyXoa() {
        String mssv = txtMSSV.getText().trim();
        if (!mssv.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa sinh viên " + mssv + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (quanLy.xoaSinhVien(mssv)) {
                    capNhatBang(quanLy.getDanhSachSV());
                    xoaTrangForm();
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy MSSV!");
                }
            }
        }
    }

    private void xuLyTimKiemMSSV() {
        String mssv = JOptionPane.showInputDialog(this, "Nhập MSSV cần tìm:");
        if (mssv != null && !mssv.isEmpty()) {
            SinhVien sv = quanLy.timSinhVienTheoMssv(mssv);
            ArrayList<SinhVien> kq = new ArrayList<>();
            if (sv != null) kq.add(sv);
            capNhatBang(kq);
            if (kq.isEmpty()) JOptionPane.showMessageDialog(this, "Không tìm thấy!");
        }
    }

    private void xuLyTimKiemTen() {
        String ten = JOptionPane.showInputDialog(this, "Nhập tên cần tìm:");
        if (ten != null && !ten.isEmpty()) {
            ArrayList<SinhVien> kq = quanLy.timSinhVienTheoTen(ten);
            capNhatBang(kq);
            if (kq.isEmpty()) JOptionPane.showMessageDialog(this, "Không tìm thấy!");
        }
    }

    // Thêm nếu dùng btnTimNganh
    private void xuLyTimKiemNganh() {
        String nganh = JOptionPane.showInputDialog(this, "Nhập ngành cần tìm:");
        if (nganh != null && !nganh.isEmpty()) {
            ArrayList<SinhVien> kq = quanLy.timSinhVienTheoNganh(nganh);
            capNhatBang(kq);
            if (kq.isEmpty()) JOptionPane.showMessageDialog(this, "Không tìm thấy!");
        }
    }

    private void capNhatBang(ArrayList<SinhVien> danhSach) {
        tableModel.setRowCount(0);
        for (SinhVien sv : danhSach) {
            tableModel.addRow(new Object[]{
                sv.getMssv(), 
                sv.getHoTen(), 
                sv.getNgaySinh(),
                sv.getGioiTinh(),  
                sv.getNganhHoc(),
                sv.getDanhSachDiem().toString(), 
                String.format("%.2f", sv.tinhDiemTrungBinh()) 
            });
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                fileService.ghiFile(quanLy.getDanhSachSV(), file.getPath());
                JOptionPane.showMessageDialog(this, "Lưu file thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + e.getMessage());
            }
        }
    }

    private void loadFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ArrayList<SinhVien> dsMoi = fileService.docFile(file.getPath());
                quanLy.setDanhSachSV(dsMoi);
                capNhatBang(dsMoi);
                JOptionPane.showMessageDialog(this, "Đọc file thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + e.getMessage());
            }
        }
    }
    
    private void doDuLieuLenForm() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMSSV.setText(table.getValueAt(row, 0).toString());
            txtHoTen.setText(table.getValueAt(row, 1).toString());
            txtNgaySinh.setText(table.getValueAt(row, 2).toString());
            String gt = table.getValueAt(row, 3).toString();
            if (gt.equalsIgnoreCase("Nam")) male.setSelected(true); else female.setSelected(true);
            txtNganh.setText(table.getValueAt(row, 4).toString());
            
            String diemRaw = table.getValueAt(row, 5).toString();
            txtDiem.setText(diemRaw.replace("[", "").replace("]", "").replace(" ", ""));
        }
    }
    
    private void xoaTrangForm() {
        txtMSSV.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtNganh.setText("");
        txtDiem.setText("");
        male.setSelected(true);
        capNhatBang(quanLy.getDanhSachSV()); // Reset bảng về full danh sách
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
