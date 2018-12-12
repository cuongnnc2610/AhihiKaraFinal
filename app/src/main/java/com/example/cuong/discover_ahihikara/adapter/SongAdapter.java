package com.example.cuong.discover_ahihikara.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuong.discover_ahihikara.LoginActivity;
import com.example.cuong.discover_ahihikara.R;
import com.example.cuong.discover_ahihikara.VideoSinging;
import com.example.cuong.discover_ahihikara.controller.AuthController;
import com.example.cuong.discover_ahihikara.controller.ImageLoadTask;
import com.example.cuong.discover_ahihikara.model.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private String URL_FAVORITE = "https://ahihikara.herokuapp.com/api/songs/{id}/favorite";
    private String SHARED_PREFERENCES_NAME = "fileuser";
    private ArrayList<Song> songs;
    private Context context;
    private int resource;

    public SongAdapter(Context context, int resource, ArrayList<Song> songs) {
        this.context = context;
        this.resource = resource;
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(resource, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        new ImageLoadTask(songs.get(i).getSingerImageURL(), viewHolder.iconSong).execute();
        String name = songs.get(i).getName();
        if (name.length() > 15) {
            name = name.substring(0, 12) + "...";
        }
        viewHolder.nameSong.setText(name);
        viewHolder.singerSong.setText(songs.get(i).getSinger());
        try {
            if (songs.get(i).getIsFavirote() == true) {
                viewHolder.btnlike.setText("❤");
            } else {
                viewHolder.btnlike.setText("♡");
                viewHolder.btnlike.setTextColor(Color.WHITE);
            }
            viewHolder.tvLike.setText(String.valueOf(songs.get(i).getFavoritesCount()));
            viewHolder.btnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int method = Request.Method.POST;
                    String isFavorite = viewHolder.btnlike.getText().toString();
                    if(isFavorite == "❤") {
                        method = Request.Method.DELETE;
                    }

                    int id = songs.get(i).getID();
                    favorite(method, viewHolder.btnlike, viewHolder.tvLike, id);
                }
            });
        } catch (Exception e) {}
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent sing = new Intent(view.getContext(), VideoSinging.class);
                sing.putExtra("song",songs.get(position).getName());
                sing.putExtra("singer",songs.get(position).getSinger());
                sing.putExtra("urlSong",songs.get(position).getPlayURL());
                view.getContext().startActivity(sing);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView iconSong;
        TextView nameSong, singerSong;
        TextView btnlike;
        TextView tvLike;
        private ItemClickListener itemClickListener;
        public ViewHolder(View itemView) {
            super(itemView);
            iconSong = itemView.findViewById(R.id.iconSong);
            nameSong = itemView.findViewById(R.id.nameSong);
            singerSong = itemView.findViewById(R.id.singerSong);
            btnlike = itemView.findViewById(R.id.btn_like);
            tvLike = itemView.findViewById(R.id.likeNumber);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
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
                                btnLike.setTextColor(Color.WHITE);
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

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }

}
