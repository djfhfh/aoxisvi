package com.g45.ffnativo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager wm;
    private View menu;
    private ESPView espView;
    private WindowManager.LayoutParams params;

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
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#E6000000"));
        bg.setCornerRadius(40);
        bg.setStroke(3, Color.parseColor("#00FFCC"));
        layout.setBackground(bg);
        layout.setPadding(30, 30, 30, 30);

        TextView tv = new TextView(this);
        tv.setText("G45 HACK");
        tv.setTextColor(Color.parseColor("#00FFCC"));
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, 0, 0, 20);

        Button btnEsp = createStyledButton("ESP: OFF", Color.parseColor("#333333"));
        btnEsp.setOnClickListener(v -> {
            boolean isOn = btnEsp.getText().toString().contains("ON");
            btnEsp.setText(isOn ? "ESP: OFF" : "ESP: ON");
            btnEsp.setBackgroundColor(isOn ? Color.parseColor("#333333") : Color.parseColor("#00FF00"));
            espView.setEspEnabled(!isOn);
            toggleEsp(!isOn);
        });

        layout.addView(tv);
        layout.addView(btnEsp);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100; params.y = 200;

        menu = layout;
        wm.addView(menu, params);
        setupDrag(menu, params);
    }

    private Button createStyledButton(String text, int color) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextColor(Color.WHITE);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(20);
        shape.setColor(color);
        btn.setBackground(shape);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, 100);
        lp.setMargins(0, 10, 0, 10);
        btn.setLayoutParams(lp);
        return btn;
    }

    private void setupDrag(View view, WindowManager.LayoutParams params) {
        view.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x; initialY = params.y;
                        initialTouchX = event.getRawX(); initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        wm.updateViewLayout(menu, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    class ESPView extends View {
        private Paint boxPaint, linePaint, healthPaint;
        private boolean isEspEnabled = false;

        public ESPView(Context context) {
            super(context);
            boxPaint = new Paint(); boxPaint.setColor(Color.parseColor("#FF0000")); boxPaint.setStyle(Paint.Style.STROKE); boxPaint.setStrokeWidth(4f);
            linePaint = new Paint(); linePaint.setColor(Color.WHITE); linePaint.setStrokeWidth(2f);
            healthPaint = new Paint(); healthPaint.setColor(Color.parseColor("#00FF00")); healthPaint.setStyle(Paint.Style.FILL);
        }

        public void setEspEnabled(boolean enabled) {
            this.isEspEnabled = enabled;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isEspEnabled) { invalidate(); return; }

            // AQUI SERIA O LOOP REAL DO JOGO:
            // List<Inimigo> inimigos = getInimigosDoC();
            // for(Inimigo i : inimigos) { drawBox(i.x, i.y); }
            
            // Por enquanto, desenhamos 3 caixas simulando inimigos na tela
            // para você ver que o overlay funciona por cima do jogo.
            canvas.drawRect(300, 400, 500, 800, boxPaint); 
            canvas.drawRect(800, 300, 950, 600, boxPaint);
            canvas.drawRect(600, 900, 750, 1200, boxPaint);

            invalidate();
        }
    }
}
