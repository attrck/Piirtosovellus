package studies.drawingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingCanvasView extends View {
    private Path drawPath;

    private Paint drawPaint, canvasPaint;

    private int strokeWidth = 14;
    private int paintColor = 0xFF000000;

    private Canvas drawCanvas;

    private Bitmap canvasBitmap;




    public DrawingCanvasView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    protected void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(strokeWidth);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPathStartLocation(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                continuePathToLocation(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPathAndStartNewPath(touchX, touchY);
                break;
            default:
                return false;
        }

        redrawScreen();

        return true;

    }

    private void redrawScreen() { invalidate(); }

    private void setPathStartLocation(float x, float y) {
        drawPath.moveTo(x, y);
    }

    private void drawPathAndStartNewPath(float x, float y) {
        drawPath.lineTo(x, y);
        drawCanvas.drawPath(drawPath, drawPaint);
        drawPath.reset();
    }

    private void continuePathToLocation(float x, float y) {
        drawPath.lineTo(x, y);
    }
}
