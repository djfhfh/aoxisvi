package com.g45.ffnativo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class OverlayService extends Service {
    private WindowManager wm;
    private View menu;
    private ESPView espView;

    static { System.loadLibrary("ffnativo"); }
    
    public native void toggleEsp(boolean enable);
    public native void setAimbot(boolean enable);

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        espView = new ESPView(this);
        WindowManager.LayoutParams p1 = new WindowManager.LayoutParams(
                -1, -1, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        wm.addView(espView, p1);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#DD111111"));
        layout.setPadding(20, 20, 20, 20);

        Button btnEsp = new Button(this);
        btnEsp.setText("ESP: OFF");
        btnEsp.setOnClickListener(v -> {
            boolean isOn = btnEsp.getText().toString().contains("ON");
            btnEsp.setText(isOn ? "ESP: OFF" : "ESP: ON");
            espView.setEspEnabled(!isOn);
            toggleEsp(!isOn);
        });

        Button btnAim = new Button(this);
        btnAim.setText("AIMBOT: OFF");
        btnAim.setOnClickListener(v -> {
            boolean isOn = btnAim.getText().toString().contains("ON");
            btnAim.setText(isOn ? "AIMBOT: OFF" : "AIMBOT: ON");
            setAimbot(!isOn);
        });

        layout.addView(btnEsp);
        layout.addView(btnAim);

        WindowManager.LayoutParams p2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        p2.gravity = Gravity.TOP | Gravity.LEFT;
        p2.x = 50; p2.y = 200;

        menu = layout;
        wm.addView(menu, p2);
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    class ESPView extends View {
        private Paint boxPaint, linePaint, healthPaint;
        private boolean isEspEnabled = false;

        public ESPView(Context context) {
            super(context);
            boxPaint = new Paint(); boxPaint.setColor(Color.RED); boxPaint.setStyle(Paint.Style.STROKE); boxPaint.setStrokeWidth(4f);
            linePaint = new Paint(); linePaint.setColor(Color.WHITE); linePaint.setStrokeWidth(2f);
            healthPaint = new Paint(); healthPaint.setColor(Color.GREEN); healthPaint.setStyle(Paint.Style.FILL);
        }

        public void setEspEnabled(boolean enabled) {
            this.isEspEnabled = enabled;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isEspEnabled) {
                invalidate();
                return;
            }

            int screenW = canvas.getWidth();
            int screenH = canvas.getHeight();

            float boxLeft = screenW / 2f - 100;
            float boxTop = screenH / 2f - 200;
            float boxRight = screenW / 2f + 100;
            float boxBottom = screenH / 2f + 200;
            canvas.drawRect(boxLeft, boxTop, boxRight, boxBottom, boxPaint);

            canvas.drawLine(screenW / 2f, screenH, screenW / 2f, boxBottom, linePaint);

            float healthHeight = (boxBottom - boxTop) * 0.8f;
            canvas.drawRect(boxLeft - 15, boxTop, boxLeft - 5, boxTop + healthHeight, healthPaint);

            invalidate();
        }
    }
}
