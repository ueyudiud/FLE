package farcore.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.IItemInfo;
import farcore.interfaces.item.IItemProperty;
import farcore.interfaces.item.IItemInfo.ItemInfoMix;
import farcore.util.FleLog;
import farcore.util.V;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemSubBehaviorII extends ItemSubBehavior
{
	private Map<String, IItemIconInfo> infos = new HashMap();

	protected ItemSubBehaviorII(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemSubBehaviorII(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemSubBehaviorII(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
	}
	
	public void addSubItem(int id, String name, String local, IBehavior behavior, IItemProperty property, IItemIconInfo iconInfo)
	{
		addSubItem(id, name, local, new ItemInfoMix(behavior, property), iconInfo);
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, IItemIconInfo iconInfo)
	{
		super.addSubItem(id, name, local, itemInfo);
		infos.put(name, iconInfo);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		for(IItemIconInfo info : infos.values())
		{
			try 
			{
				info.registerIcons(register);
			}
			catch (Exception e)
			{
				
			}
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
		try
		{
			return infos.get(register.name(metadata)).getPasses();
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		try
		{
			return infos.get(register.name(getDamage(stack))).getColor(stack, pass);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return 0xFFFFFF;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		try
		{
			return infos.get(register.name(getDamage(stack))).getIcon(stack, pass);
		}
		catch(Throwable throwable)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().throwing(throwable);
			}
		}
		return V.voidItemIcon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack)
	{
		return getIcon(stack, 0);
	}
}