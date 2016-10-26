package farcore.lib.crop;

import farcore.lib.collection.IntegerEntry;
import farcore.lib.collection.IntegerMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class CropInfo
{
	public String DNA;

	public int generations;
	public int grain;
	public int growth;
	public int coldResistance;
	public int hotResistance;
	public int weedResistance;
	public int dryResistance;

	public IntegerMap<String> map = new IntegerMap();

	public void readFromNBT(NBTTagCompound nbt)
	{
		DNA = nbt.getString("dna");
		grain = nbt.getInteger("gra");
		growth = nbt.getInteger("gro");
		coldResistance = nbt.getInteger("cRes");
		hotResistance = nbt.getInteger("hRes");
		weedResistance = nbt.getInteger("wRes");
		dryResistance = nbt.getInteger("dRes");
		NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			map.put(compound.getString("tag"), compound.getInteger("value"));
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("DNA", DNA);
		nbt.setInteger("gra", grain);
		nbt.setInteger("gro", growth);
		nbt.setInteger("cRes", coldResistance);
		nbt.setInteger("hRes", hotResistance);
		nbt.setInteger("wRes", weedResistance);
		nbt.setInteger("dRes", dryResistance);
		NBTTagList list = new NBTTagList();
		for(IntegerEntry<String> prop : map)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("tag", prop.getKey());
			compound.setInteger("value", prop.getValue());
			list.appendTag(compound);
		}
		nbt.setTag("prop", list);
	}
}