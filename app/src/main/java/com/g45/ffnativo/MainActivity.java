package com.g45.ffnativo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.widget.CompoundButton;

public class MainActivity extends Activity {
    static { System.loadLibrary("ffnativo"); }
    public native void toggleEsp(boolean enable);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(50, 50, 50, 50);

        Switch espSwitch = new Switch(this);
        espSwitch.setText("ESP LIGADO");
        espSwitch.setTextColor(Color.GREEN);
        
        espSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleEsp(isChecked);
            }
        });

        layout.addView(espSwitch);
        setContentView(layout);
    }
}
