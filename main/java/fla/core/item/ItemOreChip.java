package fla.core.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.FlaAPI;
import fla.api.chem.OreInfo;
import fla.api.util.Registry;
import fla.core.Fla;
import fla.core.FlaCreativeTab;
import fla.core.FlaItems;
import fla.core.block.ore.BlockOre1;
import fla.core.tech.TechManager;

public class ItemOreChip extends ItemBase
{
	private static final Registry<OreInfo> registry = new Registry<OreInfo>();

	static
	{
		for(int i = 0; i < BlockOre1.oreInfo.length; ++i)
		{
			register(i, BlockOre1.oreInfo[i]);
		}
	}
	static void register(int id, OreInfo info)
	{
		if(id >= Short.MAX_VALUE)
		{
			++id;
		}
		registry.register(info, info.getOreName());
	}
	
	public static ItemStack a(String name)
	{
		return a(name, 1);
	}
	public static ItemStack a(String name, int size)
	{
		int meta = registry.serial(name);
		ItemStack ret = new ItemStack(FlaItems.subs, size, meta);
		FlaItems.subs.setDamage(ret, meta);
		return ret;
	}
	
	public ItemOreChip()
	{
		setHasSubtypes(true);
		customName = true;
		setCreativeTab(FlaCreativeTab.fla_other_tab);
	}
	
	@Override
	public String getUnlocalizedNameInefficiently(ItemStack itemstack) 
	{
		return FlaAPI.techManager.getPlayerInfo(Fla.fla.p.get().getPlayerInstance()).isPlayerKnowTech(TechManager.mineTire1) ? super.getUnlocalizedNameInefficiently(itemstack) : super.getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return super.getUnlocalizedName(stack) + ":" + registry.name(stack.getItemDamage());
	}
	
	@Override
	public void addInformation(ItemStack i, EntityPlayer e, List l, boolean u) 
	{
		if(FlaAPI.techManager.getPlayerInfo(e).isPlayerKnowTech(TechManager.mineTire1))
		{
			l.add(registry.get(i.getItemDamage()).getChemicalFormulaName());
		}
		else
		{
			l.add("?");
		}
	}
	
	private IIcon rockIcon;
	private IIcon ore1Icon;
	private IIcon ore2Icon;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) 
	{
		itemIcon = register.registerIcon(this.getIconString() + "_ore");
		ore1Icon = register.registerIcon(this.getIconString() + "_ore1");
		ore2Icon = register.registerIcon(this.getIconString() + "_ore2");
		rockIcon = register.registerIcon(this.getIconString() + "_rock");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() 
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int meta, int pass) 
	{
		return pass == 0 ? rockIcon : pass == 1 ? ore1Icon : ore2Icon;
	}
	
	@Override
	public int getRenderPasses(int metadata) 
	{
		return 3;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return pass == 0 ? 0xFFFFFF : registry.get(stack.getItemDamage()).getColor()[pass - 1];
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(OreInfo tag : registry)
		{
			int d = registry.serial(tag);
			ItemStack stack = new ItemStack(item, 1);
			setDamage(stack, d);
			list.add(stack);
		}
	}
}