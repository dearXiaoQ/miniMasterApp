package com.masterdroup.minimasterapp.module.welcomeModule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masterdroup.minimasterapp.R;

/**
 * Created by 11473 on 2016/11/29.
 */

public class RegisteredFragment extends Fragment implements Contract.RegisteredView{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_welcome_view3, container, false);

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }
}
