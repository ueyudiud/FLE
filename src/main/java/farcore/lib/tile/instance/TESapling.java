package farcore.lib.tile.instance;

import java.util.List;
import java.util.Random;

import farcore.data.M;
import farcore.data.MP;
import farcore.lib.block.IDebugableBlock;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyTree;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.TreeInfo;
import nebula.common.data.Misc;
import nebula.common.tile.TEAged;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TESapling extends TEAged
implements ISaplingAccess, IDebugableBlock
{
	private float age;
	public TreeInfo info;
	public PropertyTree tree = PropertyTree.VOID;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTs.setString(nbt, "tree", tree);
		nbt.setFloat("age", age);
		return nbt;
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("t", tree.material().id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tree = (PropertyTree) Mat.material(nbt.getString("tree")).getProperty(MP.property_wood, PropertyTree.VOID);
		age = nbt.getFloat("age");
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("t"))
		{
			tree = (PropertyTree) Mat.getMaterialByIDOrDefault(nbt, "t", M.oak).getProperty(MP.property_wood, PropertyTree.VOID);
			markBlockRenderUpdate();
		}
	}
	
	public void setTree(EntityLivingBase entity, Mat material)
	{
		tree = (PropertyTree) material.getProperty(MP.property_wood, PropertyTree.VOID);
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
		age += tree.onSaplingUpdate(this);
		markDirty();
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
		markDirty();
	}
	
	public boolean grow()
	{
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, Misc.AIR, 4);
		if(tree.generateTreeAt(world, pos, random, info))
			return true;
		world.setBlockState(pos, state, 4);
		return false;
	}
	
	public int getMaxAge()
	{
		return tree.getGrowAge(this);
	}
	
	@Override
	public PropertyTree tree()
	{
		return tree;
	}
	
	@Override
	public TreeInfo info()
	{
		return info;
	}
	
	@Override
	public Biome biome()
	{
		return world.getBiome(pos);
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
	
	@Override
	public void addInformation(EntityPlayer player, World world, BlockPos pos, Direction side, List<String> list)
	{
		list.add("Name : " + tree.getRegisteredName());
		list.add("DNA : " + info.DNA);
		list.add("Grow Progress : " + (int) age + "/" + getMaxAge());
	}
}