package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Context mContext=MainActivity.this;

    private static final int REQUEST = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
            } else {
                //do here
            }
        } else {
            //do here
        }
      //  downloadFile();
       getFiles();

    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getFiles()
    {
//        File file2 = new File(getApplicationContext().getFilesDir(), "");
//        Log.e("inside22 ",";inside2222" + file2);
//        MediaPlayer mp = new MediaPlayer();
//
//        String musicUrl = "";
//
//            musicUrl = file2.toString() + "/dstress.mp3";
//        Log.e("inside22 ",";inside2222" + musicUrl);
//            FileInputStream fileInputStream = null;
//            try {
//                fileInputStream = new FileInputStream(musicUrl);
//                mp.setDataSource(fileInputStream.getFD());
//                fileInputStream.close();
//                mp.prepare();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        PackageManager m = getPackageManager();
//        String s = getPackageName();
//        try {
//            PackageInfo p = m.getPackageInfo(s, 0);
//            s = p.applicationInfo.dataDir;
//            Log.e("inside ",";inside2" + s);
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.w("yourtag", "Error Package name not found ", e);
//        }

        String path = getApplicationContext().getFilesDir() + "/test.mp3";
        Log.e("test", path);
        MediaPlayer player = new MediaPlayer();

        try {
            player.setDataSource(path);
            player.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No Such File Present",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println("Exception of type" + e.toString());
            Toast.makeText(getApplicationContext(),"No Such File Present",Toast.LENGTH_SHORT).show();
        }

        player.start();
    }

    private void downloadFile() {
        File directory = getApplicationContext().getFilesDir();
        Log.e("olddd ",";local" +directory);
        Log.e("olddd ",";local" +getApplicationContext().getFilesDir().getPath());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sleep-solutionsapk.appspot.com");
        StorageReference  islandRef = storageRef.child("regen.mp3");
        Log.e("downloaded ",";local" +getApplicationContext().getFilesDir().getPath());
        String file_path=getApplicationContext().getFilesDir().getPath();
        File rootPath = new File(Environment.getExternalStorageDirectory(), "dstress");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(file_path, "regen.mp3");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                getFiles();
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  downloadFile();
                } else {
                    Toast.makeText(mContext, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}