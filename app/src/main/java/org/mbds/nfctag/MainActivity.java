package org.mbds.nfctag;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.mbds.nfctag.databinding.ActivityMainBinding;
import org.mbds.nfctag.read.NFCReaderActivity;
import org.mbds.nfctag.write.NFCWriterActivity;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleActions();
    }


    private void handleActions() {
        binding.btnReadTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager manager = getPackageManager();
                if (!manager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
                    // Avertir l’utilisateur
                    Context context = getApplicationContext();
                    CharSequence text = "Erreur don't have NFC";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    finish();
                } else {

                    startActivity(new Intent(MainActivity.this, NFCReaderActivity.class));
                }
            }


        });

        binding.btnWriteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager manager = getPackageManager();
                if
                (!manager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
                    // Avertir l’utilisateur
                    Context context = getApplicationContext();
                    CharSequence text = "Erreur don't have NFC";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    startActivity(new Intent(MainActivity.this, NFCWriterActivity.class));
                }
            }
        });
    }

}