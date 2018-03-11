package com.example.xiewencai.material_learning.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.adapter.TabFragmentAdapter;
import com.example.xiewencai.material_learning.fragment.ChooseAreaFragment;
import com.example.xiewencai.material_learning.fragment.HorosFragment;
import com.example.xiewencai.material_learning.fragment.NoteFragment;
import com.example.xiewencai.material_learning.util.ActivityCollector;
import com.example.xiewencai.material_learning.util.NotificationUtils;
import com.zhouwei.mzbanner.MZBannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String BROADCAST_FLAG = "com.example.xiewencai.material_learning.FORCE_OFFLINE";
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private View statusView;
    private MZBannerView mzBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusView = initStatusBar();

        NavigationView navView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);//导航抽屉

        //初始化tab和ViewPager
        initTabViewPager();

        //设置默认选中菜单项
        navView.setCheckedItem(R.id.call);
        //给导航抽屉的选项设置点击监听器
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mail:
                        sentMessage();
                        break;

                    case R.id.group:
                        Intent intent = new Intent(MainActivity.this, WeTalkActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.call:
                        Intent intent1 = new Intent(Intent.ACTION_DIAL);
                        intent1.setData(Uri.parse("tel:1008611"));
                        startActivity(intent1);
                        break;

                    case R.id.callCM:
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionDialog();
                        } else {
                            call();
                        }
                        break;
                    case R.id.location:
                        Intent intent5 = new Intent(MainActivity.this, BaiduMapActivity.class);
                        startActivity(intent5);
                        break;

                    case R.id.task:
                        Intent intent2 = new Intent(MainActivity.this, DownloadTaskActivity.class);
                        startActivity(intent2);
                        break;

                    default:
                        break;
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//设置toolbar
        //设置toolbar上的汉堡菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

    }
//onCreate结束
/*
*/

    //为toolbar填充菜单布局
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    //给toolbar的选项设置点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.backup:
                Toast.makeText(this, "well, you clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                showToast("Software Copyright Reserved ");

                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.settings: //写个新的activity关于
                Intent intent1 = new Intent(this, SettingActivity.class);
                startActivity(intent1);
                break;

            default:
                break;
        }
        return true;
    }

    //拨打电话的方法
    private void call() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("拨打电话").setCancelable(false);
            builder.setMessage("将要给1008611打电话查询话费，是否继续？");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent2 = new Intent(Intent.ACTION_CALL);
                    intent2.setData(Uri.parse("tel:1008611"));
                    startActivity(intent2);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //获取权限时弹出警示对话框，提示用户需要申请权限 并申请权限
    private void requestPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("说明");
        dialog.setCancelable(false);
        dialog.setMessage("拨打电话需要获取 电话权限，不会用于其他用途，请放心");
        dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);//申请权限
            }
        });
        dialog.show();


    }

    //用户对权限申请做出选择后，回调该方法
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    call();
                else
                    Toast.makeText(MainActivity.this, "you denied the permission", Toast.LENGTH_LONG).show();
                break;
        }

    }

    //按返回键时执行该方法弹出对话框 是否退出并 杀掉进程
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Sure to exit ?");
        dialog.setMessage("Make sure or just cancel");
        dialog.setCancelable(false);//用户不能拒绝应答此对话框，必须做出选择
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                ActivityCollector.finishAll();
                // android.os.Process.killProcess(android.os.Process.myPid());//杀掉当前进程
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//取消对话框
            }
        });
        dialog.show();


    }

    //初始化Tab和ViewPager的绑定
    private void initTabViewPager() {
        final List<String> tabList = new ArrayList<>();
        final int[] materalColor = getResources().getIntArray(R.array.material_color);
        tabList.add("星座故事");
        tabList.add("笔记");
        tabList.add("天气");

        final TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //让tab和toolbar实现颜色渐变；
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int evaluate = (Integer) evaluator.evaluate(positionOffset, materalColor[position], materalColor[(position + 1) % tabList.size()]);
                tabLayout.setBackgroundColor(evaluate);
                toolbar.setBackgroundColor(evaluate);
                statusView.setBackgroundColor(evaluate);
                getWindow().setNavigationBarColor(evaluate);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //设置tab模式，MODE_FIXED是固定的，MODE_SCROLLABLE可超出屏幕范围滚动的
        //  tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        List<Fragment> fragmentList = new ArrayList<>();

        Fragment f1 = new HorosFragment();
        Fragment f2 = new ChooseAreaFragment();
        Fragment f3 = new NoteFragment();
        fragmentList.add(f1);
        fragmentList.add(f3);
        fragmentList.add(f2);


        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        viewPager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        //tabLayout.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器

    }

    //初始化状态栏
    public View initStatusBar() {
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        Window window = getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        int finalColor = Color.argb(50, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        window.setStatusBarColor(finalColor);
        View statusView = createStatusBarView(this, ContextCompat.getColor(this, R.color.colorPrimary));
        frameLayout.addView(statusView);
        decorView.setSystemUiVisibility(option);
        return statusView;
    }

    private static View createStatusBarView(Activity activity, int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }


    private void sentMessage() {
        Intent intent = new Intent(this, SettingActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification("Send developer an Email?", "I am the developer,Gmail accepted,you know it", R.mipmap.ic_launcher, pi, 1);

        /*
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获取系统服务：通知管理器
        Notification notification = new NotificationCompat.Builder(MainActivity.this).setContentTitle("Do you want to send a email?").setStyle(new NotificationCompat.BigTextStyle().bigText("hi, would you want to make a communication with the developer? Gmail accepted,you know it"))
                .setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pi).setDefaults(NotificationCompat.DEFAULT_ALL).setSmallIcon(R.mipmap.ic_launcher).
                        setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).build();
        notificationManager.notify(01, notification);
        */
    }


}










