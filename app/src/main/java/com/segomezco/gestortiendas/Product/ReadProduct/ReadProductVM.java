package com.segomezco.gestortiendas.Product.ReadProduct;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.segomezco.gestortiendas.Product.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ReadProductVM extends ViewModel {

    private final MutableLiveData<List<ProductModel>> productListLiveData = new MutableLiveData<>();

    public LiveData<List<ProductModel>> getProductListLiveData() {
        return productListLiveData;
    }

    public void readDatabase(String uid, String selectedStore) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refProducts = database.getReference("Users").child(uid)
                .child("Stores").child(selectedStore).child("Products");

        refProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ProductModel> products = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ProductModel product = child.getValue(ProductModel.class);
                    if (product != null) {
                        products.add(product);
                    }
                }
                productListLiveData.setValue(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ReadProductVM", "Error al leer productos: " + error.getMessage());
            }
        });
    }
}
