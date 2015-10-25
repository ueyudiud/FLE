package fle.core.init;

import java.lang.reflect.ParameterizedType;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.core.entity.EntityFleArrow;
import fle.core.render.RenderArgil;
import fle.core.render.RenderAsh;
import fle.core.render.RenderCastingPool;
import fle.core.render.RenderCeramicFurnaceCrucible;
import fle.core.render.RenderColdForging;
import fle.core.render.RenderCrop;
import fle.core.render.RenderDitch;
import fle.core.render.RenderDryingTable;
import fle.core.render.RenderEmpty;
import fle.core.render.RenderFleArrow;
import fle.core.render.RenderHandler;
import fle.core.render.RenderLeverOilMill;
import fle.core.render.RenderMultiTank;
import fle.core.render.RenderOilLamp;
import fle.core.render.RenderOre;
import fle.core.render.RenderOreCobble;
import fle.core.render.RenderRock;
import fle.core.render.RenderSifter;
import fle.core.render.RenderStoneMill;
import fle.core.render.RenderWoodenFrame;
import fle.core.render.TESRArgilItems;
import fle.core.render.TESRBase;
import fle.core.render.TESRColdForging;
import fle.core.render.TESRDryingTable;
import fle.core.render.TESRPolishTable;
import fle.core.render.TESRStoneMill;

public class Renders
{
	@SideOnly(Side.CLIENT)
	public static void init()
	{
		RenderHandler.register(IB.rock, OreDictionary.WILDCARD_VALUE, RenderRock.class);
        RenderHandler.register(IB.ore, OreDictionary.WILDCARD_VALUE, RenderOre.class);
        RenderHandler.register(IB.oilLamp, OreDictionary.WILDCARD_VALUE, RenderOilLamp.class);
        RenderHandler.register(IB.ash, OreDictionary.WILDCARD_VALUE, RenderAsh.class);
        RenderHandler.register(IB.argil_unsmelted, 0, RenderArgil.class);
        RenderHandler.register(IB.argil_unsmelted, 1, RenderEmpty.class);
        RenderHandler.register(IB.argil_smelted, 0, RenderArgil.class);
        RenderHandler.register(IB.woodMachine1, 0, RenderDryingTable.class);
        RenderHandler.register(IB.woodMachine1, 1, RenderWoodenFrame.class);
        RenderHandler.register(IB.woodMachine1, 2, RenderStoneMill.class);
        RenderHandler.register(IB.woodMachine1, 3, RenderSifter.class);
        RenderHandler.register(IB.woodMachine1, 4, RenderLeverOilMill.class);
        RenderHandler.register(IB.stoneMachine1, 0, RenderCastingPool.class);
        RenderHandler.register(IB.stoneMachine1, 1, RenderCeramicFurnaceCrucible.class);
        RenderHandler.register(IB.stoneMachine1, 2, RenderColdForging.class);
        RenderHandler.register(IB.crop, OreDictionary.WILDCARD_VALUE, RenderCrop.class);
        RenderHandler.register(IB.ore_cobble, OreDictionary.WILDCARD_VALUE, RenderOreCobble.class);
        RenderHandler.register(IB.ditch, OreDictionary.WILDCARD_VALUE, RenderDitch.class);
        RenderHandler.register(IB.tank, OreDictionary.WILDCARD_VALUE, RenderMultiTank.class);
        FleValue.FLE_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(false));
        FleValue.FLE_NOINV_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(true));
    	registerTESR(TESRDryingTable.class);
    	registerTESR(TESRArgilItems.class);
    	registerTESR(TESRColdForging.class);
    	registerTESR(TESRPolishTable.class);
    	registerTESR(TESRStoneMill.class);
	    RenderingRegistry.registerEntityRenderingHandler(EntityFleArrow.class, new RenderFleArrow("arrow"));
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