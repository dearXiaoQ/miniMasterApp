package com.mastergroup.smartcook.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.Recipes;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;
import com.yuyh.library.imgsel.utils.LogUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends Activity implements Contract.SearchView {

    Contract.Presenter mPresenter;

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.search_rl)
    RelativeLayout searchRl;
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.clear_iv)
    ImageView clearIv;
    @Bind(R.id.search_result_lv)
    ListView resultLv;

    @Bind(R.id.search_history_lv)
    ListView searchHistoryLv;

    @Bind(R.id.search_not_result_rl)
    RelativeLayout notResultRl;

    Context mContext;

    MySearchLvAdapter mySearchLvAdapter;
    ArrayList<String> historyList = new ArrayList<>();

    MyRecipesAdapter myRecipesAdapter;
    List<Recipes.RecipesBean> recipesResults = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        ButterKnife.bind(this);
        new MenuPresenter(this);
        mPresenter.start();
        initData();
        initView();
    }


    private void initData() {
        String historyStr = App.spUtils.getString("history");

        if( historyStr != null) {
            String[] history = App.spUtils.getString("history").split(",");
            for (String str : history) {
                historyList.add(str);
            }
        }  else {
            LogUtils.i("historyStr", "historyStr = null" );
        }
    }


    private void initView() {
        mySearchLvAdapter = new MySearchLvAdapter();
        searchHistoryLv.setAdapter(mySearchLvAdapter);

        myRecipesAdapter = new MyRecipesAdapter();
        resultLv.setAdapter(myRecipesAdapter);

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    resultLv.setVisibility(View.GONE);
                    searchHistoryLv.setVisibility(View.GONE);
                    String searchText = searchEt.getText().toString().trim();
                    if(searchText.length() == 0) {
                        return false;
                    }

                    historyList.add(0, searchText);
                    if(historyList.size() > 5 ) {
                        historyList.remove((historyList.size() - 1));
                    }

                    String s = historyList.toString();
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    s = s.replace(" ", "");

                    App.spUtils.putString("history", s);
                    mySearchLvAdapter.notifyDataSetChanged();
                    mPresenter.search(searchText, 0, 100);
                }

                return false;
            }
        });

        searchHistoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    mPresenter.search(historyList.get(position - 1), 0, 100);
                } else {
                    searchHistoryLv.setVisibility(View.VISIBLE);
                }
            }
        });


        resultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    startActivity(new Intent(view.getContext(), MenuViewActivity.class).putExtra("_id", recipesResults.get(position - 1).get_id()));
                }
            }
        });


        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                } else {
                    clearIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onGetRecipesSuccess(List<Recipes.RecipesBean> recipesList) {
        this.recipesResults = recipesList;
        searchHistoryLv.setVisibility(View.GONE);
        if(recipesList.size() == 0) {
            notResultRl.setVisibility(View.VISIBLE);
        } else {
            resultLv.setVisibility(View.VISIBLE);
            myRecipesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetRecipesFailure(String info) {
        ToastUtils.showCustomToast(mContext, ToastUtils.TOAST_BOTTOM, info);
    }

    /** 搜索结果ListView Adapter */
    class MyRecipesAdapter extends BaseAdapter {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            return recipesResults.size() + 1;   //头部加上结果集合
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(position > 0) {
                convertView = inflater.inflate(R.layout.collection_lv_item, null);

                ImageView recipesIv = (ImageView) convertView.findViewById(R.id.menu_iv);
                TextView titleTv = (TextView) convertView.findViewById(R.id.title_tv);
                TextView timeTv = (TextView) convertView.findViewById(R.id.time_tv);
                TextView favoriteTv = (TextView) convertView.findViewById(R.id.favorite_num_tv);
                RelativeLayout quarantineRl = (RelativeLayout) convertView.findViewById(R.id.quarantine_rl);

                quarantineRl.setVisibility(View.GONE);
                Recipes.RecipesBean bean = recipesResults.get(position - 1);
                ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + bean.getDetail().getImgSrc(), recipesIv, mContext, false);
                titleTv.setText(bean.getName());
                timeTv.setText(bean.getOwner().getOwnerUid().getName());
                favoriteTv.setText(bean.getFavorite().size() + "");

            } else {
                convertView = inflater.inflate(R.layout.srearch_result_first_item, null);
            }

            return convertView;
        }
    }


    /** 历史搜索Adapter */
    class MySearchLvAdapter extends BaseAdapter {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            return historyList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.search_history_item, null);
            ImageView closeIv = (ImageView) convertView.findViewById(R.id.close_iv);
            TextView historyTv = (TextView) convertView.findViewById(R.id.history_name_tv);
            if (position > 0) {
                closeIv.setVisibility(View.GONE);
                historyTv.setTextColor(mContext.getResources().getColor(R.color.gray_777));
                historyTv.setText(historyList.get(position - 1));
                historyTv.setTextSize(14);
            } else {
                closeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historyList.clear();
                        App.spUtils.putString("history","");
                        mySearchLvAdapter.notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }
    }




    @OnClick({R.id.clear_iv, R.id.iv_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_iv:
                searchEt.setText("");
                resultLv.setVisibility(View.GONE);
                searchHistoryLv.setVisibility(View.VISIBLE);
                notResultRl.setVisibility(View.GONE);
                break;

            case R.id.iv_return:
                this.finish();
                break;

        }
    }
}
