package farcore.lib.tree;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.GeneticMaterial.GenticMaterialFactory;
import nebula.common.base.IntegerEntry;
import nebula.common.base.IntegerMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TreeInfo
{
	public GeneticMaterial gm;
	public int height;
	public int growth;
	public int resistance;
	public int vitality;
	
	public IntegerMap<String> map = new IntegerMap();
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.gm = GeneticMaterial.GenticMaterialFactory.INSTANCE.readFromNBT(nbt, "gm");
		this.height = nbt.getInteger("hei");
		this.growth = nbt.getInteger("gro");
		this.resistance = nbt.getInteger("res");
		this.vitality = nbt.getInteger("vit");
		NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			this.map.put(compound.getString("tag"), compound.getInteger("value"));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		GenticMaterialFactory.INSTANCE.writeToNBT(this.gm, nbt, "gm");
		nbt.setInteger("hei", this.height);
		nbt.setInteger("gro", this.growth);
		nbt.setInteger("res", this.resistance);
		nbt.setInteger("vit", this.vitality);
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