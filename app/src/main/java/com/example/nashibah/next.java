package com.example.nashibah;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class next extends AppCompatActivity {
    private TextView textView;
    private Dialog flowerDialog;
    private MusicPlayerService musicPlayerService;
    private boolean isServiceBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicPlayerBinder binder = (MusicPlayerService.MusicPlayerBinder) service;
            musicPlayerService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextact);

        // Bind to the MusicPlayerService
        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        textView = findViewById(R.id.textView2);
        textView.setVisibility(View.GONE);

        Button button = findViewById(R.id.popup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();

                //delay then replace it with text
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissFlower();
                    }
                }, 5000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the MusicPlayerService
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    void showPopup(){
        flowerDialog = new Dialog(this);
        flowerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        flowerDialog.setContentView(R.layout.flower_layout);
        flowerDialog.setCancelable(false); // Prevent dialog from being dismissed by tapping outside
        flowerDialog.show();

        View flowerView = flowerDialog.findViewById(R.id.flower_image_view);
        if (flowerView != null)
            fadeInAnimation(flowerView);
    }

    void dismissFlower(){
        if(flowerDialog != null && flowerDialog.isShowing()){
            flowerDialog.dismiss();
        }
        textView.setVisibility(View.VISIBLE);
        slideInTextView();
    }

    void slideInTextView() {
        // Calculate the start and end positions for the slide animation
        float startX = -textView.getWidth(); // Start from outside the screen on the left
        float endX = 0; // End at the final position (left edge of the screen)

        // Create a TranslateAnimation to move the text view from startX to endX
        Animation slideAnimation = new TranslateAnimation(startX, endX, 0, 0);
        slideAnimation.setDuration(500); // Set the duration of the animation in milliseconds

        // Apply the animation to the text view
        textView.startAnimation(slideAnimation);

        // Make the text view visible after starting the animation
        textView.setVisibility(View.VISIBLE);
    }

    private void fadeInAnimation(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000); // Duration in milliseconds (1 second in this example)
        view.startAnimation(fadeIn);
    }
}
