package farcore.lib.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For item tool tip,
 * auto transfer unlocalized string to localized string.
 * @author ueyudiud
 *
 */
public class UnlocalizedList
{
	List<String> list;

	public UnlocalizedList()
	{
		this(new ArrayList());
	}
	public UnlocalizedList(List<String> list)
	{
		this.list = list;
	}

	public void add(String arg, Object...translation)
	{
		list.add(LanguageManager.translateToLocal(arg, translation));
	}

	public void addNotNull(String arg, Object...translation)
	{
		String val = LanguageManager.translateToLocalWithIgnoreUnmapping(arg, translation);
		if(val != null)
		{
			list.add(val);
		}
	}
	
	/**
	 * For add tooltip for displaying. It can put
	 * more than one line for localized translation.<p>
	 * For example:<br>
	 * <tt>a.tooltip=XY</tt><br>
	 * will display<br>
	 * <tt>XY</tt><br>
	 * <tt>a.tooltip.1=X<br>
	 * a.tooltip.2=Y</tt><br>
	 * will display<br>
	 * <tt>X<br>Y</tt>
	 * @param arg
	 */
	public void addToolTip(String arg)
	{
		String val = LanguageManager.translateToLocalWithIgnoreUnmapping(arg);
		if(val != null)
		{
			list.add(val);
		}
		else
		{
			int i = 1;
			while((val = LanguageManager.translateToLocalWithIgnoreUnmapping(arg + "." + i)) != null)
			{
				list.add(val);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean isSneakDown()
	{
		return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
	}

	@SideOnly(Side.CLIENT)
	public void addShiftClickInfo()
	{
		add("info.shift.click", Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName());
	}

	public void addLocal(String arg)
	{
		list.add(arg);
	}

	public List<String> list()
	{
		return list;
	}
}