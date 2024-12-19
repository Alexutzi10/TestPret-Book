package com.example.testprep_carte.network;

import android.util.Log;

import com.example.testprep_carte.data.Carte;
import com.example.testprep_carte.data.DateConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarteParser {
    public static List<Carte> getFromJSON(String json) {
        List<Carte> carti = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);
            JSONObject details = root.getJSONObject("details");
            JSONArray array = details.getJSONArray("datasets");

            for (int i = 0; i < array.length(); i++) {
                JSONObject datasets = array.getJSONObject(i);
                JSONObject carte = datasets.getJSONObject("carte");

                String name = carte.getString("name");
                int pages = carte.getInt("nbPages");
                String dateString = carte.getString("publishDate");

                Date publishDate;
                try {
                    publishDate = DateConverter.toDate(dateString);
                } catch (Exception ex) {
                    Log.e("Parse", "Exception when parsing the date");
                    continue;
                }

                Carte book = new Carte(name, pages, publishDate);
                carti.add(book);
            }
            return carti;
        } catch (JSONException ex) {
            Log.e("Parse", "Exception when parsing the JSON");
        }
        return new ArrayList<>();
    }
}
