package com.example.carwash.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.models.Booking;
import com.example.carwash.utils.Helpers;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingsAdapter.java
 * RecyclerView Adapter for displaying bookings list
 */
public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingsList;
    private OnBookingClickListener listener;

    public BookingsAdapter(Context context) {
        this.context = context;
        this.bookingsList = new ArrayList<>();
    }

    public void setBookingsList(List<Booking> bookingsList) {
        this.bookingsList = bookingsList;
        notifyDataSetChanged();
    }

    public void setOnBookingClickListener(OnBookingClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingsList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {

        CardView cardBooking;
        TextView tvServiceName;
        TextView tvCarModel;
        TextView tvBookingDate;
        TextView tvBookingTime;
        TextView tvBookingStatus;
        TextView tvBookingPrice;
        View statusIndicator;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            cardBooking = itemView.findViewById(R.id.cardBooking);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvCarModel = itemView.findViewById(R.id.tvCarModel);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvBookingPrice = itemView.findViewById(R.id.tvBookingPrice);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }

        public void bind(Booking booking) {
            // Display booking info
            tvServiceName.setText(booking.getServiceName() != null ?
                    booking.getServiceName() : "Service");
            tvCarModel.setText(booking.getCarModel() != null ?
                    booking.getCarModel() : "Car");
            tvBookingDate.setText(booking.getBookingDate());
            tvBookingTime.setText(booking.getBookingTime());
            tvBookingPrice.setText(Helpers.formatPrice(booking.getTotalPrice()));

            // Set status
            String statusText = Helpers.getStatusText(booking.getStatus());
            tvBookingStatus.setText(statusText);

            // Set status color
            String statusColor = Helpers.getStatusColor(booking.getStatus());
            tvBookingStatus.setTextColor(Color.parseColor(statusColor));
            statusIndicator.setBackgroundColor(Color.parseColor(statusColor));

            // Click listener
            cardBooking.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookingClick(booking);
                }
            });
        }
    }

    // Interface for click events
    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }
}