package net.toptophat.alkimia.gui.data;

import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public class TextData {
    public Text text;
    public Vec2f startPos;
    public TextData(Text txt, Vec2f pos)
    {
        text = txt;
        startPos = pos;
    }
}
