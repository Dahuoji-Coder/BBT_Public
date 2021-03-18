package cn.dahuoji.body_temperature.skinview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import org.xml.sax.XMLReader;

import cn.dahuoji.body_temperature.R;
import cn.dahuoji.body_temperature.util.ResourcesUtil;
import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2018/5/28.
 */

public class TTFTextView extends AppCompatTextView {

    private Context context;
    private Typeface typeface;
    private int currentBackgroundResourceId,
            currentDrawableTopId, currentDrawableRightId, currentDrawableLeftId,
            currentTextColorId;
    private int drawableSize;

    public TTFTextView(Context context) {
        super(context);
    }

    public TTFTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TTFTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        setLayoutParams(layoutParams);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) return;
        this.context = context;
        currentBackgroundResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0);
        currentDrawableTopId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableTop", 0);
        currentDrawableRightId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableRight", 0);
        currentDrawableLeftId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableLeft", 0);
        currentTextColorId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.pfStyle);
        drawableSize = (int) typedArray.getDimension(R.styleable.pfStyle_drawableSize, 0);
        String textStyle = typedArray.getString(R.styleable.pfStyle_textStyle);
        typedArray.recycle();
//        if (SPUtil.getLanguage().startsWith("zh")) {
//            typeface = LocalApplication.getPf_normal();
//            if (null != textStyle && "bold".equals(textStyle)) {
//                setTypeface(typeface, Typeface.BOLD);
//            } else {
//                setTypeface(typeface);
//            }
//        } else {
//            typeface = LocalApplication.getPf_en();
//            if (null != textStyle && "bold".equals(textStyle)) {
//                setTypeface(typeface, Typeface.BOLD);
//            } else {
//                setTypeface(typeface);
//            }
//        }
        updateTheme();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (onSizeChangedListener != null) {
            onSizeChangedListener.sizeChanged(w, h);
        }
    }

    @Override
    public void setBackgroundResource(int resId) {
        //currentBackgroundResourceId = resId;
        resId = ThemeUtil.getResourceIdByTheme(resId);
        super.setBackgroundResource(resId);
    }

    public void setCompoundDrawablesById(int start, int top, int end, int bottom) {
        currentDrawableTopId = top;
        currentDrawableLeftId = start;
        currentDrawableRightId = end;
        top = ThemeUtil.getResourceIdByTheme(top);
        start = ThemeUtil.getResourceIdByTheme(start);
        end = ThemeUtil.getResourceIdByTheme(end);
        setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        if (Build.VERSION.SDK_INT >= 17) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        }
    }

    public void setTextColorById(int colorId) {
        currentTextColorId = colorId;
        colorId = ThemeUtil.getResourceIdByTheme(colorId);
        if (colorId != 0) {
            super.setTextColor(ResourcesUtil.getColor(context, colorId));
        }
    }

    public void setTextBold() {
        setTypeface(typeface, Typeface.BOLD);
    }

    public void setTextNormal() {
        setTypeface(typeface, Typeface.NORMAL);
    }

    public void setDrawableSize(int drawableSize) {
        this.drawableSize = drawableSize;
        updateTheme();
    }

    public void updateTheme() {
        int currentTheme = ThemeUtil.getTheme();

        int backgroundResourceId = currentBackgroundResourceId;
        if (backgroundResourceId != 0) {
            backgroundResourceId = ThemeUtil.getResourceIdByTheme(backgroundResourceId);
            setBackgroundResource(backgroundResourceId);
        }
        int drawableTopId = currentDrawableTopId;
        int drawableRightId = currentDrawableRightId;
        int drawableLeftId = currentDrawableLeftId;
        if (drawableTopId != 0) {
            drawableTopId = ThemeUtil.getResourceIdByTheme(drawableTopId);
            if (drawableSize > 0) {
                Drawable drawableTop = ContextCompat.getDrawable(context, drawableTopId);
                drawableTop.setBounds(0, 0, drawableSize, drawableSize);
                setCompoundDrawables(null, drawableTop, null, null);
            } else {
                setCompoundDrawablesRelativeWithIntrinsicBounds(0, drawableTopId, 0, 0);
            }
        } else if (drawableRightId != 0) {
            drawableRightId = ThemeUtil.getResourceIdByTheme(drawableRightId);
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawableRightId, 0);
        } else if (drawableLeftId != 0) {
            drawableLeftId = ThemeUtil.getResourceIdByTheme(drawableLeftId);
            if (drawableSize > 0) {
                Drawable drawableLeft = ContextCompat.getDrawable(context, drawableLeftId);
                drawableLeft.setBounds(0, 0, drawableSize, drawableSize);
                setCompoundDrawables(drawableLeft, null, null, null);
            } else {
                setCompoundDrawablesRelativeWithIntrinsicBounds(drawableLeftId, 0, 0, 0);
            }
        }

        int textColorId = ThemeUtil.getResourceIdByTheme(currentTextColorId);
        if (textColorId != 0) {
            setTextColor(ResourcesUtil.getColor(context, textColorId));
        }
    }

    public void setMargins(int l, int t, int r, int b) {
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            p.setMargins(l, t, r, b);
            requestLayout();
        }
    }

    public void setImageSpanString(String str, int drawableId, int imgPosition) {
        SpannableString spannableString = new SpannableString(str);
        BitmapFactory.Options options = new BitmapFactory.Options();
        drawableId = ThemeUtil.getResourceIdByTheme(drawableId);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId, options);
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(context, bitmap);
        spannableString.setSpan(imageSpan, imgPosition, imgPosition + 1, ImageSpan.ALIGN_BASELINE);
        setText(spannableString);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * 为了解决使用 setMovementMethod(LinkMovementMethod.getInstance()); 来实现文字中的超链接点击事件导致的如下问题：
         * 1. TextView本身的点击事件会被屏蔽
         * 2. Ellipsize会失效
         * */
        int action = event.getAction();
        CharSequence text = getText();
        if (text instanceof Spanned) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            x += getScrollX();
            y += getScrollY();

            Layout layout = getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);
            if (action == MotionEvent.ACTION_UP) {
                if (link.length != 0) {
                    link[0].onClick(this);
                } else {
                    //TextView的默认点击事件
                    return super.onTouchEvent(event);
                }
            }

            if (link.length != 0) {
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private OnSizeChangedListener onSizeChangedListener;

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    public interface OnSizeChangedListener {
        void sizeChanged(int width, int height);
    }
}