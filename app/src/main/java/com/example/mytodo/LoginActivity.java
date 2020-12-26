package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserName, editTextPass;
    Button btnLogin;
    TextView textViewRegister;
    String urlLogin = "https://mytododemo.000webhostapp.com/checkLogin.php";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnhXa();
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextUserName.equals("")||editTextPass.equals("")){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    CheckLogin(urlLogin);
                }
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        editTextUserName = findViewById(R.id.editTextLoginName);
        editTextPass = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewLogister);
    }

    private void CheckLogin(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("error")) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String userid = String.valueOf(json.getInt("UserID"));
                                String username = json.getString("Username");
                                String pass = json.getString("Password");

                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!" , Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userid", userid);
                                editor.putString("username", username);
                                editor.putString("pass", pass);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", editTextUserName.getText().toString().trim());
                params.put("pass", editTextPass.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}