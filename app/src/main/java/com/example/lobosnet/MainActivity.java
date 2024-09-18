package com.example.lobosnet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Authentication e Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Verifica se o usuário está autenticado
        checkAuthentication();
    }

    /**
     * Verifica se o usuário está logado. Se estiver, tenta buscar o nível do usuário
     * no Firestore; caso contrário, redireciona para a tela de login.
     */
    private void checkAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("MainActivity", "Usuário autenticado: " + currentUser.getEmail());
            buscarNivelUsuario(currentUser.getEmail());
        } else {
            Log.d("MainActivity", "Usuário não autenticado, redirecionando para a tela de login.");
            redirectToLoginScreen();
        }
    }

    /**
     * Busca o nível do usuário no Firestore usando o email do usuário.
     *
     * @param email O email do usuário autenticado.
     */
    private void buscarNivelUsuario(String email) {
        if (email == null || email.isEmpty()) {
            Log.e("MainActivity", "Email do usuário é nulo ou vazio.");
            redirectToLoginScreen();
            return;
        }

        Log.d("MainActivity", "Buscando nível para o email: " + email);

        // Faz a busca no Firestore utilizando o email como chave
        firestore.collection("Usuario")
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Integer nivel = document.getLong("Nivel").intValue();
                                Log.d("MainActivity", "Nível do usuário encontrado: " + nivel);
                                redirecionarParaTela(nivel);
                                return;
                            }
                        } else {
                            Log.d("MainActivity", "Usuário não encontrado no banco de dados.");
                            redirectToLoginScreen();
                        }
                    } else {
                        Log.e("MainActivity", "Erro ao acessar o banco de dados: " + task.getException());
                        redirectToLoginScreen();
                    }
                });
    }

    /**
     * Redireciona o usuário para a tela correspondente ao seu nível.
     *
     * @param nivel O nível do usuário (1 = Direção, 2 = Desbravador, 3 = Responsável).
     */
    private void redirecionarParaTela(Integer nivel) {
        Intent intent;

        if (nivel == null) {
            Log.e("MainActivity", "Nível do usuário é nulo, redirecionando para a tela principal.");
            intent = new Intent(this, tela_pricipal.class);
        } else {
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
                    Log.e("MainActivity", "Nível desconhecido, redirecionando para a tela principal.");
                    intent = new Intent(this, tela_pricipal.class);
                    break;
            }
        }

        startActivity(intent);
        finish();
    }

    /**
     * Redireciona o usuário para a tela principal de login.
     */
    private void redirectToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, tela_pricipal.class);
        startActivity(intent);
        finish();
    }
}
