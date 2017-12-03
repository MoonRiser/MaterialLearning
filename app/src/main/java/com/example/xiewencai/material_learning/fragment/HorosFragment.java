package com.example.xiewencai.material_learning.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xiewencai.material_learning.Horoscope;
import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.adapter.HoroAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorosFragment extends Fragment {


    private List<Horoscope> horoList=new ArrayList<>();
    private FloatingActionButton fab;
    private    SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    HoroAdapter adapter=new HoroAdapter(horoList);
    private Horoscope[] horos={new Horoscope("Pisces","双鱼座", R.drawable.pisces),
            new Horoscope("Cancro","巨蟹座",R.drawable.cancro),new Horoscope("Scorpio","天蝎座",R.drawable.scropio),
            new Horoscope("Aquarius","水瓶座",R.drawable.aquarius),new Horoscope("Gemini","双子座",R.drawable.gemini),
            new Horoscope("Libra","天秤座",R.drawable.libra),new Horoscope("Aries","白羊座",R.drawable.aries),
            new Horoscope("Sagit","射手座",R.drawable.sagit),new Horoscope("Leo","狮子座",R.drawable.leo),
            new Horoscope("Taurus","金牛座",R.drawable.taurus),new Horoscope("Capricorn","摩羯座",R.drawable.capricorn),
            new Horoscope("Virgo","处女座",R.drawable.virgo)
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_horos,container,false);
        return view;

    }


    public void  onActivityCreated(Bundle saveInstaceState){
        super.onActivityCreated(saveInstaceState);

        initHoros();
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);//recyclerView
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab=(FloatingActionButton) view.findViewById(R.id.floatingActionButton);//获取fab悬浮按钮
        //设置fab悬浮按钮的点击事件监听器
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Someone call the doctor",Snackbar.LENGTH_SHORT).setAction("undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"This is what you came for",Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        //设置滑动刷新的颜色和滑动刷新的监听器
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipRe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHoros();
            }
        });

    }


    //初始化星座列表
    private void initHoros() {

        horoList.clear();
        while(horoList.size()<=11){
            Random random= new Random();
            int index= random.nextInt(horos.length);//horos.length=12
            //index为0-11之间随机数；
            if(horoList.size()==0)
                horoList.add(horos[index]);//给空的horolist加入第一个元素，只运行一次

            for(int i=0;i<horoList.size();i++){
                if(horos[index]==horoList.get(i))
                    break;
                if(i==horoList.size()-1)
                    horoList.add(horos[index]);
            }
        }
    }

    //写滑动刷新的方法,处理具体的逻辑
    private  void refreshHoros(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){e.printStackTrace();}
                getActivity().runOnUiThread(new Runnable() {  //切换到主线程更新ui
                    @Override
                    public void run() {
                        initHoros();
                        adapter.notifyDataSetChanged();//通知适配器数据发生改变
                        swipeRefreshLayout.setRefreshing(false);//滑动结束
                    }
                });
            }
        }).start();
    }




}
