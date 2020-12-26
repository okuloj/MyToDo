package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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

public class AddCongViec extends AppCompatActivity {
    EditText editTextTitle, editTextDate, editTextTime;
    Button btnAdd, btnHuy;
    String date = "";
    String urlInsert = "https://mytododemo.000webhostapp.com/insert.php";
    SharedPreferences sharedPreferences;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cong_viec);


        AnhXa();
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        UserId = sharedPreferences.getString("userid", "");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();
                if(title.isEmpty()||date.isEmpty()||time.isEmpty()){
                    Toast.makeText(AddCongViec.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    ThemCongViec(urlInsert);
                }

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
                //Toast.makeText(AddCongViec.this, "adt", Toast.LENGTH_SHORT).show();
            }
        });
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonGio();
            }
        });
    }

    private void ThemCongViec(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(AddCongViec.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCongViec.this, MainActivity.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCongViec.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Lỗi! \n"+ error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("userid", UserId);
                params.put("title", editTextTitle.getText().toString().trim());
                params.put("time", date + " " + editTextTime.getText().toString().trim());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void ChonGio() {
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(calendar.HOUR_OF_DAY);
        int phut = calendar.get(calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0,0,0, hourOfDay, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                editTextTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },gio, phut, true);
        timePickerDialog.show();
    }

    private void AnhXa() {
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTitle = findViewById(R.id.edtAddCV);
        btnAdd = findViewById(R.id.buttonAdd);
        btnHuy = findViewById(R.id.buttonHuy);
    }

    private void ChonNgay(){
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                editTextDate.setText(simpleDateFormat.format(calendar.getTime()));
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                date = simpleDateFormat2.format(calendar.getTime()).toString();
                editTextDate.setText(date);
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
}