/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.GeneticMaterial.GenticMaterialFactory;
import nebula.base.IntEntry;
import nebula.base.collection.HashIntMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class CropInfo
{
	public GeneticMaterial geneticMaterial;
	
	public int	grain;
	public int	growth;
	public int	resistance;
	public int	vitality;
	public int	saving;
	
	public HashIntMap<String> map = new HashIntMap<>();
	
	public GeneticMaterial gamete = null;
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.geneticMaterial = GenticMaterialFactory.INSTANCE.readFrom(nbt, "gm");
		this.grain = nbt.getInteger("gra");
		this.growth = nbt.getInteger("gro");
		this.resistance = nbt.getInteger("res");
		this.vitality = nbt.getInteger("vit");
		this.saving = nbt.getInteger("sav");
		NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			this.map.put(compound.getString("tag"), compound.getInteger("value"));
		}
		this.gamete = GenticMaterialFactory.INSTANCE.readFrom(nbt, "gamete");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		GenticMaterialFactory.INSTANCE.writeTo(nbt, "gm", this.geneticMaterial);
		nbt.setInteger("gra", this.grain);
		nbt.setInteger("gro", this.growth);
		nbt.setInteger("res", this.resistance);
		nbt.setInteger("vit", this.vitality);
		nbt.setInteger("sav", this.saving);
		NBTTagList list = new NBTTagList();
		for (IntEntry<String> prop : this.map)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("tag", prop.getKey());
			compound.setInteger("value", prop.getValue());
			list.appendTag(compound);
		}
		nbt.setTag("prop", list);
		if (this.gamete != null)
		{
			GenticMaterialFactory.INSTANCE.writeTo(nbt, "gamete", this.gamete);
		}
	}
}
