package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lobosnet.databinding.ActivityHomeDirecaoBinding;

public class home_direcao extends AppCompatActivity {
    private ActivityHomeDirecaoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeDirecaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //botão de enviar material
        binding.btnEnviarMaterail.setOnClickListener(view -> {
            Intent intent = new Intent(this, enviar_material.class);
            startActivity(intent);
        });
        //botão de avisos
        binding.btnAvisos.setOnClickListener(view -> {
            Intent intent = new Intent(this, avisos.class);
            startActivity(intent);
        });
        //botão pontuaçao
        binding.imageView6.setOnClickListener(view -> {
            Intent intent = new Intent(this, pontuacao.class);
            startActivity(intent);
        });
        //botão lobitos
        binding.btnLobitos.setOnClickListener(view -> {
            Intent intent = new Intent(this, Lobitos.class);
            startActivity(intent);
        });
        //botão desbravador
        binding.imageView10.setOnClickListener(view -> {
            Intent intent = new Intent(this, Cadastrar_Desbaravdor.class);
            startActivity(intent);
        });
        //botão agenda
    }
}