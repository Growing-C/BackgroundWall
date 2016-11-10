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

    public DataModel(int width, int height, int leftLineSize, int topLineSize, int verticalCount, int horizontalCount) {
        this.width = width;
        this.height = height;
        this.leftLineSize = leftLineSize;
        this.topLineSize = topLineSize;
        this.verticalCount = verticalCount;
        this.horizontalCount = horizontalCount;


    }
}
