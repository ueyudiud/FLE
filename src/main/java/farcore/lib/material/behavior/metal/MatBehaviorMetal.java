/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.material.behavior.metal;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.material.behavior.MatBehaviorTemperature;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.util.EnumChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class MatBehaviorMetal<T extends MatBehaviorMetal.Metal> extends MatBehaviorTemperature<T, NBTTagCompound>
{
	public int boilHeat = Integer.MAX_VALUE;
	public int meltHeat = Integer.MAX_VALUE;
	public int woughtHeat = Integer.MAX_VALUE;
	public int workHeat = Integer.MAX_VALUE;
	
	public static class Metal
	{
		protected Mat material;
		protected int heat;
		
		public Metal(Mat material)
		{
			this.material = material;
		}
		public Metal(Mat material, int heat)
		{
			this.material = material;
			this.heat = heat;
		}
		
		public Metal copy()
		{
			return new Metal(this.material, this.heat);
		}
	}
	
	@Override
	public int getOffsetMetaCount()
	{
		return 1;
	}
	
	@Override
	public String getReplacedLocalName(int metaOffset, Mat material)
	{
		return material.localName;
	}
	
	@Override
	public T instance(int metaOffset, Mat material)
	{
		return (T) new Metal(material);
	}
	
	@Override
	public int getMetaOffset(T value, Mat material)
	{
		return 0;
	}
	
	@Override
	public T copyOf(T source, Mat material)
	{
		return source == null ? null : (T) source.copy();
	}
	
	static
	{
		LanguageManager.registerLocal("info.material.behavior.metal.boil.warn", EnumChatFormatting.RED + "To Boil");
		LanguageManager.registerLocal("info.material.behavior.metal.melt", EnumChatFormatting.WHITE + "Melt");
		LanguageManager.registerLocal("info.material.behavior.metal.melt.warn", EnumChatFormatting.YELLOW + "To Melt");
		LanguageManager.registerLocal("info.material.behavior.metal.can.work", EnumChatFormatting.WHITE + "Can work");
		LanguageManager.registerLocal("info.material.behavior.metal.can.wought", EnumChatFormatting.WHITE + "Can wought");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(T value, Mat material, MatCondition condition, UnlocalizedList list)
	{
		if (value.heat >= (this.boilHeat - this.meltHeat) * 4 / 5 + this.meltHeat)
		{
			list.add("info.matprop.metal.boil.warn");
		}
		else if (value.heat >= this.meltHeat)
		{
			list.add("info.matprop.metal.melt");
		}
		else if (value.heat >= (this.meltHeat - this.woughtHeat) * 4 / 5 + this.woughtHeat)
		{
			list.add("info.matprop.metal.melt.warn");
		}
		if (value.heat >= this.workHeat)
		{
			list.add("info.matprop.metal.can.work");
		}
		if (value.heat >= this.woughtHeat)
		{
			list.add("info.matprop.metal.can.wought");
		}
	}
	
	@Override
	public float entityAttackDamageMultiple(T value, Mat material, Entity target)
	{
		return 1.0F;
	}
	
	public NBTTagCompound write(Mat material, T value)
	{
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger("heat", value.heat);
		return result;
	}
	
	public T read(NBTTagCompound nbt, Mat material)
	{
		T instance = instance(material);
		read(nbt, instance);
		return instance;
	}
	
	protected void read(NBTTagCompound nbt, T arg)
	{
		arg.heat = nbt.getInteger("heat");
	}
	
	@Override
	protected int getHeat(T arg)
	{
		return arg.heat;
	}
	
	@Override
	protected void setHeat(T arg, int value)
	{
		arg.heat = value;
	}
}
