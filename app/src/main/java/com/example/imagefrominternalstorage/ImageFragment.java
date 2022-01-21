package com.example.imagefrominternalstorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imagefrominternalstorage.databinding.FragmentImageBinding;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferences.getString("hasImages","false").equals("false")){
            storeImageInInternal();
        }
    }

    private FragmentImageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater,container,false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String Folder = preferences.getString("FolderPath","Images");
        setActionColor(getHexColor(preferences.getString("ActionColor","Blue")));
        setStatusColor(getHexColor(preferences.getString("StatusColor","Blue")));


        File Directory = getContext().getDir(Folder, Context.MODE_PRIVATE);
        Log.i("data",Directory.list().toString());

        binding.rcvImages.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageAdapter adapter = new ImageAdapter();
        adapter.setAdapterData(getContext(),Folder);
        binding.rcvImages.setAdapter(adapter);



    }

    private void storeImageInInternal() {
        File Directory[] = {getContext().getDir("Images",Context.MODE_PRIVATE),
                            getContext().getDir("Picture",Context.MODE_PRIVATE),
                            getContext().getDir("Download",Context.MODE_PRIVATE),
                             getContext().getDir("Photos",Context.MODE_PRIVATE)};

        for(int i=0; i<Directory.length;i++){
            FileOutputStream fileOutputStream = null;
            String fileName1 = String.valueOf(i+1)+".jpg";
            String fileName2 = String.valueOf(i+4)+".jpg";
            try {
                Log.i("storeImageInInternal",getContext().getAssets().toString());
                //we want to add to image file in single folder from assets folder
                //for first image
                File file1 = new File(Directory[i],fileName1);
                fileOutputStream = new FileOutputStream(file1);
                Bitmap bitmap1 = BitmapFactory.decodeStream(getContext().getAssets().open(fileName1));
                bitmap1.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                //for second image
                File file2 = new File(Directory[i],fileName2);
                fileOutputStream = new FileOutputStream(file2);
                Bitmap bitmap2 = BitmapFactory.decodeStream(getContext().getAssets().open(fileName2));
                bitmap2.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            }
            catch (Exception e){
                Log.i("storeImageInInternal",e.toString());
            }
            finally {
                try {
                    fileOutputStream.close();
                }catch (Exception e){
                    Log.i("fileOutputStreamClose",e.toString());
                }
            }

        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("hasImages","True");
        editor.commit();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.setting){
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.Activity_Main,new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionColor(String color) {
        ColorDrawable drawable = new ColorDrawable(Color.parseColor(color));
        MainActivity.actionBar.setBackgroundDrawable(drawable);
    }
    public void setStatusColor(String color){
        requireActivity().getWindow().setStatusBarColor(Color.parseColor(color));
    }
    public static String getHexColor(String colorName){
        switch (colorName){
            case "Red":
                return "#FF0000";

            case "Green":
                return "#4CFF00";

            case "Yellow":
                return "#FFDD00";

            case "Pink":
                return "#FF009A";

            case "Blue":
                return "#FF6200EE";

            case "Teal":
                return "#FF03DAC5";

            case "Gray":
                return "#878787";

            case "Black":
                return "#FF000000";
        }
        return "#FF6200EE";
    }
}