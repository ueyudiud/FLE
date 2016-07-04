package fle.core.block;

import java.util.Random;

import farcore.alpha.interfaces.block.ISmartFallableBlock;
import farcore.block.ItemBlockBase;
import farcore.entity.EntityFallingBlockExtended;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceRock;
import farcore.lib.substance.SubstanceSand;
import farcore.util.U;
import fle.api.block.BlockSubstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BlockSand extends BlockSubstance<SubstanceSand> implements ISmartFallableBlock
{
	public static void init()
	{
		Register<Block> register = new Register();
		for(SubstanceSand sand : SubstanceSand.getSands())
		{
			Block block = new BlockSand(sand, register);
			register.register(sand.getName(), block);
			if(sand == SubstanceSand.VOID_SAND)
			{
				EnumItem.sand_block.set(new ItemStack(block));
				EnumBlock.sand.setBlock(block, EnumItem.sand_block);				
			}
		}
	}
	
	protected BlockSand(SubstanceSand substance, IRegister<Block> register)
	{
		super("sand", Material.sand, substance, register);
		setBlockTextureName("fle:sand");
		setCreativeTab(CreativeTabs.tabBlock);
	}

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		U.Worlds.checkAndFallBlock(world, x, y, z, this);
	}
	
	@Override
	public int tickRate(World world)
	{
		return 2;
	}

	@Override
	public void onStartFalling(World world, int x, int y, int z)
	{
		
	}

	@Override
	public boolean canFallingBlockStay(World world, int x, int y, int z, int meta)
	{
		return !EntityFallingBlockExtended.canFallAt(world, x, y, z, this);
	}

	@Override
	public boolean onFallOnGround(World world, int x, int y, int z, int meta, int height, NBTTagCompound tileNBT)
	{
		return false;
	}

	@Override
	public boolean onDropFallenAsItem(World world, int x, int y, int z, int meta, NBTTagCompound tileNBT)
	{
		return false;
	}

	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		return 0.6F;
	}

	@Override
	public boolean spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return spawn(world, x, y, z, register.get((String) objects[0]));
			}
			else if(objects[0] instanceof SubstanceSand)
			{
				Block block = register.get(((SubstanceSand) objects[0]).getName());
				if(block != null)
				{
					return world.setBlock(x, y, z, block);
				}
			}
		}
		return false;
	}
}