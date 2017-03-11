package edu.csulb.android.photonotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by parth on 3/7/16.
 */

public class ViewPhoto extends AppCompatActivity {

    ImageView iv;
    TextView tv;
    NotesData nd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        iv = (ImageView)findViewById(R.id.imageView);
        tv = (TextView)findViewById(R.id.textView);

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        nd = new NotesData();
        nd = databaseHelper.getPhoto(id);

        System.out.println(nd.location);
        Bitmap myBitMap = BitmapFactory.decodeFile(nd.location);
        iv.setImageBitmap(myBitMap);
        tv.setText(nd.caption);
        nd = databaseHelper.getPhoto(id);
    }
}
