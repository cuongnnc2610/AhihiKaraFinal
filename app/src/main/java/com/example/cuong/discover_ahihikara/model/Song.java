package com.example.cuong.discover_ahihikara.model;

import android.net.Uri;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Song {
    private int id;
    private String name;
    private String playURL;
    private String singer;
    private String singerImageURL;
    private String category;
    private Boolean isFavorite;
    private int favoritesCount;

    public Song(int id, String name, String playURL, String singer, String singerImageURL, String category, Boolean isFavorite, int favoritesCount) {
        this.id = id;
        this.name = name;
        this.playURL = playURL;
        this.singer = singer;
        this.singerImageURL = singerImageURL;
        this.category = category;
        this.isFavorite = isFavorite;
        this.favoritesCount = favoritesCount;
    }

    public int getID() {
        return id;
    }

    public void setIcon(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayURL() {
        return playURL;
    }

    public void setPlayURL(String playURL) {
            this.playURL = playURL;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSingerImageURL() {
        return singerImageURL;
    }

    public void setSingerImageURLURL(String singerImageURL) {
        this.singerImageURL = singerImageURL;
    }

    public Boolean getIsFavirote() {
        return isFavorite;
    }

    public void setIsFavirote(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public static ArrayList<Song> parseSongs(JSONObject jsonListSong) {
        ArrayList<Song> list = new ArrayList<Song>();
        try {
            JSONArray jsonArrayListSong = jsonListSong.getJSONArray("list_song");
            for (int i = 0; i<jsonArrayListSong.length(); i++) {
                JSONObject jsonSong = jsonArrayListSong.getJSONObject(i);
                int id = jsonSong.getInt("id");
                String name = jsonSong.getString("name");
                String playURL = jsonSong.getString("play_url");
                String singer = jsonSong.getString("singer");
                String singerImageURL = jsonSong.getString("singer_image_url");
                String category = jsonSong.getString("category");
                Boolean isFavorite = jsonSong.getBoolean("is_favorite");
                int favoritesCount = jsonSong.getInt("favorites_count");
                list.add(new Song(id, name, playURL, singer, singerImageURL, category, isFavorite, favoritesCount));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
