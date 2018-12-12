package com.example.cuong.discover_ahihikara.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuong.discover_ahihikara.R;
import com.example.cuong.discover_ahihikara.controller.ImageLoadTask;
import com.example.cuong.discover_ahihikara.model.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongBookAdapter extends BaseAdapter {

    private String URL_FAVORITE = "https://ahihikara.herokuapp.com/api/songs/{id}/favorite";
    private String SHARED_PREFERENCES_NAME = "fileuser";
    private List<Song> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    ViewHolder holder;

    public SongBookAdapter(Context aContext, ArrayList<Song> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_song, null);
            holder = new ViewHolder();
            holder.imageNameView =  convertView.findViewById(R.id.iconSong);
            holder.songNameView =  convertView.findViewById(R.id.nameSong);
            holder.singerView =  convertView.findViewById(R.id.singerSong);
            holder.btnlike = convertView.findViewById(R.id.btn_like);
            holder.tvLike = convertView.findViewById(R.id.likeNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = this.listData.get(position);
        holder.songNameView.setText(song.getName());
        holder.singerView.setText(song.getSinger());
        try {
            if (listData.get(position).getIsFavirote() == true) {
                holder.btnlike.setText("❤");
            } else {
                holder.btnlike.setText("♡");
            }
            holder.tvLike.setText(String.valueOf(listData.get(position).getFavoritesCount()));
            holder.btnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int method = Request.Method.POST;
                    String isFavorite = holder.btnlike.getText().toString();
                    if(isFavorite == "❤") {
                        method = Request.Method.DELETE;
                        holder.btnlike.setText("♡");
                    }
                    int id = listData.get(position).getID();
                    favorite(method, holder.btnlike, holder.tvLike, id);
                }
            });
        } catch (Exception e) {
        }
        //new LoadImageInternet().execute(song.getSingerImageURL());
        new ImageLoadTask(listData.get(position).getSingerImageURL(), holder.imageNameView).execute();
        String name = listData.get(position).getName();
        holder.songNameView.setText(name);
        holder.singerView.setText(listData.get(position).getSinger());

        try {
            holder.tvLike.setText(String.valueOf(listData.get(position).getFavoritesCount()));
        } catch (Exception e) {}
        return convertView;
    }

    private class LoadImageInternet extends AsyncTask<String, Void, Bitmap>{
        Bitmap bitmapHinh = null;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmapHinh = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmapHinh;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            holder.imageNameView.setImageBitmap(bitmap);
        }
    }

    public void favorite(int method, final TextView btnLike, final TextView tvLike, int id){
        StringRequest request = new StringRequest(method, URL_FAVORITE.replace("{id}", String.valueOf(id)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Boolean isFavorite = json.getBoolean("is_favorite");
                            int  count = json.getInt("count");
                            if (isFavorite == true) {
                                btnLike.setText("❤");
                            } else {
                                btnLike.setText("♡");
                            }
                            tvLike.setText(String.valueOf(count));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            /**
             * set header
             * */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token","");
                headers.put("Authorization", token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    static class ViewHolder {
        ImageView imageNameView;
        TextView songNameView, singerView;
        TextView btnlike;
        TextView tvLike;
    }

}