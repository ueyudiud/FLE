/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.material.behavior.metal;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.environment.IEnvironment;
import nebula.common.util.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class MatBehaviorCopper extends MatBehaviorMetal<MatBehaviorCopper.MetalCopper>
{
	public static class MetalCopper extends MatBehaviorMetal.Metal
	{
		public int rustness;
		
		public MetalCopper(Mat material, int heat, int rustness)
		{
			super(material, heat);
			this.rustness = rustness;
		}
		
		@Override
		public Metal copy()
		{
			return new MetalCopper(this.material, this.heat, this.rustness);
		}
	}
	
	{
		LanguageManager.registerLocal("info.material.behavior.metal.copper.rustness", "Rustness: %s");
	}
	
	@Override
	public int getOffsetMetaCount()
	{
		return 4;
	}
	
	@Override
	public String getReplacedLocalName(int metaOffset, Mat material)
	{
		switch (metaOffset)
		{
		case 3:
			return "Rusted " + material.localName;
		case 2:
		case 1:
			return "Rusting " + material.localName;
		default:
			return material.localName;
		}
	}
	
	private static final int MAX_RUSTNESS = 1_0000_0000;
	
	@Override
	public int getMetaOffset(MetalCopper value, Mat material)
	{
		return Math.round(value.rustness / (MAX_RUSTNESS / 3.0F));
	}
	
	@Override
	public MetalCopper instance(int metaOffset, Mat material)
	{
		return new MetalCopper(material, 0, MAX_RUSTNESS * metaOffset / 3);
	}
	
	@Override
	public ItemStack updateItem(MetalCopper arg, ItemStack stack, Mat material, MatCondition condition, IEnvironment environment)
	{
		//		float speed = 1.0F;
		//		float temp = ThermalNet.getTemperature(environment);
		//		float h2o = L.cast(environment.getValue(WP.H2OConcentration));
		//		float co2 = L.cast(environment.getValue(WP.CO2Concentration));
		//		if (temp > 280)
		//		{
		//			temp = (temp - 280) / 10;
		//			speed *= temp / (1 + temp);
		//		}
		
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(MetalCopper value, Mat material, MatCondition condition, UnlocalizedList list)
	{
		float amount = value.rustness / (float) MAX_RUSTNESS;
		if (amount > 0.1F)
		{
			list.add("info.material.behavior.metal.copper.rustness", Strings.progress(amount));
		}
	}
	
	@Override
	public NBTTagCompound write(Mat material, MetalCopper arg)
	{
		NBTTagCompound nbt = super.write(material, arg);
		nbt.setInteger("rustness", arg.rustness);
		return nbt;
	}
	
	@Override
	protected void read(NBTTagCompound nbt, MetalCopper arg)
	{
		super.read(nbt, arg);
		arg.rustness = nbt.getInteger("rustness");
	}
	
	@Override
	public float entityAttackDamageMultiple(MetalCopper value, Mat material, Entity target)
	{
		return 1.0F - 0.4F * value.rustness / MAX_RUSTNESS;
	}
}
