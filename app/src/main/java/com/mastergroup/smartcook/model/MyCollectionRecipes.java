package com.mastergroup.smartcook.model;

import java.util.List;

/**
 * Created by xiaoQ on 2017/6/29.
 */
/** 收藏列表 */
public class MyCollectionRecipes {
    int total;
    List<CollectionRecipes.RecipesBean> list;

    public void setList(List<CollectionRecipes.RecipesBean> list) {
        this.list = list;
    }

    public List<CollectionRecipes.RecipesBean> getList() {
        return list;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}
