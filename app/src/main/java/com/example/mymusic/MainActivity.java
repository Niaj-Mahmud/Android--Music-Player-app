package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    Button newbutton;
    private String[] items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview_id);


        runtimepermission();


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity( new Intent(getApplicationContext(),PlayerActivity2.class)
//                        .putExtra("songs",mySong)
//                        .putExtra());
//
//            }
//        });


    }


    public void runtimepermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaysongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSearch_id) {

            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId()==R.id.menufeedBack){

            Intent intent= new Intent(MainActivity.this,feedbackActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    public ArrayList<File> findsong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for (File singlefile : files) {

            try {
                if (singlefile.isDirectory() && !singlefile.isHidden()) {
                    ArrayList<File> innerFiles = findsong(singlefile);
                    arrayList.addAll(innerFiles);
                } else {

                    if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {

                        arrayList.add(singlefile);
                    }
                }
            } catch (Exception e) {
                Log.e("Exception", "findsong: " + e.getMessage());
                e.printStackTrace();
            }

        }

        return arrayList;

    }


    ArrayList<File> mysong;
    public void displaysongs() {

      //  File f = Environment.getExternalStorageDirectory();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mysong = findsong(Environment.getExternalStorageDirectory());
                items = new String[mysong.size()];
                for (int i = 0; i < mysong.size(); i++) {
                    items[i] = mysong.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ArrayAdapter<String> myadepter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
                        CustomAdapter myadapter = new CustomAdapter();
                        listView.setAdapter(myadapter);

                    }
                });
            }
        };

        Thread t  = new Thread(runnable);
        t.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Sname = (String) listView.getItemAtPosition(position);

                startActivity(new Intent(getApplicationContext(), PlayerActivity2.class)
                        .putExtra("songs", mysong)
                        .putExtra("songname", Sname)
                        .putExtra("pos", position));
            }
        });


    }

    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View myView = getLayoutInflater().inflate(R.layout.sample_layout, null);
            TextView textView = myView.findViewById(R.id.sampletextView_id);
            textView.setSelected(true);
            textView.setText(items[position]);


            return myView;
        }
    }


}