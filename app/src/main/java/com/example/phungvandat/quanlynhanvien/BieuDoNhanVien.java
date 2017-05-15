package com.example.phungvandat.quanlynhanvien;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BieuDoNhanVien extends AppCompatActivity {

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;


    private static String TAG = "MainActivity";
    private float[] yData;
    private String[] xData;
    PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bieu_do_nhan_vien);
        xuLySaoChepCSDLTuAsssetsVaoHeThongMobile();
        addControls();
        addEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ve_bieu_do, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        xuLyOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
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

    private void addControls() {
        setTitle("BIỂU ĐỒ NHÂN VIÊN");
        bieuDoPhongBan();
    }

    private void addEvents() {
    }

    private void bieuDoPhongBan() {

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        try {
            Cursor cTong = database.rawQuery("select * from NhanVien", null);
            float tong = cTong.getCount();
            xData = new String[]{"Tài Chính", "Kỹ Thuật", "Nhân Sự", "Kinh Doanh"};
            Cursor cTaiChinh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Tài Chính'", null);
            Cursor cKyThuat = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kỹ Thuật'", null);
            Cursor cNhanSu = database.rawQuery("select * from NhanVien where PhongBan='Phòng Nhân Sự'", null);
            Cursor cKinhDoanh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kinh Doanh'", null);
            float phanTramTaiChinh = ((float) cTaiChinh.getCount() / tong) * 100;
            float phanTramKyThuat = ((float) cKyThuat.getCount() / tong) * 100;
            float phanTramNhanSu = ((float) cNhanSu.getCount() / tong) * 100;
            float phanTramKinhDoanh = ((float) cKinhDoanh.getCount() / tong) * 100;

            yData = new float[]{phanTramTaiChinh, phanTramKyThuat, phanTramNhanSu, phanTramKinhDoanh};
        } catch (Exception ex) {
        }

        Log.d(TAG, "onCreate: starting on create chart");
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setDescription("Biểu đồ nhân viên");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("PHÒNG BAN");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        addDataSet();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == Float.parseFloat(sales)) {
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(BieuDoNhanVien.this, "Phòng " + employee + "\n" + "Chiếm: " + sales + "% tổng số nhân viên", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], xData[i]));
        }

        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(new String(xData[i]));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "CHÚ THÍCH");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void xuLyOptionsItemSelected(MenuItem item) {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        try {
            Cursor cTong = database.rawQuery("select * from NhanVien", null);
            float tong = cTong.getCount();
            if (item.getItemId() == R.id.mnuPhongBan) {
                xData = new String[]{"Tài Chính", "Kỹ Thuật", "Nhân Sự", "Kinh Doanh"};
                Cursor cTaiChinh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Tài Chính'", null);
                Cursor cKyThuat = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kỹ Thuật'", null);
                Cursor cNhanSu = database.rawQuery("select * from NhanVien where PhongBan='Phòng Nhân Sự'", null);
                Cursor cKinhDoanh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kinh Doanh'", null);
                float phanTramTaiChinh = ((float) cTaiChinh.getCount() / tong) * 100;
                float phanTramKyThuat = ((float) cKyThuat.getCount() / tong) * 100;
                float phanTramNhanSu = ((float) cNhanSu.getCount() / tong) * 100;
                float phanTramKinhDoanh = ((float) cKinhDoanh.getCount() / tong) * 100;

                yData = new float[]{phanTramTaiChinh, phanTramKyThuat, phanTramNhanSu, phanTramKinhDoanh};
                pieChart.setCenterText("PHÒNG BAN");

            } else if (item.getItemId() == R.id.mnuGioiTinh) {
                xData = new String[]{"Nam", "Nữ"};
                Cursor cNam = database.rawQuery("Select * from NhanVien where GioiTinh='Nam'", null);
                Cursor cNu = database.rawQuery("Select * from NhanVien where GioiTinh='Nữ'", null);
                float phanTramNam = ((float) cNam.getCount() / tong) * 100;
                float phanTramNu = ((float) cNu.getCount() / tong) * 100;

                yData = new float[]{phanTramNam, phanTramNu};
                pieChart.setCenterText("GIỚI TÍNH");

            } else if (item.getItemId() == R.id.mnuHonNhan) {
                xData = new String[]{"Đã kết hôn", "Chưa kết hôn", "Không rõ"};
                Cursor cDaKetHon = database.rawQuery("Select * from NhanVien where HonNhan='Đã kết hôn'", null);
                Cursor cChuaKetHon = database.rawQuery("Select * from NhanVien where HonNhan='Chưa kết hôn'", null);
                Cursor cKhongRo = database.rawQuery("Select * from NhanVien where HonNhan='Không rõ'", null);

                float phanTramDaKetHon = ((float) cDaKetHon.getCount() / tong) * 100;
                float phanTramChuaKetHon = ((float) cChuaKetHon.getCount() / tong) * 100;
                float phanTramKhongRo = ((float) cKhongRo.getCount() / tong) * 100;

                yData = new float[]{phanTramDaKetHon, phanTramChuaKetHon, phanTramKhongRo};
                pieChart.setCenterText("HÔN NHÂN");
            }
        } catch (Exception ex) {
        }

        Log.d(TAG, "onCreate: starting on create chart");
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setDescription("Biểu đồ nhân viên");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        addDataSet();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == Float.parseFloat(sales)) {
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(BieuDoNhanVien.this, "Phòng " + employee + "\n" + "Chiếm: " + sales + "% tổng số nhân viên", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}
