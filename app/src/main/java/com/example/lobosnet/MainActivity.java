package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000; // Tempo de duração da Splash Screen (3 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Certifique-se de que o layout existe

        // Cria um atraso de 3 segundos e depois inicia a TipoDeAcessoActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, tela_pricipal.class);
            startActivity(intent);
            finish(); // Fecha a SplashActivity para que o usuário não possa voltar a ela
        }, SPLASH_DURATION);
    }
}