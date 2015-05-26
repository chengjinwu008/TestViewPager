package com.cjq.TestViewPager;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.cjq.IPhoneMenu.IPhoneMenu;

import java.util.ArrayList;
import java.util.List;

public class MyLinearLayout extends RelativeLayout implements View.OnClickListener, IPhoneMenu.IPhoneMenuListener {
    private int mWidth;
    private int cols;
    private Context context;
    private ImageView btn;
    private boolean addViewFlag=false;
    private ImageAddListener listener;
    private List<Pair<String,Bitmap>> items=new ArrayList<>();

    public List<Pair<String, Bitmap>> getItems() {
        return items;
    }

    public void setItems(List<Pair<String, Bitmap>> items) {
        this.items = items;
    }

    public void setListener(ImageAddListener listener) {
        this.listener = listener;
    }

    @Override
    public void onclick(AdapterView<?> parent, View view, int position, long id, AlertDialog dialog) {
        if(id==0){
            //从相册选取
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            String[] projection = { MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE };
            String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
            String[] selectionArgs = { "image/jpeg" };
            String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
            Cursor cursor = contentResolver.query(uri, projection, selection,
                    selectionArgs, sortOrder);


            cursor.close();
        }else if(id==1){
            //拍照
        }
        dialog.dismiss();
    }

    public interface ImageAddListener{
        void imageAdded();
    }

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        this.context =context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWidth = metrics.widthPixels;
        Log.i("width", String.valueOf(mWidth));
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyLinearLayout);
        cols = typedArray.getInt(R.styleable.MyLinearLayout_cols,3);
        btn = new ImageView(context);
        btn.setImageResource(R.drawable.as2);

        btn.setOnClickListener(this);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed || addViewFlag){
            if(addViewFlag && listener!=null)
                listener.imageAdded();
            addViewFlag=false;
            int cellWidth = mWidth/cols;

            int nowRow = 0;
            int nowCol =0;

            for(int i=0;i<getChildCount();i++){
                int left = nowRow*cellWidth;
                int top = nowCol*cellWidth;
                View view = getChildAt(i);
                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width=cellWidth;
                params.height =cellWidth;
                params.leftMargin = left;
                params.topMargin = top;
                view.setLayoutParams(params);

                nowRow++;
                if(nowRow>=cols){
                    nowCol++;
                    nowRow=0;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(btn.getParent()==null)
        this.addView(btn);
        int height = (items.size()+1)%cols==0?(items.size()+1)*mWidth/cols/cols:((items.size()+1)/cols+1)*mWidth/cols;
        setMeasuredDimension(getMeasuredWidth(),height);
    }

    @Override
    public void addView(View child) {
        removeView(btn);
        super.addView(child);
        addViewFlag=true;
    }

    public void addItem(Pair<String,Bitmap> pair){
        items.add(pair);
        ImageView imageView = new ImageView(context);
        //todo 在添加的同时添加子view
        imageView.setImageBitmap(pair.second);
        addView(imageView);
    }

    @Override
    public void onClick(View v) {
        List<Pair<Integer,String>> menuData = new ArrayList<>();
        menuData.add(new Pair<>(null,"从相册选取"));
        menuData.add(new Pair<>(null,"拍照"));
        IPhoneMenu iPhoneMenu = new IPhoneMenu(context,menuData,this);
        iPhoneMenu.show();
//        addItem(new Pair<>("url", BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher)));
    }
}
