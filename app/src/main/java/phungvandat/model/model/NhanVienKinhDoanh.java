package phungvandat.model.model;

/**
 * Created by PHUNGVANDAT on 3/10/2017.
 */
public class NhanVienKinhDoanh extends NhanVien {
    private int soSanPham;

    public int getSoSanPham() {
        return soSanPham;
    }

    public void setSoSanPham(int soSanPham) {
        this.soSanPham = soSanPham;
    }

    public NhanVienKinhDoanh(String maNhanVien, String hoTen, int ngaySinh, int thangSinh,
                             int namSinh, String gioiTinh, int soCMND, String soPhone, String email,
                             String danToc, String nguyenQuan, String hoKhau, String tonGiao,
                             String tinhTrangHonNhan, String phongBanTrucThuoc, float heSoLuong,byte[] hinhAnh, int soSanPham) {
        super(maNhanVien, hoTen, ngaySinh, thangSinh, namSinh, gioiTinh, soCMND, soPhone, email,
                danToc, nguyenQuan, hoKhau, tonGiao, tinhTrangHonNhan, phongBanTrucThuoc, heSoLuong,hinhAnh);
        this.phongBanTrucThuoc="Phòng Kinh Doanh";
        this.heSoLuong=(float)5.5;
        this.soSanPham=soSanPham;


    }
}
