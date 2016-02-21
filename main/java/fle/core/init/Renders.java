package fle.core.init;

import java.lang.reflect.ParameterizedType;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.util.FleValue;
import fle.core.render.RenderCrop;
import fle.core.render.RenderDitch;
import fle.core.render.RenderHandler;
import fle.core.render.TESRBase;
import fle.core.render.TESRDitch;
import fle.energy.block.RenderThermalWire;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class Renders
{
	@SideOnly(Side.CLIENT)
	public static void init()
	{
        RenderHandler.instance = new RenderHandler(false);
//        RenderHandler.register(IB.ore, OreDictionary.WILDCARD_VALUE, RenderOre.class);
//        RenderHandler.register(IB.oilLamp, OreDictionary.WILDCARD_VALUE, RenderOilLamp.class);
//        RenderHandler.register(IB.ash, OreDictionary.WILDCARD_VALUE, RenderAsh.class);
//        RenderHandler.register(IB.argil_unsmelted, 0, RenderArgil.class);
//        RenderHandler.register(IB.argil_unsmelted, 1, RenderEmpty.class);
//        RenderHandler.register(IB.argil_smelted, 0, RenderArgil.class);
//        RenderHandler.register(IB.woodMachine1, 0, RenderDryingTable.class);
//        RenderHandler.register(IB.woodMachine1, 1, RenderWoodenFrame.class);
//        RenderHandler.register(IB.woodMachine1, 2, RenderStoneMill.class);
//        RenderHandler.register(IB.woodMachine1, 3, RenderSifter.class);
//        RenderHandler.register(IB.woodMachine1, 4, RenderLeverOilMill.class);
//        RenderHandler.register(IB.stoneMachine1, 0, RenderCastingPool.class);
//        RenderHandler.register(IB.stoneMachine1, 1, RenderCeramicFurnaceCrucible.class);
//        RenderHandler.register(IB.stoneMachine1, 2, RenderColdForging.class);
        RenderHandler.register(IB.crop, OreDictionary.WILDCARD_VALUE, RenderCrop.class);
//        RenderHandler.register(IB.plant, OreDictionary.WILDCARD_VALUE, RenderPlants.class);
//        RenderHandler.register(IB.ore_cobble, OreDictionary.WILDCARD_VALUE, RenderOreCobble.class);
        RenderHandler.register(IB.ditch, OreDictionary.WILDCARD_VALUE, RenderDitch.class);
//        RenderHandler.register(IB.tank, OreDictionary.WILDCARD_VALUE, RenderMultiTank.class);
        RenderHandler.register(IB.thermalWire, OreDictionary.WILDCARD_VALUE, RenderThermalWire.class);
//        RenderHandler.register(Item.getItemFromBlock(IB.thermalWire), OreDictionary.WILDCARD_VALUE, RIThermalWire.class);
//        RenderHandler.register(IB.plant_rattan, OreDictionary.WILDCARD_VALUE, RenderRattan.class);
        FleValue.FLE_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(false));
        FleValue.FLE_NOINV_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(true));
//    	registerTESR(TESRDryingTable.class);
//    	registerTESR(TESRArgilItems.class);
//    	registerTESR(TESRPlacedItem.class);
//    	registerTESR(TESRColdForging.class);
//    	registerTESR(TESRPolishTable.class);
//    	registerTESR(TESRStoneMill.class);
//    	registerTESR(TESRMT.class);
    	registerTESR(TESRDitch.class);
//    	registerTESR(TESRTD.class);
	    //RenderingRegistry.registerEntityRenderingHandler(EntityFleArrow.class, new RenderFleArrow("arrow"));
//    	RenderingRegistry.registerEntityRenderingHandler(EntityFleFallingBlock.class, new EntityRenderFallingBlock());
	}
	
	public static void registerTESR(Class<? extends TESRBase> tesr)
	{
		try
		{
			ParameterizedType type = (ParameterizedType) tesr.getGenericSuperclass();
			Class<?> type1 = (Class) type.getActualTypeArguments()[0];
			if(type1.isAssignableFrom(type1))
			{
				ClientRegistry.bindTileEntitySpecialRenderer((Class<? extends TileEntity>) type1, tesr.newInstance());
			}
			else throw new RuntimeException("FLE find a class not extends TileEntity!");
		}
		catch(Throwable e)
		{
			new RuntimeException("FLE fail to register a TESR.", e).printStackTrace();
		}
	}
}