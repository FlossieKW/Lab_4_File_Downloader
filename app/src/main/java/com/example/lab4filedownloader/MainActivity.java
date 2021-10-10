package com.example.lab4filedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private volatile boolean stopThread = false;
    private TextView downloadProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        downloadProgressText = findViewById(R.id.downloadProgressText);
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("DOWNLOADING...");
                startButton.setEnabled(false);
            }
        });

        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress + 10) {
            if (stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        downloadProgressText.setText("");
                        startButton.setEnabled(true);
                    }
                });
                return;
            }

            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadProgressText.setText("Download Progress: " + finalDownloadProgress + "%");
                }
            });
            // Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
                downloadProgressText.setText("");
                startButton.setEnabled(true);
            }
        });
    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }

    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }
}