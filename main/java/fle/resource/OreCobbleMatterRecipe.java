package fle.resource;

import net.minecraft.item.ItemStack;
import flapi.chem.base.MatterStack;
import flapi.chem.base.Part;
import flapi.chem.particle.Atoms;
import flapi.material.MaterialOre;
import flapi.recipe.SingleMatterOutputRecipe;
import fle.core.init.IB;
import fle.core.init.Parts;
import fle.resource.item.ItemOre;

public class OreCobbleMatterRecipe implements SingleMatterOutputRecipe
{
	@Override
	public boolean matchInput(ItemStack stack)
	{
		return stack.getItem() == IB.oreChip;
	}

	@Override
	public ItemStack[] getShowStack()
	{
		ItemStack[] stacks = new ItemStack[MaterialOre.getOres().size()];
		for(int i = 0; i < stacks.length; ++i)
			stacks[i] = ItemOre.a(MaterialOre.getOreFromID(i));
		return stacks;
	}

	@Override
	public MatterStack getOutput(ItemStack stack)
	{
		if(stack == null) return new MatterStack(Atoms.Nt.asMatter());
		MaterialOre material = MaterialOre.getOreFromID(stack.getItemDamage());
		int amount = ((ItemOre) IB.oreChip).getOreAmount(stack);
		return amount == 0 ? new MatterStack(material.getMatter(), Parts.chip, stack.stackSize) : new MatterStack(material.getMatter(), Part.part("chip_fle_" + amount, amount), stack.stackSize);
	}
}