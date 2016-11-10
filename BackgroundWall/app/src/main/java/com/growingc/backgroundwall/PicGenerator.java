package com.growingc.backgroundwall;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by RB-cgy on 2016/11/9.
 */
public class PicGenerator {

    /**
     * 画原图
     *
     * @param width
     * @param height
     * @param leftLine
     * @param topLine
     * @param verticalCount
     * @param horizontalCount
     * @return
     */
    public static Bitmap drawOriginal(int width, int height, int leftLine, int topLine, int verticalCount, int horizontalCount) {
        System.out.println("drawOriginal-----##################- :");
        System.out.println("mWidth------ :" + width);
        System.out.println("mHeight--------- :" + height);
        System.out.println("mLeftLine---------- :" + leftLine);
        System.out.println("mTopLine----------- :" + topLine);

        int recVertical = calculateVertical(height, topLine, verticalCount);
        int recHorizontal = calculateHorizontal(width, leftLine, horizontalCount);

        if (recVertical <= 0 || recHorizontal <= 0)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//右边和下方增加文字区域
        Canvas canvas = new Canvas(bitmap);

        ArrayList<Point> topPoints = calculateTopPoints(topLine, leftLine, recHorizontal, horizontalCount);
        ArrayList<Point> bottomPoints = calculateBottomPoints(height, topLine, leftLine, recHorizontal, horizontalCount);
        ArrayList<Point> leftPoints = calculateLeftPoints(topLine, leftLine, recVertical, verticalCount);
        ArrayList<Point> rightPoints = calculateRightPoints(width, topLine, leftLine, recVertical, verticalCount);

        canvas.drawColor(Color.WHITE);//大背景色

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        drawTopLine(topPoints, canvas, paint);
        drawBottomLine(bottomPoints, topLine, canvas, paint);
        drawLeftLine(leftPoints, canvas, paint);
        drawRightLine(rightPoints, leftLine, canvas, paint);

        drawCenter(topPoints, bottomPoints, leftPoints, rightPoints, canvas, paint);
        return bitmap;
    }

    /**
     * 画预览图  长宽等尺寸都放大了10
     *
     * @param width
     * @param height
     * @param leftLine
     * @param topLine
     * @param verticalCount
     * @param horizontalCount
     * @return
     */
    public static Bitmap drawInstrument(int width, int height, int leftLine, int topLine, int verticalCount, int horizontalCount) {
        System.out.println("mWidth------ :" + width);
        System.out.println("mHeight--------- :" + height);
        System.out.println("mLeftLine---------- :" + leftLine);
        System.out.println("mTopLine----------- :" + topLine);

        int recVertical = calculateVertical(height, topLine, verticalCount);
        int recHorizontal = calculateHorizontal(width, leftLine, horizontalCount);

        if (recVertical <= 0 || recHorizontal <= 0)
            return null;
//                A4纸的尺寸是210mm×297mm，
//                当你设定的分辨率是72像素/英寸时，A4纸的尺寸的图像的像素是595×842，
//                当你设定的分辨率是150像素/英寸时，A4纸的尺寸的图像的像素是1240×1754，
//                当你设定的分辨率是300像素/英寸时，A4纸的尺寸的图像的像素是2479×3508，
        Bitmap bitmap = Bitmap.createBitmap(width + 200, height + 200, Bitmap.Config.ARGB_8888);//右边和下方增加文字区域
        Canvas canvas = new Canvas(bitmap);

        ArrayList<Point> topPoints = calculateTopPoints(topLine, leftLine, recHorizontal, horizontalCount);
        ArrayList<Point> bottomPoints = calculateBottomPoints(height, topLine, leftLine, recHorizontal, horizontalCount);
        ArrayList<Point> leftPoints = calculateLeftPoints(topLine, leftLine, recVertical, verticalCount);
        ArrayList<Point> rightPoints = calculateRightPoints(width, topLine, leftLine, recVertical, verticalCount);

        canvas.drawColor(Color.WHITE);//大背景色

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#b8ad9b"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0f, 0f, width, height, paint);//画作图区域

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        drawTopLine(topPoints, canvas, paint);
        drawBottomLine(bottomPoints, topLine, canvas, paint);
        drawLeftLine(leftPoints, canvas, paint);
        drawRightLine(rightPoints, leftLine, canvas, paint);

        drawCenter(topPoints, bottomPoints, leftPoints, rightPoints, canvas, paint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);// 设置文字对齐方式在其中心
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        canvas.drawText("Ⅰ", (leftLine + recHorizontal / 2) / 2, (topLine + recVertical / 2) / 2, textPaint);//左上角的块
        canvas.drawText("Ⅱ", leftLine + recHorizontal, (topLine + recVertical / 2) / 2, textPaint);//上排的块
        canvas.drawText("Ⅲ", (leftLine + recHorizontal / 2) / 2, topLine + recVertical, textPaint);//左排的块
        canvas.drawText("Ⅳ", leftLine + recHorizontal / 2, topLine + recVertical / 2, textPaint);//四边形块

        drawText(height, topLine, leftLine, recHorizontal, recVertical, topPoints.size(), leftPoints.size(), canvas, textPaint);
        return bitmap;
    }


    private static void drawTopLine(ArrayList<Point> topPoints, Canvas canvas, Paint paint) {
        for (int i = 0, len = topPoints.size(); i < len; i++) {
            Point p = topPoints.get(i);
            canvas.drawLine(p.x, 0, p.x, p.y, paint);
        }
    }

    private static void drawBottomLine(ArrayList<Point> bottomPoints, int topLineSize, Canvas canvas, Paint paint) {
        for (int i = 0, len = bottomPoints.size(); i < len; i++) {
            Point p = bottomPoints.get(i);
            canvas.drawLine(p.x, p.y, p.x, p.y + topLineSize, paint);
        }
    }

    private static void drawLeftLine(ArrayList<Point> leftPoints, Canvas canvas, Paint paint) {
        for (int i = 0, len = leftPoints.size(); i < len; i++) {
            Point p = leftPoints.get(i);
            canvas.drawLine(0, p.y, p.x, p.y, paint);
        }
    }

    private static void drawRightLine(ArrayList<Point> rightPoints, int leftLineSize, Canvas canvas, Paint paint) {
        for (int i = 0, len = rightPoints.size(); i < len; i++) {
            Point p = rightPoints.get(i);
            canvas.drawLine(p.x, p.y, p.x + leftLineSize, p.y, paint);
        }
    }

    private static void drawCenter(ArrayList<Point> topPoints, ArrayList<Point> bottomPoints, ArrayList<Point> leftPoints, ArrayList<Point> rightPoints, Canvas canvas, Paint paint) {
        int totalLen = topPoints.size() + rightPoints.size();
        int topSize = topPoints.size();
        int leftSize = leftPoints.size();
        //画右斜向上的线
        for (int i = 0; i < totalLen; i++) {
            Point pAbove, pBelow;
            if (i < topSize) {
                pAbove = topPoints.get(i);
            } else {
                pAbove = rightPoints.get(i - topSize);
            }

            if (i < leftSize) {
                pBelow = leftPoints.get(i);
            } else {
                pBelow = bottomPoints.get(i - leftSize);
            }

            canvas.drawLine(pAbove.x, pAbove.y, pBelow.x, pBelow.y, paint);
        }
        //右斜向下的线
        for (int i = 0; i < totalLen; i++) {
            Point pAbove, pBelow;
            if (i < topSize) {
                pAbove = topPoints.get(topSize - 1 - i);
            } else {
                pAbove = leftPoints.get(i - topSize);
            }

            if (i < leftSize) {
                pBelow = rightPoints.get(i);
            } else {
                pBelow = bottomPoints.get(totalLen - i - 1);
            }

            canvas.drawLine(pAbove.x, pAbove.y, pBelow.x, pBelow.y, paint);
        }
    }

    private static void drawText(int heightI, int topLineSizeI, int leftLineSizeI, int recHorizontalI, int recVerticalI,
                                 int topPointsSize, int leftPointsSize, Canvas canvas, Paint paint) {
        float recLen = ((float) Math.sqrt(Math.pow(recVerticalI / 2, 2) + Math.pow(recHorizontalI / 2, 2))) / 10;
        float topLineSize = ((float) topLineSizeI) / 10;
        float leftLineSize = ((float) leftLineSizeI) / 10;
        float recHorizontal = ((float) recHorizontalI) / 10;
        float recVertical = ((float) recVerticalI) / 10;
        //画下边
        canvas.drawText("Ⅰ号块", 200, heightI + 50, paint);//左上角的块
        canvas.drawText("Ⅱ号块", 500, heightI + 50, paint);//上排的块
        canvas.drawText("Ⅲ号块", 800, heightI + 50, paint);//左排的块
        canvas.drawText("Ⅳ号块", 1100, heightI + 50, paint);//四边形块

        DecimalFormat decimalFormat = new DecimalFormat(".0");
        //下面为尺寸
        canvas.drawText((leftLineSize + recHorizontal / 2) + "", (leftLineSizeI + recHorizontalI / 2) / 2, 100, paint);//左上角
        canvas.drawText(topLineSize + "", leftLineSizeI + recHorizontalI / 2, topLineSizeI / 2, paint);//右边
        canvas.drawText(leftLineSize + "", leftLineSizeI / 2, (topLineSizeI + recVerticalI / 2) - 20, paint);//下边
        canvas.drawText(decimalFormat.format(recLen) + "", leftLineSizeI + recHorizontalI / 4, topLineSizeI + recVerticalI / 4, paint);//四边形边
        canvas.drawText(recVertical + "*" + recHorizontal, leftLineSizeI + recHorizontalI + recHorizontalI / 2, topLineSizeI + recVerticalI / 2, paint);//四边形对角

        //下面为块数
        canvas.drawText("块数", 50, heightI + 150, paint);
        canvas.drawText("4", 200, heightI + 150, paint);
        canvas.drawText((topPointsSize - 1) * 2 + "", 500, heightI + 150, paint);
        canvas.drawText((leftPointsSize - 1) * 2 + "", 800, heightI + 150, paint);
        canvas.drawText(leftPointsSize * topPointsSize + (leftPointsSize - 1) * (topPointsSize - 1) + "", 1100, heightI + 150, paint);


        //画右边
//        canvas.drawText("尺寸", width + 50, 50, paint);

    }


//    ----------------------以下为计算---------------------

    /**
     * 计算四边形纵向对角线
     *
     * @return
     */
    private static int calculateVertical(int height, int topLine, int verticalCount) {
        return (height - topLine * 2) / verticalCount;
    }

    /**
     * 计算四边形横向对角线
     *
     * @return
     */
    private static int calculateHorizontal(int width, int leftLine, int horizontalCount) {
        return (width - 2 * leftLine) / horizontalCount;
    }

    private static ArrayList<Point> calculateTopPoints(int topLineSize, int leftLineSize, int recHorizontal, int horizontalCount) {
        ArrayList<Point> topPoints = new ArrayList<>();
        int x = leftLineSize + recHorizontal / 2;
        for (int i = 0; i < horizontalCount; i++) {
            Point p = new Point();
            p.set(x, topLineSize);
            topPoints.add(p);

            x += recHorizontal;
        }

        return topPoints;
    }

    private static ArrayList<Point> calculateBottomPoints(int height, int topLineSize, int leftLineSize, int recHorizontal, int horizontalCount) {
        ArrayList<Point> bottomPoints = new ArrayList<>();
        int x = leftLineSize + recHorizontal / 2;
        int y = height - topLineSize;
        for (int i = 0; i < horizontalCount; i++) {
            Point p = new Point();
            p.set(x, y);
            bottomPoints.add(p);

            x += recHorizontal;
        }

        return bottomPoints;
    }

    private static ArrayList<Point> calculateLeftPoints(int topLineSize, int leftLineSize, int recVertical, int verticalCount) {
        ArrayList<Point> leftPoints = new ArrayList<>();
        int y = topLineSize + recVertical / 2;
        for (int i = 0; i < verticalCount; i++) {
            Point p = new Point();
            p.set(leftLineSize, y);
            leftPoints.add(p);

            y += recVertical;
        }
        return leftPoints;
    }

    private static ArrayList<Point> calculateRightPoints(int width, int topLineSize, int leftLineSize, int recVertical, int verticalCount) {
        ArrayList<Point> rightPoints = new ArrayList<>();
        int x = width - leftLineSize;
        int y = topLineSize + recVertical / 2;
        for (int i = 0; i < verticalCount; i++) {
            Point p = new Point();
            p.set(x, y);
            rightPoints.add(p);

            y += recVertical;
        }
        return rightPoints;
    }
}
