package com.example.cuong.discover_ahihikara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuong.discover_ahihikara.adapter.SongAdapter;
import com.example.cuong.discover_ahihikara.controller.ImageLoadTask;
import com.example.cuong.discover_ahihikara.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private Button btnEditUser, btnSetting, btnBack;
    private TextView tvNameUser, tvAboutUser, tvUserName, tvGender;
    private ImageView imgView;
    private Button favoriteSongs;
    private final String URL_USER = "http://ahihikara.herokuapp.com/api/users/edit";
    private String SHARED_PREFERENCES_NAME = "fileuser";
    private static String URL_FAVORITE_SONGS = "https://ahihikara.herokuapp.com/api/users/favorite";

    private RecyclerView rv_baihatuseryeuthich;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnEditUser = findViewById(R.id.btn_editUser);
        btnSetting = findViewById(R.id.btn_setting);
        btnBack = findViewById(R.id.btn_back);
        tvNameUser = findViewById(R.id.tv_nameUser);
        tvAboutUser = findViewById(R.id.tv_aboutUser);
        tvUserName = findViewById(R.id.tv_user_name);
        imgView = findViewById(R.id.imv_profilePhoto);
        //tvGender = findViewById(R.id.tv_gender);
        favoriteSongs = findViewById(R.id.btn_baihatdathich);
        rv_baihatuseryeuthich = findViewById(R.id.rv_baihatuseryeuthich);

        getUser();
        getFavoriteSongs();
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        favoriteSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfileActivity.this, MyFavoriteActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getUser()
    {
        StringRequest request = new StringRequest(Request.Method.GET, URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user = jsonObject.getJSONObject("user");
                    String username = user.getString("user_name");
                    String nickname = user.getString("nick_name");
                    String about = user.getString("about");
                    String avatar = user.getString("avatar");
                    int gender = user.getInt("gender");
                    tvUserName.setText(username);
                    tvNameUser.setText(nickname);
                    /*if (gender == 0) {
                        tvGender.setText("Nữ");
                    } else {
                        tvGender.setText("Nam");
                    }*/
                    if (about == "null") tvAboutUser.setText("Chưa có mô tả cá nhân");
                    else tvAboutUser.setText(about);
                    new ImageLoadTask(avatar, imgView).execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401){
                            Toast.makeText(ProfileActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
        }) {
            /**
             * set header
             * */
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token","");
                headers.put("Authorization", token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void getFavoriteSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_FAVORITE_SONGS+"?limit=5",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_baihatuseryeuthich.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_baihatuseryeuthich.setLayoutManager(mLayoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_baihatuseryeuthich.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration2 =
                                    new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                            rv_baihatuseryeuthich.addItemDecoration(itemDecoration2);
//                            MySongAdapter mySongAdapter = new MySongAdapter(MyFavoriteActivity.this,R.layout.item_song,list);
//                            lvMySong.setAdapter(mySongAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Toast.makeText(ProfileActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
                        }
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
        queue.add(request);
    }
}
