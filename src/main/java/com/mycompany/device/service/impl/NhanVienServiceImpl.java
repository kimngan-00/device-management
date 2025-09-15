package com.mycompany.device.service.impl;

import com.mycompany.device.dao.NhanVienDAO;
import com.mycompany.device.dao.impl.NhanVienDAOMySQLImpl;
import com.mycompany.device.model.NhanVien;
import com.mycompany.device.observer.NhanVienObserver;
import com.mycompany.device.observer.NhanVienSubject;
import com.mycompany.device.service.NhanVienService;
import com.mycompany.device.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của NhanVienService với Observer Pattern
 * @author Kim Ngan - Service Implementation Layer
 */
public class NhanVienServiceImpl implements NhanVienService {
    
    private static final Logger logger = LoggerFactory.getLogger(NhanVienServiceImpl.class);
    
    private final NhanVienDAO nhanVienDAO;
    private final NhanVienSubject subject;
    
    public NhanVienServiceImpl() {
        // Sử dụng MySQL implementation
        this.nhanVienDAO = new NhanVienDAOMySQLImpl();
        this.subject = new NhanVienSubject();
        
        // Thêm logger observer mặc định
        this.subject.addObserver(new com.mycompany.device.observer.NhanVienLogger());
        
        logger.info("Khởi tạo NhanVienServiceImpl với MySQL database và Observer pattern");
    }
    
    public NhanVienServiceImpl(NhanVienDAO nhanVienDAO) {
        this.nhanVienDAO = nhanVienDAO;
        this.subject = new NhanVienSubject();
        
        // Thêm logger observer mặc định
        this.subject.addObserver(new com.mycompany.device.observer.NhanVienLogger());
        
        logger.info("Khởi tạo NhanVienServiceImpl với DAO được inject và Observer pattern");
    }
    
    /**
     * Thêm observer
     */
    public void addObserver(NhanVienObserver observer) {
        subject.addObserver(observer);
        logger.info("Đã thêm observer: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Xóa observer
     */
    public void removeObserver(NhanVienObserver observer) {
        subject.removeObserver(observer);
        logger.info("Đã xóa observer: {}", observer.getClass().getSimpleName());
    }
    
    @Override
    public boolean taoNhanVien(String maNhanVien, String tenNhanVien, String email, String password, 
                              String soDienThoai, NhanVien.NhanVienRole role, String maPhongBan) {
        logger.info("Bắt đầu tạo nhân viên: maNhanVien={}, tenNhanVien={}, email={}", maNhanVien, tenNhanVien, email);
        
        // Validate input
        if (!validateInput(maNhanVien, tenNhanVien, email, password, maPhongBan)) {
            logger.warn("Thông tin nhân viên không hợp lệ");
            return false;
        }
        
        // Kiểm tra nhân viên đã tồn tại chưa
        if (nhanVienDAO.existsNhanVien(maNhanVien.trim())) {
            logger.warn("Nhân viên với mã '{}' đã tồn tại", maNhanVien);
            return false;
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (nhanVienDAO.existsEmail(email.trim())) {
            logger.warn("Email '{}' đã được sử dụng", email);
            return false;
        }
        
        // Hash password
        String hashedPassword = PasswordUtil.hashPasswordSimple(password);
        logger.info("Password đã được hash");
        
        // Tạo object NhanVien
        NhanVien nhanVien = new NhanVien(maNhanVien.trim(), tenNhanVien.trim(), email.trim(), hashedPassword);
        nhanVien.setSoDienThoai(soDienThoai);
        nhanVien.setRole(role);
        nhanVien.setMaPhongBan(maPhongBan.trim());
        
        // Validate object
        if (!validateNhanVien(nhanVien)) {
            logger.warn("Object nhân viên không hợp lệ");
            return false;
        }
        
        // Gọi DAO để tạo
        boolean result = nhanVienDAO.createNhanVien(nhanVien);
        
        if (result) {
            logger.info("Tạo nhân viên thành công: {}", maNhanVien);
            // Thông báo cho observers
            subject.notifyNhanVienAdded(nhanVien);
        } else {
            logger.error("Tạo nhân viên thất bại: {}", maNhanVien);
        }
        
        return result;
    }
    
    @Override
    public boolean taoNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            logger.warn("Không thể tạo nhân viên: object null");
            return false;
        }
        
        return taoNhanVien(nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), nhanVien.getEmail(), 
                          nhanVien.getPassword(), nhanVien.getSoDienThoai(), nhanVien.getRole(), nhanVien.getMaPhongBan());
    }
    
    @Override
    public boolean capNhatNhanVien(String maNhanVien, String tenNhanVien, String email, String password, 
                                  String soDienThoai, NhanVien.NhanVienRole role, String maPhongBan) {
        logger.info("Bắt đầu cập nhật nhân viên: maNhanVien={}, tenNhanVien={}, email={}", maNhanVien, tenNhanVien, email);
        
        // Validate input
        if (!validateInput(maNhanVien, tenNhanVien, email, password, maPhongBan)) {
            logger.warn("Thông tin nhân viên không hợp lệ");
            return false;
        }
        
        // Kiểm tra nhân viên có tồn tại không
        if (!nhanVienDAO.existsNhanVien(maNhanVien.trim())) {
            logger.warn("Nhân viên với mã '{}' không tồn tại", maNhanVien);
            return false;
        }
        
        // Lấy thông tin cũ để so sánh
        Optional<NhanVien> oldNhanVienOpt = nhanVienDAO.findNhanVienByMa(maNhanVien.trim());
        if (!oldNhanVienOpt.isPresent()) {
            logger.warn("Không thể lấy thông tin nhân viên cũ: {}", maNhanVien);
            return false;
        }
        NhanVien oldNhanVien = oldNhanVienOpt.get();
        
        // Kiểm tra email có bị trùng với nhân viên khác không
        Optional<NhanVien> existingByEmail = nhanVienDAO.findNhanVienByEmail(email.trim());
        if (existingByEmail.isPresent() && !existingByEmail.get().getMaNhanVien().equals(maNhanVien.trim())) {
            logger.warn("Email '{}' đã được sử dụng bởi nhân viên khác", email);
            return false;
        }
        
        // Hash password
        String hashedPassword = PasswordUtil.hashPasswordSimple(password);
        logger.info("Password đã được hash");
        
        // Tạo object NhanVien mới
        NhanVien nhanVien = new NhanVien(maNhanVien.trim(), tenNhanVien.trim(), email.trim(), hashedPassword);
        nhanVien.setSoDienThoai(soDienThoai);
        nhanVien.setRole(role);
        nhanVien.setMaPhongBan(maPhongBan.trim());
        
        // Validate object
        if (!validateNhanVien(nhanVien)) {
            logger.warn("Object nhân viên không hợp lệ");
            return false;
        }
        
        // Gọi DAO để cập nhật
        boolean result = nhanVienDAO.updateNhanVien(nhanVien);
        
        if (result) {
            logger.info("Cập nhật nhân viên thành công: {}", maNhanVien);
            // Thông báo cho observers
            subject.notifyNhanVienUpdated(nhanVien, oldNhanVien);
        } else {
            logger.error("Cập nhật nhân viên thất bại: {}", maNhanVien);
        }
        
        return result;
    }
    
    @Override
    public boolean capNhatNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            logger.warn("Không thể cập nhật nhân viên: object null");
            return false;
        }
        
        return capNhatNhanVien(nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), nhanVien.getEmail(), 
                              nhanVien.getPassword(), nhanVien.getSoDienThoai(), nhanVien.getRole(), nhanVien.getMaPhongBan());
    }
    
    @Override
    public List<NhanVien> xemDanhSachNhanVien() {
        logger.info("Lấy danh sách tất cả nhân viên");
        
        List<NhanVien> result = nhanVienDAO.getAllNhanVien();
        
        logger.info("Lấy danh sách nhân viên thành công: {} nhân viên", result.size());
        return result;
    }
    
    @Override
    public Optional<NhanVien> timNhanVienTheoMa(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            logger.warn("Không thể tìm nhân viên: mã nhân viên không hợp lệ");
            return Optional.empty();
        }
        
        logger.info("Tìm nhân viên theo mã: {}", maNhanVien);
        
        Optional<NhanVien> result = nhanVienDAO.findNhanVienByMa(maNhanVien.trim());
        
        if (result.isPresent()) {
            logger.info("Tìm thấy nhân viên: {}", maNhanVien);
        } else {
            logger.info("Không tìm thấy nhân viên: {}", maNhanVien);
        }
        
        return result;
    }
    
    @Override
    public Optional<NhanVien> timNhanVienTheoEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Không thể tìm nhân viên: email không hợp lệ");
            return Optional.empty();
        }
        
        logger.info("Tìm nhân viên theo email: {}", email);
        
        Optional<NhanVien> result = nhanVienDAO.findNhanVienByEmail(email.trim());
        
        if (result.isPresent()) {
            logger.info("Tìm thấy nhân viên theo email: {}", email);
        } else {
            logger.info("Không tìm thấy nhân viên theo email: {}", email);
        }
        
        return result;
    }
    
    @Override
    public List<NhanVien> timKiemNhanVienTheoTen(String tenNhanVien) {
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            logger.warn("Không thể tìm kiếm nhân viên: tên nhân viên không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm kiếm nhân viên theo tên: {}", tenNhanVien);
        
        List<NhanVien> result = nhanVienDAO.searchNhanVienByTen(tenNhanVien.trim());
        
        logger.info("Tìm kiếm nhân viên thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public List<NhanVien> timNhanVienTheoPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Không thể tìm nhân viên: mã phòng ban không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm nhân viên theo phòng ban: {}", maPhongBan);
        
        List<NhanVien> result = nhanVienDAO.findNhanVienByPhongBan(maPhongBan.trim());
        
        logger.info("Tìm nhân viên theo phòng ban thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public List<NhanVien> timNhanVienTheoVaiTro(NhanVien.NhanVienRole role) {
        if (role == null) {
            logger.warn("Không thể tìm nhân viên: vai trò không hợp lệ");
            return List.of();
        }
        
        logger.info("Tìm nhân viên theo vai trò: {}", role);
        
        List<NhanVien> result = nhanVienDAO.findNhanVienByRole(role);
        
        logger.info("Tìm nhân viên theo vai trò thành công: {} kết quả", result.size());
        return result;
    }
    
    @Override
    public boolean xoaNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            logger.warn("Không thể xóa nhân viên: mã nhân viên không hợp lệ");
            return false;
        }
        
        logger.info("Bắt đầu xóa nhân viên: {}", maNhanVien);
        
        // Kiểm tra nhân viên có tồn tại không
        if (!nhanVienDAO.existsNhanVien(maNhanVien.trim())) {
            logger.warn("Nhân viên với mã '{}' không tồn tại", maNhanVien);
            return false;
        }
        
        // Gọi DAO để xóa
        boolean result = nhanVienDAO.deleteNhanVien(maNhanVien.trim());
        
        if (result) {
            logger.info("Xóa nhân viên thành công: {}", maNhanVien);
            // Thông báo cho observers
            subject.notifyNhanVienDeleted(maNhanVien.trim());
        } else {
            logger.error("Xóa nhân viên thất bại: {}", maNhanVien);
        }
        
        return result;
    }
    
    @Override
    public boolean kiemTraNhanVienTonTai(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            return false;
        }
        
        return nhanVienDAO.existsNhanVien(maNhanVien.trim());
    }
    
    @Override
    public boolean kiemTraEmailTonTai(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return nhanVienDAO.existsEmail(email.trim());
    }
    
    @Override
    public int demSoLuongNhanVien() {
        int count = nhanVienDAO.countNhanVien();
        logger.info("Đếm số lượng nhân viên: {}", count);
        return count;
    }
    
    @Override
    public int demSoLuongNhanVienTheoPhongBan(String maPhongBan) {
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            return 0;
        }
        
        int count = nhanVienDAO.countNhanVienByPhongBan(maPhongBan.trim());
        logger.info("Đếm số lượng nhân viên theo phòng ban '{}': {}", maPhongBan, count);
        return count;
    }
    
    @Override
    public boolean validateNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            return false;
        }
        
        return validateInput(nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), 
                           nhanVien.getEmail(), nhanVien.getPassword(), nhanVien.getMaPhongBan());
    }
    
    @Override
    public Optional<NhanVien> dangNhap(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            logger.warn("Thông tin đăng nhập không hợp lệ");
            return Optional.empty();
        }
        
        logger.info("Thử đăng nhập với email: {}", email);
        
        Optional<NhanVien> nhanVien = nhanVienDAO.findNhanVienByEmail(email.trim());
        
        if (nhanVien.isPresent()) {
            // Verify password với hash
            if (PasswordUtil.verifyPasswordSimple(password, nhanVien.get().getPassword())) {
                logger.info("Đăng nhập thành công: {}", email);
                // Thông báo cho observers
                subject.notifyNhanVienLoggedIn(nhanVien.get());
                return nhanVien;
            } else {
                logger.warn("Mật khẩu không đúng: {}", email);
            }
        } else {
            logger.warn("Không tìm thấy nhân viên với email: {}", email);
        }
        
        return Optional.empty();
    }
    
    /**
     * Đăng xuất nhân viên
     */
    public void dangXuat(NhanVien nhanVien) {
        if (nhanVien != null) {
            logger.info("Nhân viên đăng xuất: {}", nhanVien.getEmail());
            // Thông báo cho observers
            subject.notifyNhanVienLoggedOut(nhanVien);
        }
    }
    
    /**
     * Validate input cơ bản
     */
    private boolean validateInput(String maNhanVien, String tenNhanVien, String email, String password, String maPhongBan) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            logger.warn("Mã nhân viên không được để trống");
            return false;
        }
        
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            logger.warn("Tên nhân viên không được để trống");
            return false;
        }
        
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Email không được để trống");
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.warn("Mật khẩu không được để trống");
            return false;
        }
        
        if (maPhongBan == null || maPhongBan.trim().isEmpty()) {
            logger.warn("Mã phòng ban không được để trống");
            return false;
        }
        
        if (maNhanVien.trim().length() > 20) {
            logger.warn("Mã nhân viên không được vượt quá 20 ký tự");
            return false;
        }
        
        if (tenNhanVien.trim().length() > 255) {
            logger.warn("Tên nhân viên không được vượt quá 255 ký tự");
            return false;
        }
        
        if (email.trim().length() > 255) {
            logger.warn("Email không được vượt quá 255 ký tự");
            return false;
        }
        
        if (!email.trim().contains("@")) {
            logger.warn("Email không hợp lệ");
            return false;
        }
        
        return true;
    }
}
