package net.toptophat.alkimia.gui.data;

import java.util.List;

public class EntryData {
    public List<PageData> pages;
    public String entryTitle;
    public String entrySearchAlias;
    //later add criteria by achievements
    public EntryData(List<PageData> page, String title, String searchAlias)
    {
        entryTitle = title;
        pages = page;
        entrySearchAlias = searchAlias;
    }
}
