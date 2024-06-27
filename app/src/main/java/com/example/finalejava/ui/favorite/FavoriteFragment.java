package com.example.finalejava.ui.favorite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalejava.data.Root;
import com.example.finalejava.data.favoriteAdapter;
import com.example.finalejava.databinding.FragmentFavoriteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private static final String TAG = "FavoritesActivity";
    private RecyclerView recyclerView;
    private ArrayList<Root> list;
    private favoriteAdapter adapter;
    private GridLayoutManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavoriteViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FavoriteViewModel.class);

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerviewf;
        list = new ArrayList<>();
        adapter = new favoriteAdapter( getContext(),list);
        manager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        //fetchFavorites();
        loadFavorites();


        return root;
    }

    private void loadFavorites() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("favorites")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Root root = document.toObject(Root.class);
                                list.add(root);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }
    {/*private void fetchFavorites() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Root root = document.toObject(Root.class);
                            list.add(root);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }*/}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}