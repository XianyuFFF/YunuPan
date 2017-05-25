package com.logn.yunupan.utils.file_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.easybar.EasyBar;
import com.logn.yunupan.App;
import com.logn.yunupan.R;
import com.logn.yunupan.activity.base.BaseActivity;
import com.logn.yunupan.data.FileManagerItem;
import com.logn.yunupan.data.LocalFile;
import com.logn.yunupan.utils.EasyBarUtils;
import com.logn.yunupan.utils.FileGetPathUtil;
import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.FileUtils;
import com.logn.yunupan.utils.StatusBarUtil;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventGetFilePath;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class FileManager extends BaseActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private static final String MY_ROOT_PATH = "com.qjm3662.cloud_u_pan.my_root_path";
    private List<FileManagerItem> fileList = null;
    private EasyBar easyBar;
    private ListView listView;
    private Intent intent;
    private boolean flag_is_select_file;
    private String currentListPath;
    //private int where = 0;             // 0-上传文件       1-选择头像     2-选择存储路径
    private Stack<String> parent_stack = new Stack<String>();
    private String SdPath = FileUtils.getSDPath1();

    private boolean isInRoot = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.status_bar_color));
        initView();
        //intent = getIntent();
        //where = intent.getIntExtra("WHERE", 0);
//        if (intent.getIntExtra(MainActivity.FILE_SELECT, 0) == MainActivity.FILE_SELECT_CODE) {

        flag_is_select_file = true;
        ShowMyDir(MY_ROOT_PATH);

//        } else if (where == 1) {
//            ShowPhotoDir(MY_ROOT_PATH);
//            System.out.println("ShowPhotoDir");
//        } else if (where == 2) {
//            SelectSaveDir(MY_ROOT_PATH);
//            EasySweetAlertDialog.ShowNormal(this, "Tip", "长按可选择~");
//        }
    }

    private void initView() {
        easyBar = EasyBarUtils.justSetTitleAndBack(" ", this, 2);

        easyBar.setTitle("选择文件");
        /*if (where == 0) {
            easyBar.setTitle("选择文件");
        } else if (where == 1) {
            easyBar.setTitle("选择图片");
        } else if (where == 2) {
            easyBar.setTitle("选择文件夹");
        }*/

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }


    private void ShowMyDir(String path) {
        fileList = new ArrayList<>();
        //当前显示文件的路径
        currentListPath = path;
        if (path.equals(MY_ROOT_PATH)) {
            initRootPath();
        } else {
            File file = new File(path);
            List<File> files = Arrays.asList(file.listFiles());
            fileList.add(new FileManagerItem("/根目录", MY_ROOT_PATH, null, App.b_directory, false, false));
            fileList.add(new FileManagerItem("/返回上一级", parent_stack.peek(), null, App.b_directory, false, false));
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });

            //添加所有文件
            for (File f : files) {
                if (f.isDirectory()) {
                    fileList.add(new FileManagerItem(f.getName(), f.getPath(), f.getParent(), App.b_directory, f.isDirectory(), false));
                } else {
                    Bitmap icon = FileUtils.getImgHead_not_down(f);
                    if (icon.equals(App.b_video) || icon.equals(App.b_photo)) {
                        fileList.add(new FileManagerItem(f.getName(), f.getPath(), f.getParent(), icon, f.isDirectory(), true));
                    } else {
                        fileList.add(new FileManagerItem(f.getName(), f.getPath(), f.getParent(), icon, f.isDirectory(), false));
                    }
                }
            }
        }

        listView.setAdapter(new FileManagerAdapter(this, fileList));
    }

    private void initRootPath() {
        //根目录
        fileList.add(new FileManagerItem("手机相册", SdPath + "/DCIM", null, App.b_directory, true, false));
        fileList.add(new FileManagerItem("系统下载", SdPath + "/Download", null, App.b_directory, true, false));
        fileList.add(new FileManagerItem("SDCard1", SdPath, null, App.b_directory, true, false));
        fileList.add(new FileManagerItem("云U盘文件夹", SdPath + "/yup", null, App.b_directory, true, false));
        fileList.add(new FileManagerItem("其他", SdPath + "/", null, App.b_directory, true, false));
    }


    /**
     * 显示图片文件
     *
     * @param path
     */
    private void ShowPhotoDir(String path) {
        try {
            fileList = new ArrayList<FileManagerItem>();
            currentListPath = path;
            //如果当前目录是根目录
            if (path.equals(MY_ROOT_PATH)) {
                initRootPath();
            } else {
                File file = new File(path);
                List<File> files = Arrays.asList(file.listFiles());
                fileList.add(new FileManagerItem("/根目录", MY_ROOT_PATH, null, App.b_directory, false, false));
                fileList.add(new FileManagerItem("/返回上一级", parent_stack.peek(), null, App.b_directory, false, false));
                Collections.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return -1;
                        if (o1.isFile() && o2.isDirectory())
                            return 1;
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                //添加所有文件
                for (File f : files) {
                    if (FileTypeUtils.getFileType(f).equals(LocalFile.PHOTO) || f.isDirectory()) {
                        if (f.isDirectory()) {
                            fileList.add(new FileManagerItem(f.getName(), f.getPath(), f.getParent(), App.b_directory, f.isDirectory(), false));
                        } else {
                            fileList.add(new FileManagerItem(f.getName(), f.getPath(), f.getParent(), FileUtils.getImgHead_not_down(f), f.isDirectory(), true));
                        }
                    }
                }
            }
            listView.setAdapter(new FileManagerAdapter(this, fileList));
        } catch (Exception e) {
            System.out.println("What : " + e.toString());
        }
    }


    /**
     * 选择下载文件路径
     *
     * @param path
     */
    private void SelectSaveDir(String path) {
        fileList = new ArrayList<FileManagerItem>();
        currentListPath = path;
        //如果当前目录是根目录
        if (path.equals(MY_ROOT_PATH)) {
            initRootPath();
        } else {
            File file = new File(path);
            List<File> files = Arrays.asList(file.listFiles());
            fileList.add(new FileManagerItem("/根目录", MY_ROOT_PATH, null, App.b_directory, false, false));
            fileList.add(new FileManagerItem("/返回上一级", parent_stack.peek(), null, App.b_directory, false, false));
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });
            //添加所有文件
            for (File f : files) {
                if (f.isDirectory()) {
                    fileList.add(new FileManagerItem(f.getName(), f.getPath(), f.getParent(), App.b_directory, f.isDirectory(), false));
                }
            }
        }
        listView.setAdapter(new FileManagerAdapter(this, fileList));
    }


    @Override
    public void onBackPressed() {
        if (currentListPath.equals(MY_ROOT_PATH)) {
            finish();
        } else {

            ShowMyDir(parent_stack.pop());
            /*
            switch (where) {
                case 0:
                    ShowMyDir(parent_stack.pop());
                    break;
                case 1:
                    ShowPhotoDir(parent_stack.pop());
                    break;
                case 2:
                    SelectSaveDir(parent_stack.pop());
                    break;
            }*/
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //查看返回的数据
            Logger.e("读取文件返回的数据：\nrequestCode:\t" + requestCode + "\nresultCode:\t" + resultCode + "\ndata:\t" + data.toString());
            Uri uri = data.getData();
            String path = FileGetPathUtil.getPathByUri4kitkat(FileManager.this, uri);
            Logger.e("get_path:" + path);
            File file = new File(path);
            if (file != null && file.isFile()) {
                ToastShort.show(FileManager.this, file.getName() + "\n" + path);
                //开始准备上传，并将文件数据
                // （文件名，文件类型-图标，大小，时间（上传开始时间），文件路径）
                // 实时加入数据库并显示在界面上，
                //mPresenter.start2Prepare(path);
                EventBusInstance.getBusInstance().post(new EventGetFilePath(path));
                FileManager.this.finish();
            }
        }
    }

    /*
     * 获取上传文件
     */
    private void start2GetFile(String type) {
        //type   "*/*"  全类型
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        /*if (where == 2) {
            String path = fileList.get(position).getFilePath();
            intent = new Intent();
            intent.putExtra(UserMain.PATH, path);
            this.setResult(UserMain.PATH_REQUEST, intent);
            this.finish();
        }*/
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isInRoot && position == fileList.size() - 1) {
            start2GetFile("*/*");
            return;
        }
        parent_stack.push(currentListPath);
        String path = fileList.get(position).getFilePath();
        if (path.equals(MY_ROOT_PATH)) {
            ShowMyDir(MY_ROOT_PATH);
            isInRoot = true;
            return;
        }
        isInRoot = false;
        File file = new File(path);
        //文件存在并可读
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                //显示子目录及其文件
                ShowMyDir(path);
                //EventBusInstance.getBusInstance().post(new EventToastInfo("文件路径：" + path));
                /*
                if (where == 0) {
                    ShowMyDir(path);
                } else if (where == 1) {
                    ShowPhotoDir(path);
                } else if (where == 2) {
                    SelectSaveDir(path);
                }*/

            } else {
                if (flag_is_select_file) {
                    intent = new Intent();
                    //获得路径
                    EventBusInstance.getBusInstance().post(new EventGetFilePath(path));

//                    intent.putExtra(MainActivity.PATH, path);
//                    this.setResult(MainActivity.resultCode, intent);
                    this.finish();
                }

                /*else if (where == 1) {
                    intent = new Intent();
                    intent.putExtra(UserMain.PATH, path);
                    this.setResult(UserMain.SELECT_PHOTO_RESULT_CODE, intent);
                    this.finish();
                } else {
                    //处理文件
                    fileHandle(file);
                }*/
            }
        } else {//没有权限
            //EasySweetAlertDialog.ShowTip(this, "没有获得权限");
            //显示相关信息
        }
    }

    //对文件进行增删改
    private void fileHandle(final File file) {
        /*DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 打开文件
                if (which == 0) {
                    FileUtils.OpenFile(FileManager.this, file.getPath(), file.getName());
                }
                //删除文件
                else {
                    new AlertDialog.Builder(FileManager.this)
                            .setTitle("注意!")
                            .setMessage("确定要删除此文件吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (file.delete()) {
                                        //更新文件列表
                                        ShowMyDir(parent_stack.pop());
                                        displayToast("删除成功！");
                                    } else {
                                        displayToast("删除失败！");
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        };
        //选择文件时，弹出增删该操作选项对话框
        String[] menu = {"打开文件", "重命名", "删除文件"};
        new AlertDialog.Builder(FileManager.this)
                .setTitle("请选择要进行的操作!")
                .setItems(menu, listener)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();*/
    }

    private void displayToast(String message) {
        Toast.makeText(FileManager.this, message, Toast.LENGTH_SHORT).show();
    }

}
