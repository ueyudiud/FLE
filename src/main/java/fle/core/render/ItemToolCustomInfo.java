package fle.core.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class ItemToolCustomInfo implements IItemIconInfo
{
	private final String locate;
	private final Set<String> set;
	private Map<String, IIcon> icons;
	
	public ItemToolCustomInfo(String locate, String...strings)
	{
		this.locate = locate;
		this.set = ImmutableSet.copyOf(strings);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		icons = new HashMap();
		for(String string : set)
		{
			icons.put(string, register.registerIcon(locate + "/" + string));
		}
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		try
		{
			return icons.getOrDefault(U.Inventorys.setupNBT(stack, false).getString("head"), V.voidItemIcon);
		}
		catch(Exception exception)
		{
			return V.voidItemIcon;
		}
	}

	@Override
	public int getColor(ItemStack stack, int pass)
	{
		return 0xFFFFFF;
	}

	@Override
	public int getPasses()
	{
		return 1;
	}
}