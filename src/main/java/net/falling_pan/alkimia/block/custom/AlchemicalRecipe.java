package net.falling_pan.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

import java.util.List;

public class AlchemicalRecipe {
    public List<AlchemicalCrucible.aspect> contents;
    public Item catalyst;
    public boolean isPotion;
    public Item result;
    public int amount;
    public FluidVariant base;
    public AlchemicalRecipe(List<AlchemicalCrucible.aspect> ingredients, Item catalystNeeded, boolean isThisAPotion, Item resultingItem, int resultAmount, FluidVariant fluid)
    {
        contents = ingredients;
        catalyst = catalystNeeded;
        isPotion = isThisAPotion;
        result = resultingItem;
        amount = resultAmount;
        base = fluid;
    }
}
