package com.example.wuziqitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WuziqiPanel extends View {

    private int mPanelWidth; //Panel的宽度
    private float mLineHeight; //每一个格子高度,使用float避免误差
    private final int NUMBER_OF_ROW_AND_COLUMN = 15; //棋盘行列数
    private final int CHESS_NUMBER_REQUIRED_TO_WIN = 5; //设定赢棋所需棋子数

    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4; //设置棋子的大小为棋盘格子的3/4
    private boolean mIsBlack = true; //默认黑棋先手
    private ArrayList<Point> mWhiteArray = new ArrayList<>(); //初始化白棋数组
    private ArrayList<Point> mBlackArray = new ArrayList<>(); //初始化黑棋数组

    private boolean mIsGameOver; //游戏是否结束

    private boolean mIsWhiteWinner; //是否是白棋胜利

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(); //对Paint进行初始化
    }

    private void init() {
        mPaint.setColor(0x88000000); // 设置颜色
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖动
        mPaint.setStyle(Paint.Style.STROKE); // 设置画笔样式为“仅描边”
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2); //初始化白棋的图片
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1); //初始化黑棋的图片
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //棋盘是正方形，边长为min(屏幕宽，屏幕高)
        int width = Math.min(widthSize, heightSize);

        //防止嵌套在scrollView中出现问题，UNSPECIFIED为0
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    //设置棋子尺寸
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        mPanelWidth = newWidth;
        mLineHeight = mPanelWidth * 1.0f / NUMBER_OF_ROW_AND_COLUMN;
        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    //重写用户点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) { //手指抬起
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            //判断该点是否有棋子
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (mIsBlack) {
                mBlackArray.add(p);
            } else {
                mWhiteArray.add(p);
            }
            //请求重绘
            invalidate();

            mIsBlack = !(mIsBlack);
            return true;
        }
        return true;
    }

    //返回适当的落点
    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    //重写绘制方法（初始化，落子，悔棋时触发）
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        isWin();
    }

    private void isWin() {
        //判断是否达成获胜条件
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);

        if (whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private boolean checkFiveInLine(List<Point> points) {
        if (points.size() < CHESS_NUMBER_REQUIRED_TO_WIN) {
            return false;
        }

        Point p = points.get(points.size() - 1);
        int xIndex = p.x;
        int yIndex = p.y;

        //连续棋子的个数
        int continueCount = 1;

        //横向向西寻找
        for (int x = xIndex - 1; x >= 0; x--) {
            if (points.contains(new Point(x, yIndex)))
                continueCount++;
            else
                break;
        }
        //横向向东寻找
        for (int x = xIndex + 1; x <= NUMBER_OF_ROW_AND_COLUMN; x++) {
            if (points.contains(new Point(x, yIndex)))
                continueCount++;
            else
                break;
        }
        // 判断是否满足获胜条件
        if (continueCount >= CHESS_NUMBER_REQUIRED_TO_WIN)
            return true;
        else
            continueCount = 1;

        //继续另一种搜索纵向
        //向上搜索
        for (int y = yIndex - 1; y >= 0; y--) {
            if (points.contains(new Point(xIndex, y)))
                continueCount++;
            else
                break;
        }
        //纵向向下寻找
        for (int y = yIndex + 1; y <= NUMBER_OF_ROW_AND_COLUMN; y++) {
            if (points.contains(new Point(xIndex, y)))
                continueCount++;
            else
                break;
        }
        // 判断是否满足获胜条件
        if (continueCount >= CHESS_NUMBER_REQUIRED_TO_WIN)
            return true;
        else
            continueCount = 1;


        //继续另一种情况的搜索：斜向
        //东北寻找
        for (int x = xIndex + 1, y = yIndex - 1; y >= 0 && x <= NUMBER_OF_ROW_AND_COLUMN; x++, y--) {
            if (points.contains(new Point(x, y)))
                continueCount++;
            else
                break;
        }
        //西南寻找
        for (int x = xIndex - 1, y = yIndex + 1; x >= 0 && y <= NUMBER_OF_ROW_AND_COLUMN; x--, y++) {
            if (points.contains(new Point(x, y)))
                continueCount++;
            else
                break;
        }
        // 判断是否满足获胜条件
        if (continueCount >= CHESS_NUMBER_REQUIRED_TO_WIN)
            return true;
        else
            continueCount = 1;


        //继续另一种情况的搜索：斜向
        //西北寻找
        for (int x = xIndex - 1, y = yIndex - 1; x >= 0 && y >= 0; x--, y--) {
            if (points.contains(new Point(x, y)))
                continueCount++;
            else
                break;
        }
        //东南寻找
        for (int x = xIndex + 1, y = yIndex + 1; x <= NUMBER_OF_ROW_AND_COLUMN && y <= NUMBER_OF_ROW_AND_COLUMN; x++, y++) {
            if (points.contains(new Point(x, y)))
                continueCount++;
            else
                break;
        }
        // 判断是否满足获胜条件
        if (continueCount >= CHESS_NUMBER_REQUIRED_TO_WIN)
            return true;
//        else continueCount=1;

        return false;

    }

    //绘制所有棋子
    private void drawPiece(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }

    }

    //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for (int i = 0; i < NUMBER_OF_ROW_AND_COLUMN; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            mPaint.setStrokeWidth((float) 4.0);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);

        }
    }

    //重开
    public void restart() {
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsBlack = true;
        mIsGameOver = false;
        mIsWhiteWinner = false;
        invalidate(); //请求重绘
    }

    //悔棋
    public void retract() {
        if (mBlackArray.size() > 0 && !mIsBlack) {
            mBlackArray.remove(mBlackArray.size() - 1);
        }
        if (mWhiteArray.size() > 0 && mIsBlack) {
            mWhiteArray.remove( mWhiteArray.size() - 1);
        }
        mIsBlack = !mIsBlack;
        invalidate(); //请求重绘
    }

    //View的存储于恢复，防止程序被杀死导致棋局丢失，否则即使屏幕旋转也会丢失棋局
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof  Bundle) {
            Bundle bundle = (Bundle)state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public boolean getMIsGameOver() {
        return mIsGameOver;
    }


}

