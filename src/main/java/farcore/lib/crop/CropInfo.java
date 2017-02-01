package farcore.lib.crop;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.GeneticMaterial.GenticMaterialFactory;
import nebula.common.base.IntegerEntry;
import nebula.common.base.IntegerMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class CropInfo
{
	public GeneticMaterial geneticMaterial;
	
	/**
	 * The genertic material already contain this prop.
	 */
	@Deprecated
	public int generations;
	public int grain;
	public int growth;
	public int coldResistance;
	public int hotResistance;
	public int weedResistance;
	public int dryResistance;
	
	public IntegerMap<String> map = new IntegerMap();
	
	public GeneticMaterial gamete = null;
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.geneticMaterial = GenticMaterialFactory.INSTANCE.readFromNBT(nbt, "gm");
		this.grain = nbt.getInteger("gra");
		this.growth = nbt.getInteger("gro");
		this.coldResistance = nbt.getInteger("cRes");
		this.hotResistance = nbt.getInteger("hRes");
		this.weedResistance = nbt.getInteger("wRes");
		this.dryResistance = nbt.getInteger("dRes");
		NBTTagList list = nbt.getTagList("prop", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			this.map.put(compound.getString("tag"), compound.getInteger("value"));
		}
		this.gamete = GenticMaterialFactory.INSTANCE.readFromNBT(nbt, "gamete");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		GenticMaterialFactory.INSTANCE.writeToNBT(this.geneticMaterial, nbt, "gm");
		nbt.setInteger("gra", this.grain);
		nbt.setInteger("gro", this.growth);
		nbt.setInteger("cRes", this.coldResistance);
		nbt.setInteger("hRes", this.hotResistance);
		nbt.setInteger("wRes", this.weedResistance);
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
		GenticMaterialFactory.INSTANCE.writeToNBT(this.gamete, nbt, "gamete");
	}
}