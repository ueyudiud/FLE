package fle.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import farcore.entity.EntityFleFallingBlock;
import fle.core.handler.ItemEventHandler;
import fle.core.handler.PlayerEventHandler;
import fle.core.handler.WorldEventHandler;
import fle.core.init.Entities;
import fle.core.init.IBF;
import fle.core.init.Lang;
import fle.core.init.Rs;
import fle.core.init.S;
import fle.core.init.WorldCof;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy extends Proxy
{	
	@Override
	public void onPreload() 
	{
		S.init();
//		MinecraftForge.EVENT_BUS.register(new FleMatter());
//		Conditions.init();
//		Materials.init();
//		Plants.init();
//		FlePotionEffect.init();
		Lang.init();
		S.postInit();
		Entities.init();
		IBF.init();
	}

	@Override
	public void onLoad() 
	{
//		EntityRegistry.registerModEntity(EntityFleArrow.class, "fleArrow", EntityRegistry.findGlobalUniqueEntityId(), FLE.MODID, 10, 1, true);
//		FleAPI.registerFuelHandler(new FuelHandler());
//		GameRegistry.registerWorldGenerator(new FleWorldGen(), 1);
//		GameRegistry.registerTileEntity(TileEntityOilLamp.class, "oilLamp");
//		GameRegistry.registerTileEntity(TileEntityFirewood.class, "firewood");
//		GameRegistry.registerTileEntity(TileEntityCrop.class, "fleCrop");
//		GameRegistry.registerTileEntity(TileEntityDitch.class, "Ditch");
//		GameRegistry.registerTileEntity(TileEntityChest3By3.class, "Chest3x3");
	}

	@Override
	public void onPostload() 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(FLE.MODID, this);
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
		MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		FMLCommonHandler.instance().bus().register(new WorldEventHandler());
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		WorldCof.init();
	}

	@Override
	public void onCompleteLoad() 
	{
		IBF.reloadIB();
		Rs.reloadRecipe();
//		Plants.postInit();
		Rs.init();
//		Materials.postInit();
		Rs.completeInit();
//		Other.init();
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
//		switch(ID)
//		{
//		case -1 : return new ContainerWashing(player.inventory);
//		case -2 : return new ContainerItemBagable(player.inventory, player.inventory.currentItem);
//		case -3 : return new ContainerCeramics(world, x, y, z, player.inventory);
//		//case -4 : return new ContainerEmpty();
//		case -5 : return new ContainerPlayerCrafting(player.inventory);
//		}
//		if(world.getBlock(x, y, z) instanceof IGuiBlock)
//			return ((IGuiBlock) world.getBlock(x, y, z)).openContainer(world, x, y, z, player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
//		switch(ID)
//		{
//		case -1 : return new GuiWashing(player);
//		case -2 : return new GuiItemBagable(player);
//		case -3 : return new GuiCeramics(world, x, y, z, player);
//		case -4 : return new GuiBook(((ICG) player.getCurrentEquippedItem().getItem()).getBookTab(player.getCurrentEquippedItem()));
//		case -5 : return new GuiPlayerCrafting(player);
//		}
//		if(world.getBlock(x, y, z) instanceof IGuiBlock)
//			return ((IGuiBlock) world.getBlock(x, y, z)).openGui(world, x, y, z, player);
		return null;
	}
}