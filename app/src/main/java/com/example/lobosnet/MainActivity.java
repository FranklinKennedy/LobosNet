package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000; // Tempo de duração da Splash Screen (3 segundos)
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Certifique-se de que o layout existe

        // Inicializa o FirebaseAuth e DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Direcao");

        // Cria um atraso de 3 segundos e depois verifica o estado de autenticação
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // Usuário está logado, busca o nível do usuário no Firebase
                Log.d("MainActivity", "Usuário autenticado, buscando nível no Firebase");
                buscarNivelUsuario(currentUser.getEmail());
            } else {
                // Usuário não está logado, redireciona para a tela de login
                Log.d("MainActivity", "Usuário não autenticado, redirecionando para a tela de login");
                Intent intent = new Intent(this, tela_pricipal.class); // Atualize para a tela de login apropriada
                startActivity(intent);
                finish(); // Fecha a SplashActivity para que o usuário não possa voltar a ela
            }
        }, SPLASH_DURATION);
    }

    private void buscarNivelUsuario(String email) {
        if (email == null) {
            Log.d("MainActivity", "Email do usuário é nulo");
            Intent intent = new Intent(MainActivity.this, tela_pricipal.class); // Atualize para a tela de login apropriada
            startActivity(intent);
            finish();
            return;
        }

        Log.d("MainActivity", "Buscando nível para o email: " + email);

        // Busca o documento pelo email
        databaseReference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("MainActivity", "Dados recebidos do Firebase: " + snapshot.toString());

                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Obtém o ID do documento
                        String documentId = userSnapshot.getKey();
                        Log.d("MainActivity", "ID do documento encontrado: " + documentId);
                        buscarNivelPorId(documentId);
                        return;
                    }
                } else {
                    Log.d("MainActivity", "Usuário não encontrado no banco de dados");
                    Intent intent = new Intent(MainActivity.this, tela_pricipal.class); // Atualize para a tela de login apropriada
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MainActivity", "Erro ao acessar o banco de dados: " + error.getMessage());
                Intent intent = new Intent(MainActivity.this, tela_pricipal.class); // Atualize para a tela de login apropriada
                startActivity(intent);
                finish();
            }
        });
    }

    private void buscarNivelPorId(String documentId) {
        // Usa o ID do documento para buscar o nível
        databaseReference.child(documentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Integer nivel = snapshot.child("Nivel").getValue(Integer.class);
                Log.d("MainActivity", "Nível encontrado: " + nivel);
                redirecionarParaTela(nivel);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MainActivity", "Erro ao acessar o banco de dados: " + error.getMessage());
                Intent intent = new Intent(MainActivity.this, tela_pricipal.class); // Atualize para a tela de login apropriada
                startActivity(intent);
                finish();
            }
        });
    }

    private void redirecionarParaTela(Integer nivel) {
        Intent intent;

        if (nivel != null) {
            switch (nivel) {
                case 1:
                    intent = new Intent(this, home_direcao.class);
                    break;
                case 2:
                    intent = new Intent(this, home_debravador.class);
                    break;
                case 3:
                    intent = new Intent(this, home_responsaveis.class);
                    break;
                default:
                    Log.d("MainActivity", "Nível desconhecido");
                    intent = new Intent(this, tela_pricipal.class); // Redireciona para a tela de login
                    break;
            }
        } else {
            Log.d("MainActivity", "Nível não encontrado");
            intent = new Intent(this, tela_pricipal.class); // Redireciona para a tela de login
        }

        startActivity(intent);
        finish(); // Fecha a SplashActivity para que o usuário não possa voltar a ela
    }
}
