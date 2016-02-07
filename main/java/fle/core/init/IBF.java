package fle.core.init;

import static farcore.util.Vs.water_freeze_point;

import java.lang.reflect.Field;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item.ToolMaterial;

import fle.core.item.ItemDebug;
import fle.init.Blocks1;
import fle.init.Fluids1;
import fle.init.Substance2;
import fle.resource.block.BlockDirt;
import fle.resource.block.BlockMineral;
import fle.resource.block.BlockRock;
import fle.resource.block.BlockSand;
import fle.resource.block.BlockStoneChip;
import fle.resource.fluid.FluidWater;

public class IBF
{
	public static void preinit()
	{
	
	}
	
	public static void init()
	{
		Fluids1.water = new FluidWater("water").setSubstance(Substance2.water)
				.setTemperature(water_freeze_point + 25);
		Fluids1.saltyWater = new FluidWater("salty_water")
				.setSubstance(Substance2.saltyWater)
				.setTemperature(water_freeze_point + 25);
				
		Blocks1.rock = new BlockRock("rock");
		Blocks1.rock.setLocalizedName("rock")
				.setCreativeTab(CreativeTabs.tabBlock);
		Blocks1.mineral = new BlockMineral("mineral");
		Blocks1.mineral.setLocalizedName("mineral")
				.setCreativeTab(CreativeTabs.tabBlock);
		Blocks1.sand = new BlockSand("sand");
		Blocks1.sand.setLocalizedName("sand")
				.setCreativeTab(CreativeTabs.tabBlock);
		Blocks1.dirt = new BlockDirt("dirt");
		Blocks1.dirt.setLocalizedName("dirt")
				.setCreativeTab(CreativeTabs.tabBlock);
		Blocks1.stoneChip = new BlockStoneChip("stone.chip");
		Blocks1.stoneChip.setLocalizedName("stone chip")
				.setCreativeTab(CreativeTabs.tabDecorations);
		new ItemDebug().setMaxStackSize(1)
				.setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	public static void postinit()
	{
		try
		{
			Field field = ToolMaterial.class.getDeclaredField("harvestLevel");
			field.setAccessible(true);
			field.setInt(ToolMaterial.WOOD, 4);
			field.setInt(ToolMaterial.STONE, 9);
			field.setInt(ToolMaterial.IRON, 13);
			field.setInt(ToolMaterial.GOLD, 4);
			field.setInt(ToolMaterial.EMERALD, 28);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}