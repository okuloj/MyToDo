package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText editTexRegisterUsername, editTextRegisterPass, editTextRegisterRepass;
    Button buttonRegister;
    TextView textViewReturnLogin;
    String url = "https://mytododemo.000webhostapp.com/insertUser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AnhXa();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((editTexRegisterUsername.getText().toString().trim().equals("")||editTextRegisterPass.getText().toString().trim().equals("")||editTextRegisterRepass.getText().toString().trim().equals(""))
                || !editTextRegisterPass.getText().toString().trim().equals(editTextRegisterRepass.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "Thông tin nhập vào không chính xác!", Toast.LENGTH_SHORT).show();
                } else {
                    ThemUser(url);
                }
            }
        });

        textViewReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void AnhXa(){
        editTexRegisterUsername = findViewById(R.id.editTextRegisterName);
        editTextRegisterPass    = findViewById(R.id.editTextRegisterPassword);
        editTextRegisterRepass  = findViewById(R.id.editTextRegisterPassword2);
        buttonRegister          = findViewById(R.id.buttonRegister);
        textViewReturnLogin     = findViewById(R.id.textViewReturnLogin);
    }

    private void ThemUser(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
                else if(response.trim().equals("datontai")) {
                    Toast.makeText(RegisterActivity.this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", editTexRegisterUsername.getText().toString().trim());
                params.put("pass", editTextRegisterPass.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}