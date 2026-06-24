package com.example.ceylotrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    Context context;
    List<PackageModel> packageList; // කලින් හදපු Model එකේ නම PackageModel කියලා හිතමු

    public PackageAdapter(Context context, List<PackageModel> packageList) {
        this.context = context;
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // අර අපි හදපු item_package.xml ඩිසයින් එක මෙතනින් සම්බන්ධ කරනවා
        View view = LayoutInflater.from(context).inflate(R.layout.item_package, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // ලිස්ට් එකෙන් අදාළ පැකේජ් එක ගන්නවා
        PackageModel currentPackage = packageList.get(position);

        // අකුරු ටික සෙට් කරනවා
        holder.tvTitle.setText(currentPackage.getTitle());
        holder.tvDuration.setText(currentPackage.getDuration());
        holder.tvPrice.setText("Rs. " + currentPackage.getPrice());

        // Glide පාවිච්චි කරලා Internet එකෙන් පින්තූරය Load කරනවා
        Glide.with(context)
                .load(currentPackage.getImageUrl())
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return packageList.size(); // පැකේජ් කීයක් තියෙනවද කියලා ගණන් කරනවා
    }

    // කාඩ් එකේ තියෙන UI අංග ටික අඳුන්වලා දෙන Class එක
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDuration, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivPackageImage);
            tvTitle = itemView.findViewById(R.id.tvPackageTitle);
            tvDuration = itemView.findViewById(R.id.tvPackageDuration);
            tvPrice = itemView.findViewById(R.id.tvPackagePrice);
        }
    }
}