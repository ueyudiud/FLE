package fla.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class FlaCreativeTab extends CreativeTabs
{
	public static final CreativeTabs fla_other_tab = new FlaCreativeTab("fla_other_age")
	{
		@Override
		public ItemStack getIconItemStack() 
		{
			return new ItemStack(Blocks.bedrock);
		}
	};
	public static final CreativeTabs fla_old_stone_age_tab = new FlaCreativeTab("fla_old_stone_age")
	{
		@Override
		public ItemStack getIconItemStack() 
		{
			return new ItemStack(FlaItems.flint_awl);
		}
	};
	public static final CreativeTabs fla_new_stone_age_tab = new FlaCreativeTab("fla_new_stone_age")
	{
		@Override
		public ItemStack getIconItemStack() 
		{
			return new ItemStack(FlaItems.whetstone);
		}
	};
	public static final CreativeTabs fla_copper_age_tab = new FlaCreativeTab("fla_copper_age")
	{
		@Override
		public ItemStack getIconItemStack() 
		{
			return new ItemStack(Items.gold_ingot);
		}
	};
	public static final CreativeTabs fla_bronze_age_tab = new FlaCreativeTab("fla_bronze_age")
	{
		@Override
		public ItemStack getIconItemStack() 
		{
			return new ItemStack(FlaBlocks.ore1);
		}
	};
	public static final CreativeTabs fla_iron_age_tab = new FlaCreativeTab("fla_iron_age")
	{
		@Override
		public ItemStack getIconItemStack() 
		{
			return new ItemStack(Items.iron_ingot);
		}
	};

	public FlaCreativeTab(String lable) 
	{
		super(lable);
	}
	
	public abstract ItemStack getIconItemStack();

	@Override
	public Item getTabIconItem() 
	{
		return getIconItemStack() != null ? getIconItemStack().getItem() : Item.getItemFromBlock(Blocks.stone);
	}
}