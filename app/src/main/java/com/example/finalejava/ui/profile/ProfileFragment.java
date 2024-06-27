package com.example.finalejava.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.finalejava.databinding.FragmentProfileBinding;
import com.example.finalejava.loginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    FirebaseAuth auth;
    MaterialButton signOutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();




        signOutButton = binding.signout;

        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(binding.imageViewProfile);
            binding.textViewName.setText(user.getDisplayName());
            binding.textViewEmail.setText(user.getEmail());
            binding.textViewphone.setText(user.getPhoneNumber());
        }

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(getActivity(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), loginActivity.class));
                requireActivity().finish();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}