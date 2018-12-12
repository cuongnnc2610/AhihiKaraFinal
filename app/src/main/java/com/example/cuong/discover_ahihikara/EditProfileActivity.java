package com.example.cuong.discover_ahihikara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuong.discover_ahihikara.controller.AuthController;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnChangePass, btnSaveProfile, btnCancelProfile;
    private RadioGroup radioGR;
    private RadioButton male, female;
    private EditText edtNewName, edtNewAbout;
    public static final String URL_UPDATE_USER="http://ahihikara.herokuapp.com/api/users/update";
    public static final String KEY_NICKNAME="nick_name";
    public static final String KEY_ABOUT="about";
    public static final String KEY_GENDER="gender";
    private String SHARED_PREFERENCES_NAME = "fileuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnChangePass = findViewById(R.id.btn_changePass);
        edtNewAbout = findViewById(R.id.edt_newAbout);
        edtNewName = findViewById(R.id.edt_newName);
        btnSaveProfile = findViewById(R.id.btn_saveProfile);
        btnCancelProfile = findViewById(R.id.btn_cancelProfile);
        radioGR = findViewById(R.id.rdbtngender);
        male = findViewById(R.id.rdmale);
        female = findViewById(R.id.rdfemale);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String nickname = sharedPreferences.getString("nickname","");
        String about = sharedPreferences.getString("about","");
        int gender = sharedPreferences.getInt("gender",0);

        edtNewAbout.setText(about);
        edtNewName.setText(nickname);
        if (gender == 0) {
            female.setChecked(true);
        } else {
            female.setChecked(false);
        }

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this,EditPassActivity.class);
                startActivity(intent);
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thay đổi trên database
                String nickname=edtNewName.getText().toString().trim();
                String about = edtNewAbout.getText().toString().trim();
                String gender="0";
                RadioGroup group= findViewById(R.id.rdbtngender);
                int idchecked = group.getCheckedRadioButtonId();
                switch (idchecked)
                {
                    case R.id.rdmale: gender="1";
                        break;
                    case R.id.rdfemale: gender="0";
                        break;
                }
                if(check_information(nickname, about, idchecked)==true) updateAccount(nickname, about, gender);

            }
        });

        btnCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean check_information(String username, String about, int id) {
        //Kiem tra ten tai khoan hop le

        if (username.isEmpty())
        {Toast.makeText(this, "Bạn chưa nhập đủ nội dung", Toast.LENGTH_SHORT).show();
            return false;}
        if (username.length()>16 ||username.length()<6 )
        {
            edtNewName.requestFocus();
            edtNewName.selectAll();
            Toast.makeText(this, "Tên tài khoản phải từ 6 đến 16 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        //Kiem tra about
        if (about.length()>100 )
        {
            edtNewAbout.requestFocus();
            edtNewAbout.selectAll();
            Toast.makeText(this, "Mô tả không được vượt quá 100 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        //Kiem tra giới tính

        if (id==-1){
            Toast.makeText(this, "Bạn chưa chọn giới tính", Toast.LENGTH_SHORT).show();return false;
        }
        return true;
    }

    public void updateAccount(final String nickName, final String about, final String gender) {

        StringRequest requestLogin = new StringRequest(Request.Method.PUT, URL_UPDATE_USER+ "?"+KEY_NICKNAME+"="+nickName+"&"+KEY_ABOUT+"="+about+"&"+KEY_GENDER+"="+gender,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AuthController auth = new AuthController();
                        auth.saveData(response, EditProfileActivity.this);
                        //Chuyển màn hình về lại profile
                        Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        Toast.makeText(EditProfileActivity.this, "Trang cá nhân đã được cập nhập", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }) {

            /**
             * set header
             * */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token","");
                headers.put("Authorization", token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(requestLogin);
    }
}
