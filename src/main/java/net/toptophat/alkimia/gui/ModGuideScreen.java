package net.toptophat.alkimia.gui;

import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.gui.data.BookData;
import net.toptophat.alkimia.gui.data.EntryData;
import net.toptophat.alkimia.gui.data.GuidebookData;
import net.toptophat.alkimia.gui.data.PageData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;


public class ModGuideScreen extends Screen {
    private final ItemStack itemStack;
    public ButtonWidget nextPage;
    public ButtonWidget lastPage;
    public ButtonWidget bestResult;
    public ButtonWidget secondResult;
    public ButtonWidget thirdResult;
    public ButtonWidget fourthResult;
    public TextFieldWidget field;

    public ModGuideScreen(Text title, ItemStack stack) {
        super(title);
        this.itemStack = stack;
    }

    @Override
    protected void init() {
        nextPage = ButtonWidget.builder(Text.of("->"), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            );
            itemStack.set(ModDataComponentTypes.PAGE, itemStack.get(ModDataComponentTypes.PAGE) + 1 > GuidebookData.book.lastPage ? 0 : itemStack.get(ModDataComponentTypes.PAGE) + 1);
        }).dimensions(360, 200, 25, 25).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(this.nextPage);
        lastPage = ButtonWidget.builder(Text.of("<-"), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            );
            itemStack.set(ModDataComponentTypes.PAGE, itemStack.get(ModDataComponentTypes.PAGE) - 1 < 0 ? GuidebookData.book.lastPage : itemStack.get(ModDataComponentTypes.PAGE) - 1);
        }).dimensions(40, 200, 25, 25).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(this.lastPage);

        field = new TextFieldWidget(client.textRenderer,2, 30, 80, 20, Text.of("Search..."));

        this.addDrawableChild(this.field);
        bestResult = ButtonWidget.builder(Text.of(""), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            );
            System.out.println("Best result clicked");
            if (searchForEntries(this.field.getText(), GuidebookData.book).get(0) != null && !searchForEntries(this.field.getText(), GuidebookData.book).get(0).pages.isEmpty())
            {
                System.out.println("Best result sets page it is " + searchForEntries(this.field.getText(), GuidebookData.book).get(0).entryTitle);
                itemStack.set(ModDataComponentTypes.PAGE, searchForEntries(field.getText(), GuidebookData.book).get(0).pages.get(0).pageNumber);
            }
        }).dimensions(2, 50, 80, 20).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(this.bestResult);

        secondResult = ButtonWidget.builder(Text.of(""), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            );
            System.out.println("Second result clicked");
            if (searchForEntries(field.getText(), GuidebookData.book).get(1) != null && !searchForEntries(this.field.getText(), GuidebookData.book).get(1).pages.isEmpty())
            {
                System.out.println("Second result sets page it is " + searchForEntries(this.field.getText(), GuidebookData.book).get(1).entryTitle);
                itemStack.set(ModDataComponentTypes.PAGE, searchForEntries(field.getText(), GuidebookData.book).get(1).pages.get(0).pageNumber);
            }
        }).dimensions(2, 70, 80, 20).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(this.secondResult);

        thirdResult = ButtonWidget.builder(Text.of(""), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            );
            System.out.println("Third result clicked");
            if (searchForEntries(field.getText(), GuidebookData.book).get(2) != null && !searchForEntries(this.field.getText(), GuidebookData.book).get(2).pages.isEmpty())
            {
                System.out.println("Third result sets page it is " + searchForEntries(this.field.getText(), GuidebookData.book).get(2).entryTitle);
                itemStack.set(ModDataComponentTypes.PAGE, searchForEntries(field.getText(), GuidebookData.book).get(2).pages.get(0).pageNumber);
            }
        }).dimensions(2, 90, 80, 20).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(this.thirdResult);

        fourthResult = ButtonWidget.builder(Text.of(""), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            );
            System.out.println("Fourth result clicked");
            if (searchForEntries(field.getText(), GuidebookData.book).get(3) != null && !searchForEntries(this.field.getText(), GuidebookData.book).get(3).pages.isEmpty())
            {
                System.out.println("Fourth result sets page it is " + searchForEntries(this.field.getText(), GuidebookData.book).get(3).entryTitle);
                itemStack.set(ModDataComponentTypes.PAGE, searchForEntries(field.getText(), GuidebookData.book).get(3).pages.get(0).pageNumber);
            }
        }).dimensions(2, 110, 80, 20).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(this.fourthResult);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        Identifier texture = Identifier.of("alkimia", "textures/gui/intro1.png");
        context.drawTexture(texture, 86, 30, 0, 0, 256, 192, 16, 16);
        PageData page = findPage(itemStack.get(ModDataComponentTypes.PAGE), GuidebookData.book);
        context.drawText(client.textRenderer, findEntryTitle(page.pageNumber, GuidebookData.book), 10, 10, 0xFFFFFFFF, false);
        for (int i = 0; i < page.texts.size(); i++) {
            context.drawText(client.textRenderer, page.texts.get(i).text, Math.round(page.texts.get(i).startPos.x), Math.round(page.texts.get(i).startPos.y), 0xFFFFFFFF, false);
        }
        for (int i = 0; i < page.images.size(); i++) {
            context.drawTexture(page.images.get(i).sprite, Math.round(page.images.get(i).position.x), Math.round(page.images.get(i).position.y), 0, 0, Math.round(page.images.get(i).size.x), Math.round(page.images.get(i).size.y), Math.round(page.images.get(i).size.x), Math.round(page.images.get(i).size.y));
        }
        for (int i = 0; i < page.items.size(); i++) {
            context.drawTexture(page.items.get(i).itemToRender, Math.round(page.items.get(i).position.x), Math.round(page.items.get(i).position.y), 0, 0, Math.round(page.items.get(i).size.x), Math.round(page.items.get(i).size.x), Math.round(page.items.get(i).size.x), Math.round(page.items.get(i).size.x));
        }
        //render text field, buttons and list of results
        context.drawText(this.textRenderer, "Next page", 352, 200 - this.textRenderer.fontHeight - 7, 0xFFFFFFFF, true);
        context.drawText(this.textRenderer, "Last page", 32, 200 - this.textRenderer.fontHeight - 7, 0xFFFFFFFF, true);

        if (this.field != null)
        {
            if (searchForEntries(field.getText(), GuidebookData.book).get(0) != null && !searchForEntries(field.getText(), GuidebookData.book).get(0).pages.isEmpty())
            {
                this.bestResult.setMessage(Text.literal(searchForEntries(field.getText(), GuidebookData.book).get(0).entryTitle));
                this.bestResult.setAlpha(255);
            }
            else
            {
                this.bestResult.setMessage(Text.literal(""));
                this.bestResult.setAlpha(0);
            }

            if (searchForEntries(field.getText(), GuidebookData.book).get(1) != null && !searchForEntries(field.getText(), GuidebookData.book).get(1).pages.isEmpty())
            {
                this.secondResult.setMessage(Text.literal(searchForEntries(field.getText(), GuidebookData.book).get(1).entryTitle));
                this.secondResult.setAlpha(255);
            }
            else
            {
                this.secondResult.setMessage(Text.literal(""));
                this.secondResult.setAlpha(0);
            }

            if (searchForEntries(field.getText(), GuidebookData.book).get(2) != null && !searchForEntries(field.getText(), GuidebookData.book).get(2).pages.isEmpty())
            {
                this.thirdResult.setMessage(Text.literal(searchForEntries(field.getText(), GuidebookData.book).get(2).entryTitle));
                this.thirdResult.setAlpha(255);
            }
            else
            {
                this.thirdResult.setMessage(Text.literal(""));
                this.thirdResult.setAlpha(0);
            }

            if (searchForEntries(field.getText(), GuidebookData.book).get(3) != null && !searchForEntries(field.getText(), GuidebookData.book).get(3).pages.isEmpty())
            {
                this.fourthResult.setMessage(Text.literal(searchForEntries(field.getText(), GuidebookData.book).get(3).entryTitle));
                this.fourthResult.setAlpha(255);
            }
            else
            {
                this.fourthResult.setMessage(Text.literal(""));
                this.fourthResult.setAlpha(0);
            }
        }
    }

    public PageData findPage(int pageNumber, BookData book)
    {
        for (int i = 0; i < book.entries.size(); i++) {
            for (int j = 0; j < book.entries.get(i).pages.size(); j++) {
                if (book.entries.get(i).pages.get(j).pageNumber == pageNumber)
                {
                    return book.entries.get(i).pages.get(j);
                }
            }
        }
        return new PageData(List.of(), List.of(), List.of(), 0);
    }

    public String findEntryTitle(int pageNumber, BookData book)
    {
        for (int i = 0; i < book.entries.size(); i++) {
            for (int j = 0; j < book.entries.get(i).pages.size(); j++) {
                if (book.entries.get(i).pages.get(j).pageNumber == pageNumber)
                {
                    return book.entries.get(i).entryTitle;
                }
            }
        }
        return "";
    }

    public List<EntryData> searchForEntries(String search, BookData book)
    {
        EntryData bestMatch = new EntryData(List.of(), "", "");
        EntryData secondMatch = new EntryData(List.of(), "", "");
        EntryData thirdMatch = new EntryData(List.of(), "", "");
        EntryData fourthMatch = new EntryData(List.of(), "", "");
        int matchingBest = 0;
        int matchingSecond = 0;
        int matchingThird = 0;
        int matchingFourth = 0;
        int searchLength = search.toLowerCase().length();
        int matchingLetters = 0;
        int matchingLettersAlias = 0;
        for (int i = 0; i < book.entries.size(); i++) {
            for (int j = 0; j < (Math.min(searchLength, book.entries.get(i).entryTitle.toLowerCase().length())); j++) {
                if (search.toLowerCase().substring(0, j + 1).equals(book.entries.get(i).entryTitle.toLowerCase().substring(0, j + 1)))
                {
                    matchingLetters++;
                }
                else
                {
                    break;
                }
            }
            for (int j = 0; j < (Math.min(searchLength, book.entries.get(i).entrySearchAlias.toLowerCase().length())); j++) {
                if (search.toLowerCase().substring(0, j + 1).equals(book.entries.get(i).entrySearchAlias.toLowerCase().substring(0, j + 1)))
                {
                    matchingLettersAlias++;
                }
                else
                {
                    break;
                }
            }
            int matches = Math.max(matchingLetters, matchingLettersAlias);
            if (matches > matchingBest)
            {
                bestMatch = book.entries.get(i);
                matchingBest = matches;
            }
            else if(matches > matchingSecond)
            {
                secondMatch = book.entries.get(i);
                matchingSecond = matches;
            }
            else if(matches > matchingThird)
            {
                thirdMatch = book.entries.get(i);
                matchingThird = matches;
            }
            else if(matches > matchingFourth)
            {
                fourthMatch = book.entries.get(i);
                matchingFourth = matches;
            }
            matchingLetters = 0;
            matches = 0;
            matchingLettersAlias = 0;
        }
        return List.of(bestMatch, secondMatch, thirdMatch, fourthMatch);
    }
}