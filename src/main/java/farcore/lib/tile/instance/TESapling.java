package farcore.lib.tile.instance;

import java.util.Random;

import farcore.data.M;
import farcore.lib.material.Mat;
import farcore.lib.tile.TEAged;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.Biome;

public class TESapling extends TEAged implements ISaplingAccess
{
	private float age;
	public TreeInfo info;
	public Mat material = M.VOID;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("tree", material.name);
		nbt.setFloat("age", age);
		return nbt;
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
		material = Mat.material(nbt.getString("tree"));
		age = nbt.getFloat("age");
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("t"))
		{
			material = Mat.material(nbt.getString("t"));
		}
	}

	public void setTree(EntityLivingBase entity, Mat tree)
	{
		material = tree;
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

	public float age()
	{
		return age;
	}

	public void setAge(float age)
	{
		this.age = age;
	}

	public boolean grow()
	{
		IBlockState state = worldObj.getBlockState(pos);
		worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		if(material.tree.generateTreeAt(worldObj, pos, random, info))
			return true;
		worldObj.setBlockState(pos, state, 4);
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
	public Biome biome()
	{
		return worldObj.getBiomeGenForCoords(pos);
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