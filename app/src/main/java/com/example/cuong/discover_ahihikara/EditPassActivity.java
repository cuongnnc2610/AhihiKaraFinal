package com.example.cuong.discover_ahihikara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditPassActivity extends AppCompatActivity {
    private Button btnSavePass, btnCancelPass;
    private EditText edtOldPass, edtNewPass, edtNewPass2;
    public static final String URL_UPDATE_USER="http://ahihikara.herokuapp.com/api/users/update";
    public static final String KEY_PASSWORD="password";
    private String SHARED_PREFERENCES_NAME = "fileuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);

        btnSavePass = findViewById(R.id.btn_savePass);
        btnCancelPass = findViewById(R.id.btn_cancelPass);
        edtOldPass = findViewById(R.id.edt_oldPass);
        edtNewPass = findViewById(R.id.edt_newPass);
        edtNewPass2 = findViewById(R.id.edt_newPass2);

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = edtNewPass.getText().toString();
                String newPass2 = edtNewPass2.getText().toString();
                if (checkPass(newPass, newPass2)) updatePass(newPass);

            }
        });

        btnCancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPassActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updatePass(final String pass) {

        StringRequest requestLogin = new StringRequest(Request.Method.PUT, URL_UPDATE_USER+ "?"+KEY_PASSWORD+"="+pass,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AuthController auth = new AuthController();
                        auth.saveData(response, EditPassActivity.this);
                        Intent intent = new Intent(EditPassActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                        Toast.makeText(EditPassActivity.this, "Bạn đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditPassActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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

    public boolean checkPass(String pass1, String pass2)
    {
        if ((pass1.equals(pass2)) == false)
        {
            Toast.makeText(EditPassActivity.this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((pass1.isEmpty()) || (pass2.isEmpty()))
        {
            Toast.makeText(EditPassActivity.this, "Mật khẩu mới không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
