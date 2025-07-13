package com.segomezco.gestortiendas.Store.ReadStore;

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
import com.segomezco.gestortiendas.Store.StoreModel;

import java.util.ArrayList;
import java.util.List;

public class StoreReadVM extends ViewModel {

    private final MutableLiveData<List<StoreModel>> storeListLiveData = new MutableLiveData<>();

    public LiveData<List<StoreModel>> getProductListLiveData() {
        return storeListLiveData;
    }

    public void readDatabase(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refStore = database.getReference("Users").child(uid).child("Stores");

        refStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<StoreModel> stores = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    StoreModel store = child.getValue(StoreModel.class);
                    if (store != null) {
                        stores.add(store);

                    }
                }
                storeListLiveData.setValue(stores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("StoreProductVM", "Error al leer tiendas: " + error.getMessage());
            }
        });
    }
}
