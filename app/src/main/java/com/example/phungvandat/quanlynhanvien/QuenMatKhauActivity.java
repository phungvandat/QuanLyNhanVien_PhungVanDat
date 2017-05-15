package com.example.phungvandat.quanlynhanvien;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class QuenMatKhauActivity extends Activity {

    TextView txtNoiDung;
    EditText txtMSNV;
    Button btnGui;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    private static final int PERMISSION_REQUEST_CODE = 1;

    int matKhauMoi = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        xuLySaoChepCSDLTuAsssetsVaoHeThongMobile();
        addControls();
        addEventts();
    }

    private void addEventts() {
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyGui();
            }
        });
    }

    private void xuLyGui() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor ckt = null;
        try {
            ckt = database.rawQuery("select MSNV from NhanVien", null);
            boolean checkTonTai = false;
            while (ckt.moveToNext()) {
                if (String.valueOf(ckt.getInt(0)).equals(txtMSNV.getText().toString())) {
                    checkTonTai = true;
                    break;
                }
            }
            if (checkTonTai == false) {
                Toast.makeText(this, "Không tồn tại nhân viên " + txtMSNV.getText().toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            ckt = database.rawQuery("select MSNV_TK from TaiKhoanNhanVien where MSNV_TK=" + txtMSNV.getText().toString(), null);
            int check = 0;
            while (ckt.moveToNext()) {
                check++;
            }
            if (check == 0) {
                Toast.makeText(this, "Tài khoản nhân viên " + txtMSNV.getText().toString() + " không tồn tại," +
                        " vui lòng đăng kí mới", Toast.LENGTH_SHORT).show();
                return;
            }

            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select Phone from NhanVien where MSNV="
                    + txtMSNV.getText().toString(), null);
            String soDienThoaiNhanVien = null;
            while (cursor.moveToNext())
                soDienThoaiNhanVien = cursor.getString(0);
            Random r = new Random();
            matKhauMoi = 1000 + r.nextInt(8999);

            //lấy mặc định SmsManager
            final SmsManager sms = SmsManager.getDefault();
            Intent msgSent = new Intent("ACTION_MSG_SENT");
            //Khai báo pendingintent để kiểm tra kết quả
            final PendingIntent pendingMsgSent =
                    PendingIntent.getBroadcast(this, 0, msgSent, 0);
            registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int result = getResultCode();
                    String msg = "Send OK";
                    if (result != Activity.RESULT_OK) {
                        msg = "Send failed";
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("MatKhau", String.valueOf(matKhauMoi));
                        String[] dieuKien = {String.valueOf(txtMSNV.getText().toString())};
                        int ret = database.update("TaiKhoanNhanVien", values, "MSNV_TK=?", dieuKien);
                    }
                    Toast.makeText(QuenMatKhauActivity.this, msg, Toast.LENGTH_LONG).show();

                }
            }, new IntentFilter("ACTION_MSG_SENT"));
            //Gọi hàm gửi tin nhắn đi
            sms.sendTextMessage(soDienThoaiNhanVien, null, ("Mật khẩu mới của bạn là: ").toString() + String.valueOf(matKhauMoi).toString() + "",
                    pendingMsgSent, null);

        } catch (Exception ex) {

        }
    }

    private void addControls() {
        txtNoiDung = (TextView) findViewById(R.id.txtNoiDung);
        txtMSNV = (EditText) findViewById(R.id.txtMSNV);
        btnGui = (Button) findViewById(R.id.btnGui);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void xuLySaoChepCSDLTuAsssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAcssets();
                Toast.makeText(this, "Sao chep database thanh cong", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyDataBaseFromAcssets() {
        try {

            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);

            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception ex) {
            Log.e("Loi sao chep", ex.toString());

        }
    }

    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}
