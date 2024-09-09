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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

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

        // Nome no título da tela
        TextView textNomeDoCargo = binding.textNomeDoCargo;
        Intent intent = getIntent();
        String cargo = intent.getStringExtra("cargo");
        int nivel = intent.getIntExtra("nivel", 0);
        // Definindo o nome do cargo
        textNomeDoCargo.setText(cargo);

        // Botão de login
        binding.button.setOnClickListener(v -> validacoes());
    }

    private void validacoes() {
        String email = binding.editText.getText().toString().trim();
        String senha = binding.senha.getText().toString().trim();

        if (email.isEmpty()) {
            binding.editText.setError("Digite seu ID");
            return;
        }

        if (!isValidEmail(email)) {
            binding.editText.setError("Email inválido");
            return;
        }

        if (senha.isEmpty()) {
            binding.senha.setError("Digite sua senha");
            return;
        }

        loginFireBase(email, senha);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void loginFireBase(String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            proximaTela();
                        }
                    } else {
                        // Falha de autenticação.
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        handleFirebaseAuthError(errorCode);
                    }
                });
    }

    private void handleFirebaseAuthError(String errorCode) {
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(this, "Email inválido.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(this, "Senha incorreta.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                break;
            case "ERROR_USER_DISABLED":
                Toast.makeText(this, "Usuário desativado.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Erro de autenticação: " + errorCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void proximaTela() {
        int nivel = getIntent().getIntExtra("nivel", 0);
        Intent intent;

        if (nivel == 1) {
            intent = new Intent(this, home_direcao.class);
            finish();
        } else if (nivel == 2) {
            intent = new Intent(this, home_responsaveis.class);
            finish();
        } else if (nivel == 3) {
            intent = new Intent(this, home_debravador.class);
            finish();
        } else {
            Toast.makeText(this, "Nível desconhecido", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(intent);
        finish(); // Opcional: Finaliza a atividade atual para não retornar a ela ao pressionar "voltar"
    }
}


