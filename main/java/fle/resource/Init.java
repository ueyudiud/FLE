package fle.resource;

import static fle.init.Blocks.dirt;
import static fle.init.Blocks.mineral;
import static fle.init.Blocks.rock;
import static fle.init.Blocks.sand;
import static fle.init.Blocks.sand1;
import static fle.init.Blocks.stoneChip;
import static fle.init.Fluids.*;
import static fle.init.Items.ingot;

import farcore.substance.Substance;
import farcore.substance.SubstanceRegistry;
import farcore.util.FleCreativeTab;
import farcore.util.Part;
import flapi.FleResource;
import flapi.util.Values;
import fle.core.block.MaterialWater;
import fle.core.enums.EnumDirtState;
import fle.core.enums.EnumRockSize;
import fle.core.enums.EnumRockState;
import fle.core.init.Entities;
import fle.init.Materials;
import fle.init.Substances;
import fle.resource.block.auto.BlockUniversalDirt;
import fle.resource.block.auto.BlockUniversalMineral;
import fle.resource.block.auto.BlockUniversalRock;
import fle.resource.block.auto.BlockUniversalSand;
import fle.resource.block.auto.BlockUniversalStoneChip;
import fle.resource.block.sand.BlockSand1;
import fle.resource.entity.EntityThrowStone;
import fle.resource.fluid.FluidFlashable;
import fle.resource.fluid.FluidWater;
import fle.resource.item.auto.ItemUniversalIngot;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;

public class Init
{
	public void preLoad()
	{
		Entities.EntityThrowStone = EntityThrowStone.class;
		Values.tabFLEResource = new FleCreativeTab("fle.resource", "FLE Resource")
		{
			@Override
			public Item getTabIconItem()
			{
				return Item.getItemFromBlock(rock);
			}
			
			@Override
			public ItemStack getIconItemStack()
			{
				return ((BlockUniversalRock) rock).setRock(new ItemStack(rock), Substances.andesite);
			}
		};
		Materials.saltyWater = new MaterialWater(MapColor.waterColor);
		water = new FluidWater(Substances.water, Material.water).setTextureName(Values.TEXTURE_FILE + ":fluids/water");
		saltyWater = new FluidWater(Substances.saltyWater, Materials.saltyWater).setTextureName(Values.TEXTURE_FILE + ":fluids/water_salty");
		hydrogen = new FluidFlashable(Substances.hydrogen, Materials.gas).setFlashPoint(617).setExplosionLevel(680).setTextureName(Values.TEXTURE_FILE + ":void").setGaseous(true);
		
		sand1 = new BlockSand1();
		sand1.setCreativeTab(Values.tabFLEResource);
		
		//Auto generated.
		rock = new BlockUniversalRock("rock");
		rock.setCreativeTab(Values.tabFLEResource);
		mineral = new BlockUniversalMineral("mineral");
		mineral.setCreativeTab(Values.tabFLEResource);
		sand = new BlockUniversalSand("sand");
		sand.setCreativeTab(Values.tabFLEResource);
		dirt = new BlockUniversalDirt("dirt");
		dirt.setCreativeTab(Values.tabFLEResource);
		stoneChip = new BlockUniversalStoneChip("stone.chip");
		stoneChip.setCreativeTab(Values.tabFLEResource);
		ingot = new ItemUniversalIngot("ingot");
		ingot.setCreativeTab(Values.tabFLEResource);
		for(Substance substance : FleResource.rock.asCollection())
		{
			ItemStack stack;
			for(EnumRockSize size : EnumRockSize.values())
			{
				stack = ((BlockUniversalStoneChip) stoneChip).setRock(new ItemStack(stoneChip), substance, size);
				SubstanceRegistry.register(substance, size.part(), stack, true);
			}
			for(EnumRockState state : EnumRockState.values())
			{
				stack = ((BlockUniversalRock) rock).setRock(new ItemStack(rock), substance, state);
				SubstanceRegistry.register(substance, state.part(), stack, true);
				for(Substance substance2 : FleResource.mineral.asCollection())
				{
					stack = ((BlockUniversalMineral) mineral).setMineral(new ItemStack(mineral), substance, state, substance2, Part.cube.resolution);
					SubstanceRegistry.register(substance2, state.part(), stack, true);
				}
			}
		}
		for(EnumDirtState state : EnumDirtState.values())
			for(Substance substance : FleResource.dirt.asCollection())
			{
				ItemStack stack = ((BlockUniversalDirt) dirt).setDirt(new ItemStack(dirt), substance.getName(), state);
				SubstanceRegistry.register(substance, Part.dirt, stack, true);
			}
		for(Substance substance : FleResource.sand.asCollection())
		{
			ItemStack stack = ((BlockUniversalSand) sand).setSand(new ItemStack(sand), substance.getName());
			SubstanceRegistry.register(substance, Part.sand, stack, true);
		}
	}

	public void load()
	{
		MinecraftForge.EVENT_BUS.register(new ResourceHandler());
	}

	public void postLoad()
	{
		
	}
	
	public void completeLoad()
	{
		
	}
}