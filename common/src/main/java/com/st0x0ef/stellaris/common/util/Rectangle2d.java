package com.st0x0ef.stellaris.common.util;

public class Rectangle2d
{
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Rectangle2d(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle2d(net.minecraft.client.renderer.Rect2i vanilla) {
        this.x = vanilla.getX();
        this.y = vanilla.getY();
        this.width = vanilla.getWidth();
        this.height = vanilla.getHeight();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean contains(int _x, int _y) {
        return _x >= this.x && _x <= this.x + this.width && _y >= this.y && _y <= this.y + this.height;
    }

    public net.minecraft.client.renderer.Rect2i toRect2i() {
        return new net.minecraft.client.renderer.Rect2i(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
