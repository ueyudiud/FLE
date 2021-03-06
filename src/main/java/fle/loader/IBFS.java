/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader;

import farcore.data.CT;
import farcore.data.M;
import farcore.data.MC;
import farcore.items.ItemStoneChip;
import farcore.lib.solid.SolidBase;
import fle.core.blocks.BlockDirtMixture;
import fle.core.blocks.BlockDitch;
import fle.core.blocks.BlockGasExplosive;
import fle.core.blocks.BlockPottery;
import fle.core.blocks.BlockResourceMisc;
import fle.core.blocks.BlockRockyMiscMachine;
import fle.core.blocks.BlockRockySimpleMachine;
import fle.core.blocks.BlockTools;
import fle.core.blocks.BlockWoodenMiscMachine;
import fle.core.blocks.BlockWoodenSimpleWorkbench;
import fle.core.blocks.container.BlockChest;
import fle.core.blocks.container.BlockRockyTank;
import fle.core.items.ItemMiscResources;
import fle.core.items.ItemSack;
import fle.core.items.ItemSimpleFluidContainer;
import fle.core.items.ItemSubCropRelated;
import fle.core.items.ItemToolFar;
import nebula.common.fluid.FluidBase;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

/**
 * Item, Block, Fluid, Solid
 * 
 * @author ueyudiud
 */
public class IBFS
{
	public static void registerItemsAndBlocks()
	{
		iCropRelated = (ItemSubCropRelated) new ItemSubCropRelated().setCreativeTab(CT.CROP_AND_WILD_PLANTS);
		
		bTool = new BlockTools();
		iTool = (ItemToolFar) new ItemToolFar().setCreativeTab(CT.TOOL);
		iFluidContainer = (ItemSimpleFluidContainer) new ItemSimpleFluidContainer().setCreativeTab(CT.TOOL);
		iResources = (ItemMiscResources) new ItemMiscResources().setCreativeTab(CT.RESOURCE_ITEM);
		iSack = (ItemSack) new ItemSack().setCreativeTab(CT.TOOL);
		bDitch = new BlockDitch();
		bResources = new BlockResourceMisc().setCreativeTab(CT.MACHINE);
		bWoodenWorkbench = new BlockWoodenSimpleWorkbench().setCreativeTab(CT.MACHINE);
		bWoodenMachine = new BlockWoodenMiscMachine().setCreativeTab(CT.MACHINE);
		bRockyMachine = new BlockRockySimpleMachine().setCreativeTab(CT.MACHINE);
		bRockyMachine2 = new BlockRockyMiscMachine().setCreativeTab(CT.MACHINE);
		bPottery = new BlockPottery().setCreativeTab(CT.MACHINE);
		bDirtMixture = new BlockDirtMixture().setCreativeTab(CT.MACHINE);
		bRockyTank = new BlockRockyTank().setCreativeTab(CT.MACHINE);
		bChest = new BlockChest().setCreativeTab(CT.MACHINE);
		
		fLacquer = new FluidBase("lacquer", "Lacquer").setDensity(1200).setViscosity(5000);
		fLacquerDried = new FluidBase("dried_lacquer", "Dried Lacquer").setDensity(1180).setViscosity(12000);
		ResourceLocation loc = new ResourceLocation("fle", "fluids/juice");
		fsJuice = new FluidBase[] {
				new FluidBase("sugarcane_juice", "Sugarcane Juice", loc, loc).setColor(0xFF8D8F67).setDensity(1250),
				new FluidBase("citrus_juice", "Citrus Juice", loc, loc).setColor(0xFFE5F35A).setDensity(1250),
				new FluidBase("bitter_orange_juice", "Bitter Orange Juice", loc, loc).setColor(0xFFECB447).setDensity(1250),
				new FluidBase("lemon_juice", "Lemon Juice", loc, loc).setColor(0xFFFFEB7F).setDensity(1250),
				new FluidBase("tangerine_juice", "Tangerine Juice", loc, loc).setColor(0xFFFFA830).setDensity(1250),
				new FluidBase("pomelo_juice", "Pomelo Juice", loc, loc).setColor(0xFFECF376).setDensity(1250),
				new FluidBase("lime_juice", "Lime Juice", loc, loc).setColor(0xFFF69500).setDensity(1250),
				new FluidBase("orange_juice", "Orange Juice", loc, loc).setColor(0xFFF69500).setDensity(1250),
				new FluidBase("grapefruit_juice", "Grapefruit Juice", loc, loc).setColor(0xFFF67C00).setDensity(1250)
		};
		fLimeMortar = new FluidBase("lime_mortar", "Lime Mortar").setDensity(1400).setViscosity(1100);
		fAnimalOil = new FluidBase("animal_oil", "Animal Oil").setDensity(800).setViscosity(900);
		fNaturalGas = new FluidBase("natural_gas", "Natural Gas").setDensity(-20).setViscosity(20).setBurning().setGaseous(true);
		
		fNaturalGas.setBlock(new BlockGasExplosive(fNaturalGas, 360, 200, 9.0F));
		
		sMillet = new SolidBase("millet", "Millet");
		sWholemealMillet = new SolidBase("millet_wholemeal", "Wholemeal Millet");
		sWheat = new SolidBase("wheat", "Wheat");
		sWholemealWheat = new SolidBase("wheat_wholemeal", "Wholemeal Wheat");
		sBlackPepper = new SolidBase("black_pepper", "Black Pepper");
		sTurmericRhizome = new SolidBase("turmeric_rhizome", "Thurmeric Rhizome");
		sPricklyAsh = new SolidBase("prickly_ash", "Prickly Ash");
		sCinnamon = new SolidBase("cinnamon", "Cinnamon");
	}
	
	public static void setBlocksItemsProperties()
	{
		MC.pile.addToWhiteList(M.limestone, M.marble);
		
		ItemStoneChip.shootStoneChipExp = 0.7F;
	}
	
	public static ItemToolFar				iTool;
	public static ItemSimpleFluidContainer	iFluidContainer;
	public static ItemMiscResources			iResources;
	public static ItemSubCropRelated		iCropRelated;
	public static ItemSack					iSack;
	public static Block						bDitch;
	public static Block						bWoodenWorkbench;
	public static Block						bWoodenMachine;
	public static Block						bRockyMachine;
	public static Block						bRockyMachine2;
	public static Block						bPottery;
	public static Block						bDirtMixture;
	public static Block						bRockyTank;
	public static Block						bChest;
	public static Block						bTool;
	public static Block						bResources;
	public static FluidBase					fLacquer;
	public static FluidBase					fLacquerDried;
	public static FluidBase[]				fsJuice;
	public static FluidBase					fLimeMortar;
	public static FluidBase					fAnimalOil;
	public static FluidBase					fNaturalGas;
	public static SolidBase					sMillet;
	public static SolidBase					sWholemealMillet;
	public static SolidBase					sWheat;
	public static SolidBase					sWholemealWheat;
	public static SolidBase					sBlackPepper;
	public static SolidBase					sTurmericRhizome;
	public static SolidBase					sPricklyAsh;
	public static SolidBase					sCinnamon;
}
