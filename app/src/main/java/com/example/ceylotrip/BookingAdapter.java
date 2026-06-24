package com.example.ceylotrip;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    Context context;
    List<BookingModel> bookingList;

    public BookingAdapter(Context context, List<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // අර අපි හදපු item_booking.xml කාඩ් ඩිසයින් එක මෙතනින් ගන්නවා
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // ලිස්ට් එකෙන් අදාළ බුකිං එක ගන්නවා
        BookingModel currentBooking = bookingList.get(position);

        // අකුරු ටික සෙට් කරනවා
        holder.tvPackageName.setText(currentBooking.getPackageName());
        holder.tvDate.setText("Date: " + currentBooking.getTravelDate());
        holder.tvPrice.setText("Total: Rs. " + currentBooking.getTotalPrice());

        // Status එක පෙන්වීම සහ ලස්සනට පාට වෙනස් කිරීම
        String status = currentBooking.getStatus();
        holder.tvStatus.setText("Status: " + status);

        if ("Pending".equals(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#FFA000")); // Pending නම් තැඹිලි පාටයි
        } else if ("Confirmed".equals(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#009772")); // Confirmed නම් කොළ පාටයි
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size(); // බුකිං කීයක් තියෙනවද කියලා ගණන් කරනවා
    }

    // කාඩ් එකේ තියෙන UI අංග ටික අඳුන්වලා දෙන Class එක
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPackageName, tvDate, tvStatus, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_booking.xml එකේ ID ටික සම්බන්ධ කිරීම
            tvPackageName = itemView.findViewById(R.id.tvBookedPackageName);
            tvDate = itemView.findViewById(R.id.tvBookedDate);
            tvStatus = itemView.findViewById(R.id.tvBookedStatus);
            tvPrice = itemView.findViewById(R.id.tvBookedPrice);
        }
    }
}