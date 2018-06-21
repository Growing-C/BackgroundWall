package com.growingc.backgroundwall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 第二种背景墙设计模式，中间是固定的30*30的正方形，其他随意
 */
public class StaticRecActivity extends AppCompatActivity {
    EditText mWidthTx, mHeightTx, mDefaultSquareTx, mLeftLineTx, mTopLineTx, mVerticalCountTx, mHorizontalCountTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_rec);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWidthTx = (EditText) findViewById(R.id.wall_width);//背景墙宽度
        mHeightTx = (EditText) findViewById(R.id.wall_height);//背景墙高度
        mDefaultSquareTx = (EditText) findViewById(R.id.default_rec_size);//默认正方形尺寸

        mLeftLineTx = (EditText) findViewById(R.id.left_line);//左边距
        mTopLineTx = (EditText) findViewById(R.id.top_line);//上边距

        mVerticalCountTx = (EditText) findViewById(R.id.vertical_count);//纵向四边形数目
        mHorizontalCountTx = (EditText) findViewById(R.id.horizontal_count);//横向四边形数目

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged:" + s.toString());
                autoVerify2();
            }
        };
        mWidthTx.addTextChangedListener(textWatcher);
        mHeightTx.addTextChangedListener(textWatcher);
        mDefaultSquareTx.addTextChangedListener(textWatcher);
//        mVerticalCountTx.addTextChangedListener(textWatcher);
//        mHorizontalCountTx.addTextChangedListener(textWatcher);

        //-----------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(FileUtils.sFilePath);
                if (null == file || !file.exists()) {
                    Snackbar.make(view, "文件不存在！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                it.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。

                //intent.setType(“image/*”);
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                it.addCategory(Intent.CATEGORY_OPENABLE);
                //it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//用了newtask会直接返回resultcanceled，所以这里不能用！
                it.setDataAndType(Uri.fromFile(file), "file/*");
                try {
                    startActivityForResult(it, 1);
                } catch (Exception e) {
                    Snackbar.make(view, "请安装文件管理器", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    /**
     * 第二代验证方式
     *
     * @return
     */
    private DataModel autoVerify2() {
        String widthS = mWidthTx.getText().toString();//背景宽度
        String heightS = mHeightTx.getText().toString();//背景高度
        String squareS = mDefaultSquareTx.getText().toString();//正方形尺寸

        String horizontalCountS = mHorizontalCountTx.getText().toString();
        String verticalCountS = mVerticalCountTx.getText().toString();

        int horizontalCount = Integer.parseInt(horizontalCountS);//横向数目
        int verticalCount = Integer.parseInt(verticalCountS);//纵向正方形数目

        if (TextUtils.isEmpty(widthS) || TextUtils.isEmpty(heightS) || TextUtils.isEmpty(squareS)) {//三者缺一不可
            return null;
        }

        float width = Float.parseFloat(widthS);//背景宽度
        float height = Float.parseFloat(heightS);//背景高度
        float squareSize = Float.parseFloat(squareS);//正方形边长
        float squareDiagonalLine = (float) Math.sqrt(Math.pow(squareSize, 2) + Math.pow(squareSize, 2));//对角线

        if (width < squareDiagonalLine || height < squareDiagonalLine)//背景宽高必须大于等于正方形的对角线
            return null;

        DataModel model = new DataModel((int) (width * 10), (int) (height * 10), (int) (squareSize * 10), verticalCount, horizontalCount);

        if (!model.isDataRight()) {
            Snackbar.make(mLeftLineTx, "计算尺寸为负，请重新设置!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }
        //验证正确则设置值且返回
        mTopLineTx.setText("" + ((float) model.topLineSize) / 10);
        mLeftLineTx.setText("" + ((float) model.leftLineSize) / 10);

        mHorizontalCountTx.setText("" + model.horizontalCount);
        mVerticalCountTx.setText("" + model.verticalCount);
        return model;
    }

    public void onCountChange(View v) {
        String horizontalCountS = mHorizontalCountTx.getText().toString();
        String verticalCountS = mVerticalCountTx.getText().toString();

        int horizontalCount = Integer.parseInt(horizontalCountS);//横向数目
        int verticalCount = Integer.parseInt(verticalCountS);//纵向正方形数目
        switch (v.getId()) {
            case R.id.clear:
                mVerticalCountTx.setText("0");
                mHorizontalCountTx.setText("0");
                mLeftLineTx.setText("");
                mTopLineTx.setText("");
                mWidthTx.setText("");
                mHeightTx.setText("");
                break;
            case R.id.vertical_minus:
                if (verticalCount > 0) {
                    mVerticalCountTx.setText("" + (--verticalCount));
                    autoVerify2();
                }
                break;
            case R.id.vertical_add:
                mVerticalCountTx.setText("" + (++verticalCount));
                autoVerify2();
                break;
            case R.id.horizontal_minus:
                if (horizontalCount > 0) {
                    mHorizontalCountTx.setText("" + (--horizontalCount));
                    autoVerify2();
                }
                break;
            case R.id.horizontal_add:
                mHorizontalCountTx.setText("" + (++horizontalCount));
                autoVerify2();
                break;
            default:
                break;
        }
    }

    public void onClick(View v) {
        String widthS = mWidthTx.getText().toString();
        String heightS = mHeightTx.getText().toString();
        String squareS = mDefaultSquareTx.getText().toString();//正方形尺寸

        String horizontalCountS = mHorizontalCountTx.getText().toString();
        String verticalCountS = mVerticalCountTx.getText().toString();

        int horizontalCount = Integer.parseInt(horizontalCountS);//横向数目
        int verticalCount = Integer.parseInt(verticalCountS);//纵向正方形数目

        System.out.println("horizontal spinner:" + horizontalCount);
        System.out.println("vertical spinner:" + verticalCount);
        System.out.println("mWidthTx:" + widthS);
        System.out.println("mHeightTx:" + heightS);
        System.out.println("squareS:" + squareS);

        if (TextUtils.isEmpty(widthS) || TextUtils.isEmpty(heightS) || TextUtils.isEmpty(squareS)) {
            Snackbar.make(mLeftLineTx, "请先完善数据!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        DataModel dataModel = autoVerify2();

        if (dataModel == null) {
            Snackbar.make(mLeftLineTx, "数据错误!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        switch (v.getId()) {
            case R.id.generate://生成图片
                //开始画图
                Bitmap bitmap = PicGenerator.drawOriginalSquare(dataModel);
                if (bitmap == null) {
                    Snackbar.make(mLeftLineTx, "数据错误,图片绘制失败!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    FileUtils.saveBitmap(StaticRecActivity.this, bitmap, "原图" + widthS + "*" + heightS + ".png");
                    Snackbar.make(mLeftLineTx, "图片保存成功!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.preview://预览
//                Intent it = new Intent(StaticRecActivity.this, FullscreenActivity.class);
//                it.putExtra("data", dataModel);
//                startActivity(it);

                saveAndPreview(dataModel);
                break;
            default:
                break;
        }
    }

    /**
     * 保存并查看
     */
    private void saveAndPreview(DataModel data) {
        Bitmap bitmap = PicGenerator.drawInstrumentSquare(data);
        if (bitmap == null) {
            Snackbar.make(mLeftLineTx, "数据错误,图片绘制失败!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MMddHHmm");
            Date date = new Date();

            String fileName = "设计图" + data.width / 10 + "*" + data.height / 10 + "-"+format.format(date) + ".png";
            FileUtils.saveBitmap(StaticRecActivity.this, bitmap, fileName);
            Snackbar.make(mLeftLineTx, "保存成功!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            File file = new File(FileUtils.sFilePath + fileName);

            if (!file.exists()) {
                Snackbar.make(mLeftLineTx, "文件不存在!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            Uri uri = Uri.fromFile(file);
            System.out.println(uri.toString());

            String path = uri.getPath();
            System.out.println(path);

            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setDataAndType(uri, "image/*");
            startActivity(it);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult" + resultCode);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            System.out.println(uri.toString());

            String path = uri.getPath();
            System.out.println(path);

            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setDataAndType(uri, "image/*");
            startActivity(it);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
