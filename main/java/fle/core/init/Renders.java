package fle.core.init;

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
import fle.core.render.RenderCrop;
import fle.core.render.RenderDryingTable;
import fle.core.render.RenderFleArrow;
import fle.core.render.RenderHandler;
import fle.core.render.RenderOilLamp;
import fle.core.render.RenderOre;
import fle.core.render.RenderOreCobble;
import fle.core.render.RenderRock;
import fle.core.render.TESRDryingTable;
import fle.core.te.TileEntityDryingTable;

public class Renders
{
	@SideOnly(Side.CLIENT)
	public static void init()
	{
		RenderHandler.register(IB.rock, OreDictionary.WILDCARD_VALUE, RenderRock.class);
        RenderHandler.register(IB.ore, OreDictionary.WILDCARD_VALUE, RenderOre.class);
        RenderHandler.register(IB.oilLamp, OreDictionary.WILDCARD_VALUE, RenderOilLamp.class);
        RenderHandler.register(IB.ash, OreDictionary.WILDCARD_VALUE, RenderAsh.class);
        RenderHandler.register(IB.argil_unsmelted, OreDictionary.WILDCARD_VALUE, RenderArgil.class);
        RenderHandler.register(IB.argil_smelted, OreDictionary.WILDCARD_VALUE, RenderArgil.class);
        RenderHandler.register(IB.woodMachine1, 0, RenderDryingTable.class);
        RenderHandler.register(IB.stoneMachine1, 0, RenderCastingPool.class);
        RenderHandler.register(IB.crop, OreDictionary.WILDCARD_VALUE, RenderCrop.class);
        RenderHandler.register(IB.ore_cobble, OreDictionary.WILDCARD_VALUE, RenderOreCobble.class);
        FleValue.FLE_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(false));
        FleValue.FLE_NOINV_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(true));
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingTable.class, new TESRDryingTable());
	    RenderingRegistry.registerEntityRenderingHandler(EntityFleArrow.class, new RenderFleArrow("arrow"));
	}
}
