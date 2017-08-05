package com.chewie.myguide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    ListView listView;
    SimpleCursorAdapter mAdapter;
    ArrayList<String> chooseAction;
    View row;
    LayoutInflater inflater = getLayoutInflater();
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ArrayAdapter<String> adapter;
       // String [] chooseAction = {"הוספת יישומונים(ויג'טים)", "אנשי קשר", "שימוש בחנות אפליקציות"};
        listView = (ListView)findViewById(R.id.list_view);
        chooseAction = new ArrayList<String>();
        chooseAction.add("הוספת יישומונים(ויג'טים)");
        chooseAction.add("אנשי קשר");
        chooseAction.add("שימוש בחנות אפליקציות");
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chooseAction);
        //listView.setAdapter(adapter);
        listView.setAdapter(new ArrayAdapter<String>(this, 0, chooseAction));

        //MediaPlayer mediaPlayer = MediaPlayer.create(CategoriesActivity.this, R.raw.select_category);
        //mediaPlayer.start();
    }
    public View getView(int position, View convertView, ViewGroup parent){
            row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            tv = (TextView) row.findViewById(android.R.id.text1);
            tv.setText(chooseAction.get(position));
            return row;
};
    public void onSubCatergories(View view)
    {
        Intent intent = new Intent(this, SubCategoriesActivity.class);
        startActivity(intent);
        finish();
    }
}
