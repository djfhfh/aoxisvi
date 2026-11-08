package com.g45.ffnativo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    static { System.loadLibrary("ffnativo"); }

    // Função C++
    public native void toggleEsp(boolean enable);

    private WindowManager windowManager;
    private View menuView;
    private ESPCanvas espView;
    private boolean isEspEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 1. Pedir permissão de sobreposição
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            // Se já tem permissão, inicia o painel
            startOverlay();
            finish(); // Fecha a tela principal
        }
    }

    private void startOverlay() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 2. Cria a tela invisível para desenhar o ESP
        espView = new ESPCanvas(this);
        WindowManager.LayoutParams espParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        espParams.gravity = Gravity.TOP | Gravity.START;
        windowManager.addView(espView, espParams);

        // 3. Cria o menu flutuante (Botão)
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#AA000000"));
        layout.setPadding(30, 30, 30, 30);

        Button espBtn = new Button(this);
        espBtn.setText("ESP: OFF");
        espBtn.setTextColor(Color.WHITE);
        espBtn.setBackgroundColor(Color.DKGRAY);

        espBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEspEnabled = !isEspEnabled;
                espBtn.setText(isEspEnabled ? "ESP: ON" : "ESP: OFF");
                espBtn.setBackgroundColor(isEspEnabled ? Color.GREEN : Color.DKGRAY);
                
                // Chama o C++
                toggleEsp(isEspEnabled); 
                // Atualiza a tela
                espView.invalidate();
            }
        });

        layout.addView(espBtn);

        WindowManager.LayoutParams menuParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        menuParams.gravity = Gravity.TOP | Gravity.LEFT;
        menuParams.x = 50;
        menuParams.y = 200;

        menuView = layout;
        windowManager.addView(menuView, menuParams);
    }

    // Classe que desenha na tela
    class ESPCanvas extends View {
        private Paint paint;
        public ESPCanvas(Context context) {
            super(context);
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isEspEnabled) return;

            // DESENHO DE TESTE: um quadrado vermelho no meio da tela
            // Aqui é onde você vai colocar a matemática do WorldToScreen depois
            canvas.drawRect(500, 500, 800, 900, paint);
            canvas.drawText("HACK ATIVO", 510, 490, paint);
        }
    }
}
