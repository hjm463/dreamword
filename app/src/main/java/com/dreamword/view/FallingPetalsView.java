package com.dreamword.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingPetalsView extends View {
    private final List<Petal> petals = new ArrayList<>();
    private final Random random = new Random();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean isRunning = true;

    private static class Petal {
        float x, y, size, speed, angle, alpha;
        int color;
    }

    public FallingPetalsView(Context context) {
        super(context);
        init();
    }

    public FallingPetalsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 20; i++) {
            addPetal();
        }
        startAnimation();
    }

    private void addPetal() {
        Petal p = new Petal();
        p.x = random.nextFloat() * 1000;
        p.y = -random.nextFloat() * 200;
        p.size = 4 + random.nextFloat() * 6;
        p.speed = 0.5f + random.nextFloat() * 1.5f;
        p.angle = random.nextFloat() * 360;
        p.alpha = 80 + random.nextInt(120);
        int[] colors = {0xFFF5F0E8, 0xFFC9A962, 0xFF2D8B8B, 0xFFA0B8B8};
        p.color = colors[random.nextInt(colors.length)];
        petals.add(p);
    }

    private void startAnimation() {
        postOnAnimation(new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;
                updatePetals();
                invalidate();
                postOnAnimation(this);
            }
        });
    }

    private void updatePetals() {
        for (Petal p : petals) {
            p.y += p.speed;
            p.x += (float) Math.sin(p.y * 0.01) * 0.5f;
            p.angle += 1;
            if (p.y > getHeight() + 50) {
                p.y = -50;
                p.x = random.nextFloat() * getWidth();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Petal p : petals) {
            paint.setColor(p.color);
            paint.setAlpha((int) p.alpha);
            canvas.save();
            canvas.translate(p.x, p.y);
            canvas.rotate(p.angle);
            canvas.drawOval(-p.size, -p.size * 0.6f, p.size, p.size * 0.6f, paint);
            canvas.restore();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }
}
