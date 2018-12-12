package com.example.cuong.discover_ahihikara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuong.discover_ahihikara.adapter.MySongAdapter;
import com.example.cuong.discover_ahihikara.adapter.SongAdapter;
import com.example.cuong.discover_ahihikara.model.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFavoriteActivity extends AppCompatActivity {
    private RecyclerView rv_baihatyeuthich;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String SHARED_PREFERENCES_NAME = "fileuser";
    private static String URL_FAVORITE_SONGS = "https://ahihikara.herokuapp.com/api/users/favorite";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle(R.string.my_favorites);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfavorite);
        rv_baihatyeuthich = (RecyclerView) findViewById(R.id.rv_myfavorite);
        getFavoriteSongs();
    }

    private void getFavoriteSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_FAVORITE_SONGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_baihatyeuthich.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_baihatyeuthich.setLayoutManager(mLayoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_baihatyeuthich.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration2 =
                                    new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                            rv_baihatyeuthich.addItemDecoration(itemDecoration2);
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
                            Toast.makeText(MyFavoriteActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(MyFavoriteActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
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
