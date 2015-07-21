package fla.core.block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.block.BlockBaseContainer;
import fla.api.block.IFacing;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;

public abstract class BlockBaseHasTile extends BlockBaseContainer implements IFacing
{
	protected BlockBaseHasTile(Material material, SubTag...tags)
	{
		super(material, tags);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) 
	{
		return getIcon(new BlockPos(access, x, y, z), ForgeDirection.VALID_DIRECTIONS[side]);
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) 
	{
		return getIcon(metadata, ForgeDirection.VALID_DIRECTIONS[side]);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, 
			float xPos, float yPos, float zPos,	int meta)
	{
    	BlockPos pos = new BlockPos(world, x, y, z);
    	if(!checkFacingByEntity())
    	{
    		setBlockFacing(pos, getPointFacing(world, x, y, z, null, xPos, yPos, zPos));
    	}
    	return hasSubs() ? meta : world.getBlockMetadata(x, y, z);
	}

	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase base, ItemStack itemstack)
    {
    	BlockPos pos = new BlockPos(world, x, y, z);
    	if(checkFacingByEntity())
    	{
    		setBlockFacing(pos, getPointFacing(world, x, y, z, base, 0D, 0D, 0D));
    	}
    }
	
	protected boolean checkFacingByEntity(){return true;}
    
    public void setBlockFacing(BlockPos pos, ForgeDirection dir){}
	
	public abstract IIcon getIcon(BlockPos pos, ForgeDirection side);

	public abstract IIcon getIcon(int meta, ForgeDirection side);
	
	public boolean canSetDirection(World world, BlockPos pos)
	{
		return false;
	}

	public boolean canSetDirectionWith(World world, BlockPos pos, double xPos, double yPos, double zPos, ItemStack itemstack)
	{
		return false;
	}
	
	public ForgeDirection setDirectionWith(World world, BlockPos pos, double xPos, double yPos, double zPos, ItemStack itemstack)
	{
		return ForgeDirection.UNKNOWN;
	}
	
	@Override
	public int damageDropped(int meta) 
	{
		return hasSubs() ? meta : 0;
	}

	protected Map<Integer, Map<String, Integer>> map = new HashMap();

	public void setEffectiveTool(String str)
	{
		for(int i = 0; i < 16; ++i)
		{
			setEffectiveTool(str, i);
		}
	}
	public void setEffectiveTool(String str, int meta)
	{
		if(!map.containsKey(meta))
		{
			map.put(meta, new HashMap());
		}
		map.get(meta).put(str, -1);
	}
	public void setEffectiveTool(String str, int meta, int level)
	{
		if(!map.containsKey(meta))
		{
			map.put(meta, new HashMap());
		}
		map.get(meta).put(str, level);
	}
	
	@Override
	public String getHarvestTool(int metadata) 
	{
		if(map.get(metadata) != null)
		{
			return map.get(metadata).size() != 0 ? map.get(metadata).keySet().iterator().next() : null;
		}
		return null;
	}
	
	@Override
	public int getHarvestLevel(int metadata) 
	{
		if(map.get(metadata) != null)
		{
			return map.get(metadata).size() != 0 ? map.get(metadata).get(map.get(metadata).keySet().iterator().next()) : -1;
		}
		return -1;
	}
	
    public boolean isToolEffective(String type, int level, int metadata)
    {
        return map.get(metadata).containsKey(type) ? map.get(metadata).get(type) <= level : false;
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) 
    {
        ItemStack stack = player.inventory.getCurrentItem();
        
        if (stack == null || (map.get(meta) == null ? true : map.get(meta).size() == 0))
        {
            return player.canHarvestBlock(this);
        }

        Iterator<String> tools = stack.getItem().getToolClasses(stack).iterator();
        while(tools.hasNext())
        {
        	String toolClass = tools.next();
        	if(this.isToolEffective(toolClass, stack.getItem().getHarvestLevel(stack, toolClass), meta))
        		return true;
        }
        return super.canHarvestBlock(player, meta);
    }
    
    protected abstract boolean canRecolour(World world, BlockPos pos, ForgeDirection side, int colour);

    protected void onRecolor(World world, BlockPos pos, ForgeDirection dir, int colour)
    {
    	;
    }
    
    public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour)
    {
    	if(this.canRecolour(world, new BlockPos(world, x, y, z), side, colour))
    	{
    		this.onRecolor(world, new BlockPos(world, x, y, z), side, colour);
    		return true;
    	}
    	return false;
    }
    
    public void getBlockInfomation(ItemStack itemstack, boolean shiftKey, boolean pKey, List list) 
    {
		
	}
    
    @Override
    public boolean hasTopOrDownState(World world, BlockPos pos) 
    {
    	return false;
    }
    
    protected ForgeDirection getPointFacing(World world, int x, int y, int z, EntityLivingBase entity, double xPos, double yPos, double zPos)
    {
    	if(this.hasTopOrDownState(world, new BlockPos(world, x, y, z)))
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
    	else
    	{
            int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            switch(l)
            {
            case 0 : return ForgeDirection.SOUTH;
            case 1 : return ForgeDirection.WEST;
            case 2 : return ForgeDirection.NORTH;
            case 3 : return ForgeDirection.EAST;
            default : return ForgeDirection.UNKNOWN;
            }
    	}
    }
}
