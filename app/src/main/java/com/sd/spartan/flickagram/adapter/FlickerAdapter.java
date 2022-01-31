package com.sd.spartan.flickagram.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sd.spartan.flickagram.R;
import com.sd.spartan.flickagram.activity.DetailActivity;
import com.sd.spartan.flickagram.model.FlickerModel;

import java.util.List;
@SuppressLint("StaticFieldLeak")
public class FlickerAdapter extends RecyclerView.Adapter<FlickerAdapter.FlickerViewHolder> {
    private static Context mCtx;
    private final List<FlickerModel> flickerList;

    public FlickerAdapter(Context mCtx, List<FlickerModel> flickerList) {
        FlickerAdapter.mCtx = mCtx;
        this.flickerList = flickerList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public FlickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate( R.layout.layout_image, null );
        return new FlickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickerViewHolder holder, int position) {
        final FlickerModel flickerModel = flickerList.get(position);

        holder.bind(flickerModel, position) ;

    }

    @Override
    public int getItemCount() {
        return flickerList.size();
    }

    static class FlickerViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV ;
        ImageView imageView;
        public FlickerViewHolder(View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.text_title);
            imageView = itemView.findViewById(R.id.image_flicker);

        }

        public void bind(FlickerModel flickerModel, int position) {
            titleTV.setText(flickerModel.getTitle()) ;

            Glide.with(mCtx)
                    .load(flickerModel.getUrl_h())
                    .placeholder(R.drawable.flickr)
                    .into(imageView);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mCtx, DetailActivity.class) ;
                intent.putExtra("pos", position+"") ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            });
        }
    }
}
