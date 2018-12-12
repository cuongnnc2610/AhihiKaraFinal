package com.example.cuong.discover_ahihikara;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuong.discover_ahihikara.adapter.SongAdapter;
import com.example.cuong.discover_ahihikara.adapter.SongBookAdapter;
import com.example.cuong.discover_ahihikara.model.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongBookActivity extends TabActivity implements TabHost.OnTabChangeListener {

    private String URL_LIST_SONGS = "https://ahihikara.herokuapp.com/api/songs/list";
    private String SHARED_PREFERENCES_NAME = "fileuser";

    private static final String LIST1_TAB_TAG = "BÀI HÁT MỚI";
    private static final String LIST2_TAB_TAG = "  BÀI HÁT        YÊU THÍCH";
    private static final String LIST3_TAB_TAG = "VIỆT NAM";
    private static final String LIST4_TAB_TAG = "ÂU MĨ";
    private static final String LIST5_TAB_TAG = "TRỮ TÌNH";

    //private ListView lv1, lv2, lv3, lv4, lv5;
    private RecyclerView rv_baihatmoi, rv_baihatyeuthich, rv_nhacviet, rv_nhacaumy, rv_nhachan;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager layoutManager;
    private String type;

    private TabHost tabHost;
    private static ArrayList<Song> nhacmoi,tatca, viet, aumi, han;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songbook);

        rv_baihatmoi = findViewById(R.id.list1);
        rv_baihatyeuthich = findViewById(R.id.list2);
        rv_nhacviet = findViewById(R.id.list3);
        rv_nhacaumy = findViewById(R.id.list4);
        rv_nhachan = findViewById(R.id.list5);

        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);

        nhacmoi = new ArrayList<Song>();
        tatca = new ArrayList<Song>();
        viet = new ArrayList<Song>();
        aumi = new ArrayList<Song>();
        han = new ArrayList<Song>();

//        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = lv1.getItemAtPosition(position);
//                Song song = (Song) o;
//                Toast.makeText(SongBookActivity.this, "Selected :" + " " + song, Toast.LENGTH_LONG).show();
//            }
//        });
//        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = lv2.getItemAtPosition(position);
//                Song song = (Song) o;
//                Toast.makeText(SongBookActivity.this, "Selected :" + " " + song, Toast.LENGTH_LONG).show();
//            }
//        });
//        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = lv3.getItemAtPosition(position);
//                Song song = (Song) o;
//                Toast.makeText(SongBookActivity.this, "Selected :" + " " + song, Toast.LENGTH_LONG).show();
//            }
//        });
//        lv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = lv4.getItemAtPosition(position);
//                Song song = (Song) o;
//                Toast.makeText(SongBookActivity.this, "Selected :" + " " + song, Toast.LENGTH_LONG).show();
//            }
//        });
//        lv5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = lv5.getItemAtPosition(position);
//                Song song = (Song) o;
//                Toast.makeText(SongBookActivity.this, "Selected :" + " " + song, Toast.LENGTH_LONG).show();
//            }
//        });

        tabHost.addTab(tabHost.newTabSpec(LIST1_TAB_TAG).setIndicator(LIST1_TAB_TAG).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String arg0) {
                return rv_baihatmoi;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec(LIST2_TAB_TAG).setIndicator(LIST2_TAB_TAG).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String arg0) {
                return rv_baihatyeuthich;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec(LIST3_TAB_TAG).setIndicator(LIST3_TAB_TAG).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String arg0) {
                return rv_nhacviet;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec(LIST4_TAB_TAG).setIndicator(LIST4_TAB_TAG).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String arg0) {
                return rv_nhacaumy;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec(LIST5_TAB_TAG).setIndicator(LIST5_TAB_TAG).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String arg0) {
                return rv_nhachan;
            }
        }));

        tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = 270;
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().width = 300;
        tabHost.getTabWidget().getChildAt(2).getLayoutParams().width = 150;
        tabHost.getTabWidget().getChildAt(3).getLayoutParams().width = 100;
        tabHost.getTabWidget().getChildAt(4).getLayoutParams().width = 80;

        Intent intent = this.getIntent();
        this.type= intent.getStringExtra("type");

        if (this.type.equals("1")){
            onTabChanged(LIST1_TAB_TAG);
        }
        else if (this.type.equals("2")){
            onTabChanged(LIST2_TAB_TAG);
        }
        else if (this.type.equals("3")){
            onTabChanged(LIST3_TAB_TAG);
        }
        else if (this.type.equals("4")){
            onTabChanged(LIST4_TAB_TAG);
        }
        else if (this.type.equals("5")){
            onTabChanged(LIST5_TAB_TAG);
        }
    }


    public void onTabChanged(String tabName) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv =  tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {
                    ((TextView) textView).setGravity(Gravity.CENTER);
                }
            }
        }

        if(tabName.equals(LIST1_TAB_TAG)) {
            System.out.println("do 1");
            //lv1.setAdapter(new SongBookAdapter(this, nhacmoi));
            tabHost.setCurrentTabByTag(LIST1_TAB_TAG);
            TextView tv =  tabHost.getCurrentTabView().findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffe500"));
            getNewSongs();
        }
        else if(tabName.equals(LIST2_TAB_TAG)) {
            System.out.println("do 2");
            //lv2.setAdapter(new SongBookAdapter(this, tatca));
            tabHost.setCurrentTabByTag(LIST2_TAB_TAG);
            TextView tv =  tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#ffe500"));
            getFavoriteSongs();
        }
        else if(tabName.equals(LIST3_TAB_TAG)) {
            System.out.println("do 3");
            //lv3.setAdapter(new SongBookAdapter(this, viet));
            tabHost.setCurrentTabByTag(LIST3_TAB_TAG);
            TextView tv =  tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#ffe500"));
            getVietnameseSongs();
        }
        else if(tabName.equals(LIST4_TAB_TAG)) {
            System.out.println("do 4");
            //lv4.setAdapter(new SongBookAdapter(this, aumi));
            tabHost.setCurrentTabByTag(LIST4_TAB_TAG);
            TextView tv =  tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#ffe500"));
            getUSUKSongs();
        }
        else if(tabName.equals(LIST5_TAB_TAG)) {
            System.out.println("do 5");
            //lv5.setAdapter(new SongBookAdapter(this, han));
            tabHost.setCurrentTabByTag(LIST5_TAB_TAG);
            TextView tv =  tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#ffe500"));
            getTruTinhSongs();
        }
    }

    public void returnDiscover(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
    }


    public void getNewSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_SONGS+"?option=0&limit=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_baihatmoi.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_baihatmoi.setLayoutManager(layoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_baihatmoi.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration3 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL);
                            rv_baihatmoi.addItemDecoration(itemDecoration3);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode;
                        statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Toast.makeText(SongBookActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(SongBookActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
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

    public void getFavoriteSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_SONGS+"?option=4&limit=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_baihatyeuthich.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_baihatyeuthich.setLayoutManager(layoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_baihatyeuthich.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration3 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL);
                            rv_baihatyeuthich.addItemDecoration(itemDecoration3);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode;
                        statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Toast.makeText(SongBookActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(SongBookActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
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

    public void getVietnameseSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_SONGS+"?option=1&limit=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_nhacviet.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_nhacviet.setLayoutManager(layoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_nhacviet.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration3 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL);
                            rv_nhacviet.addItemDecoration(itemDecoration3);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode;
                        statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Toast.makeText(SongBookActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(SongBookActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
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

    public void getUSUKSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_SONGS+"?option=2&limit=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_nhacaumy.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_nhacaumy.setLayoutManager(layoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_nhacaumy.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration3 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL);
                            rv_nhacaumy.addItemDecoration(itemDecoration3);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode;
                        statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Toast.makeText(SongBookActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(SongBookActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
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

    public void getTruTinhSongs() {
        final ArrayList<Song> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_SONGS+"?option=3&limit=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonListSong = new JSONObject(response);
                            ArrayList<Song> list = Song.parseSongs(jsonListSong);
                            rv_nhachan.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_nhachan.setLayoutManager(layoutManager);
                            mAdapter = new SongAdapter(getApplicationContext(), R.layout.item_song, list);
                            rv_nhachan.setAdapter(mAdapter);
                            RecyclerView.ItemDecoration itemDecoration3 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL);
                            rv_nhachan.addItemDecoration(itemDecoration3);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int statusCode;
                        statusCode = error.networkResponse.statusCode;
                        if (statusCode == 401) {
                            Toast.makeText(SongBookActivity.this, "Need login first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                        } else {
                            Toast.makeText(SongBookActivity.this, "Something was error, please try again", Toast.LENGTH_SHORT).show();
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
