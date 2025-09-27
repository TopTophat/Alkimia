package net.falling_pan.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

import java.util.List;

public class TransmutingRecipe {
    //rules if there are multiple fluids, and you click with a flask, if empty take the topmost fluid, else take the matching fluid, if it's there
    public List<FluidVariant> inputFluids; //max 4
    public List<Item> inputItems; //max 4
    public List<Integer> inputContents;
    public Item mainItem;
    public Item resultItem;
    public int cooktime; //ticks

    public TransmutingRecipe(List<FluidVariant> inputFluids, List<Item> inputItems, List<Integer> inputContents, Item mainItem, Item resultItem, int cooktime)
    {
        this.inputFluids = inputFluids;
        this.inputItems = inputItems;
        this.inputContents = inputContents;
        this.mainItem = mainItem;
        this.resultItem = resultItem;
        this.cooktime = cooktime;
    }

    //example recipe
        //Quartz central + Redstone side + Quicksilver side + Prismarine crystals side + coal side + lava + water + liquid soul + soul + 3 glow + 3 water + 3senses
}
