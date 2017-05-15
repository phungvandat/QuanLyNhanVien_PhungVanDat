package phungvandat.model.model;

/**
 * Created by PHUNGVANDAT on 3/10/2017.
 */
public class NhanVienNhanSu extends NhanVien {
    private int gioLamViec;

    public int getGioLamViec() {
        return gioLamViec;
    }

    public void setGioLamViec(int gioLamViec) {
        this.gioLamViec = gioLamViec;
    }

    public NhanVienNhanSu(String maNhanVien, String hoTen, int ngaySinh, int thangSinh, int namSinh,
                          String gioiTinh, int soCMND, String soPhone, String email, String danToc,
                          String nguyenQuan, String hoKhau, String tonGiao, String tinhTrangHonNhan,
                          String phongBanTrucThuoc, float heSoLuong,byte[] hinhAnh, int gioLamViec) {
        super(maNhanVien, hoTen, ngaySinh, thangSinh, namSinh, gioiTinh, soCMND, soPhone, email,
                danToc, nguyenQuan, hoKhau, tonGiao, tinhTrangHonNhan, phongBanTrucThuoc, heSoLuong, hinhAnh);
        this.phongBanTrucThuoc="Phòng Nhân Sự";
        this.heSoLuong=(float)4.5;
        this.gioLamViec=gioLamViec;

    }
}
