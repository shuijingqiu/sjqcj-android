package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.stocks.SearchSharesActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.ScreenshotImageAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实盘报名页面
 * Created by Administrator on 2017/8/29.
 */
public class FirmSignUpActivity extends Activity {

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 99; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 98; // 照相获取
    // 网络请求提示
    private CustomProgress dialog;
    private LinearLayout codell;
    private List<EditText> listEt;
    private EditText codeEt1, codeEt2, codeEt3;
    // 空仓
    private CheckBox nullCodeCb;
    // 显示截图的上传方式
    private RelativeLayout screenshotRl;
    // 截图的显示列表
    private GridView screenshotGv;
    // gridView的控制器
    private ScreenshotImageAdapter imageAdapter;
    // 截图显示的集合
    private ArrayList<Bitmap> bitmapArrayList;
    // 参赛金额
    private EditText moneyEt;
    // 上传截图的路径
    private List<String> imageList;
    // 参加比赛返回的数据
    private String joinStr;
    // 赛场的id
    private String firmId;
    // 上传图片返回的id
    private String attachId = "";
    // 上次的图片
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 可以在主线程中使用http网络访问 这个要改的 menghuan
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // 禁止键盘的弹出
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_firm_sign_up);
        findView();
    }

    private void findView() {
        dialog = new CustomProgress(this);
        firmId = getIntent().getStringExtra("firmId");
        listEt = new ArrayList<EditText>();
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nullCodeCb = (CheckBox) findViewById(R.id.null_code_cb);
        codell = (LinearLayout) findViewById(R.id.code_ll);
        moneyEt = (EditText) findViewById(R.id.money_et);
        codeEt1 = (EditText) findViewById(R.id.code_et1);
        codeEt2 = (EditText) findViewById(R.id.code_et2);
        codeEt3 = (EditText) findViewById(R.id.code_et3);
        screenshotRl = (RelativeLayout) findViewById(R.id.screenshot_rl);
        screenshotRl.getBackground().setAlpha(150);
        screenshotRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshotRl.setVisibility(View.GONE);
            }
        });
        screenshotGv = (GridView) findViewById(R.id.screenshot_gv);
        imageAdapter = new ScreenshotImageAdapter(FirmSignUpActivity.this);
        screenshotGv.setAdapter(imageAdapter);
        bitmapArrayList = new ArrayList<Bitmap>();
        screenshotGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 删除并刷新列表
                bitmapArrayList.remove(position);
                imageList.remove(position);
                imageAdapter.setlistData(bitmapArrayList);
            }
        });
        imageList = new ArrayList<String>();

        codeEt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmSignUpActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent, 15);
            }
        });
        codeEt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmSignUpActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent, 16);
            }
        });
        codeEt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmSignUpActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent, 17);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从相册获取图片
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    ContentResolver resolver = getContentResolver();
                    Bitmap bmp;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
                        double proportion = ImageUtil.getProportion(bmp.getWidth(), bmp.getHeight());
                        bitmap = Bitmap.createScaledBitmap(bmp,
                                (int) (bmp.getWidth() * proportion), (int) (bmp.getHeight() * proportion), true);
                        dialog.showDialog();
                        // 报名参加
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    attachId = ImageUtil.sendData(bitmap);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(1);
                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            try {
                final ContentResolver resolver = getContentResolver();
                File file = new File(ImageUtil.getSDPath());
                if (file.exists()) {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, Uri.fromFile(file));
                    double proportion = ImageUtil.getProportion(photo.getWidth(), photo.getHeight());
                    bitmap = Bitmap.createScaledBitmap(photo,
                            (int) (photo.getWidth() * proportion), (int) (photo.getHeight() * proportion), true);
                    dialog.showDialog();
                    // 报名参加
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                attachId = ImageUtil.sendData(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (data == null) {
                return;
            }
            String code = data.getExtras().get("code") + "";
            // 处理选择的股票的
            if (!"".equals(code)) {
                String codeName = data.getExtras().get("name").toString() + "(" + code + ")";
                switch (requestCode) {
                    case 15:
                        codeEt1.setText(codeName);
                        break;
                    case 16:
                        codeEt2.setText(codeName);
                        break;
                    case 17:
                        codeEt3.setText(codeName);
                        break;
                    default:
                        listEt.get(requestCode - 1).setText(codeName);
                        break;
                }
            }
        }

    }

    /**
     * 追加文本框
     *
     * @param view
     */
    public void AddEtClick(View view) {
        if (nullCodeCb.isChecked() || listEt.size() > 12) {
            return;
        }
        EditText editText = new EditText(this);
        editText.setHint("请输入股票代码");
        editText.setBackgroundResource(R.drawable.shape);
        editText.setPadding(10, 10, 10, 10);
        editText.setTextSize(14);
        editText.setTag(listEt.size() + 1);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        editText.setLayoutParams(params);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int code = Integer.valueOf(v.getTag().toString());
                Intent intent = new Intent(FirmSignUpActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent, code);
            }
        });
        codell.addView(editText);
        listEt.add(editText);
    }

    /**
     * 刷新截图页面
     */
    private void RefreshScreenshot() {
        if (bitmapArrayList.size() == 5) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, screenshotGv.getHeight() * 2);
            screenshotGv.setLayoutParams(params);
        } else if (bitmapArrayList.size() == 4) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            screenshotGv.setLayoutParams(params);
        } else if (bitmapArrayList.size() == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            screenshotGv.setLayoutParams(params);
        }
        imageAdapter.setlistData(bitmapArrayList);
    }

    /**
     * 截图的点击事件
     *
     * @param view
     */
    public void ScreenshotClick(View view) {
        // 最多8张图
        if (bitmapArrayList.size() > 7) {
            return;
        }
        screenshotRl.setVisibility(View.VISIBLE);
    }

    /**
     * 空仓的点击事件
     *
     * @param view
     */
    public void ShortPositionsClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            for (EditText et : listEt) {
                codell.removeView(et);
            }
            listEt.clear();
            codeEt1.setText("");
            codeEt2.setText("");
            codeEt3.setText("");
            codeEt1.setBackgroundResource(R.drawable.shape5);
            codeEt2.setBackgroundResource(R.drawable.shape5);
            codeEt3.setBackgroundResource(R.drawable.shape5);
            codeEt1.setEnabled(false);
            codeEt2.setEnabled(false);
            codeEt3.setEnabled(false);
        } else {
            codeEt1.setBackgroundResource(R.drawable.shape);
            codeEt2.setBackgroundResource(R.drawable.shape);
            codeEt3.setBackgroundResource(R.drawable.shape);
            codeEt1.setEnabled(true);
            codeEt2.setEnabled(true);
            codeEt3.setEnabled(true);
        }
    }

    /**
     * 提交信息
     *
     * @param view
     */
    public void SubmitData(View view) {
        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读《实盘比赛协议》", Toast.LENGTH_SHORT).show();
            return;
        }
        // 参赛金额
        String money = moneyEt.getText() + "";
        if ("".equals(money)) {
            Toast.makeText(getApplicationContext(), "请输入参赛资产", Toast.LENGTH_SHORT).show();
            moneyEt.setFocusable(true);
            moneyEt.requestFocus();
            return;
        }
//        if (Integer.valueOf(money) > 100000000) {
//            Toast.makeText(getApplicationContext(), "本场比赛参赛金额上限1亿", Toast.LENGTH_SHORT).show();
//            moneyEt.setFocusable(true);
//            return;
//        }
        // 参数股票
        String code = "";
        if (!nullCodeCb.isChecked()) {
            String str = codeEt1.getText() + "";
            if (!"".equals(str)) {
                code += str + "|";
            }
            str = codeEt2.getText() + "";
            if (!"".equals(str)) {
                code += str + "|";
            }
            str = codeEt3.getText() + "";
            if (!"".equals(str)) {
                code += str + "|";
            }
            for (EditText et : listEt) {
                str = et.getText() + "";
                if (!"".equals(str)) {
                    code += str + "|";
                }
            }
            if ("".equals(code)) {
                Toast.makeText(getApplicationContext(), "请输入参赛股票", Toast.LENGTH_SHORT).show();
                return;
            }
            code = Utils.getNumber(code);
        }
        // 截图
        String image = "";
        if (imageList.size() < 1) {
            Toast.makeText(getApplicationContext(), "请上传账户截图", Toast.LENGTH_SHORT).show();
            return;
        }
        for (String str : imageList) {
            image += str + "|";
        }
        final String moneyStr = money;
        final String codeStr = code;
        final String imageStr = image;
        dialog.showDialog();
        // 报名参加
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                // 比赛id
                dataList.add(new BasicNameValuePair("match_id", firmId));
                // 初始资产
                dataList.add(new BasicNameValuePair("initial", moneyStr));
                // 持有股票
                dataList.add(new BasicNameValuePair("stocks", codeStr));
                // 账户截图
                dataList.add(new BasicNameValuePair("image", imageStr));
                joinStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/match/join", dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    /**
     * 相机的单机事件
     *
     * @param view
     */
    public void CameraClick(View view) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 调用相机前先删除图片
            ImageUtil.scanFileAsync(this, ImageUtil.getSDPath());
            Intent getImageByCamera = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ImageUtil.getSDPath())));
            startActivityForResult(getImageByCamera,
                    REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            CustomToast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_SHORT).show();
        }
        screenshotRl.setVisibility(View.GONE);
    }

    /**
     * 相册的单机事件
     *
     * @param view
     */
    public void AlbumClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相册
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        screenshotRl.setVisibility(View.GONE);
    }

    /**
     * 实盘比赛服务协议
     *
     * @param view
     */
    public void ServiceClick(View view) {
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "28");
        startActivity(intent);
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(joinStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        dialog.dismissDlog();
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {

                            Intent intent = new Intent();
                            intent.putExtra("isSuccess", "true");
                            setResult(0, intent);
                            // 参加成功关闭页面（返回参数提示成功 menghuan）
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    imageList.add(attachId);
                    bitmapArrayList.add(bitmap);
                    RefreshScreenshot();
                    dialog.dismissDlog();
                    break;
            }
        }
    };
}
