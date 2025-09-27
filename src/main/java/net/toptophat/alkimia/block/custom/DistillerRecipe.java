package net.toptophat.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

import java.util.List;

public class DistillerRecipe {
    //rules if there are multiple fluids, and you click with a flask, if empty take the topmost fluid, else take the matching fluid, if it's there
    public List<FluidVariant> inputFluids; //max 4
    public List<Integer> fluidInputAmounts;
    public List<FluidVariant> outputFluids; //max 4
    public List<Integer> fluidOutputAmounts;
    public List<Item> inputItems; //max 5
    public List<Item> remainders; //max 5
    public int cooktime; //ticks

    public DistillerRecipe(List<FluidVariant> inputFluids, List<Integer> fluidInputAmounts, List<FluidVariant> outputFluids, List<Integer> fluidOutputAmounts, List<Item> inputItems, List<Item> remainders, int cooktime)
    {
        this.inputFluids = inputFluids;
        this.fluidInputAmounts = fluidInputAmounts;
        this.outputFluids = outputFluids;
        this.fluidOutputAmounts = fluidOutputAmounts;
        this.inputItems = inputItems;
        this.remainders = remainders;
        this.cooktime = cooktime;
    }

    //example recipe item -> fluid
        //ice -> water
    //example recipe item -> remainder item + fluid
        //magma -> netherrack + lava
    //example fluid -> fluid
        //soul -> liquid soul
    //example fluid -> remainder fluid + fluid
        //diamond dust -> water + soul
    //most complex 2 fluids + 2 items -> 2 remainder fluids + 2 remainder items + 2 fluids
        //soul + diamond dust + soul sand + quicksilver -> water + lava + sand + copper ingot + liquid soul + lava
}
