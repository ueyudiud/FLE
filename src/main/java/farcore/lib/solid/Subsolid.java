/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.solid;

import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import nebula.common.nbt.INBTSelfReaderAndWriter;
import nebula.common.nbt.NBTFormat;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
@ParametersAreNonnullByDefault
public class Subsolid implements INBTSelfReaderAndWriter<NBTTagCompound>
{
	public static Subsolid loadFromNBT(NBTTagCompound nbt)
	{
		return new Subsolid(nbt);
	}
	
	private Solid solid;
	private NBTFormat format;
	
	public Subsolid(@Nullable Solid solid, NBTFormat format)
	{
		this.solid = solid;
		this.format = format;
	}
	Subsolid(NBTTagCompound nbt)
	{
		readFrom(nbt);
	}
	
	public Solid getFluid()
	{
		return this.solid;
	}
	
	public SolidStack stack(int amount)
	{
		return new SolidStack(this.solid, amount, this.format.template());
	}
	
	public boolean match(SolidStack stack)
	{
		return (this.solid == null && stack == null) ||
				(this.solid == stack.getSolid() && this.format.test(stack.tag));
	}
	
	@Override
	public void readFrom(NBTTagCompound nbt)
	{
		if (nbt.hasKey("solid"))
		{
			this.solid = Solid.REGISTRY.get(nbt.getString("solid"));
			if (nbt.hasKey("nbt"))
			{
				this.format = NBTFormat.deserialize(nbt.getByteArray("nbt"));
			}
		}
	}
	
	@Override
	public NBTTagCompound writeTo()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.solid != null)
		{
			nbt.setString("solid", this.solid.getRegisteredName());
			if (this.format.hasRules())
			{
				nbt.setByteArray("nbt", this.format.serialize());
			}
		}
		return nbt;
	}
	
	@Override
	public int hashCode()
	{
		return 31 * Objects.hashCode(this.solid) + this.format.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (!(obj instanceof Subsolid)) return false;
		Subsolid subfluid = (Subsolid) obj;
		return this.solid == subfluid.solid &&
				Arrays.equals(this.format.serialize(), subfluid.format.serialize());
	}
}