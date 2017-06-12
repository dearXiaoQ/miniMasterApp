package com.masterdroup.minimasterapp.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.masterdroup.minimasterapp.R;

/**
 * Created by xiaoQ on 2017/6/8.
 */

public class ToastUtils {

    public static final int TOAST_BOTTOM = 0;
    public static final int TOAST_CENTER = 1;
    public static final int TOAST_TOP    = 2;

    /**
     *
     * @param context   Context
     * @param position
     * @param content
     */
    public static void showCustomToast(Context context, int position, String content) {

        if(content == null) {
            return ;
        }

        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.exit_view_item, null);
        TextView toastTv = (TextView) view.findViewById(R.id.toast_tv);
        toastTv.setText(content);
        int gravity;

        switch (position) {

            case TOAST_TOP :
                gravity = Gravity.TOP;
                break;

            case TOAST_CENTER :
                gravity = Gravity.CENTER;
                break;

            default:
                gravity = Gravity.BOTTOM;
                break;



        }

        toast.setGravity(gravity, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

}
