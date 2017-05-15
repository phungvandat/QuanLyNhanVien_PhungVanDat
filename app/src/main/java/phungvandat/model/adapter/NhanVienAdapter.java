package phungvandat.model.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phungvandat.quanlynhanvien.HienThiThongTinActivity;
import com.example.phungvandat.quanlynhanvien.R;
import com.example.phungvandat.quanlynhanvien.SuaChuaThongTinActivity;
import com.example.phungvandat.quanlynhanvien.XoaNhanVienActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import phungvandat.model.model.NhanVien;

/**
 * Created by PHUNGVANDAT on 3/10/2017.
 */
public class NhanVienAdapter extends ArrayAdapter<NhanVien> {


    Activity context;
    int resource;
    List<NhanVien> objects;
    ArrayList<NhanVien> dsNhanVien;
    int quyenTruyCap=1;


    public NhanVienAdapter(Activity context, int resource, List<NhanVien> objects, int quyenTruyCap) {
        super(context, resource, objects);
        this.context= context;
        this.resource= resource;
        this.objects= objects;
        this.dsNhanVien= new ArrayList<>();
        this.dsNhanVien.addAll(objects);
        this.quyenTruyCap=quyenTruyCap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);

        ImageView imgAvatar= (ImageView) row.findViewById(R.id.imgAvatar);
        TextView txtMsnv= (TextView) row.findViewById(R.id.txtMsnv);
        TextView txtHoTen= (TextView) row.findViewById(R.id.txtHoTen);
        ImageButton btnInfo= (ImageButton) row.findViewById(R.id.btnInfo);
        ImageButton btnUpdate= (ImageButton) row.findViewById(R.id.btnUpdate);
        final ImageButton btnDelete= (ImageButton) row.findViewById(R.id.btnDelete);

        final NhanVien nhanVien=this.objects.get(position);
        txtHoTen.setText(nhanVien.getHoTen());
        txtMsnv.setText(nhanVien.getMaNhanVien());
        if(nhanVien.getHinhAnh()==null)
            imgAvatar.setImageResource(R.drawable.imgavatar);
        else {
            Bitmap bmImage = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            imgAvatar.setImageBitmap(bmImage);
        }
        if(quyenTruyCap!=1){
            btnUpdate.setBackgroundResource(R.drawable.item_blurry);
            btnDelete.setBackgroundResource(R.drawable.item_blurry);
        }

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getContext(),HienThiThongTinActivity.class);
                i.putExtra("KIEU_NHAN_VIEN",nhanVien);
                getContext().startActivity(i);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quyenTruyCap==1) {
                    Intent i = new Intent(getContext(), SuaChuaThongTinActivity.class);
                    i.putExtra("KIEU_NHAN_VIEN", nhanVien);
                    getContext().startActivity(i);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quyenTruyCap==1) {
                    Intent i = new Intent(getContext(), XoaNhanVienActivity.class);
                    i.putExtra("KIEU_NHAN_VIEN", nhanVien);
                    getContext().startActivity(i);
                }
            }
        });



        return row;
    }

    public void filter(String noiDungAuto){
        noiDungAuto= noiDungAuto.toLowerCase(Locale.getDefault());
        objects.clear();
        if (noiDungAuto.length()==0) {
            objects.addAll(dsNhanVien);
        }
        else {
            for (NhanVien nv: dsNhanVien){
                if (nv.getMaNhanVien().toLowerCase(Locale.getDefault()).contains(noiDungAuto) ||
                        nv.getHoTen().toLowerCase(Locale.getDefault()).contains(noiDungAuto))
                {
                    objects.add(nv);
                }
            }
        }
        notifyDataSetChanged();
    }
}
