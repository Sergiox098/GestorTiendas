package com.segomezco.gestortiendas.Store.ReadStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.segomezco.gestortiendas.R;
import com.segomezco.gestortiendas.Store.StoreModel;
import com.segomezco.gestortiendas.databinding.StoreCardviewBinding;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private final List<StoreModel> storeList;
    private final OnItemClickListener listener;
    private final Context context;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(StoreModel product);
    }

    public StoreAdapter(Context context, List<StoreModel> productList, OnItemClickListener listener) {
        this.context = context;
        this.storeList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StoreCardviewBinding binding = StoreCardviewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new StoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        holder.bind(storeList.get(position), position == selectedPosition);

        holder.binding.radioSelectStore.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            int previousPosition = selectedPosition;
            selectedPosition = adapterPosition;

            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            listener.onItemClick(storeList.get(adapterPosition));
        });

        holder.binding.cardProducts.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            int previousPosition = selectedPosition;
            selectedPosition = adapterPosition;

            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            SharedPreferences prefs = context.getSharedPreferences("store_prefs", Context.MODE_PRIVATE);
            prefs.edit().putString("selected_store_name", storeList.get(adapterPosition).getName()).apply();

            listener.onItemClick(storeList.get(adapterPosition));
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        private final StoreCardviewBinding binding;

        public StoreViewHolder(StoreCardviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StoreModel product, boolean isSelected) {

            binding.NameStore.setText(product.getName());
            binding.DescriptionStore.setText(product.getDescription());
            binding.AddressStore.setText(product.getAddress());
            binding.PhoneStore.setText(product.getPhone());
            binding.CategoryStore.setText(product.getCategory());
            binding.ScheduleStore.setText(product.getOpenHours());
            binding.OwnerStore.setText(product.getOwner());

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_warning)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(binding.ivIcon);
            } else {
                binding.ivIcon.setImageResource(R.drawable.ic_store);
            }

            binding.radioSelectStore.setChecked(isSelected);
        }
    }
}
