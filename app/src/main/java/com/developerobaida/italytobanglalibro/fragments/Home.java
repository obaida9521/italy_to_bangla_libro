package com.developerobaida.italytobanglalibro.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developerobaida.italytobanglalibro.adLibrary.AdmobAd;
import com.developerobaida.italytobanglalibro.adLibrary.AdmobAdCallBack;
import com.developerobaida.italytobanglalibro.adapters.AdapterCategory;
import com.developerobaida.italytobanglalibro.api.ApiController;
import com.developerobaida.italytobanglalibro.databinding.FragmentHomeBinding;
import com.developerobaida.italytobanglalibro.models.ModelCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {

    FragmentHomeBinding binding;
    AdapterCategory adapter;
    List<ModelCategory> arrayList = new ArrayList<>();
    Call<List<ModelCategory>> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        new AdmobAd(getActivity(), new AdmobAdCallBack() {
            @Override
            public void onAdLoaded() {

            }
        }).loadBanner(binding.bannerAd);
        getData();


        return binding.getRoot();
    }

    private void getData() {
        binding.progress.setVisibility(View.VISIBLE);
        call = ApiController.getInstance().getApi().showAllCategories();
        call.enqueue(new Callback<List<ModelCategory>>() {
            @Override
            public void onResponse(Call<List<ModelCategory>> call, Response<List<ModelCategory>> response) {
                binding.progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    arrayList = response.body();
                    Log.d("response", arrayList.toString());

                    adapter = new AdapterCategory(getContext(), arrayList);
                    binding.itemList.setAdapter(adapter);
                    binding.itemList.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    Log.e("error", "Response body is null or unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<ModelCategory>> call, Throwable throwable) {
                binding.progress.setVisibility(View.GONE);
                binding.noConnection.setVisibility(View.VISIBLE);
                Log.e("API Error", throwable.getMessage(), throwable);
            }
        });
    }
}