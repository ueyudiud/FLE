/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items;

import java.util.List;

import com.google.common.collect.Maps;

import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import fle.core.FLE;
import fle.core.items.behavior.BehaviorArgilBall;
import fle.core.items.behavior.BehaviorBlockableTool;
import fle.core.items.behavior.BehaviorResearchItems1;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.item.IBehavior;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.item.ItemSubBehavior;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemMiscResources extends ItemSubBehavior implements IPolishableItem, IIP_Containerable
{
	public ItemMiscResources()
	{
		super(FLE.MODID, "misc_resource");
		initalize();
		EnumItem.misc_resource.set(this);
	}
	
	protected void initalize()
	{
		addSubItem(1, "flint_fragment", "Flint Fragment");
		addSubItem(2, "flint_sharp", "Sharp Flint Chip");
		addSubItem(3, "flint_small", "Small Flint");
		addSubItem(11, "quartz_large", "Quartz");
		addSubItem(12, "quartz_chip", "Quartz Chip");
		addSubItem(21, "opal", "Opal");
		
		addSubItem(1001, "vine_rope", "Vine Rope");
		addSubItem(1002, "dry_ramie_fiber", "Dried Ramie Fiber");
		addSubItem(1003, "hay", "Hay");
		addSubItem(1004, "dry_broadleaf", "Dried Broadleaf");
		addSubItem(1005, "dry_coniferous", "Dried Coniferous");
		addSubItem(1006, "tinder", "Tinder");
		
		addSubItem(2001, "ramie_rope", "Ramie Rope");
		addSubItem(2002, "ramie_rope_bundle", "Ramie Rope Bundle");
		addSubItem(2003, "crushed_bone", "Crushed Bone");
		addSubItem(2004, "defatted_crushed_bone", "Defatted Crushed Bone");
		addSubItem(2005, "argil_plate_unsmelted", "Unsmelted Argil Plate");
		addSubItem(2006, "argil_plate", "Argil Plate");
		addSubItem(2007, "argil_brick_unsmelted", "Unsmelted Argil Brick");
		addSubItem(2008, "argil_ball", "Argil Ball", new BehaviorArgilBall());
		
		addSubItem(3001, "researchitem1", "Research Item", new BehaviorResearchItems1());
		
		addSubItem(3101, "plant_ash_soap", "Plant Ash Soap");
		
		addSubItem(4001, "wooden_brick_mold", "Brick Mold");
		addSubItem(4002, "filled_wooden_brick_mold", "Brick Mold", new BehaviorBlockableTool(2));
		addSubItem(4003, "dried_filled_wooden_brick_mold", "Brick Mold");
		
		addSubItem(5001, "black_pepper_powder", "Black Pepper");
		addSubItem(5002, "prickly_ash_powder", "Prickly Ash");
		addSubItem(5003, "turmeric_powder", "Turmeric");
		addSubItem(5004, "ginger_slice", "Sliced Ginger");
		addSubItem(5005, "cinnamon_power", "Cinnamon");
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		MC.fragment.registerOre(M.flint, new ItemStack(this, 1, 1));
		MC.fragment.registerOre(M.quartz, new ItemStack(this, 1, 11));
		MC.chip_rock.registerOre(M.quartz, new ItemStack(this, 1, 12));
		MC.clayball.registerOre(M.argil, new ItemStack(this, 1, 2008));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "group/misc_resource"));
		NebulaModelLoader.registerItemMetaGenerator(getRegistryName(), stack -> this.nameMap.get(getBaseDamage(stack)));
		NebulaModelLoader.registerTextureSet(getRegistryName(), () -> Maps.asMap(this.idMap.keySet(), key -> new ResourceLocation(FLE.MODID, "items/group/misc_resource/" + key)));
	}
	
	@Override
	public int getPolishLevel(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
		case 3:
			return 8;
		case 12:
			return 11;
		default:
			return -1;
		}
	}
	
	@Override
	public char getPolishResult(ItemStack stack, char base)
	{
		switch (stack.getItemDamage())
		{
		case 3:
			return 'c';
		case 12:
			return 'c';
		default:
			return base;
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
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> behaviors = getBehavior(stack);
		for (IBehavior behavior : behaviors)
		{
			if (behavior instanceof IIP_Containerable) return ((IIP_Containerable) behavior).openContainer(world, pos, player, stack);
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		List<IBehavior> behaviors = getBehavior(stack);
		for (IBehavior behavior : behaviors)
		{
			if (behavior instanceof IIP_Containerable)
				return ((IIP_Containerable) behavior).openGui(world, pos, player, stack);
		}
		return null;
	}
}
