package com.segomezco.gestortiendas.Store.CreateStore;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.segomezco.gestortiendas.Store.StoreModel;

import java.util.Objects;

public class StoreCreateVM extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }

    public void CreateStore(String uid, String name, String description, String category,
                             String openHours, String address, String phone, String owner, Uri imageUri) {

        isLoading.setValue(true);

        if (name == null || name.isEmpty()) {
            errorMessage.setValue("El nombre del producto es obligatorio");
            isLoading.setValue(false);
            return;
        }

        DatabaseReference storesRef = firebaseDatabase.getReference("Users")
                .child(uid)
                .child("Stores")

                .child(name);

        if (imageUri != null) {
            StorageReference imageRef = firebaseStorage.getReference()
                    .child("Users").child(uid).child("Stores").child(name).child("store_image.jpg");

            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {

                        StoreModel storeData = new StoreModel(name, description, category, openHours,
                                address, phone, owner);

                        storeData.setImageUrl(downloadUri.toString());

                        storesRef.setValue(storeData).addOnCompleteListener(task -> {
                            isLoading.setValue(false);
                            if (task.isSuccessful()) {
                                registerSuccess.setValue(true);
                            } else {
                                errorMessage.setValue("Error al guardar producto: " +
                                        Objects.requireNonNull(task.getException()).getMessage());
                            }
                        });

                    }).addOnFailureListener(e -> {
                        isLoading.setValue(false);
                        errorMessage.setValue("Error al obtener URL: " + e.getMessage());
                    })

            ).addOnFailureListener(e -> {
                isLoading.setValue(false);
                errorMessage.setValue("Error al subir imagen: " + e.getMessage());
            });

        } else {
            StoreModel storeData = new StoreModel(name, description, category, openHours,
                    address, phone, owner);
            storesRef.setValue(storeData).addOnCompleteListener(task -> {
                isLoading.setValue(false);
                if (task.isSuccessful()) {
                    registerSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Error al guardar producto: " +
                            Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }
}
