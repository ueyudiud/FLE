/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import farcore.lib.bio.BioData;
import nebula.base.IntEntry;
import nebula.base.collection.HashIntMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TreeInfo
{
	public BioData	gm;
	public int		height;
	public int		growth;
	public int		resistance;
	public int		vitality;
	
	public HashIntMap<String> map = new HashIntMap();
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.gm = TreeOrder.ORDER.readFrom(nbt, "gm");
		this.height = nbt.getInteger("hei");
		this.growth = nbt.getInteger("gro");
		this.resistance = nbt.getInteger("res");
		this.vitality = nbt.getInteger("vit");
		NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			this.map.put(compound.getString("tag"), compound.getInteger("value"));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		TreeOrder.ORDER.writeTo(nbt, "gm", this.gm);
		nbt.setInteger("hei", this.height);
		nbt.setInteger("gro", this.growth);
		nbt.setInteger("res", this.resistance);
		nbt.setInteger("vit", this.vitality);
		NBTTagList list = new NBTTagList();
		for (IntEntry<String> prop : this.map)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("tag", prop.getKey());
			compound.setInteger("value", prop.getValue());
			list.appendTag(compound);
		}
		nbt.setTag("prop", list);
	}
}
