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
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.item.IPlaceableItem;
import fla.api.util.FlaValue;
import fla.api.util.Registry;
import fla.core.Fla;
import fla.core.FlaBlocks;
import fla.core.FlaCreativeTab;
import fla.core.FlaItems;

public class ItemSub extends ItemBase implements IPlaceableItem
{
	private static final Registry<SubItemTag> registry = new Registry<SubItemTag>();
	static
	{
		register(0, "flint_a", "stones/1", FlaCreativeTab.fla_old_stone_age_tab);
		register(1, "flint_b", "stones/2", FlaCreativeTab.fla_old_stone_age_tab);
		register(2, "flint_c", "stones/3", FlaCreativeTab.fla_old_stone_age_tab);
		register(10, "stone_a", "stones/201", FlaCreativeTab.fla_new_stone_age_tab);
		register(11, "stone_b", "stones/202", FlaCreativeTab.fla_new_stone_age_tab);
		register(31, "limestone_a", "stones/1001", FlaCreativeTab.fla_new_stone_age_tab);
		register(101, "bark_oak", "tree/1", FlaCreativeTab.fla_other_tab);
		register(102, "bark_spruce", "tree/2", FlaCreativeTab.fla_other_tab);
		register(103, "bark_birch", "tree/3", FlaCreativeTab.fla_other_tab);
		register(104, "bark_jungle", "tree/4", FlaCreativeTab.fla_other_tab);
		register(105, "bark_acacia", "tree/5", FlaCreativeTab.fla_other_tab);
		register(106, "bark_darkoak", "tree/6", FlaCreativeTab.fla_other_tab);
		register(121, "branch_oak", "tree/101", FlaCreativeTab.fla_other_tab);
		register(122, "branch_spruce", "tree/102", FlaCreativeTab.fla_other_tab);
		register(123, "branch_birch", "tree/103", FlaCreativeTab.fla_other_tab);
		register(124, "branch_jungle", "tree/104", FlaCreativeTab.fla_other_tab);
		register(125, "branch_acacia", "tree/105", FlaCreativeTab.fla_other_tab);
		register(126, "branch_darkoak", "tree/106", FlaCreativeTab.fla_other_tab);
		register(141, "seed_oak", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(Item.getItemFromBlock(Blocks.sapling).onItemUse(new ItemStack(Blocks.sapling, 1, 0), player, world, x, y, z, side, xPos, yPos, zPos))
				{
					stack.stackSize -= 1;
					return true;
				}
				return false;
			} 
		}.setTextureName("tree/201").setCreativeTab(FlaCreativeTab.fla_other_tab));
		register(142, "seed_spruce", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(Item.getItemFromBlock(Blocks.sapling).onItemUse(new ItemStack(Blocks.sapling, 1, 1), player, world, x, y, z, side, xPos, yPos, zPos))
				{
					stack.stackSize -= 1;
					return true;
				}
				return false;
			} 
		}.setTextureName("tree/202").setCreativeTab(FlaCreativeTab.fla_other_tab));
		register(143, "seed_birch", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(Item.getItemFromBlock(Blocks.sapling).onItemUse(new ItemStack(Blocks.sapling, 1, 2), player, world, x, y, z, side, xPos, yPos, zPos))
				{
					stack.stackSize -= 1;
					return true;
				}
				return false;
			} 
		}.setTextureName("tree/203").setCreativeTab(FlaCreativeTab.fla_other_tab));
		register(144, "seed_jungle", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(Item.getItemFromBlock(Blocks.sapling).onItemUse(new ItemStack(Blocks.sapling, 1, 3), player, world, x, y, z, side, xPos, yPos, zPos))
				{
					stack.stackSize -= 1;
					return true;
				}
				return false;
			} 
		}.setTextureName("tree/204").setCreativeTab(FlaCreativeTab.fla_other_tab));
		register(145, "seed_acacia", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(Item.getItemFromBlock(Blocks.sapling).onItemUse(new ItemStack(Blocks.sapling, 1, 4), player, world, x, y, z, side, xPos, yPos, zPos))
				{
					stack.stackSize -= 1;
					return true;
				}
				return false;
			} 
		}.setTextureName("tree/205").setCreativeTab(FlaCreativeTab.fla_other_tab));
		register(146, "seed_darkoak", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(Item.getItemFromBlock(Blocks.sapling).onItemUse(new ItemStack(Blocks.sapling, 1, 5), player, world, x, y, z, side, xPos, yPos, zPos))
				{
					stack.stackSize -= 1;
					return true;
				}
				return false;
			} 
		}.setTextureName("tree/206").setCreativeTab(FlaCreativeTab.fla_other_tab));
		register(151, "branch_bush", "tree/121", FlaCreativeTab.fla_other_tab);
		register(161, "leaves", "tree/231", FlaCreativeTab.fla_other_tab);
		register(401, "ramie_fiber", "crop/ramie_fiber", FlaCreativeTab.fla_new_stone_age_tab);
		register(1001, "leaves_dry", "tree/1001", FlaCreativeTab.fla_other_tab);
		register(1002, "tinder", "tree/1002", FlaCreativeTab.fla_other_tab);
		register(1003, "ramie_fiber_dry", "crop/ramie_fiber_dry", FlaCreativeTab.fla_new_stone_age_tab);
		register(1004, "ramie_rope", "crop/ramie_rope", FlaCreativeTab.fla_new_stone_age_tab);
		register(1005, "ramie_bundle_rope", "crop/ramie_bundle_rope", FlaCreativeTab.fla_new_stone_age_tab);
		register(1006, "charred_log", "tree/1003", FlaCreativeTab.fla_new_stone_age_tab);
		register(2001, "lipocere", "resource/1", FlaCreativeTab.fla_new_stone_age_tab);
		register(2002, "argil_ball", new SubItemTag()
		{
			public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos) 
			{
				if(stack.stackSize >= 6)
				{
					stack.stackSize -= 6;
					player.openGui(Fla.MODID, 102, world, x, y, z);
					return true;
				}
				return false;
			}
		}.setTextureName("resource/2").setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab));
		
		register(3001, "dust_limestone", "stones/11001", FlaCreativeTab.fla_new_stone_age_tab);
		register(5001, "argil_brick", "clay/101", FlaCreativeTab.fla_new_stone_age_tab);
		register(6002, "argil_plate", "clay/102", FlaCreativeTab.fla_new_stone_age_tab);
		
		register(10001, "flint_axe", "tools/head/flint/flint_axe", FlaCreativeTab.fla_old_stone_age_tab);
		register(10002, "flint_shovel", "tools/head/flint/flint_shovel", FlaCreativeTab.fla_old_stone_age_tab);
		register(10003, "flint_hammer", "tools/head/flint/flint_hammer", FlaCreativeTab.fla_old_stone_age_tab);
		register(10004, "flint_gaff", "tools/head/flint/flint_gaff", FlaCreativeTab.fla_old_stone_age_tab);
		register(10041, "stone_axe", "tools/head/stone/stone_axe", FlaCreativeTab.fla_new_stone_age_tab);
		register(10042, "stone_shovel", "tools/head/stone/stone_shovel", FlaCreativeTab.fla_new_stone_age_tab);
		register(10043, "stone_hammer", "tools/head/stone/stone_hammer", FlaCreativeTab.fla_new_stone_age_tab);
		register(10044, "stone_spear", "tools/head/stone/stone_spear", FlaCreativeTab.fla_new_stone_age_tab);
		register(10045, "stone_gaff", "tools/head/stone/stone_gaff", FlaCreativeTab.fla_new_stone_age_tab);
		register(10046, "stone_chisel", "tools/head/stone/stone_chisel", FlaCreativeTab.fla_new_stone_age_tab);
		register(10047, "stone_sickle", "tools/head/stone/stone_sickle", FlaCreativeTab.fla_new_stone_age_tab);
		register(10048, "stone_spade_hoe", "tools/head/stone/stone_spade_hoe", FlaCreativeTab.fla_new_stone_age_tab);
	}

	static void register(int id, String name, String textureName, CreativeTabs tab)
	{
		if(id >= Short.MAX_VALUE)
		{
			++id;
		}
		registry.register(id, new SubItemTag().setTextureName(textureName).setCreativeTab(tab), name);
	}
	static void register(int id, String name, String textureName)
	{
		if(id >= Short.MAX_VALUE)
		{
			++id;
		}
		registry.register(id, new SubItemTag().setTextureName(textureName).setCreativeTab(FlaCreativeTab.fla_other_tab), name);
	}
	static void register(int id, String name, SubItemTag tag)
	{
		if(id >= Short.MAX_VALUE)
		{
			++id;
		}
		registry.register(tag, name);
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
			if(tag.tab == tab)
			{
				int d = registry.serial(tag);
				ItemStack stack = new ItemStack(item, 1);
				setDamage(stack, d);
				list.add(stack);
			}
		}
	}

    public CreativeTabs[] getCreativeTabs()
    {
        return new CreativeTabs[]{ FlaCreativeTab.fla_other_tab, FlaCreativeTab.fla_old_stone_age_tab, FlaCreativeTab.fla_new_stone_age_tab,
        		FlaCreativeTab.fla_copper_age_tab, FlaCreativeTab.fla_bronze_age_tab, FlaCreativeTab.fla_iron_age_tab};
    }
	
	public static class SubItemTag
	{
		public String textureName;
		public CreativeTabs tab = FlaCreativeTab.fla_other_tab;
		
		public SubItemTag setCreativeTab(CreativeTabs tab)
		{
			this.tab = tab;
			return this;
		}
		
		public SubItemTag setTextureName(String name)
		{
			this.textureName = FlaValue.TEXT_FILE_NAME + ":" + name;
			return this;
		}
		
		public boolean onItemUse(ItemStack stack, EntityPlayer player,
				World world, int x, int y, int z, int side, float xPos, float yPos, float zPos)
		{
			return false;
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z,
			int side, float xPos, float yPos,
			float zPos) 
	{
		if(registry.get(stack.getItemDamage()).onItemUse(stack, player, world, x, y, z, side, xPos, yPos, zPos))
		{
			return true;
		}
		return super.onItemUse(stack, player, world, x, y, z, side, xPos, yPos, zPos);
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
		return setPlacedBlock(player.getCurrentEquippedItem());
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
	
	@Override
	public ItemStack setPlacedBlock(ItemStack stack) 
	{
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
}