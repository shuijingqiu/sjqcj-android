package com.example.sjqcjstock.richeditor;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.commentrewardweiboActivity;
import com.example.sjqcjstock.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是丿ت富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有兿
 *
 * @author xmuSistone
 */
@SuppressLint({"NewApi", "InflateParams"})
public class RichTextEditor extends ScrollView {
    private static final int EDIT_PADDING = 10; // edittext常规padding昿0dp
    private static final int EDIT_FIRST_PADDING_TOP = 8; // 第一个EditText的paddingTop倿
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯丿ڄ〿
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯丿؀个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 承܉EditText的软键盘监听噿
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 承܉EditText的焦点监听listener
    public EditText lastFocusEdit; // 朿ߑ被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;


    /**
     * 传入Activity
     */

    private com.example.sjqcjstock.Activity.commentlongweiboActivity commentlongweiboActivity;

    private commentrewardweiboActivity commentlongdiscussareaweiboActivity;

    public void sendActivity(com.example.sjqcjstock.Activity.commentlongweiboActivity commentlongweiboActivity) {
        // TODO Auto-generated method stub
        this.commentlongweiboActivity = commentlongweiboActivity;

    }


    public void senddiscussareaweActivity(commentrewardweiboActivity commentlongdiscussareaweiboActivity) {
        // TODO Auto-generated method stub
        this.commentlongdiscussareaweiboActivity = commentlongdiscussareaweiboActivity;

    }


    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        allLayout.setBackgroundColor(Color.WHITE);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(allLayout, layoutParams);

        // 2. 初始化键盘ꀦ<监吿
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 3. 图片叉掉处理
        btnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
            }
        };

        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };

//		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        editNormalPadding = dip2px(EDIT_PADDING);
        //初始化值
        EditText firstEdit = createEditText("",
                dip2px(EDIT_FIRST_PADDING_TOP));
        firstEdit.setHeight(80);
        firstEdit.setMaxHeight(600);
        firstEdit.setMaxLines(30);
        firstEdit.setGravity(Gravity.BOTTOM);
        firstEdit.setHintTextColor(firstEdit.getResources().getColor(R.color.color_999999));
        firstEdit.setPadding(20, 15, 0, 25);
        firstEdit.setCursorVisible(true);
        firstEdit.setHint("正文");
        //firstEdit.set

        //firstEdit.setLineSpacing(add, mult)

        //firstEdit.setLineSpacing(add, mult)
        //动态设置光标颜色
        Field f;
        try {
            f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(firstEdit, R.drawable.color_cursor2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        firstEdit.setTextSize(16);
//		 ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
//				 300);
//	        firstEdit.setLayoutParams(lp);
        firstEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (commentlongweiboActivity != null) {
                    commentlongweiboActivity.getRichTextEditorforced();
                }

                if (commentlongdiscussareaweiboActivity != null) {
                    commentlongdiscussareaweiboActivity.getRichTextEditorforced();
                }

            }
        });
        //allLayout.addView(firstEdit, firstEditParam);
        allLayout.addView(firstEdit, firstEditParam);

        lastFocusEdit = firstEdit;


    }

    /**
     * 处理软键盘backSpace回ꀤڋ件
     *
     * @param editTxt 光标承ܨ的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上丿تview对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上丿تview对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    allLayout.setLayoutTransition(null);
                    allLayout.removeView(editTxt);
                    allLayout.setLayoutTransition(mTransitioner); // 恢复transition动画

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                }
            }
        }
    }

    /**
     * 处理图片叉掉的点击事仿
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删陿
     */
    private void onImageCloseClick(View view) {
        if (!mTransitioner.isRunning()) {
            disappearingImageIndex = allLayout.indexOfChild(view);
            allLayout.removeView(view);
        }
    }

    /**
     * 生成文本输入桿
     */
    public EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.edit_item1,
                null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        //editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, 0);
        editText.setPadding(editNormalPadding, 10, editNormalPadding, 20);
        editText.setHeight(40);
        editText.setMaxHeight(400);
        editText.setHint(hint);
        editText.setGravity(Gravity.TOP);
        editText.setTextSize(16);
        editText.setLineSpacing(1.2f, 1.2f);

        editText.setOnFocusChangeListener(focusListener);

        //动态设置光标颜色
        Field f;
        try {
            f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.color_cursor2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        editText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (commentlongweiboActivity != null) {
                    commentlongweiboActivity.getRichTextEditorforced();
                }

                if (commentlongdiscussareaweiboActivity != null) {
                    commentlongdiscussareaweiboActivity.getRichTextEditorforced();

                }

            }
        });
        return editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.edit_imageview, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        return layout;
    }

    /**
     * 根据绝对路径添加view
     *
     * @param imagePath
     */
    public void insertImage(String imagePath, String serverimagepath, Bitmap bmpCompressed) {
        insertImage(bmpCompressed, serverimagepath);
    }

    /**
     * 插入丿ܠ图片
     */
    private void insertImage(Bitmap bitmap, String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            //addEditTextAtIndex(-1, "图片间隔");

            addImageViewAtIndex(lastEditIndex, bitmap, imagePath);


        } else {
            // 如果EditText非空且光标不在最顶端，则霿Ɓ添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (allLayout.getChildCount() - 1 == lastEditIndex
                    || editStr2.length() > 0) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }
            //addEditTextAtIndex(lastEditIndex-1, editStr2);
            addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath);

//			//在获取光标的EditText出添加内容
//			for(int i=0;i<allLayout.getChildCount();i++){
//				if(allLayout.getChildAt(i) instanceof EditText){
//					EditText editer=(EditText)allLayout.getChildAt(i);
//					
//					if(editer.isFocused()){
//						//在光标处添加
//
//					}
//				
//				}
//
//			}
            //addEditTextAtIndex(i-1, editStr2);

        }
        int lastindex = allLayout.getChildCount() - 1;
        lastFocusEdit = (EditText) allLayout.getChildAt(lastindex);
        lastFocusEdit.requestFocus();
        lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
        hideKeyBoard();

        if (commentlongweiboActivity != null) {
            commentlongweiboActivity.editortotop();
        }

        if (commentlongdiscussareaweiboActivity != null) {
            commentlongdiscussareaweiboActivity.editortotop();
        }
    }

    /**
     * 隐藏小键盿
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文孿
     */
    public void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("", getResources()
                .getDimensionPixelSize(R.dimen.edit_padding_top));
        editText2.setText(editStr);
        editText2.setOnFocusChangeListener(focusListener);

//		editText2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				commentlongweiboActivity.getRichTextEditorforced();
//
//			}
//		});

        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
        allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画

        if (commentlongweiboActivity != null) {
            commentlongweiboActivity.editortotop();
        }

        if (commentlongdiscussareaweiboActivity != null) {
            commentlongdiscussareaweiboActivity.editortotop();
        }

    }

    /**
     * 获取编辑器的子布局个数
     */

    public int editerviewscounts() {
        // TODO Auto-generated method stub
        int count = 0;
        for (int i = 0; i < allLayout.getChildCount(); i++) {
            //EditText edit=(EditText)allLayout.getChildAt(i);
            //if(edit instanceof EditText){
            count++;
            //}

        }
        return count;

    }

    /**
     * 监测是否有edit获得光标
     */
    public boolean iseditgetfocused() {
        // TODO Auto-generated method stub
        for (int i = 0; i < allLayout.getChildCount(); i++) {
            if (allLayout.getChildAt(i) instanceof EditText) {
                EditText editer = (EditText) allLayout.getChildAt(i);

                if (editer.isFocused()) {


                    return true;
                }

            }

        }
        return false;

    }


    /**
     * 第一次点击时获取内容的光标
     */
    public boolean firstclickgetfource() {
        // TODO Auto-generated method stub
        int j = 0;
        for (int i = 0; i < allLayout.getChildCount(); i++) {


            if (allLayout.getChildAt(i) instanceof EditText) {
                EditText editer = (EditText) allLayout.getChildAt(i);
                j++;
                if (editer.isFocused()) {
                    return true;
                }
            }
        }
        if (j == 1) {
            allLayout.getChildAt(0).requestFocus();
            if (commentlongweiboActivity != null) {
                commentlongweiboActivity.editortodown();
            } else if (commentlongdiscussareaweiboActivity != null) {
                commentlongdiscussareaweiboActivity.shareFreshTingsTx.setVisibility(View.GONE);
                commentlongdiscussareaweiboActivity.editortodown();
            }
        }
        return false;

    }

    /**
     * 在当前的光标处加入内容
     */

    public void editeratinfoforlastview(String atinfo) {
        // TODO Auto-generated method stub

        //在获取光标的EditText出添加内容
        for (int i = 0; i < allLayout.getChildCount(); i++) {
            if (allLayout.getChildAt(i) instanceof EditText) {
                EditText editer = (EditText) allLayout.getChildAt(i);

                if (editer.isFocused()) {
                    //在光标处添加
                    int index = editer.getSelectionStart();
                    Editable editable = editer.getText();
                    editable.insert(index, atinfo);
                }

            }

        }
        if (commentlongweiboActivity != null) {
            commentlongweiboActivity.editortotop();

        } else if (commentlongdiscussareaweiboActivity != null) {
            commentlongdiscussareaweiboActivity.editortotop();
        }

//		EditText editer=(EditText)allLayout.getChildAt(allLayout.getChildCount()-1);
//		
//		//在光标处添加
//	    int index = editer.getSelectionStart();  
//	    Editable editable = editer.getText();  
//	    editable.insert(index, atinfo);  

        //String restr=editer.getText().toString();
        //editer.setText(restr+atinfo);

    }


    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(final int index, Bitmap bmp,
                                     String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout
                .findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setBitmap(bmp);
        imageView.setAbsolutePath(imagePath);

        // 调整imageView的高庿
        //int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 300);
        imageView.setLayoutParams(lp);

        // onActivityResult无法触发动画，此处post处理
        allLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                allLayout.addView(imageLayout, index);
                //插入图片时在上面插入一行edit
                addEditTextAtIndex(index, "");
            }
        }, 200);
    }

    /**
     * 根据view的宽度，动瀧ܩ放bitmap尺寸
     *
     * @param width view的宽庿
     */
    private Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    // mergeEditText();
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = allLayout.getChildAt(disappearingImageIndex - 1);
        View nextView = allLayout.getChildAt(disappearingImageIndex);
        if (preView != null && preView instanceof EditText && null != nextView
                && nextView instanceof EditText) {
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();
            String mergeText = "";
            if (str2.length() > 0) {
                mergeText = str1 + "\n" + str2;
            } else {
                mergeText = str1;
            }

            allLayout.setLayoutTransition(null);
            allLayout.removeView(nextEdit);
            preEdit.setText(mergeText);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
            allLayout.setLayoutTransition(mTransitioner);
        }
    }

    /**
     * dp和pixel转换
     *
     * @param dipValue dp倿
     * @return 像素倿
     */
    public int dip2px(float dipValue) {
        float m = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 对外提供的接叿 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> dataList = new ArrayList<EditData>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView
                        .findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
                itemData.bitmap = item.getBitmap();
            }
            dataList.add(itemData);
        }

        return dataList;
    }

    public class EditData {
        public String inputStr;
        public String imagePath;
        Bitmap bitmap;
    }
}
