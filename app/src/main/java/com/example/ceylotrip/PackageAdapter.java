package com.example.ceylotrip;

import android.content.Context;
import android.content.Intent;
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
    List<PackageModel> packageList;

    public PackageAdapter(Context context, List<PackageModel> packageList) {
        this.context = context;
        this.packageList = packageList;
    }

    // ෆිල්ටර් කරන දත්ත ලිස්ට් එකට දාන්න අලුතින් හදපු Method එක
    public void setFilteredList(List<PackageModel> filteredList) {
        this.packageList = filteredList;
        notifyDataSetChanged(); // මේකෙන් තමයි තිරයේ දත්ත අලුත් වෙන්න කියලා App එකට කියන්නේ
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_package, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageModel currentPackage = packageList.get(position);

        holder.tvTitle.setText(currentPackage.getTitle());
        holder.tvDuration.setText(currentPackage.getDuration());
        holder.tvPrice.setText("Rs. " + currentPackage.getPrice());

        Glide.with(context)
                .load(currentPackage.getImageUrl())
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("title", currentPackage.getTitle());
                intent.putExtra("duration", currentPackage.getDuration());
                intent.putExtra("price", currentPackage.getPrice());
                intent.putExtra("imageUrl", currentPackage.getImageUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

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