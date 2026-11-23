package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * stats_bars: the span's text ("RAM", "ROM") as a label of fixed width, then a thin rounded usage bar, so the
 * bars of both lines start at the same x. Drawn in the stats view's own paint: track at low alpha, fill at full.
 */
final class StatsBar extends ReplacementSpan {
    private final int percent;
    private final int labelWidth;
    private final int barWidth;
    private final int barHeight;
    private final RectF rect = new RectF();

    StatsBar(int percent, int labelWidth, int barWidth, int barHeight) {
        this.percent = clamp(percent);
        this.labelWidth = labelWidth;
        this.barWidth = barWidth;
        this.barHeight = barHeight;
    }

    /** Used share of a total as a whole percent, 0 to 100; 0 when the total is unknown. */
    static int percent(long used, long total) {
        if (total <= 0) return 0;
        return clamp((int) Math.round(used * 100.0 / total));
    }

    private static int clamp(int p) {
        return Math.max(0, Math.min(100, p));
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (fm != null) {
            Paint.FontMetricsInt p = paint.getFontMetricsInt();
            fm.ascent = p.ascent;
            fm.descent = p.descent;
            fm.top = p.top;
            fm.bottom = p.bottom;
        }
        return labelWidth + barWidth;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {
        canvas.drawText(text, start, end, x, y, paint);
        Paint.FontMetrics m = paint.getFontMetrics();
        float mid = y + (m.ascent + m.descent) / 2f;
        float left = x + labelWidth;
        float r = barHeight / 2f;
        int color = paint.getColor();
        int alpha = paint.getAlpha();
        Paint.Style style = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(alpha * 46 / 255);
        rect.set(left, mid - r, left + barWidth, mid + r);
        canvas.drawRoundRect(rect, r, r, paint);
        if (percent > 0) {
            paint.setAlpha(alpha);
            rect.right = left + Math.max(barHeight, barWidth * percent / 100f);
            canvas.drawRoundRect(rect, r, r, paint);
        }
        paint.setStyle(style);
        paint.setColor(color);
    }
}
