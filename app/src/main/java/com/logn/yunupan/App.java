package com.logn.yunupan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.logn.yunupan.utils.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Collections;

import okhttp3.OkHttpClient;

/**
 * Created by tanshunwang on 2016/9/21 0021.
 */
public class App extends Application{
//    public static FileInformation fileInformation = new FileInformation();
//
//    //本地上传记录
//    public static List<LocalFile> Public_List_Local_File_Upload = new ArrayList<LocalFile>();
//    //本地下载记录
//    public static List<LocalFile> Public_List_Local_File_Download = new ArrayList<LocalFile>();
//    //wifi直连传输记录
//    public static List<LocalFile> Public_List_Wifi_Trans_Record = new ArrayList<LocalFile>();

    //暂存当前点击的用户
//    public static User user_temp = null;
//    //关注的人信息
//    public static List<User> Public_Following_Info = Collections.synchronizedList(new ArrayList<User>());

    public static boolean Down_In_Wifi_Switch_State = false;


    //可读数据库
    public static SQLiteDatabase dbRead;
//    public static LocalFileDB localFileDB;
    public static SQLiteDatabase dbWrite;
//    private NetworkReceiver receiver;

    //1->有网络连接  2->wifi连接   3->手机连接
//    public static int NeworkFlag = -1;
//    public static boolean Flag_IsLogin = false;
//    public static String currentSavePath = FileUtils.getSDPath() + "/Download/";
//    public static boolean FLAG_IS_DATA_FINISH = false;


    public static Bitmap b_directory;
    public static Bitmap b_doc;
    public static Bitmap b_html;
    public static Bitmap b_music;
    public static Bitmap b_photo;
    public static Bitmap b_ppt_pdf;
    public static Bitmap b_video;
    public static Bitmap b_zip;

    @Override
    public void onCreate() {
        super.onCreate();
        initReceiver();
        //数据库相关操作
        new Thread(new Runnable() {
            @Override
            public void run() {
//                initUMeng();
//                getUserInfo();
//                getSwitchState();
//                CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
//                OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                        .cookieJar(cookieJar)
//                        //其他配置
//                        .build();
//                OkHttpUtils.initClient(okHttpClient);
//                initDataBase();
//                GetFileInformationFromDB();

                getBitmapIcon();
                //相册配置初始化
                initGallerFinal();
            }
        }).start();
    }


    public static void startAnim(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public static void finishAnim(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initGallerFinal() {
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);
    }

    private void initReceiver() {
        /*IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver(this);
        this.registerReceiver(receiver, intentFilter);*/
    }
/*

    private void GetFileInformationFromDB() {
        Cursor c = dbRead.query(LocalFileDB.TABLE_NAME_LOCAL_FILE_UPLOAD, null, null, null, null, null, null);
        while(c.moveToNext()){
            LocalFile localFile = new LocalFile();
            localFile.setName(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Name)));
            localFile.setPath(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Path)));
            localFile.setType(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Type)));
            Long times = c.getLong(c.getColumnIndex(LocalFileDB.COLUMN_NAME_DownTime));
            localFile.setDownTime(times);
            Public_List_Local_File_Upload.add(localFile);
        }

        c = dbRead.query(LocalFileDB.TABLE_NAME_LOCAL_FILE_DOWNLOAD, null, null, null, null, null, null);
        while(c.moveToNext()){
            LocalFile localFile = new LocalFile();
            localFile.setName(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Name)));
            localFile.setPath(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Path)));
            localFile.setType(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Type)));
            Long times = c.getLong(c.getColumnIndex(LocalFileDB.COLUMN_NAME_DownTime));
            localFile.setDownTime(times);
            Public_List_Local_File_Download.add(localFile);
        }
        c = dbRead.query(LocalFileDB.TABLE_NAME_LOCAL_FILE_WIFI_DIRECT_RECORD, null, null, null, null, null, null);
        while(c.moveToNext()){
            LocalFile localFile = new LocalFile();
            localFile.setName(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Name)));
            localFile.setPath(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Path)));
            localFile.setType(c.getString(c.getColumnIndex(LocalFileDB.COLUMN_NAME_Type)));
            Long times = c.getLong(c.getColumnIndex(LocalFileDB.COLUMN_NAME_DownTime));
            localFile.setDownTime(times);
            Public_List_Wifi_Trans_Record.add(localFile);
        }
    }
*/

    private void initDataBase() {
//        localFileDB = new LocalFileDB(this);
//        dbRead = localFileDB.getReadableDatabase();
//        dbWrite = localFileDB.getWritableDatabase();
    }

    public static void openDB() {
        /*CloseDB();
        dbRead = localFileDB.getReadableDatabase();
        dbWrite = localFileDB.getWritableDatabase();*/
    }
    public static void CloseDB(){
        if (dbWrite.isOpen()) {
            dbWrite.close();
        }
        if (dbRead.isOpen()) {
            dbRead.close();
        }
    }

    public void getUserInfo() {
//        SharedPreferences sp = this.getSharedPreferences("User", Context.MODE_PRIVATE);
//        String s_userJson = sp.getString("UserJson", "");
//        String s_avatarPath = sp.getString("AvatarPath", "");
//        if(!s_userJson.equals("")){
//            Gson gson = new Gson();
//            App.Flag_IsLogin = true;
//            User.setUser(gson.fromJson(s_userJson, User.class));
//        }else{
//            System.out.println("没有用户信息");
//        }
//        if(!s_avatarPath.equals("")){
//            User.getInstance().setBitmap(BitmapFactory.decodeFile(s_avatarPath));
//        }else{
//            System.out.println("用户头像路径无效");
//        }
//        Intent intent = new Intent();
//        intent.setAction(UserMain.ACTION_UPDATE_USERINFO);
//        sendBroadcast(intent);
//        FLAG_IS_DATA_FINISH = true;
    }

    public static void deleteUserInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        deleteSwitchState(context);
    }

    public void getSwitchState() {
        /*SharedPreferences sp = this.getSharedPreferences("SWITCH", Context.MODE_PRIVATE);
        Down_In_Wifi_Switch_State = sp.getBoolean("SWITCH_WIFI", false);
        sp = this.getSharedPreferences("PATH", Context.MODE_PRIVATE);
        currentSavePath = sp.getString("Path", currentSavePath);*/
    }

    public static void deleteSwitchState(Context context){
        SharedPreferences sp = context.getSharedPreferences("SWITCH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


    public void getBitmapIcon() {
        b_directory = BitmapFactory.decodeResource(getResources(), R.drawable.directy);
        b_doc = BitmapFactory.decodeResource(getResources(), R.drawable.file_small);
        b_music = BitmapFactory.decodeResource(getResources(), R.drawable.music);
        b_photo = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        b_ppt_pdf = BitmapFactory.decodeResource(getResources(), R.drawable.pptpdf);
        b_video = BitmapFactory.decodeResource(getResources(), R.drawable.vedio);
        b_zip = BitmapFactory.decodeResource(getResources(), R.drawable.zip);
    }
}
