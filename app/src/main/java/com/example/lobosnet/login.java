package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lobosnet.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //nome no titulo da tela
        TextView textNomeDoCargo = binding.textNomeDoCargo;
        Intent intent = getIntent();
        String cargo = intent.getStringExtra("cargo");
        int nivel = intent.getIntExtra("nivel", 0);
        //definindo o nome do cargo
        textNomeDoCargo.setText(cargo);
        //botão de login
        binding.button.setOnClickListener(v -> validacoes());

    }

    private void validacoes() {
        String email = binding.editText.getText().toString();

        if (email.isEmpty()) {
            binding.editText.setError("Digite seu ID");
            return;
        } else {
            validacaoSenha();
        }

    }

    private void validacaoSenha() {
        String senha = binding.editText.getText().toString();
        if (senha.isEmpty()) {
            binding.editText.setError("Digite sua senha");

        } else {
            loginFireBase();
        }

    }

    private void loginFireBase() {
        String email = binding.editText.getText().toString();
        String senha = binding.senha.getText().toString();
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        proximaTela();
                    } else {
                        // Falha de autenticação.
                        Toast.makeText(this, "Autenticação falhou.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void proximaTela() {
        int nivel = getIntent().getIntExtra("nivel", 0);

        if (nivel == 1) {

        }
    }
}