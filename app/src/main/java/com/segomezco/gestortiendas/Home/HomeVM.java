package com.segomezco.gestortiendas.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.segomezco.gestortiendas.Authentication.Register.RegisterUserM;
import com.segomezco.gestortiendas.Product.ProductModel;
import com.segomezco.gestortiendas.Store.StoreModel;

import java.util.ArrayList;
import java.util.List;

public class HomeVM extends ViewModel {

    private final MutableLiveData<String> userNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> urlImageLiveData = new MutableLiveData<>();

    public LiveData<String> geturlImageLiveData() {return urlImageLiveData;}
    public LiveData<String> getUserNameLiveData() {return userNameLiveData;}

    public void readDatabaseImage(String uid, String selectedStore) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDataUser = database.getReference("Users").child(uid);
        DatabaseReference refDataStore = refDataUser.child("Stores").child(selectedStore);

        refDataStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StoreModel storeData = snapshot.getValue(StoreModel.class);
                if (storeData != null) {
                    urlImageLiveData.setValue(storeData.getImageUrl());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeVM", "Error al leer la URL de la tienda: " + error.getMessage());
            }
        });
    }

    public void readDatabaseName(String uid){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDataUser = database.getReference("Users").child(uid);

        refDataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterUserM userData = snapshot.getValue(RegisterUserM.class);
                if (userData != null) {
                    userNameLiveData.setValue(userData.getUserName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeVM", "Error al leer el nombre del usuario: " + error.getMessage());
            }
        });
    }
}
