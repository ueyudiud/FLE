package fle.api.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.FLE;
import fle.api.world.BlockPos;

public class BlockFle extends Block
{
	protected int maxStackSize = 64;
	protected final String unlocalizedName;
	  
	protected BlockFle(Class<? extends ItemFleBlock> aItemClass, String aName, Material aMaterial)
	{
		super(aMaterial);
		setBlockName(unlocalizedName = aName);
	    GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
	}
	
	public void setMaxStackSize(int maxStackSize)
	{
		this.maxStackSize = maxStackSize;
	}

	public void addInformation(ItemStack aStack, List<String> aList, EntityPlayer aPlayer)
	{
		;
	}
	
	public int getItemstackMetaOnDroped(World world, BlockPos pos)
	{
		return pos.getBlockMeta();
	}
	
	public int getMaxStackSize()
	{
		return maxStackSize;
	}
	
	public boolean hasSubs()
	{
		return false;
	}
	
	public short getHarvestData(int aDamage)
	{
		return (short) (hasSubs() ? aDamage : 0);
	}

	public boolean requiresMultipleRenderPasses() 
	{
		return false;
	}

	public int getRenderPasses() 
	{
		return requiresMultipleRenderPasses() ? 2 : 1;
	}
	
	public IIcon getIcon(int aPass, BlockPos aPos, int aSide)
	{
		return getIcon(aPos.access, aPos.x, aPos.y, aPos.z, aSide);
	}
	
	public IIcon getIcon(int aPass, int aMeta, int aSide)
	{
		return getIcon(aSide, aMeta);
	}
	
	@Override
	public int getDamageValue(World aWorld, int x, int y, int z)
	{
		return hasSubs() ? aWorld.getBlockMetadata(x, y, z) : 0;
	}
	
	@Override
	public int damageDropped(int aMeta) 
	{
		return hasSubs() ? aMeta : 0;
	}
    
    protected ForgeDirection getPointFacing(World world, int x, int y, int z, EntityLivingBase entity)
    {
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch(l)
        {
        case 0 : return ForgeDirection.NORTH;
        case 1 : return ForgeDirection.EAST;
        case 2 : return ForgeDirection.SOUTH;
        case 3 : return ForgeDirection.WEST;
        default : return ForgeDirection.UNKNOWN;
        }
    }

	protected ForgeDirection getPointFacing(World world, int x, int y, int z, double xPos, double yPos, double zPos)
    {
    	double a = xPos;
    	double b = yPos;
    	double c = zPos;
    	
    	ForgeDirection dir = ForgeDirection.UNKNOWN;
    	
    	if(b == 0.00D)
    	{
    		dir = ForgeDirection.DOWN;
    	}
    	if(a == 0.00D)
    	{
    		dir = ForgeDirection.WEST;
    	}
    	if(c == 0.00D)
    	{
    		dir = ForgeDirection.SOUTH;
    	}
    	if(b == 1.00D)
    	{
    		dir = ForgeDirection.UP;
    	}
    	if(a == 1.00D)
    	{
    		dir = ForgeDirection.EAST;
    	}
    	if(c == 1.00D)
    	{
    		dir = ForgeDirection.NORTH;
    	}
    	return dir;
    }
	
	protected ThreadLocal<TileEntity> thread = new ThreadLocal();
	protected ThreadLocal<Integer> metaThread = new ThreadLocal();
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		if(aWorld.getTileEntity(x, y, z) != null)
			thread.set(aWorld.getTileEntity(x, y, z));
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		metaThread.set(new Integer(FLE.fle.getWorldManager().getData(tPos, 0)));
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
		FLE.fle.getWorldManager().removeData(tPos);
	}
}