package com.example.xiewencai.material_learning.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.adapter.HoroAdapter;
import com.example.xiewencai.material_learning.bean.Album;
import com.example.xiewencai.material_learning.bean.Horoscope;
import com.example.xiewencai.material_learning.util.CommonFab;
import com.zhouwei.mzbanner.MZBannerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorosFragment extends Fragment {


    private List<Horoscope> horoList = new ArrayList<>();
    private List<Album> albumList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    public HoroAdapter adapter;
    private CommonFab commonFab;

    public void setCommonFab(CommonFab commonFab) {
        this.commonFab = commonFab;
    }

    private Horoscope[] horos = {new Horoscope("Pisces", "双鱼座", R.drawable.pisces,"2.19-3.20",R.drawable.pisces1),
            new Horoscope("Cancro", "巨蟹座", R.drawable.cancro,"6.22-7.22",R.drawable.cancer1), new Horoscope("Scorpio", "天蝎座", R.drawable.scropio,"10.24-11.22",R.drawable.scorpio1),
            new Horoscope("Aquarius", "水瓶座", R.drawable.aquarius,"1.20-2.18",R.drawable.aquarius1), new Horoscope("Gemini", "双子座", R.drawable.gemini,"5.21-6.21",R.drawable.gemini1),
            new Horoscope("Libra", "天秤座", R.drawable.libra,"9.23-10.23",R.drawable.libra1), new Horoscope("Aries", "白羊座", R.drawable.aries,"3.21-4.19",R.drawable.aries1),
            new Horoscope("Sagit", "射手座", R.drawable.sagit,"11.23-12.11",R.drawable.sagittarius1), new Horoscope("Leo", "狮子座", R.drawable.leo,"7.23-8.22",R.drawable.leo1),
            new Horoscope("Taurus", "金牛座", R.drawable.taurus,"4.20-5.20",R.drawable.taurus1), new Horoscope("Capricorn", "摩羯座", R.drawable.capricorn,"12.22-1.19",R.drawable.capricorn1),
            new Horoscope("Virgo", "处女座", R.drawable.virgo,"8.23-9.22",R.drawable.virgo1)};

    private Album[] albums = {new Album(R.drawable.myvoice, "My Voice", "2017", "泰妍"), new Album(R.drawable.overdose, "Overdose", "2014", "EXO"),
            new Album(R.drawable.redflavor, "Red Flavor", "2017", "Red Velvet"), new Album(R.drawable.redlight, "Red Light", "2014", "f(x)"),
            new Album(R.drawable.peekaboo, "Perfect Velvet", "2017", "Red Velvet"), new Album(R.drawable.play, "Play", "2017", "Super Junior"),
            new Album(R.drawable.whenthewindblows, "如果你也想我", "2017", "允儿"), new Album(R.drawable.catchme, "Catch Me", "2012", "东方神起"),
            new Album(R.drawable.seven, "The Velvet", "2016", "Red Velvet"), new Album(R.drawable.lionheart, "Lion Heart", "2015", "少女时代"),
            new Album(R.drawable.thered, "The Red", "2015", "Red Velvet"), new Album(R.drawable.party, "Party", "2015", "少女时代")
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_horos, container, false);
        return view;

    }


    public void onActivityCreated(Bundle saveInstaceState) {
        super.onActivityCreated(saveInstaceState);

        initHoros();
        initAlbums();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);//recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HoroAdapter(horoList, albumList);
        recyclerView.setAdapter(adapter);

        //设置滑动刷新的颜色和滑动刷新的监听器
        swipeRefreshLayout = view.findViewById(R.id.swipRe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHoros();
            }
        });


    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.w("horoFragment里面先执行","TEST"+(commonFab==null));
            FloatingActionButton fab=commonFab.getCommonFab();
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.bannerViewHolder.mzBannerView.pause();
                    Snackbar.make(v, "已暂停banner轮播", Snackbar.LENGTH_SHORT).setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "已撤销，轮播已开启", Toast.LENGTH_SHORT).show();
                            adapter.bannerViewHolder.mzBannerView.start();
                            //  Log.w("测试测试看看","banner开启轮播");
                        }
                    }).show();
                }
            });
            // 相当于onResume()方法
        } else {
            // 相当于onpause()方法
        }
    }




    //初始化星座列表
    private void initHoros() {

        horoList.clear();
        while (horoList.size() <= 11) {
            Random random = new Random();
            int index = random.nextInt(horos.length);//horos.length=12
            //index为0-11之间随机数；
            if (horoList.size() == 0)
                horoList.add(horos[index]);//给空的horolist加入第一个元素，只运行一次

            for (int i = 0; i < horoList.size(); i++) {
                if (horos[index] == horoList.get(i))
                    break;
                if (i == horoList.size() - 1)
                    horoList.add(horos[index]);
            }
        }
    }

    private void initAlbums() {

        albumList.clear();
        while (albumList.size() <= 11) {
            Random random = new Random();
            int index = random.nextInt(albums.length);//horos.length=12
            //index为0-11之间随机数；
            if (albumList.size() == 0)
                albumList.add(albums[index]);//给空的horolist加入第一个元素，只运行一次

            for (int i = 0; i < albumList.size(); i++) {
                if (albums[index] == albumList.get(i))
                    break;
                if (i == albumList.size() - 1)
                    albumList.add(albums[index]);
            }
        }
    }

    //写滑动刷新的方法,处理具体的逻辑
    private void refreshHoros() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {  //切换到主线程更新ui
                    @Override
                    public void run() {
                        initHoros();
                        initAlbums();
                        adapter.notifyDataSetChanged();//通知适配器数据发生改变
                        swipeRefreshLayout.setRefreshing(false);//滑动结束
                    }
                });
            }
        }).start();
    }





}
