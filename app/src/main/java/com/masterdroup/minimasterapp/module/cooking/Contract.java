package com.masterdroup.minimasterapp.module.cooking;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;

/**
 * Created by jun on 2017/3/23.
 */

public class Contract {

    interface Presenter extends BasePresenter {


        void initDevice(GizWifiDevice device);


        /**
         * @param recipesBeanID 菜谱id
         */
        void initRecipes(String recipesBeanID);

        /**
         * 直接设置功率
         *
         * @param power 功率
         */
        void upDataDeviceInfoPower(int power);

        /**
         * 开关设备
         */
        void upDataDeviceInfoSwitch();


    }

    interface CookingView extends BaseView<Presenter> {


        /**
         * 设置步骤信息
         *
         * @param step 当前步骤
         */
        void setStepInfo(int step);


    }
}
