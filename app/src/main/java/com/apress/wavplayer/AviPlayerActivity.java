package com.apress.wavplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.File;

import wavplayer.apress.com.wavplayer.R;

public class AviPlayerActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText fileNameEdit;
    private RadioGroup playerRadioGroup;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avi_player);
        fileNameEdit = (EditText) findViewById(R.id.file_name_edit);
        playerRadioGroup = (RadioGroup) findViewById(R.id.player_radio_group);
        playButton = (Button) findViewById(R.id.avi_btn);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avi_btn :
                onPlayButtonClick();
                break;
        }
    }

    private void onPlayButtonClick() {
        Intent intent;
        int radioId = playerRadioGroup.getCheckedRadioButtonId();
        switch (radioId) {
            case R.id.bitmap_player_radio:
                intent = new Intent(this, BitmapPlayerActivity.class);
                break;
            case R.id.gl_player_radio:
                intent = new Intent(this, OpenGLPlayerActivity.class);
                break;
            case R.id.native_window_player_radio:
                intent = new Intent(this, NativeWindowPlayerActivity.class);
                break;
            default:
                throw new UnsupportedOperationException("radioId="+radioId);
        }
        File file = new File(Environment.getExternalStorageDirectory(), fileNameEdit.getText().toString());
        intent.putExtra(AbstractPlayerActivity.EXTRA_FILE_NAME, file.getAbsolutePath());
        startActivity(intent);
    }
}
