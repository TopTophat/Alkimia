package net.falling_pan.alkimia.gui.data;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class ItemData {
    public Identifier itemToRender;
    public Vec2f position;
    public Vec2f size;
    public ItemData(Identifier item, Vec2f pos, Vec2f siz)
    {
        position = pos;
        itemToRender = item;
        size = siz;
    }
}
