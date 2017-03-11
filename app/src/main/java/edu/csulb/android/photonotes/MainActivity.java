package edu.csulb.android.photonotes;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    private static final int PER_REQUEST_CODE1 = 100;
    DatabaseHelper databaseHelper;
    ArrayList<NotesData> nds;
    private static final int PER_REQUEST_CODE2 = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PER_REQUEST_CODE1);
            }
            else if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PER_REQUEST_CODE2);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
        lv = (ListView) findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        nds = new ArrayList<NotesData>();
        showlist();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ViewPhoto.class);
                i.putExtra("id",nds.get(position).id);
                startActivity(i);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddPhotoActivity.class);
                startActivityForResult(i, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showlist();
    }

    private void showlist()
    {
        nds = databaseHelper.getList();
        ArrayList<String> captions = new ArrayList<String>();

        if(nds.size()==0)
        {
            lv.setAdapter(null);
            Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_LONG);
        }
        else
        {
            for(int i=0;i<nds.size();i++)
                captions.add(nds.get(i).caption);
            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,captions);
            lv.setAdapter(adp);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PER_REQUEST_CODE1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PER_REQUEST_CODE2);
            }
            else
            {
                Toast.makeText(this, "Please provide appropriate permissions to avoid any unexpected behavior.", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == PER_REQUEST_CODE2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Done
            }
            else {
                Toast.makeText(this, "Please provide appropriate permissions to avoid any unexpected behavior.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_uninstall) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:edu.csulb.android.photonotes"));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
