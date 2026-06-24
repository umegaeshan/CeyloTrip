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

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    Context context;
    List<PlaceModel> placeList;

    public PlaceAdapter(Context context, List<PlaceModel> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_package, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceModel model = placeList.get(position); // PlaceModel භාවිතා කළා
        holder.tvName.setText(model.getName());
        holder.tvDesc.setText(model.getDescription());
        holder.tvPrice.setText(model.getPrice());

        Glide.with(context)
                .load(model.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.ivPlace);
    }

    @Override
    public int getItemCount() { return placeList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;
        ImageView ivPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPackageTitle);
            tvDesc = itemView.findViewById(R.id.tvPackageDuration);
            tvPrice = itemView.findViewById(R.id.tvPackagePrice);
            ivPlace = itemView.findViewById(R.id.ivPackageImage);
        }
    }
}