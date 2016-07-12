package farcore.lib.tree;

import farcore.lib.bio.IBiology;
import farcore.lib.collection.IntegerMap;
import farcore.lib.collection.IntegerMap.Prop;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TreeInfo implements IBiology
{
	public String DNA;
	
	public int generations;
	public int height;
	public int growth;
	public int coldResistance;
	public int hotResistance;
	public int dryResistance;
	
	public IntegerMap<String> map = new IntegerMap();
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		DNA = nbt.getString("dna");
		height = nbt.getInteger("hei");
		growth = nbt.getInteger("gro");
		coldResistance = nbt.getInteger("cRes");
		hotResistance = nbt.getInteger("hRes");
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
		nbt.setInteger("hei", height);
		nbt.setInteger("gro", growth);
		nbt.setInteger("cRes", coldResistance);
		nbt.setInteger("hRes", hotResistance);
		nbt.setInteger("dRes", dryResistance);
		NBTTagList list = new NBTTagList();
		for(Prop<String> prop : map)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("tag", prop.tag);
			compound.setInteger("value", prop.value);
			list.appendTag(compound);
		}
		nbt.setTag("prop", list);
	}

	@Override
	public String getDNA() 
	{
		return DNA;
	}
}