package com.segomezco.gestortiendas.Product.ReadProduct;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.segomezco.gestortiendas.Product.ProductModel;
import com.segomezco.gestortiendas.databinding.CardviewProductBinding;
import com.segomezco.gestortiendas.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<ProductModel> productList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductModel product);
    }

    public ProductAdapter(List<ProductModel> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewProductBinding binding = CardviewProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(productList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final CardviewProductBinding binding;

        public ProductViewHolder(CardviewProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel product, OnItemClickListener listener) {
            binding.NameProduct.setText(product.getName());
            binding.PriceProduct.setText("$" + product.getPrice());
            binding.StockProduct.setText("Stock: " + product.getStock());

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_warning)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(binding.ivIcon);
            } else {
                binding.ivIcon.setImageResource(R.drawable.ic_shopping_bag);
            }

            binding.cardProducts.setOnClickListener(v -> listener.onItemClick(product));
        }

    }
}
