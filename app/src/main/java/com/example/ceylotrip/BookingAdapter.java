package com.example.ceylotrip;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    Context context;
    List<BookingModel> bookingList;
    OnBookingClickListener listener;

    // බොත්තම් එබුවම වැඩ කරන්න Interface එකක් හැදීම
    public interface OnBookingClickListener {
        void onEditClick(BookingModel booking);
        void onDeleteClick(BookingModel booking, int position);
    }

    public BookingAdapter(Context context, List<BookingModel> bookingList, OnBookingClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel currentBooking = bookingList.get(position);

        holder.tvPackageName.setText(currentBooking.getPackageName());
        holder.tvDate.setText("Date: " + currentBooking.getTravelDate());
        holder.tvPrice.setText("Total: Rs. " + currentBooking.getTotalPrice());

        String status = currentBooking.getStatus();
        holder.tvStatus.setText("Status: " + status);

        if ("Pending".equals(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#FFA000"));
        } else if ("Confirmed".equals(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#009772"));
        }

        // බොත්තම් Click කිරීම්
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(currentBooking));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(currentBooking, position));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPackageName, tvDate, tvStatus, tvPrice;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPackageName = itemView.findViewById(R.id.tvBookedPackageName);
            tvDate = itemView.findViewById(R.id.tvBookedDate);
            tvStatus = itemView.findViewById(R.id.tvBookedStatus);
            tvPrice = itemView.findViewById(R.id.tvBookedPrice);

            // අලුත් බොත්තම් දෙක සම්බන්ධ කිරීම
            btnEdit = itemView.findViewById(R.id.btnEditBooking);
            btnDelete = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}