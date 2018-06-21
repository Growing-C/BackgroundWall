package com.growingc.backgroundwall;

import java.io.Serializable;

/**
 * Created by RB-cgy on 2016/11/10.
 */
public class DataModel implements Serializable {
    public int width;
    public int height;
    public int leftLineSize;
    public int topLineSize;
    public int verticalCount;
    public int horizontalCount;

    public int squareDiagonalLine;//正方形的对角线

    public int realWidth;//实际尺寸宽度，计算之后长宽会有些误差，所以会产生这个值
    public int realHeight;//实际尺寸高度

    public DataModel() {
    }

    /**
     * 构造方法，默认尺寸的正方形使用
     *
     * @param width           此处的值都放大了10倍
     * @param height
     * @param squareSize
     * @param verticalCount
     * @param horizontalCount
     */
    public DataModel(int width, int height, int squareSize, int verticalCount, int horizontalCount) {
        this.width = width;
        this.height = height;

        squareDiagonalLine = (int) Math.sqrt(Math.pow(squareSize, 2) + Math.pow(squareSize, 2));//对角线

        if (verticalCount > 0)//如果设置了上下数目则为该值，否则计算之
            this.verticalCount = verticalCount;
        else
            this.verticalCount = height / squareDiagonalLine;//上下能包含几个正方形就是几个

        if (horizontalCount > 0)//如果设置了左右数目则为该值，否则计算之
            this.horizontalCount = horizontalCount;
        else
            this.horizontalCount = width / squareDiagonalLine;

        System.out.println("width:" + width);
        System.out.println("height:" + height);
        System.out.println("squareDiagonalLine:" + squareDiagonalLine);
        System.out.println("verticalCount:" + this.verticalCount);
        System.out.println("horizontalCount:" + this.horizontalCount);

        if (this.verticalCount > 0 && this.horizontalCount > 0) {
            leftLineSize = (width - squareDiagonalLine * this.horizontalCount) / 2;
            topLineSize = (height - squareDiagonalLine * this.verticalCount) / 2;

            System.out.println("leftLineSize:" + leftLineSize);
            System.out.println("topLineSize:" + topLineSize);

            if (leftLineSize >= 0 && topLineSize >= 0)//如果计算无误则setRealSize
                setRealSize();
        }
    }

    /**
     * 验证数据正确性
     *
     * @return
     */
    public boolean isDataRight() {
        if (this.verticalCount <= 0 || this.horizontalCount <= 0 || leftLineSize < 0 || topLineSize < 0)
            return false;

        return true;
    }

    /**
     * 构造方法，菱形用的
     *
     * @param width
     * @param height
     * @param leftLineSize
     * @param topLineSize
     * @param verticalCount
     * @param horizontalCount
     */
    public DataModel(int width, int height, int leftLineSize, int topLineSize, int verticalCount, int horizontalCount) {
        this.width = width;
        this.height = height;
        this.leftLineSize = leftLineSize;
        this.topLineSize = topLineSize;
        this.verticalCount = verticalCount;
        this.horizontalCount = horizontalCount;
    }

    public int getTopLineSize(int width, int height, int leftLineSize, int verticalCount, int horizontalCount) {
        this.width = width;
        this.height = height;
        this.leftLineSize = leftLineSize;
        this.verticalCount = verticalCount;
        this.horizontalCount = horizontalCount;

        calculateTopLine();
        return topLineSize;
    }

    public int getLeftLineSize(int width, int height, int topLineSize, int verticalCount, int horizontalCount) {
        this.width = width;
        this.height = height;
        this.topLineSize = topLineSize;
        this.verticalCount = verticalCount;
        this.horizontalCount = horizontalCount;

        calculateLeftLine();
        return leftLineSize;
    }

    /**
     * 计算左右边线
     */
    private void calculateLeftLine() {
        squareDiagonalLine = (height - topLineSize * 2) / verticalCount;
        if (squareDiagonalLine < 1) {//正方形对角线必须大于1
            leftLineSize = -1;
            return;
        }
        leftLineSize = (width - squareDiagonalLine * horizontalCount) / 2;
        setRealSize();
    }

    /**
     * 计算上下边线
     */
    private void calculateTopLine() {
        squareDiagonalLine = (width - leftLineSize * 2) / horizontalCount;
        if (squareDiagonalLine < 1) {
            topLineSize = -1;
            return;
        }
        topLineSize = (height - squareDiagonalLine * verticalCount) / 2;
        setRealSize();
    }

    /**
     * 计算真实尺寸
     */
    private void setRealSize() {
        realHeight = squareDiagonalLine * verticalCount + topLineSize * 2;
        realWidth = squareDiagonalLine * horizontalCount + leftLineSize * 2;
    }

}
