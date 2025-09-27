package net.toptophat.alkimia.gui.data;

import java.util.List;

public class PageData {
    public List<TextData> texts;
    public List<ImageData> images;
    public List<ItemData> items;
    public int pageNumber;
    public PageData(List<TextData> text, List<ImageData> image, List<ItemData> item, int number)
    {
        texts = text;
        images = image;
        items = item;
        pageNumber = number;
    }
}
