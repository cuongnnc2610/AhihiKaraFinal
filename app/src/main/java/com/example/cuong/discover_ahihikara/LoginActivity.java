package com.example.cuong.discover_ahihikara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    AuthController auth = new AuthController();
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static EditText edtUserName;
    public static EditText edtPassWord;
    public static JSONObject jsonResponse;
    private Button btnLogin;
    private Button btnRegister;
    public static ProgressDialog pDialog;
    /**
     * URL : URL_LOGIN
     * param : KEY_USERNAME KEY_PASSWORD
     */
    public static final String URL_LOGIN = "https://ahihikara.herokuapp.com/api/auth/login";
    public static final String KEY_USERNAME = "user_name";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        addEvent();
    }

    private void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value input
                String userName = edtUserName.getText().toString().trim();
                String password = edtPassWord.getText().toString().trim();

                // Validation input
                if(checkInput(userName, password))
                    pDialog.show();

                // Call api login
                loginAccount(userName, password);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                Bundle bundle = new Bundle();
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        edtUserName = (EditText) findViewById(R.id.edtuser);
        edtPassWord = (EditText) findViewById(R.id.edtpass);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        btnRegister = findViewById(R.id.btnregister);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang đăng nhập...");
        pDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Method login
     *
     * @param userName
     * @param password result json
     */
    public void loginAccount(final String userName, final String password) {

        if (checkInput(userName, password)) {
            pDialog.show();
            StringRequest requestLogin = new StringRequest(Request.Method.POST, URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            System.out.print(response);
                            Log.d(TAG, response);
                            auth.saveData(response, LoginActivity.this);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            final int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 400) {
                                Toast.makeText(LoginActivity.this, "Login failed, please try again or register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                /**
                 * set paramater
                 * */
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(KEY_USERNAME, userName);
                    params.put(KEY_PASSWORD, password);
                    return params;
                }

                /**
                 * set header
                 * */
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "");
                    return headers;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(requestLogin);
        }
    }

    private boolean checkInput(String userName, String password) {
        if (userName.length() != 0 && password.length() != 0)
            return true;
        if (userName.length() == 0) {
            LoginActivity.edtUserName.setError("Vui lòng nhập dữ liệu!");
        }
        if (password.length() == 0) {
            LoginActivity.edtPassWord.setError("Vui lòng nhập dữ liệu!");
        }
        return false;
    }
}
