package com.masterdroup.minimasterapp.model;

import java.util.List;

/**
 * Created by 11473 on 2017/2/28.
 */

public class RecipesList {

    int total;
    List<Recipes.RecipesBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Recipes.RecipesBean> getList() {
        return list;
    }

    public void setList(List<Recipes.RecipesBean> list) {
        this.list = list;
    }
}
