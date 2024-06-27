package com.example.finalejava.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.finalejava.R;
import com.example.finalejava.data.Root;
import com.example.finalejava.data.imageAdapter;
import com.example.finalejava.databinding.FragmentHomeBinding;
import com.example.finalejava.repasiyory.RootApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Root> list;
    private imageAdapter adapter;
    private GridLayoutManager manager;
    private int page = 1;
    private String imageUrl;
    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
       //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ImageSlider imageSlider = binding.imageSlider;

        // Create image list
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.lione, "", ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.art1, "", ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.art2, "",ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.bier, "",ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.art, "",ScaleTypes.FIT));

        // Set the image list to the ImageSlider
        imageSlider.setImageList(imageList);

        recyclerView = binding.recylerview;
        list = new ArrayList<>();
        adapter =new imageAdapter(list,getContext());
        manager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) { // Check if the user has scrolled to the bottom
                    page++;
                    getData();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getData() {
        RootApiClient.getRootService().getImages(page,30)
                .enqueue(new Callback<List<Root>>() {
                    @Override
                    public void onResponse(Call<List<Root>> call, @NonNull Response<List<Root>> response) {
                        if(response.body() != null){
                            list.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Root>> call, @NonNull Throwable throwable) {
                        Log.e(TAG, "Failed to fetch data: " + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });


    }
}