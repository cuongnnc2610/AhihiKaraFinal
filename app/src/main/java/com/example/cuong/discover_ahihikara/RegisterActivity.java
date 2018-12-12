package com.example.cuong.discover_ahihikara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edituser,editpassword,editrepass;
    Button buttonsignup, buttonback;
    public static final String URL_REGISTER="http://ahihikara.herokuapp.com/api/auth/register";
    public static final String KEY_USERNAME="user_name";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_GENDER="gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edituser=findViewById(R.id.edtuser);
        editpassword=findViewById(R.id.edtpass);
        editrepass=findViewById(R.id.edtrepass);
        buttonsignup=findViewById(R.id.btnsignup);
        buttonback = findViewById(R.id.backtosign);
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username=edituser.getText().toString().trim();
                String password=editpassword.getText().toString().trim();
                String repassword=editrepass.getText().toString().trim();
                String gender= "0";
                RadioGroup group= findViewById(R.id.rdbtngender);
                int idchecked=group.getCheckedRadioButtonId();
                switch (idchecked)
                {
                    case R.id.rdmale: gender = "1";
                        break;
                }
                if(check_information(username,password,repassword,idchecked)==true) registerAccount(username,password,gender);

            }
        });
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean check_information(String username,String password,String repassword,int id) {
        //Kiem tra ten tai khoan hop le

        if (username.isEmpty() ||  password.isEmpty() || repassword.isEmpty())
        {Toast.makeText(this, "Bạn chưa nhập đủ nội dung", Toast.LENGTH_SHORT).show();
            return false;}
        if (username.length()>16 ||username.length()<6 )
        {
            edituser.requestFocus();
            edituser.selectAll();
            Toast.makeText(this, "Tên tài khoản phải từ 6 đến 16 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        //Kiem tra mat khau
        if (password.length()>16 || password.length()<6)
        {
            Toast.makeText(this, "Mật khẩu phải từ 6 đến 16 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.equals(repassword)==false)
        {
            editpassword.requestFocus();
            editrepass.requestFocus();
            editpassword.selectAll();
            editrepass.selectAll();
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (id==-1){
            Toast.makeText(this, "Bạn chưa chọn giới tính", Toast.LENGTH_SHORT).show();return false;
        }
        return true;
    }

    public void registerAccount(final String userName, final String password,final String gender) {



        StringRequest requestLogin = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(RegisterActivity.this ,LoginActivity.class);
                        startActivity(intent);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            /**
             * set paramater
             * */
            @Override
            protected Map getParams() {
                Map params = new HashMap<>();
                params.put(KEY_USERNAME, userName);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_GENDER, gender);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(requestLogin);
    }
}
