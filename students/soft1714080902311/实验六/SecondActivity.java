package edu.hzuapps.androidlabs.soft1714080902311;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText3;
    private EditText editText1;
    private EditText editText2;
    private Button button9;
    private Button button8;
    ImageView image;
    Bitmap bitmap;
    // 要申请的权限
   private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
   private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //获取布局文件
        editText3 = (EditText) findViewById(R.id.editText3);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button9 = (Button) findViewById((R.id.button9));
        button9.setOnClickListener(new ButtonListener());
       button8 = (Button) findViewById(R.id.button8);
       image = (ImageView) findViewById(R.id.image);
        button8.setOnClickListener(this);;
        image.setOnClickListener(this);;
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                         // 检查该权限是否已经获取
                       int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                        if (i != PackageManager.PERMISSION_GRANTED) {
                                 // 如果没有授予该权限，就去提示用户请求
                                showDialogTipUserRequestPermission();
                            }
                   }


    }
    // 提示用户该请求权限的弹出框
      private void showDialogTipUserRequestPermission() {

                new AlertDialog.Builder(this)
                         .setTitle("存储权限不可用")
                        .setMessage("由于应用需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用该应用")
                         .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                     @Override
                   public void onClick(DialogInterface dialog, int which) {
                                        startRequestPermission();
                                    }
                  })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                     @Override
                  public void onClick(DialogInterface dialog, int which) {
                                     finish();
                                   }
                  }).setCancelable(false).show();
             }

             // 开始提交请求权限
              private void startRequestPermission() {
                 ActivityCompat.requestPermissions(this, permissions, 321);
            }

             // 用户权限 申请 的回调方法
             @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                if (requestCode == 321) {
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                 if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                                         // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                                         boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                                       if (!b) {
                                               // 用户还是想用我的 APP 的
                                                 // 提示用户去应用设置界面手动开启权限
                                                showDialogTipUserGoToAppSettting();
                                             } else
                                               finish();
                                     } else {
                                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                                     }
                             }
                     }
            }

             // 提示用户去应用设置界面手动开启权限

             private void showDialogTipUserGoToAppSettting() {

               dialog = new AlertDialog.Builder(this)
                         .setTitle("存储权限不可用")
                        .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
                       .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                   public void onClick(DialogInterface dialog, int which) {
                                      // 跳转到应用设置界面
                                       goToAppSetting();
                                  }
               })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                   public void onClick(DialogInterface dialog, int which) {
                                         finish();
                                    }
                 }).setCancelable(false).show();
            }

            // 跳转到当前应用的设置界面
            private void goToAppSetting() {
                Intent intent = new Intent();

                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                 Uri uri = Uri.fromParts("package", getPackageName(), null);
                 intent.setData(uri);

                startActivityForResult(intent, 123);
            }

            //
             @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
               super.onActivityResult(requestCode, resultCode, data);
               if (requestCode == 123) {

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                                if (i != PackageManager.PERMISSION_GRANTED) {
                                       // 提示用户应该去应用设置界面手动开启权限
                                      showDialogTipUserGoToAppSettting();
                                  } else {
                                       if (dialog != null && dialog.isShowing()) {
                                               dialog.dismiss();
                    }
                                         Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                                    }
                             }
                    }
            }


    /**
     * 获取网络图片
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl){
        URL url;
        HttpURLConnection connection=null;
        Bitmap bitmap=null;
        try {
            url = new URL(imageurl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream=connection.getInputStream();
            bitmap=BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button8:
                //加入网络图片地址
                new Task().execute("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1555163874&di=4943c507e745a2028b8465a0896ad3e3&src=http://img3.redocn.com/tupian/20150701/shanshuifengjing_4599586.jpg");
                break;

            case R.id.image:
                //点击图片后将图片保存到SD卡根目录下的Pictures文件夹内
                SavaImage(bitmap, Environment.getExternalStorageDirectory().getPath()+"/test");
                Toast.makeText(getBaseContext(), "图片保存", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }


    Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0x123){
                image.setImageBitmap(bitmap);
            }
        };
    };


    /**
     * 异步线程下载图片
     *
     */
    class Task extends AsyncTask<String, Integer, Void>{

        protected Void doInBackground(String... params) {
            bitmap=GetImageInputStream((String)params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message=new Message();
            message.what=0x123;
            handler.sendMessage(message);
        }

    }

    /**
     * 保存位图到本地
     * @param bitmap
     * @param path 本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path){
        File file=new File(path);
        FileOutputStream fileOutputStream=null;
        //文件夹不存在，则创建它
        if(!file.exists()){
            file.mkdir();
        }
        try {
            fileOutputStream=new FileOutputStream(path+"/"+System.currentTimeMillis()+".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //定义Button按钮的点击事件
    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v){
            switch(v.getId()){
                case R.id.button9:
                String saveinfo=editText3.getText().toString().trim();
                String saveinfo1=editText1.getText().toString().trim();
                String saveinfo2=editText2.getText().toString().trim();
                    FileOutputStream fos;
                    try{
                        //保存数据
                        fos=openFileOutput("data.text", Context.MODE_APPEND);
                        fos.write(saveinfo1.getBytes());
                        fos.write(saveinfo2.getBytes());
                        fos.write(saveinfo.getBytes());
                        fos.close();
                    } catch(Exception e){
                        e.printStackTrace();;
                    }
                    Toast.makeText(SecondActivity.this,"数据保存成功",0).show();
                    break;
                default:
                    break;

            }
        }


    }
}
