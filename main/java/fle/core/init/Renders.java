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
import fle.core.render.RenderCeramicFurnaceCrucible;
import fle.core.render.RenderColdForging;
import fle.core.render.RenderCrop;
import fle.core.render.RenderDitch;
import fle.core.render.RenderDryingTable;
import fle.core.render.RenderEmpty;
import fle.core.render.RenderFleArrow;
import fle.core.render.RenderHandler;
import fle.core.render.RenderOilLamp;
import fle.core.render.RenderOre;
import fle.core.render.RenderOreCobble;
import fle.core.render.RenderRock;
import fle.core.render.TESRArgilItems;
import fle.core.render.TESRColdForging;
import fle.core.render.TESRDryingTable;
import fle.core.te.TileEntityColdForgingPlatform;
import fle.core.te.TileEntityDryingTable;
import fle.core.te.argil.TileEntityArgilItems;

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
        RenderHandler.register(IB.stoneMachine1, 0, RenderCastingPool.class);
        RenderHandler.register(IB.stoneMachine1, 1, RenderCeramicFurnaceCrucible.class);
        RenderHandler.register(IB.stoneMachine1, 2, RenderColdForging.class);
        RenderHandler.register(IB.crop, OreDictionary.WILDCARD_VALUE, RenderCrop.class);
        RenderHandler.register(IB.ore_cobble, OreDictionary.WILDCARD_VALUE, RenderOreCobble.class);
        RenderHandler.register(IB.ditch, OreDictionary.WILDCARD_VALUE, RenderDitch.class);
        FleValue.FLE_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(false));
        FleValue.FLE_NOINV_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new RenderHandler(true));
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingTable.class, new TESRDryingTable());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArgilItems.class, new TESRArgilItems());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColdForgingPlatform.class, new TESRColdForging());
	    RenderingRegistry.registerEntityRenderingHandler(EntityFleArrow.class, new RenderFleArrow("arrow"));
	}
}
