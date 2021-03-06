package com.example.classify_video.Util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.classify_video.Classifier.Classifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    public static Map<String,Float> div_Map(Map<String, Float> map, int n){
        for(String str:map.keySet()){
            map.put(str,map.get(str)/n);
        }
        return map;
    }
    public static Map<String,Float> resetMap(Map<String, Float> map){
        for(String str:map.keySet()){
            map.put(str,0f);
        }
        return map;
    }
    public static String getRealPathFromURI(Activity activity,Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void Classify(Classifier classifier, final Bitmap bitmap, Map<String, Float> map) {
        List<Classifier.Recognition> results = classifier.recognizeImage(bitmap, 0);
//        Log.d("hoa", "thread "+Thread.currentThread().getName()+"task "+ taskID +" : " + results);
        String classes = "";
        float prob = 0f;
        //tăng % của các class sau mỗi frame
        for (Classifier.Recognition rec : results) {
            classes = rec.getTitle();
            prob = map.get(classes);
            map.replace(classes, prob, prob + rec.getConfidence() * 100);
        }

    }
}
