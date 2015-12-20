package fle.core.recipe.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.recipe.IPlayerToolCraftingRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.OreStack;
import fle.core.init.Rs;
import fle.core.item.ItemFleSub;

public class PlayerToolCraftingRecipe implements IPlayerToolCraftingRecipe
{
	private static final List<IPlayerToolCraftingRecipe> list = new ArrayList();

	public static void init()
	{
		OreStack tool = new OreStack("craftingToolSaw");
		for(Entry<ItemAbstractStack, ItemStack> e : Rs.logList.entrySet())
		{
			addRecipe(new PlayerToolCraftingRecipe(null, e.getKey(), tool, 0.25F, e.getValue().copy()));
		}
		addRecipe(new ToolCraftingRecipe("rough_stone_axe", 1, new BaseStack(ItemFleSub.a("branch_bush")), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("flint_hammer", 1, new BaseStack(ItemFleSub.a("branch_bush")), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_axe", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_shovel", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_hammer", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_sickle", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_spade_hoe", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("flint_arrow", 2, new BaseStack(Items.feather), new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("stone_sickle", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_knife", 1, new OreStack("stickWood"), new OreStack("tie")));
		addRecipe(new ToolCraftingRecipe("stone_spinning_disk", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_axe", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_pickaxe", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_shovel", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_chisel", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_bowsaw", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_adz", 1, new OreStack("stickWood")));
		addRecipe(new ToolCraftingRecipe("metal_hammer", 1, new OreStack("stickWood")));
		addRecipe(new TreeCuttingRecipe());
		addRecipe(new PlayerToolCraftingRecipe(null, new OreStack("flePlankWood"), new OreStack("craftingToolAdz"), 1.0F, ItemFleSub.a("wooden_wedge")));
		addRecipe(new PlayerToolCraftingRecipe(null, new BaseStack(ItemFleSub.a("tinder")), new OreStack("craftingToolFirestarter"), 0.5F, ItemFleSub.a("tinder_smoldering")));
	}
	
	public static void addRecipe(IPlayerToolCraftingRecipe recipe)
	{
		list.add(recipe);
	}
	
	public static IPlayerToolCraftingRecipe getResult(ItemStack input1, ItemStack input2, ItemStack tool)
	{
		for(IPlayerToolCraftingRecipe recipe : list)
		{
			if(recipe.match(input1, input2, tool))
			{
				return recipe;
			}
		}
		return null;
	}

	private float damage;
	public final ItemAbstractStack input1;
	public final ItemAbstractStack input2;
	public final ItemAbstractStack input3;
	public final ItemStack output;

	public PlayerToolCraftingRecipe(ItemAbstractStack input, ItemAbstractStack tool, ItemStack output)
	{
		this(input, null, tool, 1.0F, output);
	}
	public PlayerToolCraftingRecipe(ItemAbstractStack input1, ItemAbstractStack input2, ItemAbstractStack tool, float damage, ItemStack output)
	{
		this.input1 = input1;
		this.input2 = input2;
		this.input3 = tool;
		this.output = output;
		this.damage = damage;
	}
	
	@Override
	public boolean match(ItemStack input1, ItemStack input2, ItemStack tool)
	{
		if(this.input1 != null)
		{
			if(!this.input1.equal(input1)) return false;
		}
		else
		{
			if(input1 != null) return false;
		}
		if(this.input2 != null)
		{
			if(!this.input2.equal(input2)) return false;
		}
		else
		{
			if(input2 != null) return false;
		}
		if(this.input3 != null)
		{
			if(!this.input3.equal(tool)) return false;
		}
		else
		{
			if(tool != null) return false;
		}
		return true;
	}
	
	public ItemStack useTool(EntityPlayer player, ItemStack tool)
	{
		FleAPI.damageItem(player, tool, EnumDamageResource.UseTool, damage);
		return tool;
	}
	
	public ItemStack getOutput(ItemStack input1, ItemStack input2, ItemStack tool)
	{
		return output.copy();
	}
}