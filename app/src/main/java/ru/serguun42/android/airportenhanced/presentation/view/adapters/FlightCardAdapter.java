package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FlightCardBinding;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.presentation.view.FlightDetailsFragment;

public class FlightCardAdapter extends RecyclerView.Adapter<FlightCardAdapter.FlightViewHolder> {
    MainActivity mainActivity;
    List<Flight> flightList;

    public FlightCardAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.flightList = Collections.emptyList();
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FlightCardBinding binding = FlightCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
            holder.binding.cardTimeDeparture.setText(new SimpleDateFormat("HH:mm").format(departureDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flight.isIncoming()) holder.binding.cardTimeDeparture.setTextColor(mainActivity.getColor(R.color.red_800));

        try {
            Date arrivalDate = Date.from(
                    Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getArrival()))
            );
            holder.binding.cardTimeArrival.setText(new SimpleDateFormat("HH:mm").format(arrivalDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flight.isIncoming()) holder.binding.cardTimeArrival.setTextColor(mainActivity.getColor(R.color.blue_800));

        holder.binding.cardTitle.setText(flight.getTargetIATA() + " – " + flight.getTargetName());

        holder.binding.cardIcon.setImageDrawable(
                mainActivity.getDrawable(flight.isIncoming() ?
                        R.drawable.ic_baseline_flight_land_24 :
                        R.drawable.ic_baseline_flight_takeoff_24)
        );
        holder.binding.cardIcon.setColorFilter(
                mainActivity.getColor(flight.isIncoming() ?
                        R.color.blue_800 :
                        R.color.red_800),
                android.graphics.PorterDuff.Mode.SRC_IN
        );

        holder.binding.cardFlightNumber.setText(flight.getFlightNumber());
        holder.binding.cardGate.setText(flight.getGate());
        holder.binding.cardPlaneModel.setText(flight.getPlaneModel());

        holder.binding.flightCard.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(FlightDetailsFragment.FLIGHT_ID_EXTRA_KEY, flight.getId());
            Navigation.findNavController(mainActivity.binding.navHostFragment)
                    .navigate(R.id.action_flightsList_to_flightDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public void updateFlightList(List<Flight> flightList) {
        this.flightList.clear();
        this.flightList = flightList;
        notifyDataSetChanged();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        FlightCardBinding binding;

        public FlightViewHolder(@NonNull FlightCardBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
