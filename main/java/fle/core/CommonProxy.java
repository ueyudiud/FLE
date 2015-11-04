package fle.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.block.IGuiBlock;
import fle.api.cg.GuiBook;
import fle.api.cg.ICG;
import fle.api.gui.ContainerEmpty;
import fle.api.item.ItemFleMetaBase;
import fle.core.entity.EntityFleArrow;
import fle.core.entity.EntityFleFallingBlock;
import fle.core.gui.ContainerCeramics;
import fle.core.gui.ContainerItemBagable;
import fle.core.gui.ContainerWashing;
import fle.core.gui.GuiCeramics;
import fle.core.gui.GuiItemBagable;
import fle.core.gui.GuiWashing;
import fle.core.handler.AxeHandler;
import fle.core.handler.EntityHandler;
import fle.core.handler.PlayerHandler;
import fle.core.handler.RecipeHandler;
import fle.core.handler.WorldHandler;
import fle.core.init.Conditions;
import fle.core.init.Crops;
import fle.core.init.IB;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.init.Other;
import fle.core.init.Rs;
import fle.core.init.WorldCof;
import fle.core.te.TileEntityCrop;
import fle.core.te.TileEntityDitch;
import fle.core.te.TileEntityFirewood;
import fle.core.te.TileEntityOilLamp;
import fle.core.te.chest.TileEntityChest3By3;
import fle.core.tool.BowHandler;
import fle.core.tool.ChiselHandler;
import fle.core.tool.StoneHammerHandler;
import fle.core.util.FleFuelHandler;
import fle.core.world.FleWorldGen;

public class CommonProxy extends Proxy
{	
	@Override
	public void onPreload() 
	{
		Conditions.init();
		Materials.init();
		Crops.init();
		IB.init();
		Lang.init();
	}

	@Override
	public void onLoad() 
	{
		EntityRegistry.registerModEntity(EntityFleFallingBlock.class, "entityFleBlock", EntityRegistry.findGlobalUniqueEntityId(), FLE.MODID, 16, 1, true);
		EntityRegistry.registerModEntity(EntityFleArrow.class, "fleArrow", EntityRegistry.findGlobalUniqueEntityId(), FLE.MODID, 10, 1, true);
		FleAPI.registerFuelHandler(new FleFuelHandler());
		GameRegistry.registerWorldGenerator(new FleWorldGen(), 1);
		GameRegistry.registerTileEntity(TileEntityOilLamp.class, "oilLamp");
		GameRegistry.registerTileEntity(TileEntityFirewood.class, "firewood");
		GameRegistry.registerTileEntity(TileEntityCrop.class, "fleCrop");
		GameRegistry.registerTileEntity(TileEntityDitch.class, "Ditch");
		GameRegistry.registerTileEntity(TileEntityChest3By3.class, "Chest3x3");
	}

	@Override
	public void onPostload() 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(FLE.MODID, this);
		MinecraftForge.EVENT_BUS.register(new ChiselHandler());
		MinecraftForge.EVENT_BUS.register(new AxeHandler());
		MinecraftForge.EVENT_BUS.register(new StoneHammerHandler());
		MinecraftForge.EVENT_BUS.register(new BowHandler());
		MinecraftForge.EVENT_BUS.register(new RecipeHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHandler());
		MinecraftForge.EVENT_BUS.register(new WorldHandler());
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		FMLCommonHandler.instance().bus().register(new RecipeHandler());
		FMLCommonHandler.instance().bus().register(new PlayerHandler());
		FMLCommonHandler.instance().bus().register(new WorldHandler());
		FMLCommonHandler.instance().bus().register(new EntityHandler());
		WorldCof.init();
	}

	@Override
	public void onCompleteLoad() 
	{
		IB.reloadIB();
		Rs.reloadRecipe();
		Rs.init(FLE.fle.getConfig1());
		Crops.postInit();
		Materials.postInit();
		Other.init();
	}
	
	@SubscribeEvent
	public void checkCanHarvest(HarvestCheck evt)
	{
		if(evt.entityPlayer.getCurrentEquippedItem() != null)
		{
			ItemStack item = evt.entityPlayer.getCurrentEquippedItem();
			if(item.getItem() instanceof ItemFleMetaBase)
			{
				if(((ItemFleMetaBase) item.getItem()).canHarvestBlock(evt.entityPlayer, evt.block, evt.entityPlayer.getCurrentEquippedItem()))
					evt.success = true;
			}
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		switch(ID)
		{
		case -1 : return new ContainerWashing(player.inventory);
		case -2 : return new ContainerItemBagable(player.inventory, player.inventory.currentItem);
		case -3 : return new ContainerCeramics(world, x, y, z, player.inventory);
		case -4 : return new ContainerEmpty();
		}
		if(world.getBlock(x, y, z) instanceof IGuiBlock)
			return ((IGuiBlock) world.getBlock(x, y, z)).openContainer(world, x, y, z, player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		switch(ID)
		{
		case -1 : return new GuiWashing(player);
		case -2 : return new GuiItemBagable(player);
		case -3 : return new GuiCeramics(world, x, y, z, player);
		case -4 : return new GuiBook(((ICG) player.getCurrentEquippedItem().getItem()).getBookTab(player.getCurrentEquippedItem()));
		}
		if(world.getBlock(x, y, z) instanceof IGuiBlock)
			return ((IGuiBlock) world.getBlock(x, y, z)).openGui(world, x, y, z, player);
		return null;
	}
}