package farcore.alpha.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.IRegisteredNameable;
import farcore.alpha.interfaces.item.IHookedIconInfo;
import farcore.alpha.item.behavior.IBehavior;
import farcore.alpha.util.IconHook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemSubBehaviorII<T extends ItemSubBehavior> extends ItemSubBehavior<T>
{
	private static class HookIconInfoWrapper implements IHookedIconInfo, IRegisteredNameable
	{
		String name;
		IHookedIconInfo info;
		
		public HookIconInfoWrapper(String name, IHookedIconInfo info)
		{
			this.name = name;
			this.info = info;
		}

		@SideOnly(Side.CLIENT)
		public void registerIcons(INamedIconRegister register)
		{
			info.registerIcons(register);
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIcon(INamedIconRegister register, ItemStack stack, int pass)
		{
			return info.getIcon(register, stack, pass);
		}

		@SideOnly(Side.CLIENT)
		public int getColor(ItemStack stack, int pass)
		{
			return info.getColor(stack, pass);
		}

		@SideOnly(Side.CLIENT)
		public int getPasses()
		{
			return info.getPasses();
		}

		@Override
		public String getRegisteredName()
		{
			return name;
		}
	}
	
	private Map<Short, HookIconInfoWrapper> infos = new HashMap();

	protected ItemSubBehaviorII(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemSubBehaviorII(String modid, String unlocalized)
	{
		super(modid, unlocalized);
	}
	
	public void addSubItem(int id, String name, String local, IBehavior<T> itemInfo, IHookedIconInfo iconInfo)
	{
		super.addSubItem(id, name, local, itemInfo);
		infos.put(Short.valueOf((short) id), new HookIconInfoWrapper(name, iconInfo));
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(String string : register.names())
		{
			list.add(new ItemStack(item, 1, register.id(string)));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(INamedIconRegister register)
	{
		for(HookIconInfoWrapper iconInfo : infos.values())
		{
			IconHook.push(iconInfo);
			iconInfo.registerIcons(register);
			IconHook.pop();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata)
	{
		HookIconInfoWrapper iconInfo = infos.get(Short.valueOf((short) metadata));
		return iconInfo.getPasses();
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(INamedIconRegister register, ItemStack stack, int pass)
	{
		HookIconInfoWrapper iconInfo = infos.get(Short.valueOf((short) getDamage(stack)));
		return iconInfo.getIcon(register, stack, pass);
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		HookIconInfoWrapper iconInfo = infos.get(Short.valueOf((short) getDamage(stack)));
		return iconInfo.getColor(stack, pass);
	}
}