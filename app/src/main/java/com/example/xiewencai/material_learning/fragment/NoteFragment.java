package com.example.xiewencai.material_learning.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.activity.NoteDetailActivity;
import com.example.xiewencai.material_learning.adapter.NoteAdapter;
import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.util.CommonFab;
import com.example.xiewencai.material_learning.util.NoteUtils;

import org.litepal.crud.DataSupport;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class NoteFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<Note> noteList=new LinkedList<>();
    private NoteAdapter noteAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonFab commonFab;

    public void setCommonFab(CommonFab commonFab) {
        this.commonFab = commonFab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipRe_note);
        swipeRefreshLayout.setColorSchemeResources(R.color.teal500, R.color.cyan500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFresh();
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view_note);
        recyclerView.setLayoutManager(new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        initRecyclerView();

        /*
        FloatingActionButton fab = view.findViewById(R.id.fab_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("position", -1);
                startActivityForResult(intent, 1);
            }
        });
        */
    }


    @Override
    public void onResume() {
        super.onResume();
        updateDatabaseAndUI();

    }


    @Override//当碎片可见时回调
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            FloatingActionButton fab = commonFab.getCommonFab();
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                    intent.putExtra("position", -1);
                    startActivityForResult(intent, 1);
                }
            });
            // 相当于onResume()方法
        } else {
            // 相当于onpause()方法
        }
    }


    private void updateDatabaseAndUI() {

        int lastPosition = noteAdapter.getLastPosition();
        Note note = NoteUtils.getLastItemFromDB();
        if (note != null) {//初始化的第一条提示数据去掉
            if (note.getId() != noteList.get(0).getId()) {
                if (noteList.get(0).getId() == 0) {
                    noteList.remove(0);
                    noteList.add(0, note);
                    noteAdapter.notifyDataSetChanged();

                } else {
                    List<Note> notes = NoteUtils.getNoteDataFromDB();
                    for (int i = 0; i < notes.size() - noteList.size(); i++) {
                        noteList.add(0, notes.get(i));
                        noteAdapter.notifyItemInserted(0);
                    }

                }
                // Log.w("碎片生命周期","onRusume已执行");
            } else if (lastPosition != -1) {
                Note note1 = NoteUtils.get(lastPosition);
                noteList.set(lastPosition, note1);
                noteAdapter.notifyDataSetChanged();
            }

        }


    }

    private void reFresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {  //切换到主线程更新ui
                    @Override
                    public void run() {
                        NoteUtils.updateLocalFormRemote(getActivity(), new NoteUtils.ReFreshUI() {
                            @Override
                            public void reFreshHelper() {
                                List<Note> notes = NoteUtils.getNoteDataFromDB();

                                if(noteList.get(0).getId()==0){
                                    noteList.remove(0);
                                    noteAdapter.notifyItemRemoved(0);
                                }
                             //   Log.w("本地数据库notes/notelist",notes.size()+"///"+noteList.size());
                                int length= notes.size()-noteList.size();
                                for (int i = 0; i <length ; i++) {
                                    noteList.add( notes.get(i));
                                    noteAdapter.notifyItemInserted(i);
                                }
                                noteAdapter.notifyDataSetChanged();

                            }
                        });
                        swipeRefreshLayout.setRefreshing(false);//滑动结束
                    }
                });
            }
        }).start();
    }

    private void onLongClickAction(View view, final int position){

        //长按触发振动效果
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        assert vibrator != null;
        vibrator.vibrate(50);

        Log.w("noteFrag里面LastPosition:",position+"");
        Snackbar.make(commonFab.getCommonFab(), "确定删除当前内容吗?(联网时,云端同步删除)", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteUtils.deleteLocalAndRemoteNote(noteList.get(position),getActivity());
                noteList.remove(position);
                noteAdapter.notifyItemRemoved(position);
            }
        }).show();

    }

    private void initRecyclerView(){
        noteList = NoteUtils.getNoteDataFromDB();
        noteAdapter = new NoteAdapter(noteList);
        noteAdapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                onLongClickAction(view,position);
            }
        });
        recyclerView.setAdapter(noteAdapter);
    }

    public void deleteAllLocal(){
        int range=noteList.size()-1;
        noteList.clear();
        noteAdapter.notifyItemRangeRemoved(0,range);
        List<Note> notess = NoteUtils.getNoteDataFromDB();
        noteList.add(notess.get(0));
        noteAdapter.notifyDataSetChanged();
    }




}