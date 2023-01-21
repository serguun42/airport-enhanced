package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.databinding.ImageElementBinding;
import ru.serguun42.android.airportenhanced.domain.model.Flight;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageSliderViewHolder> {
    MainActivity mainActivity;
    boolean adding;
    List<Flight> flightList;

    public ImageAdapter(MainActivity mainActivity, boolean adding, List<Flight> flights) {
        this.mainActivity = mainActivity;
        this.adding = adding;
        this.flightList = flights;
    }

    @NonNull
    @NotNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ImageSliderViewHolder(ImageElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageSliderViewHolder holder, int position) {
        if (flightList.get(position) != null && flightList.get(position).getImage() != null && !flightList.get(position).getImage().isEmpty()) {
            holder.binding.imageAdd.setVisibility(View.GONE);
            holder.binding.imageContent.setVisibility(View.VISIBLE);

            if (mainActivity != null) {
                try {
                    holder.binding.imageContent.setImageBitmap(
                            BitmapFactory.decodeFileDescriptor(
                                    mainActivity.getApplicationContext().getContentResolver().openFileDescriptor(
                                            Uri.parse(flightList.get(position).getImage()), "r").getFileDescriptor()
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            holder.binding.imageContent.setVisibility(View.GONE);
            holder.binding.imageAdd.setVisibility(View.VISIBLE);
            holder.binding.imageAdd.setOnClickListener((View v) -> {
                if (mainActivity != null) {
                    mainActivity.getActivityResultRegistry().register("key", new ActivityResultContracts.OpenDocument(), result -> {
                        if (result != null) {
                            mainActivity.getApplicationContext().getContentResolver().takePersistableUriPermission(
                                    result,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            Flight flight = flightList.get(position);
                            flight.setImage(result.toString());

                            notifyDataSetChanged();
                        }
                    }).launch(new String[]{"image/*"});
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public void updateFlights(List<Flight> flights) {
        this.flightList = flights;
        notifyDataSetChanged();
    }

    public List<String> getImages() {
        return this.flightList.stream().map(flight -> {
            if (flight == null) return null;
            return flight.getImage();
        }).collect(Collectors.toList());
    }

    class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        ImageElementBinding binding;

        public ImageSliderViewHolder(ImageElementBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
