/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader;

import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import fle.api.recipes.instance.FlamableRecipes;
import fle.api.recipes.instance.FuelHandler;
import fle.core.tile.TEFirewood;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;

/**
 * @author ueyudiud
 */
public class Fuels
{
	public static void init()
	{
		FlamableRecipes.addFlamableItem(new BaseStack(Items.PAPER), 60F, 200F);
		FlamableRecipes.addFlameSource(coord -> {
			TileEntity tile = coord.getTE();
			if (tile instanceof TEFirewood)
			{
				return ((TEFirewood) tile).isBurning() ? 700 : ((TEFirewood) tile).isSmoldering() ? 550 : -1;
			}
			return -1;
		});
		
		FuelHandler.addFuel(new BaseStack(IBFS.iResources.getSubItem("dry_ramie_fiber")), 290F, 440F, 800, 32000);
		FuelHandler.addFuel(new BaseStack(IBFS.iResources.getSubItem("dry_broadleaf")), 300F, 440F, 800, 16000);
		FuelHandler.addFuel(new BaseStack(IBFS.iResources.getSubItem("dry_coniferous")), 310F, 440F, 800, 8000);
		FuelHandler.addFuel(new BaseStack(IBFS.iResources.getSubItem("tinder")), 250F, 480F, 1000, 30000);
		for (PropertyWood property : Mat.filtAndGet(SubTags.WOOD, MP.property_wood))
		{
			FuelHandler.addFuel(new OreStack(MC.log.getOreName(property.material)), 380F, 540F, 1600, (long) (24_0000 * property.burnHeat));
			FuelHandler.addFuel(new OreStack(MC.firewood.getOreName(property.material)), 350F, 540F, 2000, (long) (6_0000 * property.burnHeat));
		}
	}
}
