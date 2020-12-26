package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateCongviecActivity extends AppCompatActivity {
    EditText editTextTitle, editTextNgay, editTextGio;
    Button buttonUpdate, buttonHuyUpdate;
    int id = 0;
    int ngay, thang, nam, gio , phut;
    String urlUpdate = "https://mytododemo.000webhostapp.com/update.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_congviec);

        Intent intent = getIntent();
        CongViec congViec = (CongViec) intent.getSerializableExtra("dataCongViec");
        //Toast.makeText(this, congViec.getId()+"", Toast.LENGTH_SHORT).show();

        AnhXa();

        id = congViec.getId();
        editTextTitle.setText(congViec.getTitle());
        String Time = congViec.getTime().toString().trim();
        editTextNgay.setText(Time.substring(0, 10));
        editTextGio.setText(Time.substring(11, 16));

        nam = Integer.parseInt(Time.substring(0, 4));
        thang = Integer.parseInt(Time.substring(5, 7));
        ngay = Integer.parseInt(Time.substring(8, 10));

        gio = Integer.parseInt(Time.substring(11, 13));
        phut = Integer.parseInt(Time.substring(14, 16));



        editTextNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
            }
        });

        editTextGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonGio();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title    = editTextTitle.getText().toString().trim();
                String Day      = editTextNgay.getText().toString().trim();
                String Gio      = editTextGio.getText().toString().trim();

                if(Title.equals("")||Day.equals("")||Gio.equals("")) {
                    Toast.makeText(UpdateCongviecActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    CapNhatCongViec(urlUpdate);
                }
            }
        });

        buttonHuyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        editTextTitle = findViewById(R.id.editTextTitleEdit);
        editTextNgay = findViewById(R.id.editTextNgayEdit);
        editTextGio = findViewById(R.id.editTextGioEdit);

        buttonHuyUpdate = findViewById(R.id.buttonEditHuy);
        buttonUpdate = findViewById(R.id.buttonEdit);
    }

    private void CapNhatCongViec(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(UpdateCongviecActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateCongviecActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(UpdateCongviecActivity.this, "Lỗi cập nhật!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateCongviecActivity.this, "Xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("title", editTextTitle.getText().toString().trim());
                params.put("time", editTextNgay.getText().toString().trim() + " " +editTextGio.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ChonGio() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0,0,0, hourOfDay, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                editTextGio.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },gio, phut, true);
        timePickerDialog.show();
    }
    private void ChonNgay(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat2.format(calendar.getTime()).toString();
                editTextNgay.setText(date);
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
}