package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

final class CircleImageView extends ImageView {
    private AnimationListener mListener;
    int mShadowRadius;

    private class OvalShadow extends OvalShape {
        private RadialGradient mRadialGradient;
        private Paint mShadowPaint = new Paint();

        OvalShadow(int shadowRadius) {
            CircleImageView.this.mShadowRadius = shadowRadius;
            updateRadialGradient((int) rect().width());
        }

        protected final void onResize(float width, float height) {
            super.onResize(width, height);
            updateRadialGradient((int) width);
        }

        public final void draw(Canvas canvas, Paint paint) {
            int viewWidth = CircleImageView.this.getWidth();
            int viewHeight = CircleImageView.this.getHeight();
            canvas.drawCircle((float) (viewWidth / 2), (float) (viewHeight / 2), (float) (viewWidth / 2), this.mShadowPaint);
            canvas.drawCircle((float) (viewWidth / 2), (float) (viewHeight / 2), (float) ((viewWidth / 2) - CircleImageView.this.mShadowRadius), paint);
        }

        private void updateRadialGradient(int diameter) {
            this.mRadialGradient = new RadialGradient((float) (diameter / 2), (float) (diameter / 2), (float) CircleImageView.this.mShadowRadius, new int[]{1023410176, 0}, null, TileMode.CLAMP);
            this.mShadowPaint.setShader(this.mRadialGradient);
        }
    }

    CircleImageView(Context context, int color) {
        ShapeDrawable circle;
        super(context);
        float density = getContext().getResources().getDisplayMetrics().density;
        int shadowYOffset = (int) (1.75f * density);
        int shadowXOffset = (int) (0.0f * density);
        this.mShadowRadius = (int) (3.5f * density);
        if (elevationSupported()) {
            circle = new ShapeDrawable(new OvalShape());
            ViewCompat.setElevation(this, 4.0f * density);
        } else {
            circle = new ShapeDrawable(new OvalShadow(this.mShadowRadius));
            ViewCompat.setLayerType(this, 1, circle.getPaint());
            circle.getPaint().setShadowLayer((float) this.mShadowRadius, (float) shadowXOffset, (float) shadowYOffset, 503316480);
            int padding = this.mShadowRadius;
            setPadding(padding, padding, padding, padding);
        }
        circle.getPaint().setColor(-328966);
        ViewCompat.setBackground(this, circle);
    }

    private static boolean elevationSupported() {
        return VERSION.SDK_INT >= 21;
    }

    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!elevationSupported()) {
            setMeasuredDimension(getMeasuredWidth() + (this.mShadowRadius << 1), getMeasuredHeight() + (this.mShadowRadius << 1));
        }
    }

    public final void setAnimationListener(AnimationListener listener) {
        this.mListener = listener;
    }

    public final void onAnimationStart() {
        super.onAnimationStart();
        if (this.mListener != null) {
            this.mListener.onAnimationStart(getAnimation());
        }
    }

    public final void onAnimationEnd() {
        super.onAnimationEnd();
        if (this.mListener != null) {
            this.mListener.onAnimationEnd(getAnimation());
        }
    }

    public final void setBackgroundColor(int color) {
        if (getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) getBackground()).getPaint().setColor(color);
        }
    }
}
