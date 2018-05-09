package com.example.helpme;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String lat;
    public String lng;
    public String time;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String lat, String lng, String time) {
        this.uid = uid;
        this.author = author;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("time", time);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
