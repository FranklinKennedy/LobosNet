package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lobosnet.databinding.ActivityTipoDeAcesoBinding;

public class tipo_de_aceso extends AppCompatActivity {
    private ActivityTipoDeAcesoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTipoDeAcesoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //DESBRAVADOR
        binding.btnDesbraador.setOnClickListener( v -> {
            Intent intent = new Intent(this, login.class);
            intent.putExtra("cargo", "Desbravador");
            intent.putExtra("nivel", 2);
            startActivity(intent);
        });
        //RESPONAVEL
        binding.btnReponsavel.setOnClickListener( v -> {
            Intent intent = new Intent(this, login.class);
            intent.putExtra("cargo", "REPONSAVEL");
            intent.putExtra("nivel", 3);
            startActivity(intent);

        });
        //DIRECAO
        binding.btnDirecao.setOnClickListener( v -> {
            Intent intent = new Intent(this, login.class);
            intent.putExtra("cargo", "DIREÇÃO");
            intent.putExtra("nivel", 1);
            startActivity(intent);
        });

    }
}