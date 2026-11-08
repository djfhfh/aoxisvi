package com.g45.ffnativo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#121212"));
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(50, 50, 50, 50);

        TextView title = new TextView(this);
        title.setText("G45 PROXY PANEL");
        title.setTextColor(Color.parseColor("#00FFCC"));
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 50);

        Button btnPair = new Button(this);
        btnPair.setText("INICIAR OVERLAY");
        btnPair.setTextColor(Color.WHITE);
        btnPair.setTextSize(16);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(30);
        shape.setColor(Color.parseColor("#FF0055"));
        btnPair.setBackground(shape);
        
        btnPair.setOnClickListener(v -> {
            startOverlay();
            finish();
        });

        layout.addView(title);
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
