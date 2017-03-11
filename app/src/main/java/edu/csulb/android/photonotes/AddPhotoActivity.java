package edu.csulb.android.photonotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by parth on 3/7/16.
 */

public class AddPhotoActivity extends AppCompatActivity {

    String location="";
    Button bt_takePhoto,bt_save;
    ImageView imageView;
    EditText editText;
    private static final int CAMERA_REQUEST = 1243;
    int flag=0;
    Bitmap bitMap;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phto);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        bt_takePhoto = (Button)findViewById(R.id.button);
        bt_save = (Button)findViewById(R.id.button2);
        editText = (EditText)findViewById(R.id.edittext);
        imageView = (ImageView)findViewById(R.id.imageView2);

//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        bt_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = editText.getText().toString();
                if(flag==1 && caption.length()!=0)
                {
                    savePhoto();
                    NotesData notesData = new NotesData();
                    notesData.location = location;
                    notesData.caption = editText.getText().toString();
                    databaseHelper.insertData(notesData);
                    Toast.makeText(getApplicationContext(), "Photo Note is Added", Toast.LENGTH_LONG).show();
                    onBackPressed();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please take a photo and add a caption to save a note.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void savePhoto() {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PhotoNotes");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 100000;
        n = generator.nextInt(n);
        String fname = "Photo_"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            location = file.getAbsolutePath();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            bitMap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitMap);
            bt_takePhoto.setText("Capture Photo");
            flag=1;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try{
            super.onSaveInstanceState(outState);
            if(bitMap!=null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                outState.putByteArray("image",byteArray);
                outState.putInt("flag",flag);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        byte[] byteArray = savedInstanceState.getByteArray("image");
        flag = savedInstanceState.getInt("flag");
        if(byteArray!=null)
        {
            bitMap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bitMap);
        }
    }

}
