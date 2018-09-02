package com.acadgildandroid.debasish.threadsloaderassignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    EditText inputText;
    TextView response;
    Button saveButton,readButton,deleteButton;
    ProgressBar progressBar;

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;
    String myData = "";
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        inputText = (EditText) findViewById(R.id.myInputText);
        response = (TextView) findViewById(R.id.response);

            progressBar=(ProgressBar) findViewById(R.id.progressBar);

        saveButton =
                (Button) findViewById(R.id.saveExternalStorage);

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               /* try {
                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write(inputText.getText().toString().getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                new MyTask().execute(R.id.myInputText);
                inputText.setText("");
                response.setText("SampleFile.txt saved to External Storage...");
            }
        });
        deleteButton=
                (Button)findViewById(R.id.removeExternalStorage);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File f = new File("SampleFile.txt");
                    f.delete();

                    //FileOutputStream fos = new FileOutputStream(myExternalFile);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                inputText.setText("");
                response.setText("SampleFile.txt removed to External Storage...");
            }
        });

        readButton = (Button) findViewById(R.id.getExternalStorage);
        readButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fis = new FileInputStream(myExternalFile);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        myData = myData + strLine;
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputText.setText(myData);
                response.setText("SampleFile.txt data retrieved from Internal Storage...");
            }
        });

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }


    }
    class MyTask extends AsyncTask<Integer,Integer,FileOutputStream>{

      @Override
        protected void onPreExecute(){
          super.onPreExecute();
          progressBar.setVisibility(ProgressBar.VISIBLE);

      }

        @Override
        protected FileOutputStream doInBackground(Integer... integers) {

            try {
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write(inputText.getText().toString().getBytes());
                fos.close();
                for(int i=0;i<10;i++){
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ex){
                        ex.fillInStackTrace();
                    }
                   publishProgress(i*10);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
          return null;
        }
        @Override
        protected void onPostExecute(FileOutputStream fos){

          super.onPostExecute(fos);
          progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
