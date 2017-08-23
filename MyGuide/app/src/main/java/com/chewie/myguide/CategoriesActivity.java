package com.chewie.myguide;

import android.content.Intent;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    public static final String ARRAY_ID_KEY = "array_id";

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        String[] chooseAction = getResources().getStringArray(R.array.array_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chooseAction);
        ListView lvCategories = (ListView)findViewById(R.id.lvCategories);
        lvCategories.setAdapter(adapter);
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSubCatergories((int)id);
            }
        });

        mediaPlayer = MediaPlayer.create(CategoriesActivity.this, R.raw.select_category);
        mediaPlayer.start();
    }

    public void onSubCatergories(int id)
    {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        TypedArray arCats = getResources().obtainTypedArray(R.array.array_categories_ids);
        int resId = arCats.getResourceId(id, 0);
        Intent intent = new Intent(this, SubCategoriesActivity.class);
        intent.putExtra(ARRAY_ID_KEY, resId);
        startActivity(intent);
    }
}
