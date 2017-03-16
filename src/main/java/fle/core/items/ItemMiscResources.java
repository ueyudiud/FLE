/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items;

import com.google.common.collect.Maps;

import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import fle.core.FLE;
import nebula.client.model.FlexibleItemSubmetaGetterLoader;
import nebula.client.model.FlexibleTextureSet;
import nebula.client.model.NebulaItemModelLoader;
import nebula.common.item.ItemSubBehavior;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemMiscResources extends ItemSubBehavior implements IPolishableItem
{
	public ItemMiscResources()
	{
		super(FLE.MODID, "misc_resource");
		initalize();
		EnumItem.misc_resource.set(this);
	}
	
	protected void initalize()
	{
		addSubItem(1, "flint_fragment", "Flint Fragment", null);
		addSubItem(2, "flint_sharp", "Sharp Flint Chip", null);
		addSubItem(3, "flint_small", "Small Flint", null);
		addSubItem(11, "quartz_large", "Quartz", null);
		addSubItem(12, "quartz_chip", "Quartz Chip", null);
		addSubItem(21, "opal", "Opal", null);
		
		addSubItem(1001, "vine_rope", "Vine Rope", null);
		addSubItem(1002, "dry_ramie_fiber", "Dried Ramie Fiber", null);
		addSubItem(1003, "hay", "Hay", null);
		addSubItem(1004, "dry_broadleaf", "Dried Broadleaf", null);
		addSubItem(1005, "dry_coniferous", "Dried Coniferous", null);
		
		addSubItem(2001, "ramie_rope", "Ramie Rope", null);
		addSubItem(2002, "ramie_rope_bundle", "Ramie Rope Bundle", null);
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		MC.fragment.registerOre(M.flint, new ItemStack(this, 1, 1));
		MC.fragment.registerOre(M.quartz, new ItemStack(this, 1, 11));
		MC.chip_rock.registerOre(M.quartz, new ItemStack(this, 1, 12));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaItemModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "group/misc_resource"));
		FlexibleTextureSet.registerTextureSetApplier(getRegistryName(), () -> Maps.asMap(this.idMap.keySet(), key -> new ResourceLocation(FLE.MODID, "items/group/misc_resource/" + key)));
		FlexibleItemSubmetaGetterLoader.registerSubmetaGetter(getRegistryName(), stack -> this.nameMap.get(getBaseDamage(stack)));
	}
	
	@Override
	public int getPolishLevel(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
		case 3 : return  8;
		case 12 : return 11;
		default: return -1;
		}
	}
	
	@Override
	public char getPolishResult(ItemStack stack, char base)
	{
		switch (stack.getItemDamage())
		{
		case 3 : return 'c';
		case 12 : return 'c';
		default: return base;
		}
	}
	
	@Override
	public void onPolished(EntityPlayer player, ItemStack stack)
	{
		if (player == null || !player.capabilities.isCreativeMode)
		{
			stack.stackSize--;
		}
	}
}