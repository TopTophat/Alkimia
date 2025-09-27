package net.toptophat.alkimia.gui.data;

import java.util.List;

public class BookData {
    public List<EntryData> entries;
    public int lastPage = 5;
    public BookData(List<EntryData> entry)
    {
        entries = entry;
    }
}
