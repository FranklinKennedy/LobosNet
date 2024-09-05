package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000; // Tempo de duração da Splash Screen (3 segundos)
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Certifique-se de que o layout existe

        // Inicializa o FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Cria um atraso de 3 segundos e depois verifica o estado de autenticação
        new Handler().postDelayed(() -> {
            Intent intent;
            if (mAuth.getCurrentUser() != null) {
                // Usuário está logado, redireciona para a tela principal
                Log.d("MainActivity", "Usuário autenticado, redirecionando para a tela principal");
                intent = new Intent(this, tela_pricipal.class);
            } else {
                // Usuário não está logado, redireciona para a tela de login
                Log.d("MainActivity", "Usuário não autenticado, redirecionando para a tela de login");
                intent = new Intent(this, tela_pricipal.class);
            }
            startActivity(intent);
            finish(); // Fecha a SplashActivity para que o usuário não possa voltar a ela
        }, SPLASH_DURATION);
    }
}
