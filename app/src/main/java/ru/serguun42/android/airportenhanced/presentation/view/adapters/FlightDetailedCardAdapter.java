package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

    @SuppressLint({"UseCompatLoadingForDrawables", "SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        if (flight == null) flight = new Flight();


        try {
            if (flight.getDeparture() != null && !flight.getDeparture().isEmpty()) {
                Date departureDate = Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getDeparture())));
                holder.binding.detailedTimeDeparture.setText(new SimpleDateFormat("EEE, MMM d HH:mm").format(departureDate));
            }
        } catch (Exception ignored) {
            holder.binding.detailedTimeDeparture.setText("—");
        }

        try {
            if (flight.getArrival() != null && !flight.getArrival().isEmpty()) {
                Date arrivalDate = Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getArrival())));
                holder.binding.detailedTimeArriving.setText(new SimpleDateFormat("EEE, MMM d HH:mm").format(arrivalDate));
            }
        } catch (Exception ignored) {
            holder.binding.detailedTimeArriving.setText("—");
        }

        holder.binding.detailedTargetName.setText(flight.getTargetName());
        holder.binding.detailedTargetIata.setText(flight.getTargetIATA());

        holder.binding.detailedIcon.setImageDrawable(
                mainActivity.getDrawable(flight.isIncoming() ?
                        R.drawable.ic_baseline_flight_land_24 :
                        R.drawable.ic_baseline_flight_takeoff_24)
        );
        holder.binding.detailedIcon.setColorFilter(
                mainActivity.getColor(flight.isIncoming() ?
                        R.color.arriving_color :
                        R.color.departing_color),
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

        String flightId = flight.getId();
        if (flightId == null || flightId.isEmpty())
            holder.binding.shareFlight.setVisibility(View.GONE);
        else {
            holder.binding.shareFlight.setVisibility(View.VISIBLE);
            holder.binding.shareFlight.setOnClickListener(view -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "app://airport.serguun42.ru/flight?id=" + flightId);
                sendIntent.setType("text/html");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                mainActivity.startActivity(shareIntent);
            });
        }

        String flightImage = flight.getImage();
        if (flightImage != null && !flightImage.isEmpty()) {
            holder.binding.recyclerview.setVisibility(View.VISIBLE);

            ImageAdapter imageAdapter = new ImageAdapter(mainActivity, false, Arrays.asList(flight));
            holder.binding.recyclerview.setHasFixedSize(true);
            holder.binding.recyclerview.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
            holder.binding.recyclerview.setAdapter(imageAdapter);
        } else
            holder.binding.recyclerview.setVisibility(View.GONE);
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
