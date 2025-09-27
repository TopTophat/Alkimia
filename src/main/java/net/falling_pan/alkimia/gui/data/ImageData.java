package net.falling_pan.alkimia.gui.data;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class ImageData {
    public Identifier sprite;
    public Vec2f position;
    public Vec2f size;
    public ImageData(Identifier spriteId, Vec2f pos, Vec2f siz)
    {
        sprite = spriteId;
        position = pos;
        size = siz;
    }
}
