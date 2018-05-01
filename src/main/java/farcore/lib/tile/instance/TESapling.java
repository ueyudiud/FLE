/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance;

import java.util.List;
import java.util.Random;

import farcore.data.M;
import farcore.data.MP;
import farcore.lib.block.IDebugableBlock;
import farcore.lib.material.Mat;
import farcore.lib.model.block.ModelSapling;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.ITree;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import nebula.client.util.Client;
import nebula.common.data.Misc;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.TE05Aged;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TESapling extends TE05Aged implements ISaplingAccess, IDebugableBlock, ITB_BlockPlacedBy, ITB_AddHitEffects, ITB_AddDestroyEffects
{
	private float	age;
	public TreeInfo	info;
	public Tree		tree	= Tree.VOID;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTs.setString(nbt, "tree", this.tree);
		nbt.setFloat("age", this.age);
		return nbt;
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("t", this.tree.material.id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.tree = Mat.getMaterialByNameOrDefault(nbt, "tree", Mat.VOID).getProperty(MP.property_tree);
		this.age = nbt.getFloat("age");
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("t"))
		{
			this.tree = Mat.getMaterialByIDOrDefault(nbt, "t", M.oak).getProperty(MP.property_tree);
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.tree = Mat.material(stack.getItemDamage()).getProperty(MP.property_tree);
		if (this.tree == ITree.VOID)
			removeBlock();
		else
			syncToNearby();
	}
	
	@Override
	protected void updateServer1()
	{
		if (!canBlockStay() || this.tree == Tree.VOID)
		{
			removeBlock();
			return;
		}
		this.age += this.tree.onSaplingUpdate(this);
		markDirty();
		if (!isInvalid() && this.age >= getMaxAge())
		{
			grow();
		}
	}
	
	@Override
	protected float getSyncRange()
	{
		return 64F;
	}
	
	public float age()
	{
		return this.age;
	}
	
	public void setAge(float age)
	{
		this.age = age;
		markDirty();
	}
	
	public boolean grow()
	{
		IBlockState state = this.world.getBlockState(this.pos);
		this.world.setBlockState(this.pos, Misc.AIR, 4);
		if (this.tree.generateTreeAt(this.world, this.pos, this.random, this.info)) return true;
		this.world.setBlockState(this.pos, state, 4);
		return false;
	}
	
	public int getMaxAge()
	{
		return this.tree.getGrowAge(this);
	}
	
	@Override
	public Tree tree()
	{
		return this.tree;
	}
	
	@Override
	public TreeInfo info()
	{
		return this.info;
	}
	
	@Override
	public Biome biome()
	{
		return this.world.getBiome(this.pos);
	}
	
	@Override
	public Random rng()
	{
		return this.random;
	}
	
	@Override
	public void killTree()
	{
		removeBlock();
	}
	
	@Override
	public void addInformation(EntityPlayer player, World world, BlockPos pos, Direction side, List<String> list)
	{
		list.add("Name : " + this.tree.getRegisteredName());
		list.add("DNA : " + this.info.gm);
		list.add("Grow Progress : " + (int) this.age + "/" + getMaxAge());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		Client.addBlockHitEffect(this.world, this.random, getBlockState(), target.sideHit, target.getBlockPos(), manager, ModelSapling.ICON_MAP.get(this.tree.getRegisteredName()));
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		Client.addBlockDestroyEffects(this.world, this.pos, getBlockState(), manager, ModelSapling.ICON_MAP.get(this.tree.getRegisteredName()));
		return true;
	}
}
