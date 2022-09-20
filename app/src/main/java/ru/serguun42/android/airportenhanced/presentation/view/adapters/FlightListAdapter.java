package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import ru.serguun42.android.airportenhanced.PickActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.api.Flight;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {
    Context context;
    List<Flight> flightList;

    public FlightListAdapter(Context context, List<Flight> flightList) {
        this.context = context;
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flight_card, parent, false);
        return new FlightViewHolder(view);
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
            holder.timeDeparture.setText(new SimpleDateFormat("HH:mm").format(departureDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.cardTitle.setText(flight.getTargetIATA() + " – " + flight.getTargetName());

        if (flight.isIncoming()) {
            holder.icon.setImageDrawable(
                    context.getDrawable(R.drawable.ic_baseline_flight_land_24)
            );
            holder.icon.setColorFilter(
                    context.getColor(R.color.blue_800),
                    android.graphics.PorterDuff.Mode.SRC_IN
            );
        } else {
            holder.icon.setImageDrawable(
                    context.getDrawable(R.drawable.ic_baseline_flight_takeoff_24)
            );
            holder.icon.setColorFilter(
                    context.getColor(R.color.red_800),
                    android.graphics.PorterDuff.Mode.SRC_IN
            );
        }

        holder.icon.setImageDrawable(
                context.getDrawable(flight.isIncoming() ?
                        R.drawable.ic_baseline_flight_land_24 :
                        R.drawable.ic_baseline_flight_takeoff_24)
        );

        try {
            Date arrivalDate = Date.from(
                    Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getArrival()))
            );
            holder.timeArrival.setText(new SimpleDateFormat("HH:mm").format(arrivalDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.flightNumber.setText(flight.getFlightNumber());
        holder.gate.setText(flight.getGate());
        holder.planeModel.setText(flight.getPlaneModel());

        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, PickActivity.class);
            intent.putExtra(PickActivity.FLIGHT_ID_EXTRA_TYPE, flight.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {
        View card;
        TextView timeDeparture;
        TextView cardTitle;
        ImageView icon;
        TextView timeArrival;
        TextView flightNumber;
        TextView gate;
        TextView planeModel;

        public FlightViewHolder(@NonNull View flightCardView) {
            super(flightCardView);

            card = flightCardView;
            timeDeparture = flightCardView.findViewById(R.id.card_time_departure);
            cardTitle = flightCardView.findViewById(R.id.card_title);
            icon = flightCardView.findViewById(R.id.card_icon);
            timeArrival = flightCardView.findViewById(R.id.card_time_arrival);
            flightNumber = flightCardView.findViewById(R.id.card_flight_number);
            gate = flightCardView.findViewById(R.id.card_gate);
            planeModel = flightCardView.findViewById(R.id.card_plane_model);
        }
    }
}
