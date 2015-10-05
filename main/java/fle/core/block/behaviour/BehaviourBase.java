package fle.core.block.behaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.block.IBlockBehaviour;

public class BehaviourBase<E extends Block> implements IBlockBehaviour<E>
{
	@Override
	public boolean onBlockActivated(E block, World aWorld, int x, int y, int z,
			EntityPlayer aPlayer, ForgeDirection aSide, float xPos, float yPos,
			float zPos)
	{
		return false;
	}
	
	@Override
	public boolean canBlockStay(E block, World aWorld, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void onBlockClicked(E block, World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		
	}

	@Override
	public void onBlockPlaced(E block, World aWorld, int x, int y, int z,
			ForgeDirection aSide, float xPos, float yPos, float zPos)
	{
		
	}

	@Override
	public void onBlockBreak(E block, World aWorld, int x, int y, int z,
			Block aBlock, int aMeta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityCollidedWithBlock(E block, World aWorld, int x, int y,
			int z, Entity aEntity)
	{
		
	}

	@Override
	public void onFallenUpon(E block, World aWorld, int x, int y, int z,
			Entity aEntity, float aHeight) 
	{
		
	}

	@Override
	public void getAdditionalToolTips(E block, List<String> list,
			ItemStack aStack) 
	{
		
	}

	@Override
	public ArrayList<ItemStack> getHarvestDrop(E block, World aWorld,
			int aMeta, TileEntity aTile, int aFotune)
	{
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(new ItemStack(block, 1, aMeta));
		return drops;
	}

	protected NBTTagCompound setupNBT(ItemStack aStack)
	{
		if(!aStack.hasTagCompound()) aStack.stackTagCompound = new NBTTagCompound();
		return aStack.stackTagCompound;
	}

	@Override
	public void onBlockPlacedBy(E block, World aWorld, int x, int y, int z,
			ItemStack aStack, EntityLivingBase aEntity)
	{
		
	}

	@Override
	public void onRenderUpdate(E block, World aWorld, int x, int y, int z, Random rand)
	{
		
	}
}