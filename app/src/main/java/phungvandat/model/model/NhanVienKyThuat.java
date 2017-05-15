package phungvandat.model.model;

/**
 * Created by PHUNGVANDAT on 3/10/2017.
 */
public class NhanVienKyThuat extends NhanVien {
    private int soProject;

    public int getSoProject() {
        return soProject;
    }

    public void setSoProject(int soProject) {
        this.soProject = soProject;
    }

    public NhanVienKyThuat(String maNhanVien, String hoTen, int ngaySinh, int thangSinh,
                           int namSinh, String gioiTinh, int soCMND, String soPhone, String email,
                           String danToc, String nguyenQuan, String hoKhau, String tonGiao,
                           String tinhTrangHonNhan, String phongBanTrucThuoc, float heSoLuong,byte[] hinhAnh, int soProject) {
        super(maNhanVien, hoTen, ngaySinh, thangSinh, namSinh, gioiTinh, soCMND, soPhone, email,
                danToc, nguyenQuan, hoKhau, tonGiao, tinhTrangHonNhan, phongBanTrucThuoc, heSoLuong, hinhAnh);
        this.phongBanTrucThuoc="Phòng Kỹ Thuật";
        this.heSoLuong=(float)6.5;
        this.soProject=soProject;

    }
}
