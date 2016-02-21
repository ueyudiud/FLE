package flapi.plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.util.ITextureLocation;

public abstract class TreeInfo implements IPlantable
{
	private Block log;
	private Block leaves;
	
	/**
	 * Check does world is generating in world generator or grow.
	 * TRUE means is plants growing, set block with update client.
	 */	
	public static boolean genFlag = true;
	protected final String name;
	
	public TreeInfo(String aName)
	{
		name = aName;
	}
    protected void dropBlockAsItem(World aWorld, int aX, int aY, int aZ, ItemStack aTarget)
    {
        if (!aWorld.isRemote && aWorld.getGameRules().getGameRuleBooleanValue("doTileDrops") && !aWorld.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            float f = 0.7F;
            double d0 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(aWorld, (double)aX + d0, (double)aY + d1, (double)aZ + d2, aTarget);
            entityitem.delayBeforeCanPickup = 10;
            aWorld.spawnEntityInWorld(entityitem);
        }
    }
	
    public final void initTreeBlock(Block b1, Block b2)
    {
    	log = b1;
    	leaves = b2;
    }
    
    public final Block log()
    {
    	return log;
    }
    
    public final Block leaves()
    {
    	return leaves;
    }
    
	public abstract int getGenerateWeight(World world, int x, int z);
	
	public abstract boolean generate(World world, int x, int y, int z, Random rand);
	
	public abstract ITextureLocation getTextureLocate(boolean isLog);

	public abstract int getDefaultLogIconID(ForgeDirection dir);
	
	public abstract int getLogIconID(ForgeDirection dir, IBlockAccess world, int x, int y, int z);

	public abstract int getDefaultLeavesIconID(ForgeDirection dir);
	
	public abstract int getLeavesIconID(ForgeDirection dir, IBlockAccess world, int x, int y, int z);
	
	public abstract boolean onLeavesToss(World world, int x, int y, int z, EntityPlayer player, ItemStack tool);
	
	public abstract void onLeavesUpdate(World world, int x, int y, int z, Random rand);
	
	public abstract boolean onLogToss(World world, int x, int y, int z, EntityPlayer player, ItemStack tool);
	
	public abstract void onLogUpdate(World world, int x, int y, int z, Random rand);
	
	public float getLogHardness()
	{
		return 1.0F;
	}
	
	public int getFlammability(IBlockAccess world, int x, int y, int z, boolean isLog)
	{
		return isLog ? 5 : 60;
	}

	public String getName() 
	{
		return name;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return Blocks.sapling;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
	
	public void getLogInfomation(World aWorld, int x, int y, int z,
			List aList)
	{
		
	}
	
	public void getLeavesInfomation(World aWorld, int x, int y, int z,
			List aList)
	{
		
	}
	
	public ArrayList<ItemStack> getLeavesDrop(World world, int x, int y, int z,
			int metadata, ArrayList<ItemStack> list)
	{
		return list;
	}
}