package com.segomezco.gestortiendas.Product.CreateProduct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProductCreateVM extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }

    public void CreateObject(String uid, String name, String ref, String description, String price,
     String category, String brand, String stock, String weight, String supplier, String contact) {

        isLoading.setValue(true);
        DatabaseReference productsRef = firebaseDatabase.getReference("Users").child(uid).child("Products");
        ProductModel productData = new ProductModel(name, ref, description, price, category,
                brand, stock, weight, supplier, contact);
        productsRef.push().setValue(productData).addOnCompleteListener(dbTask -> {
            isLoading.setValue(false);
            if (dbTask.isSuccessful()) {
                registerSuccess.setValue(true);
            } else {
                errorMessage.setValue("Error al guardar los datos en la base de datos"+
                        Objects.requireNonNull(dbTask.getException()).getMessage());
            }
        });
    }
}
