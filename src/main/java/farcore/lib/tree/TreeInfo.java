package farcore.lib.tree;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.GeneticMaterial.GenticMaterialFactory;
import farcore.lib.bio.IBiology;
import farcore.lib.collection.IntegerEntry;
import farcore.lib.collection.IntegerMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TreeInfo implements IBiology
{
	public GeneticMaterial DNA;
	@Deprecated
	public int generations;
	public int height;
	public int growth;
	public int coldResistance;
	public int hotResistance;
	public int dryResistance;
	
	public IntegerMap<String> map = new IntegerMap();
	
	@Override
	public GeneticMaterial getGeneticMaterial()
	{
		return this.DNA;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.DNA = GeneticMaterial.GenticMaterialFactory.INSTANCE.readFromNBT(nbt, "gm");
		this.height = nbt.getInteger("hei");
		this.growth = nbt.getInteger("gro");
		this.coldResistance = nbt.getInteger("cRes");
		this.hotResistance = nbt.getInteger("hRes");
		this.dryResistance = nbt.getInteger("dRes");
		NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			this.map.put(compound.getString("tag"), compound.getInteger("value"));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		GenticMaterialFactory.INSTANCE.writeToNBT(this.DNA, nbt, "gm");
		nbt.setInteger("hei", this.height);
		nbt.setInteger("gro", this.growth);
		nbt.setInteger("cRes", this.coldResistance);
		nbt.setInteger("hRes", this.hotResistance);
		nbt.setInteger("dRes", this.dryResistance);
		NBTTagList list = new NBTTagList();
		for(IntegerEntry<String> prop : this.map)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("tag", prop.getKey());
			compound.setInteger("value", prop.getValue());
			list.appendTag(compound);
		}
		nbt.setTag("prop", list);
	}
}