package farcore.load;

import static farcore.energy.thermal.ThermalNet.registerWorldThermalHandler;
import static farcore.handler.FarCoreEnergyHandler.addNet;
import static nebula.common.LanguageManager.registerLocal;
import static net.minecraftforge.fml.common.ProgressManager.pop;
import static net.minecraftforge.fml.common.ProgressManager.push;
import static net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.data.CT;
import farcore.data.Config;
import farcore.data.EnumFluid;
import farcore.data.EnumItem;
import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumRockType;
import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.Potions;
import farcore.energy.IEnergyNet;
import farcore.energy.kinetic.BlockKineticDebug;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.HeatWave;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreCapabilitiesHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreSynchronizationHandler;
import farcore.instances.TemperatureHandler;
import farcore.lib.block.instance.BlockCarvedRock;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.block.instance.BlockFire;
import farcore.lib.block.instance.BlockIce;
import farcore.lib.block.instance.BlockLog;
import farcore.lib.block.instance.BlockModelDebug;
import farcore.lib.block.instance.BlockOre;
import farcore.lib.block.instance.BlockRedstoneCircuit;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.block.instance.BlockWater;
import farcore.lib.command.CommandSkill;
import farcore.lib.fluid.FluidWater;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemDebugger;
import farcore.lib.item.instance.ItemOreChip;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.item.instance.ItemStoneFragment;
import farcore.lib.item.instance.ItemSubCropRelated;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.tile.instance.TESapling;
import nebula.client.CreativeTabBase;
import nebula.common.NebulaWorldHandler;
import nebula.common.fluid.FluidBase;
import nebula.common.tool.ToolHooks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;

public class CommonLoader
{
	public void preload()
	{
		ProgressBar bar = push("Far Core Preload", 9);
		bar.step("Add Creative Tabs");
		CT.tabCropAndWildPlants = new CreativeTabBase("farcore.crop.plants", "Far Crop And Wild Plant", () -> new ItemStack(Items.WHEAT));
		CT.tabTree = new CreativeTabBase("farcore.tree", "Far Tree",
				() -> Config.createLog ? new ItemStack(M.oak.getProperty(MP.property_wood).block) : new ItemStack(Blocks.LOG));
		CT.tabTerria = new CreativeTabBase("farcore.terria", "Far Terria",
				() -> Config.createRock ? new ItemStack(M.peridotite.getProperty(MP.property_rock).block) : new ItemStack(Blocks.STONE));
		CT.tabBuilding = new CreativeTabBase("farcore.building", "Far Building Blocks",
				() -> Config.createRock ? new ItemStack(M.marble.getProperty(MP.property_rock).block, 1, EnumRockType.brick.ordinal()) : new ItemStack(Blocks.STONEBRICK));
		CT.tabResourceItem = new CreativeTabBase("farcore.resource.item", "Far Resource Item",
				() -> Config.createRock ? new ItemStack(EnumItem.stone_chip.item, 1, M.peridotite.id) : new ItemStack(Items.GOLD_INGOT));
		CT.tabMachine = new CreativeTabBase("farcore.machine", "Far Machine",
				() -> new ItemStack(Blocks.CRAFTING_TABLE));
		CT.tabMaterial = new CreativeTabBase("farcore.material", "Far Material",
				() -> new ItemStack(Items.EMERALD));
		CT.tabTool = new CreativeTabBase("farcore.tool", "Far Tool",
				() -> new ItemStack(EnumItem.debug.item));
		CT.tabRedstone = new CreativeTabBase("farcore.redstone", "Far Redstone",
				() -> Config.createRock ? BlockRedstoneCircuit.createItemStack(1, M.stone) : new ItemStack(Items.COMPARATOR));
		//Register common handler.
		bar.step("Register Game Handlers");
		registerForgeEventListener(new FarCoreSynchronizationHandler());
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
		new ItemDebugger().setCreativeTab(CT.tabTool);
		EnumItem.display_fluid.set(Item.REGISTRY.getObject(new ResourceLocation("nebula", "display.fluid")));
		new BlockKineticDebug();
		new BlockModelDebug();
		if(Config.createRock)
		{
			new ItemStoneChip().setCreativeTab(CT.tabResourceItem);
			new ItemStoneFragment().setCreativeTab(CT.tabResourceItem);
			new ItemOreChip().setCreativeTab(CT.tabResourceItem);
			new BlockOre().setCreativeTab(CT.tabTerria);
			new BlockCarvedRock();
			new BlockRedstoneCircuit().setCreativeTab(CT.tabRedstone);
		}
		if(Config.createCrop)
		{
			new ItemSeed().setCreativeTab(CT.tabCropAndWildPlants);
			new BlockCrop();
			new ItemSubCropRelated().setCreativeTab(CT.tabCropAndWildPlants);
		}
		if(Config.createLog)
		{
			new BlockSapling().setCreativeTab(CT.tabTree);
			EnumItem.branch.set(new ItemMulti(MC.branch).setCreativeTab(CT.tabTree));
			new ItemTreeLog().setCreativeTab(CT.tabTree);
		}
		EnumItem.nugget.set(new ItemMulti(MC.nugget).setCreativeTab(CT.tabResourceItem));
		if(Config.replaceWater)
		{
			EnumFluid.water.setFluid(new FluidWater("pure.water", "Pure Water", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow")));
			new BlockWater((FluidBase) EnumFluid.water.fluid);
			new BlockIce().setCreativeTab(CT.tabTerria);
		}
		new BlockFire();
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
		ToolHooks.addEfficiencyTool(Material.ROCK, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(Material.IRON, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(Material.ANVIL, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(BlockOre.ORE, EnumToolTypes.PICKAXE, EnumToolTypes.HAMMER_DIGABLE, EnumToolTypes.EXPLOSIVE, EnumToolTypes.DRILL, EnumToolTypes.LASER);
		ToolHooks.addEfficiencyTool(BlockLog.LOG, EnumToolTypes.AXE, EnumToolTypes.ADZ, EnumToolTypes.SAW, EnumToolTypes.BOW_SAW);
		ToolHooks.addEfficiencyTool(BlockIce.ICE, EnumToolTypes.PICKAXE, EnumToolTypes.HAMMER_DIGABLE);
		ToolHooks.addHarvestableTool(BlockIce.ICE, true, EnumToolTypes.CHISEL);
		//Register languages.
		bar.step("Register localize file");
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
		//		//Setup network.
		//		bar.step("Setup network handler");
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