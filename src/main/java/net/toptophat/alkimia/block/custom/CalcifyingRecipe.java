package net.toptophat.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

public class CalcifyingRecipe {
    public Item itemInputOne;
    public Item itemInputTwo;
    public Item itemInputThree;
    public Item itemRemainderOne;
    public Item itemRemainderTwo;
    public Item itemRemainderThree;
    public FluidVariant fluidInput;
    public FluidVariant fluidOutput;
    public int fluidInputAmount;
    public int fluidOutputAmount;
    public int duration;
    public CalcifyingRecipe(Item itemIn1, Item itemIn2, Item itemIn3, Item itemOut1, Item itemOut2, Item itemOut3, FluidVariant fluidIn, FluidVariant fluidOut, int amountIn, int amountOut, int cooktime)
    {
        itemInputOne = itemIn1;
        itemInputTwo = itemIn2;
        itemInputThree = itemIn3;
        itemRemainderOne = itemOut1;
        itemRemainderTwo = itemOut2;
        itemRemainderThree = itemOut3;
        fluidInput = fluidIn;
        fluidOutput = fluidOut;
        fluidInputAmount = amountIn;
        fluidOutputAmount = amountOut;
        duration = cooktime;
    }
}
