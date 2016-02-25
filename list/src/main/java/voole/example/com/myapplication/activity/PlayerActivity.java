package voole.example.com.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import voole.example.com.myapplication.R;
import voole.example.com.myapplication.media.AndroidMediaController;
import voole.example.com.myapplication.media.IjkVideoView;

/**
 * Created by Administrator on 2016/2/22 0022.
 */
public class PlayerActivity extends AppCompatActivity {

    private IjkVideoView mVideoView;
    private boolean mBackPressed;
    private AndroidMediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String TAG = "PlayerActivty";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.playactivity_main);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//
//        AndroidMediaController androidMediaController = new AndroidMediaController(this, false);
//        androidMediaController.setSupportActionBar(actionBar);
        ActionBar actionBar = getSupportActionBar();
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
//        mVideoView.setHudView(mHudView);
        if (url != null)
            mVideoView.setVideoPath(url);
        else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }
        mVideoView.start();

    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBackPressed) {
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        }
        IjkMediaPlayer.native_profileEnd();
    }
}
