package farcore.lib.tile.instance;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tile.TEAged;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;

public class TESapling extends TEAged implements ISaplingAccess
{
	private float age;
	public TreeInfo info;
	public Mat material;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("tree", material.name);
		nbt.setFloat("age", age);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt) 
	{
		super.writeToDescription(nbt);
		nbt.setString("t", material.name);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = Mat.register.get("tree");
		age = nbt.getFloat("age");
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("t"))
			material = Mat.register.get("t");
	}
	
	public void setTree(EntityLivingBase entity, Mat tree)
	{
		this.material = tree;
		syncToNearby();
	}

	@Override
	protected void updateServer1()
	{
		if(!canBlockStay())
		{
			removeBlock();
			return;
		}
		age += material.tree.onSaplingUpdate(this);
		if(!isInvalid() && age >= getMaxAge())
		{
			grow();
		}
	}
	
	@Override
	protected float getSyncRange()
	{
		return 80F;
	}

	public boolean grow()
	{
		if(material.tree.canGenerateTreeAt(worldObj, xCoord, yCoord, zCoord, info))
		{
			removeBlock();
			material.tree.generateTreeAt(worldObj, xCoord, yCoord, zCoord, info);
			return true;
		}
		return false;
	}

	public int getMaxAge()
	{
		return material.tree.getGrowAge();
	}

	@Override
	public ITree tree()
	{
		return material.tree;
	}

	@Override
	public TreeInfo info() 
	{
		return info;
	}

	@Override
	public BiomeGenBase biome() 
	{
		return worldObj.getBiomeGenForCoords(xCoord, zCoord);
	}

	@Override
	public Random rng()
	{
		return random;
	}

	@Override
	public void killTree()
	{
		removeBlock();
	}
}