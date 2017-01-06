package farcore.load;

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
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreItemHandler;
import farcore.handler.FarCoreKeyHandler;
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
import farcore.lib.util.LanguageManager;
import farcore.network.Network;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonLoader
{
	public void preload()
	{
		ProgressBar bar = ProgressManager.push("Far Core Preload", 10);
		bar.step("Add Creative Tabs");
		if(Config.displayFluidInTab)
		{
			CT.tabFluids = new CreativeTabBase("farcore.fluids", "Fluids[FarCore]")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Items.WATER_BUCKET);
				}
			};
		}
		CT.tabCropAndWildPlants = new CreativeTabBase("farcore.crop.plants", "Far Crop And Wild Plant")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return new ItemStack(Items.WHEAT);
			}
		};
		CT.tabTree = new CreativeTabBase("farcore.tree", "Far Tree")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return Config.createLog ? new ItemStack(M.oak.getProperty(MP.property_wood).block) : new ItemStack(Blocks.LOG);
			}
		};
		CT.tabTerria = new CreativeTabBase("farcore.terria", "Far Terria")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return Config.createRock ? new ItemStack(M.peridotite.getProperty(MP.property_rock).block, 1, 2) : new ItemStack(Blocks.STONE);
			}
		};
		CT.tabBuilding = new CreativeTabBase("farcore.building", "Far Building Blocks")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return Config.createRock ? new ItemStack(M.marble.getProperty(MP.property_rock).block, 1, 5) : new ItemStack(Blocks.STONEBRICK);
			}
		};
		CT.tabResourceItem = new CreativeTabBase("farcore.resource.item", "Far Resource Item")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return Config.createRock ? new ItemStack(EnumItem.stone_chip.item, 1, M.peridotite.id) : new ItemStack(Items.GOLD_INGOT);
			}
		};
		CT.tabMachine = new CreativeTabBase("farcore.machine", "Far Machine")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return new ItemStack(Blocks.CRAFTING_TABLE);
			}
		};
		CT.tabMaterial = new CreativeTabBase("farcore.material", "Far Material")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return new ItemStack(Items.EMERALD);
			}
		};
		CT.tabTool = new CreativeTabBase("farcore.tool", "Far Tool")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return new ItemStack(EnumItem.debug.item);
			}
		};
		CT.tabRedstone = new CreativeTabBase("farcore.redstone", "Far Redstone")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return Config.createRock ? BlockRedstoneCircuit.createItemStack(1, M.stone) : new ItemStack(Items.COMPARATOR);
			}
		};
		//Register common handler.
		bar.step("Register Game Handlers");
		MinecraftForge.EVENT_BUS.register(new FarCoreKeyHandler());
		MinecraftForge.EVENT_BUS.register(FarCoreEnergyHandler.getHandler());
		MinecraftForge.EVENT_BUS.register(new FarCoreWorldHandler());
		MinecraftForge.EVENT_BUS.register(new FarCoreItemHandler());
		//Register energy nets.
		bar.step("Add Energy Nets");
		FarCoreEnergyHandler.addNet(ThermalNet.instance);
		FarCoreEnergyHandler.addNet(KineticNet.instance);
		FarCoreEnergyHandler.addNet(ElectricNet.instance);
		//Register world objects.
		bar.step("Register World Objects");
		FarCoreWorldHandler.registerObject("heat.wave", HeatWave.class);
		//Register local world handler.
		bar.step("Register Local World Handlers");
		ThermalNet.registerWorldThermalHandler(new TemperatureHandler());
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
		GameRegistry.registerTileEntity(TELossTile.class, "farcore.loss.tile");
		GameRegistry.registerTileEntity(TECrop.class, "farcore.crop");
		GameRegistry.registerTileEntity(TEOre.class, "farcore.ore");
		GameRegistry.registerTileEntity(TECustomCarvedStone.class, "farcore.carved.stone");
		GameRegistry.registerTileEntity(TESapling.class, "farcore.sapling");
		GameRegistry.registerTileEntity(TECoreLeaves.class, "farcore.core.leaves");
		//Register entities.
		bar.step("Register Entities");
		int id = 0;
		EntityRegistry.registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id++, FarCore.ID, 32, 20, true);
		EntityRegistry.registerModEntity(EntityProjectileItem.class, "fle.projectile", id++, FarCore.ID, 32, 20, true);
		//Initialize potions and mob effects.
		bar.step("Add Potion Effects");
		Potions.init();
		ProgressManager.pop(bar);
	}
	
	public void load()
	{
		//Post load item and block.
		//For register to Ore Dictionary, Tool Uses, Compatibility, etc.
		ItemBase.post();
		BlockBase.post();
		//Register languages.
		LanguageManager.registerLocal("info.debug.date", "Date : ");
		LanguageManager.registerLocal("info.log.length", "Legnth : %d");
		LanguageManager.registerLocal("info.slab.place", "Place slab in sneaking can let slab only has up or down facing.");
		LanguageManager.registerLocal("info.stone.chip.throwable", "You can throw it out to attack entities.");
		LanguageManager.registerLocal("info.crop.type", "Crop Name : %s");
		LanguageManager.registerLocal("info.crop.generation", "Generation : %d");
		LanguageManager.registerLocal("info.tool.damage", "Durability : " + ChatFormatting.GREEN + " %d / %d");
		LanguageManager.registerLocal("info.tool.harvest.level", "Harvest Level : " + ChatFormatting.YELLOW + " lv%d");
		LanguageManager.registerLocal("info.tool.hardness", "Hardness : " + ChatFormatting.BLUE + "%s");
		LanguageManager.registerLocal("info.tool.head.name", "Tool Head : " + ChatFormatting.LIGHT_PURPLE + "%s");
		LanguageManager.registerLocal("info.tool.handle.name", "Tool Handle : " + ChatFormatting.LIGHT_PURPLE + "%s");
		LanguageManager.registerLocal("info.tool.tie.name", "Tool Tie : " + ChatFormatting.LIGHT_PURPLE + "%s");
		LanguageManager.registerLocal("info.redstone.circuit.material", "Material : " + ChatFormatting.YELLOW + "%s");
		LanguageManager.registerLocal("info.shift.click", ChatFormatting.WHITE + "Press " + ChatFormatting.ITALIC + "<%s>" + ChatFormatting.RESET + " to get more information.");
		LanguageManager.registerLocal("info.food.label", ChatFormatting.RED + "Food Stat:");
		LanguageManager.registerLocal("info.food.display", ChatFormatting.RED + "F-%s S-%s W-%s");
		LanguageManager.registerLocal("info.tree.log.length", "Length : " + ChatFormatting.GREEN + "%d");
		LanguageManager.registerLocal("info.fluidcontainer.contain", "Contain : " + ChatFormatting.AQUA + "%s");
		LanguageManager.registerLocal("skill.upgrade.info", "The skill " + ChatFormatting.ITALIC + "%s" + ChatFormatting.RESET + " is upgrade from %d to %d level.");
		LanguageManager.registerLocal("commands.date.usage", "/date");
		LanguageManager.registerLocal("commands.date.arg.err", "Invalid command argument");
		LanguageManager.registerLocal(EnumPhysicalDamageType.PUNCTURE.getTranslation(), ChatFormatting.GOLD + "Puncture");
		LanguageManager.registerLocal(EnumPhysicalDamageType.SMASH.getTranslation(), ChatFormatting.GOLD + "Smash");
		LanguageManager.registerLocal(EnumPhysicalDamageType.CUT.getTranslation(), ChatFormatting.GOLD + "Cut");
		//Setup network.
		FarCore.network = Network.network(FarCore.ID);
		FarCore.network.registerPacket(PacketEntity.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketEntityAsk.class, Side.SERVER);
		FarCore.network.registerPacket(PacketKey.class, Side.SERVER);
		FarCore.network.registerPacket(PacketTESync.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketTETypeResult.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketTESAsk.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketTEAsk.class, Side.SERVER);
		FarCore.network.registerPacket(PacketTEAskType.class, Side.SERVER);
		FarCore.network.registerPacket(PacketBreakBlock.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketFluidUpdateAll.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketFluidUpdateSingle.class, Side.CLIENT);
		FarCore.network.registerPacket(PacketFluidSlotClick.class, Side.SERVER);
		FarCore.network.registerPacket(PacketGuiTickUpdate.class, Side.SERVER);
		FarCore.network.registerPacket(PacketCustomChunkData.class, Side.CLIENT);
	}
	
	public void postload()
	{
		//Reload material tool tips.
		for(Mat material : Mat.materials())
		{
			if(material.customDisplayInformation != null)
			{
				LanguageManager.registerLocal("info.material.custom." + material.name, material.customDisplayInformation);
			}
			if(material.chemicalFormula != null)
			{
				LanguageManager.registerLocal("info.material.chemical.formula." + material.name, material.chemicalFormula);
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
}