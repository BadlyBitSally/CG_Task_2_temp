package com.cgvsu.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawRectangle( final GraphicsContext graphicsContext, final int x, final int y,  final int width, final int height, final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        for (int row = y; row < y + height; ++row) {
            for (int col = x; col < x + width; ++col) {
                pixelWriter.setColor(col, row, color);
            }
        }
    }

    public static void drawOval(final GraphicsContext graphicsContext, final int centerX, final int centerY, final int width, final int height, final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        final int a = width / 2;
        final int b = height / 2;
        final int cx = centerX;
        final int cy = centerY;
        // Обходим ограничивающий прямоугольник
        for (int y = cy - b; y <= cy + b; y++) {
            for (int x = cx - a; x <= cx + a; x++) {
                // Проверяем, находится ли точка внутри эллипса
                double dx = (x - cx) / (double)a;
                double dy = (y - cy) / (double)b;

                if (dx * dx + dy * dy <= 1.0) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
    }

    public static void drawOvalBorderUniform(final GraphicsContext graphicsContext, final int centerX, final int centerY, final int width, final int height, final Color color) {
        int borderWidth = 1;
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        final int a = width / 2;
        final int b = height / 2;
        final int cx = centerX;
        final int cy = centerY;

        // Предварительно вычисляем квадраты для оптимизации
        final double a2 = a * a;
        final double b2 = b * b;

        for (int y = cy - b - borderWidth; y <= cy + b + borderWidth; y++) {
            for (int x = cx - a - borderWidth; x <= cx + a + borderWidth; x++) {
                double dx = x - cx;
                double dy = y - cy;

                // Вычисляем расстояние до границы эллипса
                double distanceToBorder = Math.abs(1.0 - (dx * dx / a2 + dy * dy / b2));

                // Вычисляем "толщину" в нормализованных координатах
                double normalizedThickness = borderWidth * Math.sqrt(dx * dx / (a2 * a2) + dy * dy / (b2 * b2));

                if (distanceToBorder <= normalizedThickness) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
    }


}
