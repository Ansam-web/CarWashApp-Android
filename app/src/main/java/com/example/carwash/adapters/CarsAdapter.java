package com.example.carwash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.models.Car;
import java.util.ArrayList;
import java.util.List;

/**
 * CarsAdapter.java
 * RecyclerView Adapter for displaying cars list
 */
public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarViewHolder> {

    private Context context;
    private List<Car> carsList;
    private OnCarClickListener listener;

    public CarsAdapter(Context context) {
        this.context = context;
        this.carsList = new ArrayList<>();
    }

    public void setCarsList(List<Car> carsList) {
        this.carsList = carsList;
        notifyDataSetChanged();
    }

    public void setOnCarClickListener(OnCarClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carsList.get(position);
        holder.bind(car);
    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

        CardView cardCar;
        TextView tvCarName;
        TextView tvCarDetails;
        TextView tvPlateNumber;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            cardCar = itemView.findViewById(R.id.cardCar);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarDetails = itemView.findViewById(R.id.tvCarDetails);
            tvPlateNumber = itemView.findViewById(R.id.tvPlateNumber);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Car car) {
            // Display car info
            tvCarName.setText(car.getBrand() + " " + car.getModel());
            tvCarDetails.setText(car.getYear() + " • " + car.getColor() + " • " + car.getCarType());
            tvPlateNumber.setText(car.getPlateNumber());

            // Click listeners
            cardCar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCarClick(car);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCarEdit(car);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCarDelete(car);
                }
            });
        }
    }

    // Interface for click events
    public interface OnCarClickListener {
        void onCarClick(Car car);
        void onCarEdit(Car car);
        void onCarDelete(Car car);
    }
}