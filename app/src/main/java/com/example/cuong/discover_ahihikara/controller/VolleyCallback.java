package com.example.cuong.discover_ahihikara.controller;

import com.example.cuong.discover_ahihikara.MainActivity;
import com.example.cuong.discover_ahihikara.model.Song;

import java.util.ArrayList;

public interface VolleyCallback {
    ArrayList<Song> onSuccess(ArrayList<Song> songs);
}
