package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FlightDetailedCardBinding;
import ru.serguun42.android.airportenhanced.domain.model.Flight;

public class FlightDetailedCardAdapter extends RecyclerView.Adapter<FlightDetailedCardAdapter.FlightViewHolder> {
    MainActivity mainActivity;
    List<Flight> flightList;

    public FlightDetailedCardAdapter(MainActivity mainActivity, List<Flight> flightList) {
        this.mainActivity = mainActivity;
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FlightDetailedCardBinding binding = FlightDetailedCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FlightViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"UseCompatLoadingForDrawables", "SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);


        try {
            Date departureDate = Date.from(
                    Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getDeparture()))
            );
            holder.binding.detailedTimeDeparture.setText(new SimpleDateFormat("EEE, MMM d HH:mm").format(departureDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Date arrivalDate = Date.from(
                    Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getArrival()))
            );
            holder.binding.detailedTimeArriving.setText(new SimpleDateFormat("EEE, MMM d HH:mm").format(arrivalDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.binding.detailedTarget.setText(flight.getTargetIATA() + " – " + flight.getTargetName());

        holder.binding.detailedIcon.setImageDrawable(
                mainActivity.getDrawable(flight.isIncoming() ?
                        R.drawable.ic_baseline_flight_land_24 :
                        R.drawable.ic_baseline_flight_takeoff_24)
        );
        holder.binding.detailedIcon.setColorFilter(
                mainActivity.getColor(flight.isIncoming() ?
                        R.color.blue_800 :
                        R.color.red_800),
                android.graphics.PorterDuff.Mode.SRC_IN
        );

        holder.binding.detailedFlightType.setText(
                mainActivity.getString(flight.isIncoming() ?
                        R.string.incoming_type :
                        R.string.departing_type)
        );
        holder.binding.detailedFromOrTo.setText(
                mainActivity.getString(flight.isIncoming() ?
                        R.string.from :
                        R.string.to)
        );

        holder.binding.detailedFlightNumber.setText(flight.getFlightNumber());
        holder.binding.detailedGate.setText(flight.getGate());
        holder.binding.detailedPlaneModel.setText(flight.getPlaneModel());
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        FlightDetailedCardBinding binding;

        public FlightViewHolder(@NonNull FlightDetailedCardBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
