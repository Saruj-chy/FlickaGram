package com.sd.spartan.flickagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FlickerAdapter extends RecyclerView.Adapter<FlickerAdapter.FlickerViewHolder> {
    private Context mCtx;
    private List<FlickerModel> flickerList;

    public FlickerAdapter(Context mCtx, List<FlickerModel> flickerList) {
        this.mCtx = mCtx;
        this.flickerList = flickerList;
    }

    public void filterList(List<FlickerModel> filteredList) {
        flickerList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public FlickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate( R.layout.layout_image, null );
        return new FlickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickerViewHolder holder, int position) {
        final FlickerModel flickerModel = flickerList.get(position);

        holder.titleTV.setText(flickerModel.getTitle()) ;
        Log.e("TAGs", "url: "+flickerModel.getUrl_h() );

        Glide.with(mCtx)
                .load(flickerModel.getUrl_h())
                .placeholder(R.drawable.authen)
                .into(holder.imageView);

        int tempPos = position ;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, DetailActivity.class) ;
                intent.putExtra("pos", tempPos+"") ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

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
    }
}
