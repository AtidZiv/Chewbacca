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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CategoriesActivity extends AppCompatActivity {

    TextView txtTitle;
    JSONObject guide;
    static final String STATE_JSON_ROOT_DEFAULT_VALUE = "root";
    String guidePath = STATE_JSON_ROOT_DEFAULT_VALUE;

    MediaPlayer mediaPlayer;

    JSONObject getJsonByPath(JSONObject jobj, String path) {
        String[] paths = path.split(":");
        for(String jpath : paths) {
            if (jpath.equals("root"))
                continue;
            try {
                jobj = jobj.getJSONObject("items");
                if (!jobj.has(jpath))
                    return null;
                jobj = jobj.getJSONObject(jpath);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return jobj;
    }

    String getStringByStringId(String titleIdString) {
        int titleStrId = getResources().getIdentifier(titleIdString, "strings", getPackageName());
        if (titleStrId == 0)
            return titleIdString;
        return getResources().getString(titleStrId);
    }

    void setItems(JSONObject guide) {
        try {
            JSONObject jitems = guide.getJSONObject("items");
            String[] items = new String[jitems.length()];
            final String[] paths = new String[jitems.length()];
            Iterator<String> keysIt = jitems.keys();
            int i=0;
            while (keysIt.hasNext()){
                String item = keysIt.next();
                paths[i] = item;
                JSONObject jobj = jitems.getJSONObject(item);
                items[i++] = getStringByStringId(jobj.getString("title"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            ListView lvCategories = (ListView)findViewById(R.id.lvCategories);
            lvCategories.setAdapter(adapter);
            lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    guidePath += ":" + paths[position];
                    setGuide();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void showSubCategory(JSONObject curGuide) throws JSONException{
        txtTitle.setText(getStringByStringId(curGuide.getString("title"))+":");
        setItems(curGuide);
    }

    void showFloater(JSONObject curGuide) throws JSONException {

        JSONArray jsteps = curGuide.getJSONArray("steps");
        int[] steps = new int[jsteps.length()+1];
        steps[0] = R.raw.explanation;
        for(int i=0; i<jsteps.length(); i++) {
            String step = (String) jsteps.get(i);
            int stepId = getResources().getIdentifier(step, "raw", getPackageName());
            steps[i+1] = stepId;
        }
        final AudioFloater floater = new AudioFloater(this, steps, CategoriesActivity.class);
        floater.Display();
        minimizeApp();
    }

    void setGuide() {
        if (guide == null)
            return;
        JSONObject curGuide = getJsonByPath(guide, guidePath);
        if (curGuide == null)
            return;

        try {
            if (curGuide.has("steps"))
                showFloater(curGuide);
            else
                showSubCategory(curGuide);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void loadJSON() {
        String jsonContent="";
        try {
            InputStream fis = getAssets().open("guides.json");
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            jsonContent = new String(buf, "UTF-8");
            guide = new JSONObject(jsonContent);
            setGuide();
        }catch (Exception e) {
        }
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

        try {
            JSONObject curGuide = getJsonByPath(guide, guidePath);
            if (curGuide == null)
                return;
            JSONArray steps = curGuide.getJSONArray("tracker_steps");
            Intent tracker = new Intent(this, TrackerService.class);
            tracker.putExtra(TrackerService.STEPS, steps.toString());
            startService(tracker);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        goBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        txtTitle = (TextView)findViewById(R.id.txtCategoriesTitle);
        loadJSON();

        mediaPlayer = MediaPlayer.create(CategoriesActivity.this, R.raw.select_category);
        mediaPlayer.start();
    }

    @Override
    protected void onPause(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onPause();
    }

    boolean goBack() {
        if (!guidePath.equals("root")) {
            guidePath = guidePath.substring(0, guidePath.lastIndexOf(":"));
            setGuide();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!goBack()) {
            return;
        }

        super.onBackPressed();
    }
}
