/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.load;

import static farcore.data.Materials.ICE;
import static farcore.data.Materials.LOG;
import static farcore.data.Materials.METALIC;
import static farcore.data.Materials.ORE;
import static farcore.data.Materials.ROCK;
import static farcore.energy.thermal.ThermalNet.registerWorldThermalHandler;
import static farcore.handler.FarCoreEnergyHandler.addNet;
import static nebula.common.LanguageManager.registerLocal;
import static net.minecraftforge.fml.common.ProgressManager.pop;
import static net.minecraftforge.fml.common.ProgressManager.push;
import static net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity;
import static org.lwjgl.input.Keyboard.KEY_P;
import static org.lwjgl.input.Keyboard.KEY_R;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.FarCore;
import farcore.data.CT;
import farcore.data.EnumFluid;
import farcore.data.EnumItem;
import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolTypes;
import farcore.data.Keys;
import farcore.data.M;
import farcore.data.MC;
import farcore.data.Potions;
import farcore.energy.IEnergyNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.HeatWave;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreCapabilitiesHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.instances.TemperatureHandler;
import farcore.lib.block.instance.BlockCarvedRock;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.block.instance.BlockFire;
import farcore.lib.block.instance.BlockIce;
import farcore.lib.block.instance.BlockMetal;
import farcore.lib.block.instance.BlockRedstoneCircuit;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.block.instance.BlockScreen;
import farcore.lib.block.instance.BlockWater;
import farcore.lib.command.CommandSkill;
import farcore.lib.fluid.FluidWater;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemDebugger;
import farcore.lib.item.instance.ItemIngot;
import farcore.lib.item.instance.ItemOreChip;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.item.instance.ItemStoneFragment;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.tile.instance.TESapling;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaWorldHandler;
import nebula.common.fluid.FluidBase;
import nebula.common.tool.ToolHooks;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;

/**
 * @author ueyudiud
 */
public class CommonLoader
{
	public void preload()
	{
		ProgressBar bar = push("Far Core Preload", 8);
		//Register common handler.
		bar.step("Register Game Handlers");
		registerForgeEventListener(FarCoreEnergyHandler.getHandler());
		registerForgeEventListener(new FarCoreCapabilitiesHandler());
		//Register energy nets.
		bar.step("Add Energy Nets");
		addNet(ThermalNet.instance);
		addNet(new IEnergyNet.Impl(KineticNet.instance));
		//Register world objects.
		bar.step("Register World Objects");
		NebulaWorldHandler.registerObject("heat.wave", HeatWave.class);
		//Register local world handler.
		bar.step("Register Local World Handlers");
		registerWorldThermalHandler(new TemperatureHandler());
		//Initialize materials
		//Some material will create blocks and items.
		//DO NOT CALL CLASS farcore.data.M BEFORE FAR CORE PRE INITIALIZED.
		bar.step("Initalize Materials");
		M.init();
		//Initialize blocks & items & fluids.
		bar.step("Add Items");
		new ItemDebugger().setCreativeTab(CT.TOOL);
		EnumItem.display_fluid.set(Item.REGISTRY.getObject(new ResourceLocation("nebula", "display.fluid")));
		new ItemStoneChip().setCreativeTab(CT.RESOURCE_ITEM);
		new ItemStoneFragment().setCreativeTab(CT.RESOURCE_ITEM);
		new ItemOreChip().setCreativeTab(CT.RESOURCE_ITEM);
		new farcore.lib.block.terria.BlockOre().setCreativeTab(CT.TERRIA);
		new BlockCarvedRock();
		new BlockRedstoneCircuit().setCreativeTab(CT.REDSTONE);
		new ItemSeed().setCreativeTab(CT.CROP_AND_WILD_PLANTS);
		new BlockCrop();
		new BlockSapling().setCreativeTab(CT.TREE);
		EnumItem.branch.set(new ItemMulti(MC.branch).setCreativeTab(CT.TREE));
		new ItemTreeLog().setCreativeTab(CT.TREE);
		new ItemMulti(MC.bark).setCreativeTab(CT.TREE);
		new ItemMulti(MC.firewood).setCreativeTab(CT.TREE);
		EnumItem.nugget.set(new ItemMulti(MC.nugget).setCreativeTab(CT.MATERIAL));
		new ItemIngot().setCreativeTab(CT.MATERIAL);
		new ItemMulti(MC.pile).setCreativeTab(CT.MATERIAL);
		new ItemMulti(MC.brick).setCreativeTab(CT.BUILDING);
		new ItemMulti(MC.roofshingle).setCreativeTab(CT.BUILDING);
		new ItemMulti(MC.rooftile).setCreativeTab(CT.BUILDING);
		EnumFluid.water.setFluid(new FluidWater("pure.water", "Pure Water", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow")));
		new BlockWater((FluidBase) EnumFluid.water.fluid);
		new BlockIce().setCreativeTab(CT.TERRIA);
		new BlockFire();
		new BlockMetal();
		new BlockScreen();
		//Register tile entities.
		bar.step("Register Tile Entities");
		registerTileEntity(TECrop.class, "farcore.crop");
		registerTileEntity(TEOre.class, "farcore.ore");
		registerTileEntity(TECustomCarvedStone.class, "farcore.carved.stone");
		registerTileEntity(TESapling.class, "farcore.sapling");
		registerTileEntity(TECoreLeaves.class, "farcore.core.leaves");
		//		//Register entities.
		//		bar.step("Register Entities");
		//Initialize potions and mob effects.
		bar.step("Add Potion Effects");
		Potions.init();
		pop(bar);
	}
	
	public void load()
	{
		ProgressBar bar = push("Far Core Load", 2);
		//Post load item and block.
		//For register to Ore Dictionary, Tool Uses, Compatibility, etc.
		bar.step("Post initalizing items and blocks");
		ToolHooks.addEfficiencyTool(ROCK, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addHarvestableTool(ROCK, true, EnumToolTypes.HAMMER_DIGABLE);
		ToolHooks.addEfficiencyTool(METALIC, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(Material.ANVIL, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(ORE, EnumToolTypes.PICKAXE, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(LOG, EnumToolTypes.AXE, EnumToolTypes.ADZ, EnumToolTypes.SAW, EnumToolTypes.BOW_SAW);
		ToolHooks.addEfficiencyTool(ICE, EnumToolTypes.PICKAXE, EnumToolTypes.HAMMER_DIGABLE);
		ToolHooks.addHarvestableTool(ICE, true, EnumToolTypes.CHISEL);
		//Register languages.
		bar.step("Register localized file");
		registerLocal("info.debug.date", "Date : ");
		registerLocal("info.log.length", "Legnth : %d");
		registerLocal("info.slab.place", "Place slab in sneaking can let slab only has up or down facing.");
		registerLocal("info.stone.chip.throwable", "You can throw it out to attack entities.");
		registerLocal("info.crop.type", "Crop Name : %s");
		registerLocal("info.crop.generation", "Generation : %d");
		registerLocal("info.tool.damage", "Durability : " + ChatFormatting.GREEN + " %d / %d");
		registerLocal("info.tool.harvest.level", "Harvest Level : " + ChatFormatting.YELLOW + " lv%d");
		registerLocal("info.tool.hardness", "Hardness : " + ChatFormatting.BLUE + "%s");
		registerLocal("info.tool.attack", "Attack : " + ChatFormatting.DARK_RED + "%s");
		registerLocal("info.tool.head.name", "Tool Head : " + ChatFormatting.LIGHT_PURPLE + "%s");
		registerLocal("info.tool.handle.name", "Tool Handle : " + ChatFormatting.LIGHT_PURPLE + "%s");
		registerLocal("info.tool.tie.name", "Tool Tie : " + ChatFormatting.LIGHT_PURPLE + "%s");
		registerLocal("info.redstone.circuit.material", "Material : " + ChatFormatting.YELLOW + "%s");
		registerLocal("info.tree.log.length", "Length : " + ChatFormatting.GREEN + "%d");
		registerLocal("info.fluidcontainer.contain", "Contain : " + ChatFormatting.AQUA + "%s");
		registerLocal("skill.upgrade.info", "The skill " + ChatFormatting.ITALIC + "%s" + ChatFormatting.RESET + " is upgrade from %d to %d level.");
		registerLocal("commands.date.usage", "/date");
		registerLocal("commands.date.arg.err", "Invalid command argument");
		registerLocal(EnumPhysicalDamageType.PUNCTURE.getTranslation(), ChatFormatting.GOLD + "Puncture");
		registerLocal(EnumPhysicalDamageType.SMASH.getTranslation(), ChatFormatting.GOLD + "Smash");
		registerLocal(EnumPhysicalDamageType.CUT.getTranslation(), ChatFormatting.GOLD + "Cut");
		registerLocal(EnumPhysicalDamageType.HIT.getTranslation(), ChatFormatting.GOLD + "Hit");
		//		//Setup network.
		//		bar.step("Setup network handler");
		NebulaKeyHandler.register(Keys.ROTATE, KEY_R, FarCore.ID);
		NebulaKeyHandler.register(Keys.PLACE, KEY_P, FarCore.ID);
		pop(bar);
	}
	
	public void postload()
	{
		//Reload material tool tips.
		for(Mat material : Mat.materials())
		{
			if(material.customDisplayInformation != null)
			{
				registerLocal("info.material.custom." + material.name, material.customDisplayInformation);
			}
			if(material.chemicalFormula != null)
			{
				registerLocal("info.material.chemical.formula." + material.name, material.chemicalFormula);
			}
		}
		CommandSkill.addCommandInformations();
	}
	
	public void complete()
	{
	}
	
	private static void registerForgeEventListener(Object object)
	{
		MinecraftForge.EVENT_BUS.register(object);
	}
}