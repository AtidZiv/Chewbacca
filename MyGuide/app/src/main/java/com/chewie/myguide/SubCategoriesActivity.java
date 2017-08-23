package com.chewie.myguide;

import android.content.Intent;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SubCategoriesActivity extends AppCompatActivity {

    static final int TITLES_INDEX = 0;
    static final int STEPS_ARRAY_INDEX = 1;
    MediaPlayer mediaPlayer;
    TypedArray arCategoryContent;
    int arTitlesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        int array_id = getIntent().getIntExtra(CategoriesActivity.ARRAY_ID_KEY, R.array.array_widgets);
        arCategoryContent = getResources().obtainTypedArray(array_id);
        arTitlesId = arCategoryContent.getResourceId(TITLES_INDEX, 0);
        final String[] titles = getResources().getStringArray(arTitlesId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        ListView lvSubCategories = (ListView)findViewById(R.id.lvSubCategories);
        lvSubCategories.setAdapter(adapter);
        lvSubCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAudioGuide(position);
            }
        });

        mediaPlayer = MediaPlayer.create(SubCategoriesActivity.this, R.raw.select_sub_category);
        mediaPlayer.start();
    }

    void startAudioGuide(int id)
    {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        int arStepsArrayId = arCategoryContent.getResourceId(STEPS_ARRAY_INDEX, 0);
        TypedArray arStepsArray = getResources().obtainTypedArray(arStepsArrayId);
        int arStepsId = arStepsArray.getResourceId(id, 0);
        TypedArray arSteps = getResources().obtainTypedArray(arStepsId);
        int[] steps = new int[arSteps.length()];
        for(int i=0; i<steps.length; i++) {
            steps[i] = arSteps.getResourceId(i, 0);
        }
        final AudioFloater floater = new AudioFloater(this, steps, SubCategoriesActivity.class);
        floater.Display();
    }
}
