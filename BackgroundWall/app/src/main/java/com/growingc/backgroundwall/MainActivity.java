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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;

/**
 * 最初版背景墙设计模式，宽高，
 */
public class MainActivity extends AppCompatActivity {
    Spinner mVerticalS, mHorizontalS;
    EditText mWidthTx, mHeightTx, mLeftLineTx, mTopLineTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWidthTx = (EditText) findViewById(R.id.wall_width);
        mHeightTx = (EditText) findViewById(R.id.wall_height);
        mLeftLineTx = (EditText) findViewById(R.id.left_line);
        mTopLineTx = (EditText) findViewById(R.id.top_line);

        mVerticalS = (Spinner) findViewById(R.id.spinner_vertical);
        mHorizontalS = (Spinner) findViewById(R.id.spinner_horizontal);

        String[] items = getResources().getStringArray(R.array.spinner_horizontal);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        mHorizontalS.setAdapter(adapter);

        mHorizontalS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("mHorizontalS onItemSelected---->" + position);
                autoVerify();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mVerticalS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("mVerticalS onItemSelected---->" + position);
                autoVerify();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTopLineTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("mTopLineTx afterTextChanged");
                if (!mTopLineTx.isEnabled()) {
                    return;
                }
                if (TextUtils.isEmpty(s.toString())) {//如果为空则使topLineTx取消锁定
                    mLeftLineTx.setEnabled(true);
                } else {
                    mLeftLineTx.setEnabled(false);
                }
                autoVerify();
            }
        });
        mLeftLineTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("mLeftLineTx afterTextChanged");
                if (!mLeftLineTx.isEnabled()) {
                    return;
                }
                if (TextUtils.isEmpty(s.toString())) {//如果为空则使topLineTx取消锁定
                    mTopLineTx.setEnabled(true);
                } else {
                    mTopLineTx.setEnabled(false);
                }

                autoVerify();
            }
        });

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
     * 自动验证正方形
     */
    private DataModel autoVerify() {
        String widthS = mWidthTx.getText().toString();
        String heightS = mHeightTx.getText().toString();
        int horizontalCount = Integer.parseInt(mHorizontalS.getSelectedItem().toString());
        int verticalCount = Integer.parseInt(mVerticalS.getSelectedItem().toString());

        String leftLineS = mLeftLineTx.getText().toString();
        String topLineS = mTopLineTx.getText().toString();

        if (horizontalCount <= 0 || verticalCount <= 0)
            return null;

        if (TextUtils.isEmpty(widthS) || TextUtils.isEmpty(heightS)) {
            Snackbar.make(mLeftLineTx, "请填写背景尺寸数据!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }
        if (TextUtils.isEmpty(leftLineS) && TextUtils.isEmpty(topLineS)) {
            Snackbar.make(mLeftLineTx, "请填写上下边线或左右边线数据!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }

        float width = Float.parseFloat(widthS);
        float height = Float.parseFloat(heightS);
        boolean leftEnable = mLeftLineTx.isEnabled();
        boolean topEnable = mTopLineTx.isEnabled();
        System.out.println("leftEnable:" + leftEnable);
        System.out.println("topEnable:" + topEnable);
        if (leftEnable && topEnable || !leftEnable && !topEnable) {//不可以全部enable或者全部不可用
            Snackbar.make(mLeftLineTx, "出现未知错误!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }
        DataModel model = new DataModel();

        int verifySize = 0;//验证用的上下边线长度或者左右边线长度
        if (leftEnable) {
            float leftLineSize = Float.parseFloat(leftLineS);
            verifySize = model.getTopLineSize((int) (width * 10), (int) (height * 10), (int) (leftLineSize * 10), verticalCount, horizontalCount);

        } else if (topEnable) {
            float topLineSize = Float.parseFloat(topLineS);
            verifySize = model.getLeftLineSize((int) (width * 10), (int) (height * 10), (int) (topLineSize * 10), verticalCount, horizontalCount);

        }

        if (verifySize <= 0) {
            Snackbar.make(mLeftLineTx, "计算尺寸为负，请重新设置!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }


        if (leftEnable) {
            mTopLineTx.setText("" + ((float) verifySize) / 10);
        } else if (topEnable) {
            mLeftLineTx.setText("" + ((float) verifySize) / 10);
        }

        return model;
    }

    public void onClick(View v) {
        String widthS = mWidthTx.getText().toString();
        String heightS = mHeightTx.getText().toString();
        String leftLineS = mLeftLineTx.getText().toString();
        String topLineS = mTopLineTx.getText().toString();

        int horizontalCount = Integer.parseInt(mHorizontalS.getSelectedItem().toString());
        int verticalCount = Integer.parseInt(mVerticalS.getSelectedItem().toString());
        System.out.println("horizontal spinner:" + horizontalCount);
        System.out.println("vertical spinner:" + verticalCount);
        System.out.println("mWidthTx:" + widthS);
        System.out.println("mHeightTx:" + heightS);
        System.out.println("mLeftLineTx:" + leftLineS);
        System.out.println("mTopLineTx:" + topLineS);

        if (TextUtils.isEmpty(widthS) || TextUtils.isEmpty(heightS) || TextUtils.isEmpty(leftLineS) || TextUtils.isEmpty(topLineS)) {
            Snackbar.make(mLeftLineTx, "请先完善数据!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        float width = Float.parseFloat(widthS);
        float height = Float.parseFloat(heightS);
        float leftLineSize = Float.parseFloat(leftLineS);
        float topLineSize = Float.parseFloat(topLineS);
        if (width <= 0 || height <= 0 || leftLineSize <= 0 || topLineSize <= 0 || leftLineSize * 2 >= width || topLineSize * 2 >= height) {
            Snackbar.make(mLeftLineTx, "数据错误!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

//        DataModel dataModel = new DataModel((int) (width * 10), (int) (height * 10), (int) (leftLineSize * 10), (int) (topLineSize * 10),
//                verticalCount, horizontalCount);

        DataModel dataModel = autoVerify();

        if (dataModel == null)
            return;

        switch (v.getId()) {
            case R.id.generate://生成图片
                //开始画图
                Bitmap bitmap = PicGenerator.drawOriginalSquare(dataModel);
                if (bitmap == null) {
                    Snackbar.make(mLeftLineTx, "数据错误,图片绘制失败!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    FileUtils.saveBitmap(MainActivity.this, bitmap, "原图" + width + "*" + height + ".png");
                    Snackbar.make(mLeftLineTx, "图片保存成功!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.preview://预览


                Intent it = new Intent(MainActivity.this, FullscreenActivity.class);
                it.putExtra("data", dataModel);
                startActivity(it);

                break;
            default:
                break;
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
