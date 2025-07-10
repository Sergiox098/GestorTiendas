package com.segomezco.gestortiendas.Product.CreateProduct;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.segomezco.gestortiendas.Product.ProductModel;

import java.util.Objects;

public class ProductCreateVM extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }

    public void CreateObject(String uid, String name, String ref, String description, String price,
                             String category, String brand, String stock, String weight, String supplier,
                             String contact, Uri imageUri) {

        isLoading.setValue(true);

        if (name == null || name.isEmpty()) {
            errorMessage.setValue("El nombre del producto es obligatorio");
            isLoading.setValue(false);
            return;
        }

        DatabaseReference productsRef = firebaseDatabase.getReference("Users")
                .child(uid)
                .child("Products")
                .child(name);

        if (imageUri != null) {
            StorageReference imageRef = firebaseStorage.getReference()
                    .child("Users").child(uid).child("Products").child(name).child("product_image.jpg");

            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {

                        ProductModel productData = new ProductModel(name, ref, description, price,
                                category, brand, stock, weight, supplier, contact);

                        productData.setImageUrl(downloadUri.toString());

                        productsRef.setValue(productData).addOnCompleteListener(task -> {
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
            ProductModel productData = new ProductModel(name, ref, description, price,
                    category, brand, stock, weight, supplier, contact);
            productsRef.setValue(productData).addOnCompleteListener(task -> {
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
