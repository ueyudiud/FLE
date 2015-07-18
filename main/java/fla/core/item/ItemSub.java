package fla.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.item.IPlaceableItem;
import fla.api.util.FlaValue;
import fla.api.util.Registry;
import fla.core.FlaBlocks;
import fla.core.FlaItems;

public class ItemSub extends ItemBase implements IPlaceableItem
{
	private static final Registry<SubItemTag> registry = new Registry<SubItemTag>();
	static
	{
		register(0, "flint_a", "stones/1");
		register(1, "flint_b", "stones/2");
		register(2, "flint_c", "stones/3");
		register(10, "stone_a", "stones/201");
		register(11, "stone_b", "stones/202");
		register(31, "limestone_a", "stones/1001");
		register(101, "bark_oak", "tree/1");
		register(102, "bark_spruce", "tree/2");
		register(103, "bark_birch", "tree/3");
		register(104, "bark_jungle", "tree/4");
		register(105, "bark_acacia", "tree/5");
		register(106, "bark_darkoak", "tree/6");
		register(121, "branch_oak", "tree/101");
		register(122, "branch_spruce", "tree/102");
		register(123, "branch_birch", "tree/103");
		register(124, "branch_jungle", "tree/104");
		register(125, "branch_acacia", "tree/105");
		register(126, "branch_darkoak", "tree/106");
		register(141, "seed_oak", "tree/201");
		register(142, "seed_spruce", "tree/202");
		register(143, "seed_birch", "tree/203");
		register(144, "seed_jungle", "tree/204");
		register(145, "seed_acacia", "tree/205");
		register(146, "seed_darkoak", "tree/206");
		register(151, "branch_bush", "tree/121");
		register(161, "leaves", "tree/231");
		register(401, "ramie_fiber", "crop/ramie_fiber");
		register(1001, "leaves_dry", "tree/1001");
		register(1002, "tinder", "tree/1002");
		register(1003, "ramie_fiber_dry", "crop/ramie_fiber_dry");
		register(1004, "ramie_rope", "crop/ramie_rope");
		register(1005, "ramie_bundle_rope", "crop/ramie_bundle_rope");
		register(2001, "lipocere", "resource/1");

		register(3001, "dust_limestone", "stones/11001");
		
		register(10001, "flint_axe", "tools/head/flint/flint_axe");
		register(10002, "flint_shovel", "tools/head/flint/flint_shovel");
		register(10003, "flint_hammer", "tools/head/flint/flint_hammer");
		register(10004, "flint_gaff", "tools/head/flint/flint_gaff");
		register(10041, "stone_axe", "tools/head/stone/stone_axe");
		register(10042, "stone_shovel", "tools/head/stone/stone_shovel");
		register(10043, "stone_hammer", "tools/head/stone/stone_hammer");
		register(10044, "stone_spear", "tools/head/stone/stone_spear");
		register(10045, "stone_gaff", "tools/head/stone/stone_gaff");
		register(10046, "stone_chisel", "tools/head/stone/stone_chisel");
		register(10047, "stone_sickle", "tools/head/stone/stone_sickle");
		register(10048, "stone_spade_hoe", "tools/head/stone/stone_spade_hoe");
	}

	static void register(int id, String name, String textureName)
	{
		if(id >= Short.MAX_VALUE)
		{
			++id;
		}
		registry.register(id, new SubItemTag().setTextureName(textureName), name);
	}
	static void register(String name, String textureName)
	{
		registry.register(new SubItemTag().setTextureName(textureName), name);
	}

	public static ItemStack a(String name)
	{
		return a(name, 1);
	}
	public static ItemStack a(String name, int size)
	{
		int meta = FlaItems.subs.registry.serial(name);
		ItemStack ret = new ItemStack(FlaItems.subs, size, meta);
		FlaItems.subs.setDamage(ret, meta);
		return ret;
	}
	
	private Map<String, IIcon> iconMap = new HashMap();
	
	public ItemSub()
	{
		this.setHasSubtypes(true);
		this.customName = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return super.getUnlocalizedName(stack) + ":" + registry.name(stack.getItemDamage());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) 
	{
		this.icons = new IIcon[registry.size()];
		for(SubItemTag tag : registry)
		{
			iconMap.put(registry.name(tag), register.registerIcon(tag.textureName));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage)
	{
		return iconMap.get(registry.name(damage));
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(SubItemTag tag : registry)
		{
			int d = registry.serial(tag);
			ItemStack stack = new ItemStack(item, 1);
			setDamage(stack, d);
			list.add(stack);
		}
	}
	
	public static class SubItemTag
	{
		public String textureName;
		
		public SubItemTag setTextureName(String name)
		{
			this.textureName = FlaValue.TEXT_FILE_NAME + ":" + name;
			return this;
		}
	}

	@Override
	public int canItemPlace(ItemStack stack) 
	{
		if(a("flint_a").isItemEqual(stack) && stack.stackSize >= 4)
		{
			return 4;
		}
		if(a("stone_a").isItemEqual(stack))
		{
			return 9;
		}
		return 0;
	}
	
	@Override
	public ItemStack setPlacedBlock(EntityPlayer player) 
	{
		ItemStack stack = player.getCurrentEquippedItem();
		if(a("flint_a").isItemEqual(stack))
		{
			return new ItemStack(FlaBlocks.cobble1, 1, 0);
		}
		if(a("stone_a").isItemEqual(stack))
		{
			return new ItemStack(Blocks.cobblestone, 1, 0);
		}
		return null;
	}
	
	@Override
	public int getDamage(ItemStack stack) 
	{
		setupNBT(stack);
		int d = stack.getTagCompound().getInteger("FlaDamage");
		return d;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage) 
	{
		setupNBT(stack);
		stack.getTagCompound().setInteger("FlaDamage", damage);
		super.setDamage(stack, damage);
	}
}