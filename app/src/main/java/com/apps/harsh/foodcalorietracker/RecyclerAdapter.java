package com.apps.harsh.foodcalorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apps.harsh.foodcalorietracker.model.InfoModel;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<InfoModel> itemList;
    private Context context;

    public RecyclerAdapter(List<InfoModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);

        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final InfoModel item = itemList.get(itemPosition);

        holder.name.setText(item.getName());
        holder.fat.setText(item.getFat());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.protein.setText(item.getProteins());
        holder.carbs.setText(item.getCarbohydrates());
        holder.calories.setText(item.getCalories());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, fat, protein, carbs, calories, time;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textName);
            date = view.findViewById(R.id.textDate);
            time = view.findViewById(R.id.textTime);
            fat = view.findViewById(R.id.textFat);
            protein = view.findViewById(R.id.textProtein);
            carbs = view.findViewById(R.id.textCarbs);
            calories = view.findViewById(R.id.textCal);
        }
    }
}
