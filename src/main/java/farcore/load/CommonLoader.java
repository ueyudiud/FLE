package farcore.load;

import static farcore.energy.thermal.ThermalNet.registerWorldThermalHandler;
import static farcore.handler.FarCoreEnergyHandler.addNet;
import static farcore.lib.util.LanguageManager.registerLocal;
import static net.minecraftforge.fml.common.ProgressManager.pop;
import static net.minecraftforge.fml.common.ProgressManager.push;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;
import static net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.FarCore;
import farcore.asm.LightFix;
import farcore.data.CT;
import farcore.data.Config;
import farcore.data.EnumFluid;
import farcore.data.EnumItem;
import farcore.data.EnumPhysicalDamageType;
import farcore.data.M;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.Potions;
import farcore.energy.electric.ElectricNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.BlockThermalDebug;
import farcore.energy.thermal.HeatWave;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreCapabilitiesHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreItemHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.handler.FarCoreSynchronizationHandler;
import farcore.handler.FarCoreWorldHandler;
import farcore.instances.TemperatureHandler;
import farcore.lib.block.BlockBase;
import farcore.lib.block.instance.BlockCarvedRock;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.block.instance.BlockFire;
import farcore.lib.block.instance.BlockIce;
import farcore.lib.block.instance.BlockModelDebug;
import farcore.lib.block.instance.BlockOre;
import farcore.lib.block.instance.BlockRedstoneCircuit;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.block.instance.BlockWater;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.fluid.FluidBase;
import farcore.lib.fluid.FluidWater;
import farcore.lib.item.ItemBase;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemDebugger;
import farcore.lib.item.instance.ItemFluidDisplay;
import farcore.lib.item.instance.ItemOreChip;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.item.instance.ItemStoneFragment;
import farcore.lib.item.instance.ItemSubCropRelated;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.lib.material.Mat;
import farcore.lib.net.PacketKey;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.net.gui.PacketFluidSlotClick;
import farcore.lib.net.gui.PacketFluidUpdateAll;
import farcore.lib.net.gui.PacketFluidUpdateSingle;
import farcore.lib.net.gui.PacketGuiTickUpdate;
import farcore.lib.net.tile.PacketChunkNetData;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.net.tile.PacketTEAskType;
import farcore.lib.net.tile.PacketTESAsk;
import farcore.lib.net.tile.PacketTESync;
import farcore.lib.net.tile.PacketTETypeResult;
import farcore.lib.net.world.PacketBreakBlock;
import farcore.lib.net.world.PacketCustomChunkData;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.tile.instance.TELossTile;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.util.CreativeTabBase;
import farcore.network.Network;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.relauncher.Side;

public class CommonLoader
{
	public void preload()
	{
		ProgressBar bar = push("Far Core Preload", 10);
		bar.step("Add Creative Tabs");
		if(Config.displayFluidInTab)
		{
			CT.tabFluids = new CreativeTabBase("farcore.fluids", "Fluids[FarCore]", () -> new ItemStack(Items.WATER_BUCKET));
		}
		CT.tabCropAndWildPlants = new CreativeTabBase("farcore.crop.plants", "Far Crop And Wild Plant", () -> new ItemStack(Items.WHEAT));
		CT.tabTree = new CreativeTabBase("farcore.tree", "Far Tree",
				() -> Config.createLog ? new ItemStack(M.oak.getProperty(MP.property_wood).block) : new ItemStack(Blocks.LOG));
		CT.tabTerria = new CreativeTabBase("farcore.terria", "Far Terria",
				() -> Config.createRock ? new ItemStack(M.peridotite.getProperty(MP.property_rock).block, 1, 2) : new ItemStack(Blocks.STONE));
		CT.tabBuilding = new CreativeTabBase("farcore.building", "Far Building Blocks",
				() -> Config.createRock ? new ItemStack(M.marble.getProperty(MP.property_rock).block, 1, 5) : new ItemStack(Blocks.STONEBRICK));
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
		registerForgeEventListener(new FarCoreKeyHandler());
		registerForgeEventListener(new FarCoreSynchronizationHandler());
		registerForgeEventListener(FarCoreEnergyHandler.getHandler());
		registerForgeEventListener(new FarCoreWorldHandler());
		registerForgeEventListener(new FarCoreItemHandler());
		registerForgeEventListener(new FarCoreCapabilitiesHandler());
		//Register energy nets.
		bar.step("Add Energy Nets");
		addNet(ThermalNet.instance);
		addNet(KineticNet.instance);
		addNet(ElectricNet.instance);
		//Register world objects.
		bar.step("Register World Objects");
		FarCoreWorldHandler.registerObject("heat.wave", HeatWave.class);
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
		new ItemFluidDisplay().setCreativeTab(CT.tabFluids);
		new BlockThermalDebug();
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
		registerTileEntity(TELossTile.class, "farcore.loss.tile");
		registerTileEntity(TECrop.class, "farcore.crop");
		registerTileEntity(TEOre.class, "farcore.ore");
		registerTileEntity(TECustomCarvedStone.class, "farcore.carved.stone");
		registerTileEntity(TESapling.class, "farcore.sapling");
		registerTileEntity(TECoreLeaves.class, "farcore.core.leaves");
		//Register entities.
		bar.step("Register Entities");
		int id = 0;
		registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id++, FarCore.ID, 32, 20, true);
		registerModEntity(EntityProjectileItem.class, "fle.projectile", id++, FarCore.ID, 32, 20, true);
		//Initialize potions and mob effects.
		bar.step("Add Potion Effects");
		Potions.init();
		pop(bar);
	}
	
	public void load()
	{
		ProgressBar bar = push("Far Core Load", 3);
		//Post load item and block.
		//For register to Ore Dictionary, Tool Uses, Compatibility, etc.
		bar.step("Post initalizing items and blocks");
		ItemBase.post();
		BlockBase.post();
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
		registerLocal("info.tool.head.name", "Tool Head : " + ChatFormatting.LIGHT_PURPLE + "%s");
		registerLocal("info.tool.handle.name", "Tool Handle : " + ChatFormatting.LIGHT_PURPLE + "%s");
		registerLocal("info.tool.tie.name", "Tool Tie : " + ChatFormatting.LIGHT_PURPLE + "%s");
		registerLocal("info.redstone.circuit.material", "Material : " + ChatFormatting.YELLOW + "%s");
		registerLocal("info.shift.click", ChatFormatting.WHITE + "Press " + ChatFormatting.ITALIC + "<%s>" + ChatFormatting.RESET + " to get more information.");
		registerLocal("info.food.label", ChatFormatting.RED + "Food Stat:");
		registerLocal("info.food.display", ChatFormatting.RED + "F-%s S-%s W-%s");
		registerLocal("info.tree.log.length", "Length : " + ChatFormatting.GREEN + "%d");
		registerLocal("info.fluidcontainer.contain", "Contain : " + ChatFormatting.AQUA + "%s");
		registerLocal("skill.upgrade.info", "The skill " + ChatFormatting.ITALIC + "%s" + ChatFormatting.RESET + " is upgrade from %d to %d level.");
		registerLocal("commands.date.usage", "/date");
		registerLocal("commands.date.arg.err", "Invalid command argument");
		registerLocal(EnumPhysicalDamageType.PUNCTURE.getTranslation(), ChatFormatting.GOLD + "Puncture");
		registerLocal(EnumPhysicalDamageType.SMASH.getTranslation(), ChatFormatting.GOLD + "Smash");
		registerLocal(EnumPhysicalDamageType.CUT.getTranslation(), ChatFormatting.GOLD + "Cut");
		//Setup network.
		bar.step("Setup network handler");
		final Network network = FarCore.network = Network.network(FarCore.ID);
		network.registerPacket(PacketEntity.class, Side.CLIENT);
		network.registerPacket(PacketEntityAsk.class, Side.SERVER);
		network.registerPacket(PacketKey.class, Side.SERVER);
		network.registerPacket(PacketTESync.class, Side.CLIENT);
		network.registerPacket(PacketTETypeResult.class, Side.CLIENT);
		network.registerPacket(PacketTESAsk.class, Side.CLIENT);
		network.registerPacket(PacketTEAsk.class, Side.SERVER);
		network.registerPacket(PacketTEAskType.class, Side.SERVER);
		network.registerPacket(PacketBreakBlock.class, Side.CLIENT);
		network.registerPacket(PacketFluidUpdateAll.class, Side.CLIENT);
		network.registerPacket(PacketFluidUpdateSingle.class, Side.CLIENT);
		network.registerPacket(PacketFluidSlotClick.class, Side.SERVER);
		network.registerPacket(PacketGuiTickUpdate.class, Side.SERVER);
		network.registerPacket(PacketCustomChunkData.class, Side.CLIENT);
		network.registerPacket(PacketChunkNetData.class, Side.CLIENT);
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
	}
	
	public void complete()
	{
		//Start light thread.
		if(Config.multiThreadLight)
		{
			LightFix.startThread();
		}
	}
	
	private static void registerForgeEventListener(Object object)
	{
		MinecraftForge.EVENT_BUS.register(object);
	}
}