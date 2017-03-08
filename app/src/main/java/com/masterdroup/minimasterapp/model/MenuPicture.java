package com.masterdroup.minimasterapp.model;

import java.util.List;

/**
 * Created by 11473 on 2017/3/8.
 */

public class MenuPicture {
    int errorCode;
    String menuCover;//菜谱封面
    List<String> setpPicture;//准备步骤图
    List<String> cookingSetpPicture;//烹饪步骤图

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMenuCover() {
        return menuCover;
    }

    public void setMenuCover(String menuCover) {
        this.menuCover = menuCover;
    }

    public List<String> getSetpPicture() {
        return setpPicture;
    }

    public void setSetpPicture(List<String> setpPicture) {
        this.setpPicture = setpPicture;
    }

    public List<String> getCookingSetpPicture() {
        return cookingSetpPicture;
    }

    public void setCookingSetpPicture(List<String> cookingSetpPicture) {
        this.cookingSetpPicture = cookingSetpPicture;
    }
}
