package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masterdroup.minimasterapp.R;

import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/20.
 */

public class DeviceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_device, container, false);
        ButterKnife.bind(view);

        return view;
    }
}
