package com.example.xiewencai.material_learning.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.activity.NoteDetailActivity;
import com.example.xiewencai.material_learning.adapter.NoteAdapter;
import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.util.NoteUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NoteFragment extends Fragment{

    private View view;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_note,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler_view_note);
        recyclerView.setLayoutManager(new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteList= NoteUtils.getNoteDataFromDB();
        noteAdapter=new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);


        fab=view.findViewById(R.id.fab_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("position",-1);
                startActivityForResult(intent,1);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        int lastPosition=noteAdapter.getLastPosition();
        Note note = NoteUtils.getLastItemFromDB();
        if(note!=null){
            if(note.getId()!=noteList.get(0).getId()){
                if(noteList.get(0).getId()==0){
                    noteList.remove(0);
                    noteList.add(0,note);
                    noteAdapter.notifyDataSetChanged();
                }else {
                    noteList.add(0,note);
                    noteAdapter.notifyItemInserted(0);
                }

               //
               // Log.w("碎片生命周期","onRusume已执行");
            }else if(lastPosition!=-1){
                Note note1= NoteUtils.get(lastPosition);
                noteList.set(lastPosition,note1);
                noteAdapter.notifyDataSetChanged();
            }

        }

    }



}
