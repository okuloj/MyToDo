package com.example.mytodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView listViewCongViec;
    ArrayList<CongViec> arrayList;
    CongViecAdapter congViecAdapter;
    String getData = "https://mytododemo.000webhostapp.com/getToDo.php";
    String URLgetData = "https://mytododemo.000webhostapp.com/getData.php";
    String URLDelete = "https://mytododemo.000webhostapp.com/delete.php";
    String UserId = "";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCongViec = findViewById(R.id.listViewCongViec);
        arrayList = new ArrayList<>();
        congViecAdapter = new CongViecAdapter(this, R.layout.dong_cong_viec, arrayList);
        listViewCongViec.setAdapter(congViecAdapter);

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        if(UserId=="") {
            UserId = sharedPreferences.getString("userid", "");
        }

        if(UserId != "") {
            getData(UserId);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        //ReadJSON(getData);
        }

//        private void ReadJSON(String url) {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//                @Override
//                public void onResponse(JSONArray response) {
//                    arrayList.clear();
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            JSONObject object = response.getJSONObject(i);
//                            arrayList.add(new CongViec(object.getInt("Id"), object.getInt("UserId"), object.getString("Title"), object.getString("Time")));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    congViecAdapter.notifyDataSetChanged();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            requestQueue.add(jsonArrayRequest);
//        }

        private void getData(String userId) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLgetData, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    arrayList.clear();
                    JSONArray json = null;
                    try {
                        json = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            JSONObject object = json.getJSONObject(i);
                            arrayList.add(new CongViec(object.getInt("Id"), object.getInt("UserId"), object.getString("Title"), object.getString("Time")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    congViecAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", userId);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

        public void DeleteCongViec(final int idCv){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDelete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.trim().equals("success")) {
                        Toast.makeText(MainActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        getData(UserId);
                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi Xóa!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Xảy ra lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idDelete", String.valueOf(idCv));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menuAddCV){
            //startActivity(new Intent(MainActivity.this, AddCongViec.class));
            Intent intent = new Intent(MainActivity.this, AddCongViec.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.menuDangXuat){
            AlertDialog.Builder dialogDangXuat = new AlertDialog.Builder(this);
            dialogDangXuat.setMessage("Bạn có muốn đăng xuất không?");
            dialogDangXuat.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("userid");
                    editor.remove("username");
                    editor.remove("pass");
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
            dialogDangXuat.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogDangXuat.show();
        }

        return super.onOptionsItemSelected(item);
    }
}