/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import nebula.base.IntEntry;
import nebula.base.collection.HashIntMap;
import nebula.base.collection.IntMap;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 */
public class BioData
{
	private final IOrder order;
	
	public ISpecie			specie;
	public String			parent;
	public int				generation;
	public byte[][]			chromosome;
	public int[]			capabilities;
	public IntMap<String>	properties = new HashIntMap<>();
	
	BioData(IOrder order)
	{
		this.order = order;
	}
	
	public BioData(ISpecie specie, String parent, int generation, byte[][] chromosome, int[] capabilities)
	{
		this.order = specie.getOrder();
		this.specie = specie;
		this.parent = parent;
		this.generation = generation;
		this.chromosome = chromosome;
		this.capabilities = capabilities;
	}
	
	public BioData copy()
	{
		return new BioData(this.specie, this.parent, this.generation, this.chromosome, this.capabilities.clone());
	}
	
	void writeTo(NBTTagCompound nbt)
	{
		nbt.setString("specie", this.specie.getRegisteredName());
		nbt.setString("parent", this.parent);
		nbt.setShort("generation", (short) this.generation);
		NBTTagList list = new NBTTagList();
		for (byte[] bs : this.chromosome)
		{
			list.appendTag(new NBTTagByteArray(bs));
		}
		nbt.setTag("chromosome", list);
		nbt.setIntArray("capabilities", this.capabilities);
		NBTTagCompound compound = new NBTTagCompound();
		for (IntEntry<String> entry : this.properties)
		{
			compound.setInteger(entry.getKey(), entry.getValue());
		}
		nbt.setTag("properties", compound);
	}
	
	void readFrom(NBTTagCompound nbt)
	{
		this.specie = this.order.getSpecie(nbt.getString("specie"));
		this.parent = nbt.getString("parent");
		this.generation = nbt.getShort("generation") & 0xFFFF;
		NBTTagList list = nbt.getTagList("chromosome", NBT.TAG_BYTE_ARRAY);
		this.chromosome = new byte[list.tagCount()][];
		for (int i = 0; i < list.tagCount(); ++i)
		{
			this.chromosome[i] = ((NBTTagByteArray) list.get(i)).getByteArray();
		}
		this.capabilities = nbt.getIntArray("capabilities");
		this.properties.clear();
		NBTTagCompound compound = nbt.getCompoundTag("properties");
		for (String key : compound.getKeySet())
		{
			this.properties.put(key, compound.getInteger(key));
		}
	}
}
