package com.example.lobosnet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lobosnet.databinding.ActivityEnviarMaterialBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class enviar_material extends AppCompatActivity {

    private ActivityEnviarMaterialBinding binding;
    private Uri selectedFileUri = null;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar o View Binding
        binding = ActivityEnviarMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Aplicar insets para gerenciar as margens das system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Registrar a seleção de arquivos
        ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedFileUri = uri;
                        String fileName = getFileName(uri);
                        binding.tituloMaterial.setText(fileName);
                    }
                });

        // Ação do botão de escolher arquivo
        binding.btnEnviarMaterial.setOnClickListener(v -> filePickerLauncher.launch("*/*"));

        // Ação do botão de adicionar material
        binding.btnAdicionarMaterial.setOnClickListener(v -> uploadMaterial());
    }

    private void uploadMaterial() {
        String descricao = binding.editTextTextMultiLine.getText().toString();

        // Verificar se a descrição e o arquivo foram fornecidos
        if (descricao.isEmpty()) {
            Toast.makeText(this, "A descrição do material é obrigatória.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, "Selecione um arquivo.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Definir o nome e o caminho no Firebase Storage
        StorageReference storageRef = storage.getReference().child("materiais/" + binding.tituloMaterial.getText().toString());
        storageRef.putFile(selectedFileUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Salvar os dados no Firestore
                Map<String, Object> materialData = new HashMap<>();
                materialData.put("Descricao", descricao);
                materialData.put("Nome", binding.tituloMaterial.getText().toString());
                materialData.put("caminho", downloadUri.toString());

                firestore.collection("materiais").add(materialData)
                        .addOnSuccessListener(documentReference -> Toast.makeText(this, "Material adicionado com sucesso!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar material no Firestore.", Toast.LENGTH_SHORT).show());
            });
        }).addOnFailureListener(e -> Toast.makeText(this, "Erro ao enviar arquivo.", Toast.LENGTH_SHORT).show());
    }

    private String getFileName(Uri uri) {
        String result = uri.getLastPathSegment();
        if (result != null) {
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
