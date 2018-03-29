package com.example.xiewencai.material_learning.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.db.NoteRemote;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Xie Wencai on 2018/3/2.
 */

public class NoteUtils {

    public interface ReFreshUI {
        void reFreshHelper();
    }

    interface UpdateRemoteNote{
        void updateTest(List<NoteRemote> noteRemotes, final Context context, Note note);
    }

    //从本地数据库获取笔记链表
    public static List<Note> getNoteDataFromDB() {//从本地数据库获取note数据
        List<Note> notesLink = new LinkedList<>();//返回链表便于从头节点插入新数据
        List<Note> notes = DataSupport.findAll(Note.class);
        if (notes.size() == 0) {
            Note note = new Note();
            note.setTitle("萌新引导✪ ω ✪");
            note.setContent("写下你的第一个笔记吧(●ˇ∀ˇ●)\n工具栏的'上传按钮'可以将所有笔记上传到云端\n下拉刷新可以将云端笔记同步到本地\n" +
                    "长按卡片可以选择删除当前笔记");
            note.setDate(getCurrentDate());
            note.setId(0);
            notes.add(note);
            //  note.save();
        }
        for (Note note : notes) {
            notesLink.add(0, note);
        }
        return notesLink;
    }

    //将本地所有未上传的数据上传到云端
    public static void uploadNoteData(final Context context) {

        List<BmobObject> noteRemotes = new ArrayList<>();
        final List<Note> nrHelps = new ArrayList<>();
        final List<Note> notes = DataSupport.findAll(Note.class);
        //Log.w("notes是否为空？",notes.get(0).getTitle());
        if (notes.size() == 0) {
            Toast.makeText(context, "无可用于上传的笔记ヾ(•ω•`)o", Toast.LENGTH_SHORT).show();
            return;
        } else {

            for (Note note : notes) {
                if (!note.getUpload()) {//当前note对象未被上传过
                    NoteRemote noteRemote = new NoteRemote();
                    noteRemote.setContent(note.getContent());
                    noteRemote.setDate(note.getDate());
                    noteRemote.setTitle(note.getTitle());
                    noteRemote.setAuthor(BmobUser.getCurrentUser());
                    noteRemotes.add(noteRemote);
                    nrHelps.add(note);
                }
            }

            if (noteRemotes.size() == 0) {
                Toast.makeText(context, "当前所有笔记已处于云端，无需上传( •̀ ω •́ )✧", Toast.LENGTH_SHORT).show();
                return;
            }
            new BmobBatch().insertBatch(noteRemotes).doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> list, BmobException e) {
                    for (int i = 0; i < list.size(); i++) {
                        BatchResult result = list.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Note note = nrHelps.get(i);
                            note.setUpload(true);
                            note.update(note.getId());//将已上传的note标志位更新数据库本地
                            //          Toast.makeText(context, "note的upload为："+notes.get(i).getUpload(), Toast.LENGTH_SHORT).show();
                            Log.e("上传Bmob", "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                        } else {
                            Log.e("上传Bmob", "第" + i + "个数据批量添加失败：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                            Log.e("上传Bmob", "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                            //   Toast.makeText(context, "第"+i+"个数据批量添加失败：", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }

    }


    public static Note getFirstItemFromDB() {
        Note note = DataSupport.findFirst(Note.class);
        return note;

    }

    public static Note getLastItemFromDB() {
        Note note = DataSupport.findLast(Note.class);
        return note;
    }

    public static Note get(int position) {
        List<Note> notes = getNoteDataFromDB();
        return notes.get(position);
    }



    //通过日期作为主键查询云端的笔记
    public static void queryNoteByDate(Note note,Context context){
        queryNoteByDateHelp(note, context, new UpdateRemoteNote() {
            @Override
            public void updateTest(List<NoteRemote> noteRemotes, final Context context, Note note) {
                NoteRemote noteRemote = noteRemotes.get(0);
                if (noteRemote == null) {
                    Toast.makeText(context, "云端同步失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                noteRemote.setTitle(note.getTitle());
                noteRemote.setContent(note.getContent());
                noteRemote.update(noteRemote.getObjectId(), new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "更新成功");
                            Toast.makeText(context, "云端笔记已同步更新", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }

                });
            }
        });
    }

    //更新云端的笔记，配合上面方法使用
    public static void queryNoteByDateHelp(final Note note, final Context context, final UpdateRemoteNote updateRemoteNote) {

        BmobQuery<NoteRemote> query = new BmobQuery<NoteRemote>();
//查询playerName叫“比目”的数据
        //    Log.w("查询note的日期到底是什么鬼？","///"+note.getDate()+"///");
        query.addWhereEqualTo("date", note.getDate());
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<NoteRemote>() {
            @Override
            public void done(List<NoteRemote> noteRemotes, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "查询成功：共" + noteRemotes.size() + "条数据。", Toast.LENGTH_SHORT).show();
                      updateRemoteNote.updateTest(noteRemotes, context, note);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    //同步删除选中的本地和云端的笔记
    public  static void deleteLocalAndRemoteNote(Note note,Context context){

        int flag = DataSupport.deleteAll(Note.class,"date = ?",note.getDate());
        if(flag==0)return;
      //  Log.w("本地数据库删除标志？",flag+"///");
        queryNoteByDateHelp(note, context, new UpdateRemoteNote() {
            @Override
            public void updateTest(List<NoteRemote> noteRemotes, final Context context, Note note) {
                NoteRemote remote= noteRemotes.get(0);
                remote.setObjectId(remote.getObjectId());
                remote.delete( new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                  //  Log.i("bmob","删除笔记成功");
                                    Toast.makeText(context, "云端笔记已成功删除", Toast.LENGTH_SHORT).show();
                                }else{
                                //    Log.i("bmob","删除笔记失败："+e.getMessage()+","+e.getErrorCode());
                                    Toast.makeText(context, "云端笔记删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
        });



    }

    //获取云端的笔记更新本地数据
    public static void updateLocalFormRemote(final Context context, final ReFreshUI freshUI) {

        BmobQuery<NoteRemote> query = new BmobQuery<NoteRemote>();
//查询playerName叫“比目”的数据
        //    Log.w("查询note的日期到底是什么鬼？","///"+note.getDate()+"///");
        query.addWhereEqualTo("author", BmobUser.getCurrentUser());
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //排序
        query.order("createdAt");
//执行查询方法
        query.findObjects(new FindListener<NoteRemote>() {
            @Override
            public void done(List<NoteRemote> noteRemotes, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "查询成功：共" + noteRemotes.size() + "条数据。", Toast.LENGTH_SHORT).show();
                    insertIntoDB(noteRemotes);
                    freshUI.reFreshHelper();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //把获取的数据写入本地数据库
    private static void  insertIntoDB(List<NoteRemote> noteRemotes) {

        for (NoteRemote noteRemote : noteRemotes) {
            List<Note> notes = DataSupport.select("title").where("date = ?", noteRemote.getDate()).find(Note.class);
            if (notes.isEmpty()) {
                Note note = new Note();
                note.setTitle(noteRemote.getTitle());
                note.setContent(noteRemote.getContent());
                note.setUpload(true);
                note.setDate(noteRemote.getDate());
                note.save();
                Log.w("云端的数据已存入本地", note.getTitle());
            }
        }

    }

    //获取系统当前时间/以字符串形式存储
    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        String str = simpleDateFormat.format(date);
     //   Log.w("date到底有没有被规格化？", str);
        return str;
    }

}
