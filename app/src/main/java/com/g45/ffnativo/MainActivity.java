package com.g45.ffnativo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(40, 40, 40, 40);

        TextView title = new TextView(this);
        title.setText("Proxy Mod Menu - ADB Pair");
        title.setTextColor(Color.GREEN);
        title.setTextSize(20);

        EditText codeInput = new EditText(this);
        codeInput.setHint("Digite o codigo de pareamento");
        codeInput.setTextColor(Color.WHITE);

        Button btnPair = new Button(this);
        btnPair.setText("Parear e Iniciar Menu");
        
        btnPair.setOnClickListener(v -> {
            startOverlay();
            finish();
        });

        layout.addView(title);
        layout.addView(codeInput);
        layout.addView(btnPair);
        setContentView(layout);
    }

    private void startOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            startService(new Intent(this, OverlayService.class));
        }
    }
}
