package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.databinding.UserCardBinding;
import ru.serguun42.android.airportenhanced.domain.model.UserRecord;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersRecordViewHolder> {
    MainActivity mainActivity;
    List<UserRecord> usersList;

    public UsersAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.usersList = Collections.emptyList();
    }

    @NonNull
    @Override
    public UsersRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersRecordViewHolder(UserCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull UsersRecordViewHolder holder, int position) {
        UserRecord userRecord = usersList.get(position);
        if (userRecord == null) return;

        holder.binding.username.setText(userRecord.getUsername());
        holder.binding.userLevel.setText(Integer.toString(userRecord.getLevel()));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void updateUsersList(List<UserRecord> usersList) {
        this.usersList.clear();
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public static class UsersRecordViewHolder extends RecyclerView.ViewHolder {
        UserCardBinding binding;

        public UsersRecordViewHolder(@NonNull UserCardBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
