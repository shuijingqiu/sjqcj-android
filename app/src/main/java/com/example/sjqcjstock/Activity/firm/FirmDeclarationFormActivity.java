package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
 * 实盘报单页面
 * Created by Administrator on 2017/8/29.
 */
public class FirmDeclarationFormActivity extends Activity {

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 99; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 98; // 照相获取
    // 网络请求提示
    private CustomProgress dialog;
    private LinearLayout codell;
    private List<EditText> listEt;
    // 实盘标题
    private TextView firmTitleTv;
    // 总资产
    private EditText totalMoneyEt;
    // 仓位
    private EditText positionEt;
    // 水晶币个数
    private EditText sjbCountEt;
    // 股票代码
    private EditText codeEt1, codeEt2, codeEt3;
    // 下拉选择列表
//    private Spinner typeSp;
    private RadioGroup typeRg;
    private RadioButton typeRb1;
    private RadioButton typeRb2;
    // 转账金额
    private EditText moneyEt;
    // 吐槽几句
    private EditText remarksEt;
    // 空仓
    private CheckBox nullCodeCb;
    private CheckBox pushKgCb;
    private TextView pushKgTv;
    // 显示截图的上传方式
    private RelativeLayout screenshotRl;
    // 用户截图的显示列表
    private GridView userScreenshotGv;
    // 明细截图的显示列表
    private GridView detailedScreenshotGv;
    // gridView的控制器
    private ScreenshotImageAdapter userImageAdapter, detailedImageAdapter;
    // 截图显示的集合
    private ArrayList<Bitmap> userBitmapArrayList, detailedBitmapArrayList;
    // 截图类型 0:用户截图 1:明细截图
    private String screenshotType;

    // 上传用户截图的路径
    private List<String> userImageList;
    // 上传明细截图的路径
    private List<String> detailedImageList;
    // 参加比赛返回的数据
    private String joinStr;
    // 赛场的id
    private String firmId;
    // 赛场id
    private String title;
    // 上传图片返回的id
    private String attachId = "";
    // 上次的图片
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_declaration_form);
        findView();
    }

    private void findView() {
        dialog = new CustomProgress(this);
        firmId = getIntent().getStringExtra("firmId");
        title = getIntent().getStringExtra("title");
        listEt = new ArrayList<EditText>();
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firmTitleTv = (TextView) findViewById(R.id.firm_title_tv);
        firmTitleTv.setText(title);
        totalMoneyEt = (EditText) findViewById(R.id.total_money_et);
        positionEt = (EditText) findViewById(R.id.position_et);
        sjbCountEt = (EditText) findViewById(R.id.sjb_count_et);
        remarksEt = (EditText) findViewById(R.id.remarks_et);
        nullCodeCb = (CheckBox) findViewById(R.id.null_code_cb);
        pushKgCb = (CheckBox) findViewById(R.id.push_kg_cb);
        pushKgTv = (TextView) findViewById(R.id.push_kg_tv);
        codell = (LinearLayout) findViewById(R.id.code_ll);
        codeEt1 = (EditText) findViewById(R.id.code_et1);
        codeEt2 = (EditText) findViewById(R.id.code_et2);
        codeEt3 = (EditText) findViewById(R.id.code_et3);
        typeRg = (RadioGroup) findViewById(R.id.type_rg);
        typeRb1 = (RadioButton) findViewById(R.id.type_rb_1);
        typeRb2 = (RadioButton) findViewById(R.id.type_rb_2);
        moneyEt = (EditText) findViewById(R.id.money_et);
        screenshotRl = (RelativeLayout) findViewById(R.id.screenshot_rl);
        screenshotRl.getBackground().setAlpha(150);
        screenshotRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshotRl.setVisibility(View.GONE);
            }
        });
        userScreenshotGv = (GridView) findViewById(R.id.user_screenshot_gv);
        detailedScreenshotGv = (GridView) findViewById(R.id.detailed_screenshot_gv);
        userImageAdapter = new ScreenshotImageAdapter(FirmDeclarationFormActivity.this);
        detailedImageAdapter = new ScreenshotImageAdapter(FirmDeclarationFormActivity.this);
        userScreenshotGv.setAdapter(userImageAdapter);
        detailedScreenshotGv.setAdapter(detailedImageAdapter);
        userBitmapArrayList = new ArrayList<Bitmap>();
        detailedBitmapArrayList = new ArrayList<Bitmap>();
        userScreenshotGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 删除并刷新列表
                userBitmapArrayList.remove(position);
                userImageList.remove(position);
                userImageAdapter.setlistData(userBitmapArrayList);
            }
        });
        detailedScreenshotGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 删除并刷新列表
                detailedBitmapArrayList.remove(position);
                detailedImageList.remove(position);
                detailedImageAdapter.setlistData(detailedBitmapArrayList);
            }
        });

        userImageList = new ArrayList<String>();
        detailedImageList = new ArrayList<String>();
        codeEt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmDeclarationFormActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent,15);
            }
        });
        codeEt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmDeclarationFormActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent,16);
            }
        });
        codeEt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmDeclarationFormActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent,17);
            }
        });
    }

    /**
     *
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data
     */
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
        }else{
            if (data==null){
                return;
            }
            String code = data.getExtras().get("code")+"";
            // 处理选择的股票的
            if (!"".equals(code)){
                String codeName  = data.getExtras().get("name").toString()+"("+code+")";
                switch (requestCode){
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
                        listEt.get(requestCode-1).setText(codeName);
                        break;
                }
            }
        }
    }

    /**
     * 转账有无的点击事件
     *
     * @param view
     */
    public void PushKgClick(View view) {
        typeRg.clearCheck();
        CheckBox checkBox = (CheckBox) view;
        if (!checkBox.isChecked()) {
            pushKgTv.setText("无");
            moneyEt.setText("");
            moneyEt.setFocusable(false);
            moneyEt.setFocusableInTouchMode(false);
            moneyEt.setBackgroundResource(R.drawable.shape5);
            typeRb2.setClickable(false);
            typeRb1.setClickable(false);
            typeRg.setBackgroundColor(typeRg.getResources().getColor(R.color.gray_line));
        } else {
            pushKgTv.setText("有");
            moneyEt.setFocusable(true);
            moneyEt.setFocusableInTouchMode(true);
            moneyEt.setBackgroundResource(R.drawable.shape);
            typeRb1.setChecked(true);
            typeRb2.setClickable(true);
            typeRb1.setClickable(true);
            typeRg.setBackgroundColor(typeRg.getResources().getColor(R.color.white));
        }
    }

    /**
     * 空仓的点击事件
     *
     * @param view
     */
    public void ShortPositionsClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (!checkBox.isChecked()) {
            codeEt1.setBackgroundResource(R.drawable.shape);
            codeEt2.setBackgroundResource(R.drawable.shape);
            codeEt3.setBackgroundResource(R.drawable.shape);
            codeEt1.setEnabled(true);
            codeEt2.setEnabled(true);
            codeEt3.setEnabled(true);
        } else {
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
        }
    }

    /**
     * 追加文本框
     *
     * @param view
     */
    public void AddEtClick(View view) {
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(screenshotRl.getWindowToken(), 0);
        if (nullCodeCb.isChecked() || listEt.size() > 12) {
            return;
        }
        EditText editText = new EditText(this);
        editText.setHint("请输入股票代码");
        editText.setBackgroundResource(R.drawable.shape);
        editText.setPadding(10, 10, 10, 10);
        editText.setTextSize(14);
        editText.setTag(listEt.size()+1);
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
                Intent intent = new Intent(FirmDeclarationFormActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent,code);
            }
        });
        codell.addView(editText);
        listEt.add(editText);
    }

    /**
     * 刷新截图页面
     */
    private void RefreshScreenshot() {
        if ("0".equals(screenshotType)) {
            if (userBitmapArrayList.size() == 5) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, userScreenshotGv.getHeight() * 2);
                userScreenshotGv.setLayoutParams(params);
            } else if (userBitmapArrayList.size() == 4) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                userScreenshotGv.setLayoutParams(params);
            } else if (userBitmapArrayList.size() == 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                userScreenshotGv.setLayoutParams(params);
            }
            userImageAdapter.setlistData(userBitmapArrayList);
        } else {
            if (detailedBitmapArrayList.size() == 5) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, detailedScreenshotGv.getHeight() * 2);
                detailedScreenshotGv.setLayoutParams(params);
            } else if (detailedBitmapArrayList.size() == 4) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                detailedScreenshotGv.setLayoutParams(params);
            } else if (detailedBitmapArrayList.size() == 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                detailedScreenshotGv.setLayoutParams(params);
            }
            detailedImageAdapter.setlistData(detailedBitmapArrayList);
        }
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
     * 用户截图
     *
     * @param view
     */
    public void UserScreenshotClick(View view) {
        // 最多8张图
        if (userBitmapArrayList.size() > 7) {
            return;
        }
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(screenshotRl.getWindowToken(), 0);
        screenshotRl.setVisibility(View.VISIBLE);
        screenshotType = "0";
    }

    /**
     * 明细截图
     *
     * @param view
     */
    public void DetailedScreenshotClick(View view) {
        // 最多8张图
        if (detailedBitmapArrayList.size() > 7) {
            return;
        }
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(screenshotRl.getWindowToken(), 0);
        screenshotRl.setVisibility(View.VISIBLE);
        screenshotType = "10";
    }

    /**
     * 提交信息
     *
     * @param view
     */
    public void SubmitData(View view) {
        // 今日总资产
        String totalMoneyStr = totalMoneyEt.getText() + "";
        if ("".equals(totalMoneyStr)) {
            Toast.makeText(getApplicationContext(), "请输入今日总资产", Toast.LENGTH_SHORT).show();
            totalMoneyEt.setFocusable(true);
            totalMoneyEt.requestFocus();
            return;
        }
        // 当前仓位
        final String positionStr = positionEt.getText() + "";
        if ("".equals(positionStr)) {
            Toast.makeText(getApplicationContext(), "请输入当前仓位", Toast.LENGTH_SHORT).show();
            positionEt.setFocusable(true);
            positionEt.requestFocus();
            return;
        }
        // 转账金额
       final String moneyStr = moneyEt.getText()+"";
        String tradeTypeStr  = "";
        // 转账类型
//        int typeStr = typeSp.getSelectedItemPosition();
        int typeStr = typeRg.getCheckedRadioButtonId();
        if (pushKgCb.isChecked()) {
            if ("".equals(moneyStr)){
                Toast.makeText(getApplicationContext(), "请输入转账金额", Toast.LENGTH_SHORT).show();
                moneyEt.setFocusable(true);
                moneyEt.requestFocus();
                return;
            }
            if (typeStr == R.id.type_rb_1){
                tradeTypeStr =  "in";
            }else{
                tradeTypeStr =  "out";
            }
        }
        // 参数股票
        String code = "";
        String codeName = "";
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
                Toast.makeText(getApplicationContext(), "请输入当前持股", Toast.LENGTH_SHORT).show();
                return;
            }
            codeName = code;
            code = Utils.getNumber(code);
        }
        if((Double.valueOf(positionStr) == 0 && !nullCodeCb.isChecked())||(Double.valueOf(positionStr) != 0 && nullCodeCb.isChecked())){
            Toast.makeText(getApplicationContext(), "请输正确的仓位或当前持股", Toast.LENGTH_SHORT).show();
            positionEt.setFocusable(true);
            positionEt.requestFocus();
            return;
        }
        // 用户截图
        String userImage = "";
        if (userImageList.size() < 1) {
            Toast.makeText(getApplicationContext(), "请上传用户截图", Toast.LENGTH_SHORT).show();
            return;
        }
        // 明细截图
        String detaileImage = "";
        // 明细截图可以不传
//        if (detailedImageList.size() < 1) {
//            Toast.makeText(getApplicationContext(), "请上传明细截图", Toast.LENGTH_SHORT).show();
//            return;
//        }
        for (String str : userImageList) {
            userImage += str + "|";
        }
        for (String str : detailedImageList) {
            detaileImage += str + "|";
        }
        String sjbCount = sjbCountEt.getText() + "";
        if ("".equals(sjbCount)){
            sjbCount = "0";
        }
        // 水晶币个数
        final String sjbCountStr = sjbCount;
        // 吐槽几句
        final String remarksStr = remarksEt.getText() + "";

        if (remarksStr.length()<1) {
            Toast.makeText(getApplicationContext(), "请输入操作总结", Toast.LENGTH_SHORT).show();
            remarksEt.setFocusable(true);
            remarksEt.requestFocus();
            return;
        }
        final String totalMoney = totalMoneyStr;
        final String codeStr = code;
        final String codeNameStr = codeName;
        final String userImageStr = userImage;
        final String detaileImageStr = detaileImage;
        final String tradeType = tradeTypeStr;
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
                // 当前资产
                dataList.add(new BasicNameValuePair("assets", totalMoney));
                // 持仓
                dataList.add(new BasicNameValuePair("positions", positionStr));
                // 持有股票
                dataList.add(new BasicNameValuePair("stocks", codeStr));
                dataList.add(new BasicNameValuePair("stock_label", codeNameStr));
                // 用户截图
                dataList.add(new BasicNameValuePair("img", userImageStr));
                // 明细截图
                dataList.add(new BasicNameValuePair("dimg", detaileImageStr));
                // 需要打赏的水晶币
                dataList.add(new BasicNameValuePair("reward", sjbCountStr));
                // 描述
                dataList.add(new BasicNameValuePair("remark", remarksStr));
                // 转入转出金额
                dataList.add(new BasicNameValuePair("access", moneyStr));
                // 交易类型  in转入 out转出
                dataList.add(new BasicNameValuePair("trade_type", tradeType));

                joinStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/match/update", dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }
//
//    /**
//     * 跳转到规则页面
//     *
//     * @param view
//     */
//    public void RuleClick(View view) {
//
//    }

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
                            // 参加成功关闭页面
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    dialog.dismissDlog();
                    if ("".equals(attachId)) {
                        // 请求失败的情况
                        CustomToast.makeText(getApplicationContext(), "图片上传失败请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ("0".equals(screenshotType)){
                        userBitmapArrayList.add(bitmap);
                        userImageList.add(attachId);
                    }else{
                        detailedBitmapArrayList.add(bitmap);
                        detailedImageList.add(attachId);
                    }
                    RefreshScreenshot();
                    break;
            }
        }
    };

}
