package com.example.jcasperson.threading;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Handler;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.File;


public class MainActivity extends ActionBarActivity{
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private static final int PROGRESS = 0x1;
    private ListView lv;
    private ProgressBar mProgress;
    private int mProgressStatus;

    //  private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        /*
        // Start lengthy operation in a background thread
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run(){
                while (mProgressStatus < 100){
                   // mProgressStatus = doWork();
                    /*
                    //upadate progress bar
                    mHandler.post(new Runnable(){
                        public void run(){
                            mProgress.setProgress(mProgressStatus);
                        }
                    });

                }
            }
        });
                */
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

    Context context = this;


    // Create button calls this function
    public void create(View view) throws IOException{

        Thread y = new Thread(){
            @Override
            public void run(){
                try {
                    String filename = "myFile.txt";
                    String string = "Hello world!";
                    FileOutputStream outputStream;
                    outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
                    for (int i = 0; i < 10; ++i) {
                        String word = Integer.toString(i);
                        outputStream.write(word.getBytes());
                        System.out.println(i);
                        Thread.sleep(250);
                    }

                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        y.start();

    } // end of create function

    public void load(View view) throws IOException {
        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println("HELLO!");

                    lv = (ListView) findViewById(R.id.list);

                    List<String> your_array_list = new ArrayList<String>();

                    FileInputStream fis;


                    fis = context.openFileInput("myFile.txt");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    String line;

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_list_item_1,
                            your_array_list );
                    while ((line = bufferedReader.readLine()) != null) {


                        for (int i = 0; i < line.length(); ++i){
                            Thread.sleep(250);
                            String joke = "" + line.charAt(i);
                            your_array_list.add(joke);
                            lv.post(new Runnable() {
                                @Override
                                public void run() {
                                    lv.setAdapter(arrayAdapter);
                                    mProgressStatus += 10;
                                    mProgress.setProgress(mProgressStatus);
                                }
                            });

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    } // end of load function


    public void clear(View view) throws IOException{
        List<String> your_array_list = new ArrayList<String>();
        lv = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                your_array_list );
        lv.post(new Runnable() {
            @Override
            public void run() {
                lv.setAdapter(arrayAdapter);
                mProgressStatus = 0;
                mProgress.setProgress(mProgressStatus);
            }
        });

    }

}