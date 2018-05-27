/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import farcore.lib.bio.BioData;
import nebula.base.IntEntry;
import nebula.base.collection.HashIntMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class CropInfo
{
	public BioData data;
	
	public int	grain;
	public int	growth;
	public int	resistance;
	public int	vitality;
	public int	saving;
	
	public HashIntMap<String> map = new HashIntMap<>();
	
	public BioData seed = null;
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.data = CropOrder.ORDER.readFrom(nbt, "data");
		this.grain = nbt.getShort("gra");
		this.growth = nbt.getShort("gro");
		this.resistance = nbt.getShort("res");
		this.vitality = nbt.getShort("vit");
		this.saving = nbt.getShort("sav");
		if (nbt.hasKey("prop", NBT.TAG_LIST))//Old data format
		{
			NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); ++i)
			{
				NBTTagCompound compound = list.getCompoundTagAt(i);
				this.map.put(compound.getString("tag"), compound.getInteger("value"));
			}
		}
		else
		{
			NBTTagCompound list = nbt.getCompoundTag("prop");
			for (String key : list.getKeySet())
			{
				this.map.put(key, list.getShort(key));
			}
		}
		this.seed = CropOrder.ORDER.readFrom(nbt, "gamete");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		CropOrder.ORDER.writeTo(nbt, "data", this.data);
		nbt.setShort("gra", (short) this.grain);
		nbt.setShort("gro", (short) this.growth);
		nbt.setShort("res", (short) this.resistance);
		nbt.setShort("vit", (short) this.vitality);
		nbt.setShort("sav", (short) this.saving);
		NBTTagCompound list = new NBTTagCompound();
		for (IntEntry<String> prop : this.map)
		{
			list.setShort(prop.getKey(), (short) prop.getValue());
		}
		nbt.setTag("prop", list);
		if (this.seed != null)
		{
			CropOrder.ORDER.writeTo(nbt, "gamete", this.seed);
		}
	}
}
