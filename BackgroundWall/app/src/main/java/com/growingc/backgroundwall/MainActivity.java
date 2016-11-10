package com.growingc.backgroundwall;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

        mLeftLineTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String widthS = mWidthTx.getText().toString();
                if (TextUtils.isEmpty(widthS))
                    return;


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        DataModel dataModel = new DataModel((int) (width * 10), (int) (height * 10), (int) (leftLineSize * 10), (int) (topLineSize * 10),
                verticalCount, horizontalCount);

        switch (v.getId()) {
            case R.id.generate://生成图片
                //开始画图
                Bitmap bitmap = PicGenerator.drawOriginal(dataModel.width, dataModel.height, dataModel.leftLineSize, dataModel.topLineSize,
                        dataModel.verticalCount, dataModel.horizontalCount);
                if (bitmap == null) {
                    Snackbar.make(mLeftLineTx, "数据错误,图片绘制失败!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    FileUtils.saveBitmap(MainActivity.this, bitmap, "backgroundOrigin.png");
                    Snackbar.make(mLeftLineTx, "图片保存成功!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.preview://预览


                Intent it = new Intent(MainActivity.this, FullscreenActivity.class);
                it.putExtra("data", dataModel);
                startActivity(it);

//                ImageView img = new ImageView(MainActivity.this);
//                img.setAdjustViewBounds(true);
//                img.setImageBitmap(bitmap);
////                img.setImageResource(R.mipmap.ic_launcher );
//                img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "我被点击啦", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                        .setTitle("预览图")
//                        .setPositiveButton("保存", null)
//                        .setView(img).create();
//                dialog.show();

                break;
            default:
                break;
        }
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
