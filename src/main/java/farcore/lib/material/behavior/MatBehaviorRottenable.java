/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.material.behavior;

import farcore.energy.thermal.ThermalNet;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.util.UnlocalizedList;
import nebula.common.environment.IEnvironment;
import nebula.common.util.ItemStacks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class MatBehaviorRottenable implements IItemMatProp
{
	private static final int	FRESH	= 0;
	private static final int	ROTTEN	= 1;
	
	@Override
	public int getOffsetMetaCount()
	{
		return 2;
	}
	
	@Override
	public String getReplacedLocalName(int metaOffset, Mat material)
	{
		switch (metaOffset)
		{
		case ROTTEN:
			return "Rotten " + material.localName;
		default:
			return material.localName;
		}
	}
	
	@Override
	public void setInstanceFromMeta(ItemStack stack, int metaOffset, Mat material, MatCondition condition, String saveTag)
	{
		switch (metaOffset)
		{
		case ROTTEN:
			ItemStacks.getSubOrSetupNBT(stack, saveTag, true).setBoolean("rotten", true);
			break;
		case FRESH:
		default:
			break;
		}
	}
	
	@Override
	public int getMetaOffset(ItemStack stack, Mat material, MatCondition condition, String saveTag)
	{
		return ItemStacks.getSubOrSetupNBT(stack, saveTag, false).getBoolean("rotten") ? 1 : 0;
	}
	
	@Override
	public ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition, IEnvironment environment, String saveTag)
	{
		NBTTagCompound nbt = ItemStacks.getSubOrSetupNBT(stack, saveTag, false);
		if (!nbt.getBoolean("rotten"))
		{
			int progress = nbt.getInteger("progress");
			int max = material.getProperty("quality_period");
			float temperature = ThermalNet.getTemperature(environment) - material.getProperty("rotten_temperature");
			int speed = (int) (Math.exp(-temperature * temperature) * condition.specificArea);
			if (speed > 0)
			{
				if (progress + speed >= max)
				{
					nbt.setBoolean("rotten", true);
					nbt.removeTag("progress");
				}
				else
				{
					nbt.setInteger("progress", progress + speed);
				}
			}
		}
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list, String saveTag)
	{
		NBTTagCompound nbt = ItemStacks.getSubOrSetupNBT(stack, saveTag, false);
		if (nbt.getBoolean("rotten"))
		{
			list.add("info.material.behavior.rottenable.rotten");
		}
		else
		{
			float progress = (float) nbt.getInteger("progress") / (float) material.getProperty("quality_period");
			list.add("info.material.behavior.rottenable.progress", nebula.common.util.Strings.progress(progress));
		}
	}
	
	@Override
	public float entityAttackDamageMultiple(ItemStack stack, Mat material, Entity target, String saveTag)
	{
		return 1.0F;
	}
}
