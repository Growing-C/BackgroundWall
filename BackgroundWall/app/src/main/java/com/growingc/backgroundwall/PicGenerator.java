package com.growingc.backgroundwall;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by RB-cgy on 2016/11/9.
 */
public class PicGenerator {

    /**
     * 画图  长宽等尺寸都放大了10
     *
     * @param width
     * @param height
     * @param leftLine
     * @param topLine
     * @param verticalCount
     * @param horizontalCount
     * @return
     */
    public static Bitmap draw(int width, int height, int leftLine, int topLine, int verticalCount, int horizontalCount) {
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
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        ArrayList<Point> topPoints = calculateTopPoints(topLine, leftLine, recHorizontal, horizontalCount);
        ArrayList<Point> bottomPoints = calculateBottomPoints(height, topLine, leftLine, recHorizontal, horizontalCount);
        ArrayList<Point> leftPoints = calculateLeftPoints(topLine, leftLine, recVertical, verticalCount);
        ArrayList<Point> rightPoints = calculateRightPoints(width, topLine, leftLine, recVertical, verticalCount);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawColor(Color.WHITE);
        paint.setStrokeWidth(5);

        drawTopLine(topPoints, canvas, paint);
        drawBottomLine(bottomPoints, topLine, canvas, paint);
        drawLeftLine(leftPoints, canvas, paint);
        drawRightLine(rightPoints, leftLine, canvas, paint);

        drawCenter(topPoints, bottomPoints, leftPoints, rightPoints, canvas, paint);
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
        for (int i = 0, len = topPoints.size(); i < len; i++) {
            Point p = topPoints.get(i);
//            canvas.drawLine(p.x, 0, p.x, p.y, paint);
        }


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
