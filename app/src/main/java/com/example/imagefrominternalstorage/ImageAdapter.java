package com.example.imagefrominternalstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    ArrayList<String> images = new ArrayList<>();
    Context context;
    String folderName;
    File path;
    String fileName[];


    public void setAdapterData(Context context,String foldername) {
        this.context = context;
        this.folderName=foldername;
        path = context.getDir(foldername,Context.MODE_PRIVATE);
        fileName= path.list();
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_row_item,parent,false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        try {
            File f=new File(path,fileName[position] );
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imageView.setImageBitmap(b);
        }
        catch (Exception e)
        {
            Log.i("adapter",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return (fileName.length+1);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ItemImage);
        }
    }
}
