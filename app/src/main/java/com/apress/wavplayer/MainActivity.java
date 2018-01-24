package com.apress.wavplayer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import wavplayer.apress.com.wavplayer.R;

import com.apress.wavplayer.jni.FmodJni;
import com.apress.wavplayer.jni.JniTest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    JniTest jni;
    FmodJni fmodJni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txt = (TextView)findViewById(R.id.txt);
        jni = new JniTest();
        fmodJni = new FmodJni();
        txt.setText(jni.getJniString());
        Button playButton = (Button)findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        Button aviButton = (Button)findViewById(R.id.aviButton);
        aviButton.setOnClickListener(this);
    }

    /**
     * On click.
     *
     * @param view
     *            view instance.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                onPlayButtonClick();
            case R.id.aviButton:
                Intent intent = new Intent(this, AviPlayerActivity.class);
                startActivity(intent);

        }
    }

    /**
     * On play button click.
     */
    private void onPlayButtonClick() {
        // Under the external storage
        File file = new File(Environment.getExternalStorageDirectory(),
                "8k16bitpcm.wav");
        Log.e("hello-jni", "ss===="+file.exists());
        fmodJni.fix(file.getAbsolutePath(), 4);
        // Start player
        //PlayTask playTask = new PlayTask();
        //playTask.execute(file.getAbsolutePath());
    }

    /**
     * Play task.
     */
    private class PlayTask extends AsyncTask<String, Void, Exception> {
        /**
         * Background task.
         *
         * @param file
         *            WAVE file.
         */
        protected Exception doInBackground(String... file) {
            Exception result = null;

            try {
                // Play the WAVE file
                jni.play(file[0]);
            } catch (Exception ex) {
                result = ex;
            }

            return result;
        }

        /**
         * Post execute.
         *
         * @param ex
         *            exception instance.
         */
        protected void onPostExecute(Exception ex) {
            // Show error message if playing failed
            if (ex != null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("title")
                        .setMessage(ex.getMessage()).show();
            }
        }
    }

}
