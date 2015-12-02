package fle.api.te;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.cover.Cover;
import fle.api.world.BlockPos;

public class TEStatic extends TEBase
{
	private boolean updateFlag = true;
	
	@Override
	public void updateEntity()
	{
		getBlockType();
		getBlockMetadata();
		updateFlag = false;
	}
	
	@Override
	public boolean canUpdate()
	{
		return updateFlag;
	}
	
	@Override
	protected boolean canAddCoverWithSide(ForgeDirection dir, Cover cover)
	{
		return false;
	}
	
	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos)
	{
		return false;
	}
	
	public boolean onBlockActivated(ForgeDirection dir, EntityPlayer aPlayer, float xPos,	float yPos, float zPos)
	{
		return false;
	}
	
	public void onBlockBreak(int aMeta)
	{
		
	}
	
	@Override
	public void markForUpdate()
	{
		super.markForUpdate();
		updateFlag = true;
	}

	public ArrayList<ItemStack> getDrop(int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		ret.add(new ItemStack(getBlockType(), 1, metadata));
		return ret;
	}
}