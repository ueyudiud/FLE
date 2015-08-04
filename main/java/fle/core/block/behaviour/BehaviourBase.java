package fle.core.block.behaviour;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.block.IBlockBehaviour;

public class BehaviourBase<E extends Block> implements IBlockBehaviour<E>
{
	@Override
	public boolean onBlockActivated(E block, World aWorld, int x, int y, int z,
			EntityPlayer aPlayer, ForgeDirection aSide, float xPos, float yPos,
			float zPos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBlockClicked(E block, World aWorld, int x, int y, int z,
			EntityPlayer aPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlockPlaced(E block, World aWorld, int x, int y, int z,
			ForgeDirection aSide, float xPos, float yPos, float zPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlockBreak(E block, World aWorld, int x, int y, int z,
			Block aBlock, int aMeta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityCollidedWithBlock(E block, World aWorld, int x, int y,
			int z, Entity aEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFallenUpon(E block, World aWorld, int x, int y, int z,
			Entity aEntity, float aHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAdditionalToolTips(E block, List<String> list,
			ItemStack aStack) {
		// TODO Auto-generated method stub
		
	}
}