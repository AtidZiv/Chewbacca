package com.chewie.myguide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SubCategoriesActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    String chosenAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        final String[] chooseAction = getResources().getStringArray(R.array.array_subcategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chooseAction);
        ListView lvSubCategories = (ListView)findViewById(R.id.lvSubCategories);
        lvSubCategories.setAdapter(adapter);
        lvSubCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenAction = chooseAction[position];
                onExplanation(view);
            }
        });

        mediaPlayer = MediaPlayer.create(SubCategoriesActivity.this, R.raw.select_sub_category);
        mediaPlayer.start();
    }

    public void onExplanation(View view)
    {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        Intent intent = new Intent(this, Explanation.class);
        intent.putExtra("action", chosenAction);
        startActivity(intent);
        finish();
    }
}
