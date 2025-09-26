package com.mycompany.device.service;

import com.mycompany.device.model.YeuCau;
import java.util.List;
import java.util.Optional;

/**
 * Service interface cho YeuCau
 * @author Kim Ngan - Service Layer
 */
public interface YeuCauService {
    
    /**
     * Tạo yêu cầu mới
     * @param thietBiId ID thiết bị
     * @param nhanVienId ID nhân viên
     * @param lyDo Lý do yêu cầu (có thể null)
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean taoYeuCau(Long thietBiId, Long nhanVienId, String lyDo);
    
    /**
     * Tạo yêu cầu mới từ object
     * @param yeuCau Yêu cầu cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean taoYeuCau(YeuCau yeuCau);
    
    /**
     * Cập nhật thông tin yêu cầu
     * @param yeuCauId ID yêu cầu cần cập nhật
     * @param thietBiId ID thiết bị mới
     * @param nhanVienId ID nhân viên mới
     * @param lyDo Lý do mới (có thể null)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatYeuCau(Long yeuCauId, Long thietBiId, Long nhanVienId, String lyDo);
    
    /**
     * Cập nhật thông tin yêu cầu từ object
     * @param yeuCau Yêu cầu cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatYeuCau(YeuCau yeuCau);
    
    /**
     * Cập nhật trạng thái yêu cầu
     * @param yeuCauId ID yêu cầu
     * @param trangThai Trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean capNhatTrangThai(Long yeuCauId, YeuCau.TrangThaiYeuCau trangThai);
    
    /**
     * Xem danh sách tất cả yêu cầu
     * @return List chứa tất cả yêu cầu
     */
    List<YeuCau> xemDanhSachYeuCau();
    
    /**
     * Tìm yêu cầu theo ID
     * @param yeuCauId ID yêu cầu
     * @return Optional chứa yêu cầu nếu tìm thấy
     */
    Optional<YeuCau> timYeuCauTheoId(Long yeuCauId);
    
    /**
     * Tìm yêu cầu theo thiết bị
     * @param thietBiId ID thiết bị
     * @return List chứa các yêu cầu của thiết bị
     */
    List<YeuCau> timYeuCauTheoThietBi(Long thietBiId);
    
    /**
     * Tìm yêu cầu theo nhân viên
     * @param nhanVienId ID nhân viên
     * @return List chứa các yêu cầu của nhân viên
     */
    List<YeuCau> timYeuCauTheoNhanVien(Long nhanVienId);
    
    /**
     * Tìm yêu cầu theo trạng thái
     * @param trangThai Trạng thái cần tìm
     * @return List chứa các yêu cầu có trạng thái
     */
    List<YeuCau> timYeuCauTheoTrangThai(YeuCau.TrangThaiYeuCau trangThai);
    
    /**
     * Tìm kiếm yêu cầu theo lý do
     * @param lyDo Lý do cần tìm
     * @return List chứa các yêu cầu có lý do chứa từ khóa
     */
    List<YeuCau> timKiemYeuCauTheoLyDo(String lyDo);
    
    /**
     * Xóa yêu cầu
     * @param yeuCauId ID yêu cầu cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean xoaYeuCau(Long yeuCauId);
    
    /**
     * Kiểm tra yêu cầu có tồn tại không
     * @param yeuCauId ID yêu cầu cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean kiemTraYeuCauTonTai(Long yeuCauId);
    
    /**
     * Đếm tổng số yêu cầu
     * @return Số lượng yêu cầu
     */
    int demSoLuongYeuCau();
    
    /**
     * Đếm số yêu cầu theo trạng thái
     * @param trangThai Trạng thái cần đếm
     * @return Số lượng yêu cầu có trạng thái
     */
    int demSoLuongYeuCauTheoTrangThai(YeuCau.TrangThaiYeuCau trangThai);
    
    /**
     * Đếm số yêu cầu của nhân viên
     * @param nhanVienId ID nhân viên
     * @return Số lượng yêu cầu của nhân viên
     */
    int demSoLuongYeuCauCuaNhanVien(Long nhanVienId);
    
    /**
     * Validate thông tin yêu cầu
     * @param yeuCau Yêu cầu cần validate
     * @return true nếu hợp lệ, false nếu không
     */
    boolean validateYeuCau(YeuCau yeuCau);
    
    /**
     * Duyệt yêu cầu
     * @param yeuCauId ID yêu cầu
     * @return true nếu duyệt thành công, false nếu thất bại
     */
    boolean duyetYeuCau(Long yeuCauId);
    
    /**
     * Từ chối yêu cầu
     * @param yeuCauId ID yêu cầu
     * @param lyDoTuChoi Lý do từ chối
     * @return true nếu từ chối thành công, false nếu thất bại
     */
    boolean tuChoiYeuCau(Long yeuCauId, String lyDoTuChoi);
    
    /**
     * Cấp phát thiết bị
     * @param yeuCauId ID yêu cầu
     * @return true nếu cấp phát thành công, false nếu thất bại
     */
    boolean capPhatThietBi(Long yeuCauId);
    
    /**
     * Hủy yêu cầu
     * @param yeuCauId ID yêu cầu
     * @param lyDoHuy Lý do hủy
     * @return true nếu hủy thành công, false nếu thất bại
     */
    boolean huyYeuCau(Long yeuCauId, String lyDoHuy);
} 