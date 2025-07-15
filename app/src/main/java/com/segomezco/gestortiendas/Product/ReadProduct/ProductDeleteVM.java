package com.segomezco.gestortiendas.Product.ReadProduct;

import android.widget.Toast;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductDeleteVM  extends ViewModel {

    private MutableLiveData<Boolean> IsSuccess = new MutableLiveData<>();
    public LiveData<Boolean> getSuccess() {return IsSuccess;}

    public void deleteProduct(Context context,String userID, String storeName, String productName) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(userID).child("Stores").child(storeName)
                .child("Products").child(productName);

        dbRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StorageReference imgRef = FirebaseStorage.getInstance().getReference("Users")
                        .child(userID).child("Stores").child(storeName)
                        .child("Products").child(productName).child("product_image.jpg");

                imgRef.delete().addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Producto e imagen eliminados", Toast.LENGTH_SHORT).show();
                    IsSuccess.setValue(true);
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Producto eliminado (sin imagen)", Toast.LENGTH_SHORT).show();
                    IsSuccess.setValue(true);
                });
            } else {
                Toast.makeText(context, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                IsSuccess.setValue(false);
            }
        });
    }


}
