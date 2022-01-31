package com.sd.spartan.flickagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sd.spartan.flickagram.BuildConfig;
import com.sd.spartan.flickagram.activity.MainActivity;
import com.sd.spartan.flickagram.R;

public class MyFragment extends Fragment {
    TextView titleTV, shareLinkTV, shareImageTV  ;
    int position ;
    public MyFragment() {

    }


    public static MyFragment newInstance(int position) {
        MyFragment fragmentFirst = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        position = getArguments().getInt("position", 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        ImageView image = view.findViewById(R.id.image_detail);
        titleTV = view.findViewById(R.id.text_title);
        shareLinkTV = view.findViewById(R.id.text_share_link);
        shareImageTV = view.findViewById(R.id.text_share_image);

        titleTV.setText(MainActivity.flickerModelList.get(position).getTitle());
        Glide.with(requireActivity())
                .load(MainActivity.flickerModelList.get(position).getUrl_h())
                .placeholder(R.drawable.flickr)
                .into(image);


        shareLinkTV.setOnClickListener(view12 -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Please share my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        shareImageTV.setOnClickListener(view1 -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse("android.resource://com.android.test/*");
            sharingIntent.setType("image/jpeg");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        });


        return view ;
    }
}