package com.example.my_campus.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.my_campus.R;

public class fragmentHostelInfo extends Fragment {

    ConstraintLayout buttonChanakya, buttonAryabhata, buttonGodavari, buttonBuddha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hostel_info, container, false);

        buttonAryabhata = view.findViewById(R.id.aryabhata);
        buttonChanakya = view.findViewById(R.id.chanakya);
        buttonGodavari = view.findViewById(R.id.godavari);
        buttonBuddha = view.findViewById(R.id.buddha);

        buttonAryabhata.setOnClickListener(v -> {
            nextFragment("aryabhata");
            clickAnimation(v);
        });

        buttonChanakya.setOnClickListener(v -> {
            nextFragment("chanakya");
            clickAnimation(v);
        });

        buttonGodavari.setOnClickListener(v -> {
            nextFragment("godavari");
            clickAnimation(v);
        });

        buttonBuddha.setOnClickListener(v -> {
            nextFragment("buddha");
            clickAnimation(v);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Fragment fragment = getParentFragmentManager().findFragmentByTag("fragmentHomepage");

                        if (fragment == null || !fragment.isVisible()) {
                            // User is NOT on homepage → go to homepage
                            fragmentHomepage homePageFragment = new fragmentHomepage();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.mainLayout, homePageFragment, "fragmentHomepage");
                            transaction.commit();
                        } else {
                            // User is already on homepage → exit app
                            requireActivity().finish();
                        }
                    }
                }
        );
    }

    private void nextFragment(String hostel) {
        Bundle bundle = new Bundle();
        bundle.putString("hostel", hostel);

        fragmentSelectedHostelInfo fragmentSelectedHostelInfo = new fragmentSelectedHostelInfo();
        fragmentSelectedHostelInfo.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        transaction.replace(R.id.mainLayout, fragmentSelectedHostelInfo);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clickAnimation(View v) {
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() ->
                v.animate().scaleX(1f).scaleY(1f).setDuration(100));
    }
}
