package fla.core.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fla.api.recipe.DropInfo;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.item.ItemSub;

public class WasherManager 
{
	private static final Map<String, DropInfo> dustList = new HashMap();

	static
	{
		Map<ItemStack, Integer> map = new HashMap();
		map.put(ItemSub.a("flint_a"), 8);
		map.put(ItemSub.a("flint_b"), 4);
		map.put(ItemSub.a("flint_c"), 1);
		map.put(new ItemStack(Items.flint), 3);
		registryDust(new DropInfo(new ItemChecker(Blocks.gravel), map));
	}
	
	public static void registryDust(DropInfo info)
	{
		dustList.put(info.toString(), info);
	}
	
	@SubscribeEvent
	public void onWashItem(PlaceEvent evt)
	{
		if(evt.player.isSneaking() && Fla.fla.km.get().isPlaceKeyDown(evt.player))
		{
			int waterCount = 0;
			for(int x1 = -4; x1 < 5; ++x1)
				for(int y1 = 0; y1 > -5; --y1)
					for(int z1 = -4; z1 < 5; ++z1)
					{
						BlockPos pos = new BlockPos(evt.world, evt.x + x1, evt.y + y1, evt.z + z1);
						if(pos.getBlock() == Blocks.water) ++waterCount;
						if(pos.getBiome() == BiomeGenBase.ocean || pos.getBiome() == BiomeGenBase.river) ++waterCount;
					}
			if(waterCount > 80)
			{
				evt.player.openGui(Fla.fla.MODID, 101, evt.world, evt.x, evt.y, evt.z);
				evt.setCanceled(true);	
			}
		}
	}
	
	public static String getRecipeName(ItemStack input)
	{
		Iterator<String> itr = dustList.keySet().iterator();
		while (itr.hasNext()) 
		{
			String str = itr.next();
			if(dustList.get(str).d.match(input))
			{
				return str;
			}
		}
		return null;
	}
	
	public static ItemStack[] outputRecipe(String recipe)
	{
		DropInfo info = dustList.get(recipe);
		List<ItemStack> list = info.getDustDrop();
		return list.toArray(new ItemStack[list.size()]);
	}
}