package fla.api.block;

import java.util.Random;

import fla.api.util.FlaValue;
import fla.api.util.SubTag;
import fla.api.util.SubTag.TagsInfomation;
import fla.api.world.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockBaseContainer extends BlockContainer
{
	private TagsInfomation tags = new TagsInfomation();
	
	public int maxStackSize = 64;
	
	protected BlockBaseContainer(Material material, SubTag...ts) 
	{
		super(material);
		tags.add(ts);
		if(tags.contain("Hardness"))
			setHardness(tags.<Float>getValue("Hardness"));
		if(tags.contain("Resistance"))
			setResistance(tags.<Float>getValue("Resistance"));
		if(tags.contain("LightLevel"))
			setLightLevel(tags.<Float>getValue("LightLevel"));
		if(tags.contain("MaxStackSize"))
			maxStackSize = tags.<Integer>getValue("MaxStackSize");
		if(tags.contain("TickRandomly"))
			setTickRandomly(tags.<Boolean>getValue("TickRandomly"));
	}
	
	@Override
	public void breakBlock(World world, int x, int y,
			int z, Block block, int meta)
	{
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) 
	{
		if(tags.contain("BlockStayType"))
		{
			byte value = tags.<Byte>getValue("BlockStayType");
			switch(value)
			{
			case 0 : return true;
			case 1 : return world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
			case 2 : return world.getBlock(x, y + 1, z).isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN);
			default : return canBlockStay(world, new BlockPos(world, x, y, z), value);
			}
		}
		return true;
	}
	
	public boolean canBlockStay(World world, BlockPos pos, byte type){return false;}
	
	@Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
    }
	
	@Override
	public int getRenderType() 
	{
		return tags.contain("Render") ? (tags.<Boolean>getValue("Render") ? FlaValue.ALL_RENDER_ID : 0) : 0;
	}
	
	public boolean hasSubs()
	{
		return tags.contain("SubBlock") ? tags.<Boolean>getValue("SubBlock") : false;
	}
	
	@Override
	public abstract boolean isOpaqueCube();
	//{
		//return tags.contain("OpaqueCube") ? tags.<Boolean>getValue("OpaqueCube") : true;
	//}
	
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) 
	{
		return super.isNormalCube(world, x, y, z);
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
			ForgeDirection side) 
	{
		if(isNormalCube(world, x, y, z)) return true;
		if(tags.contain("SolidSide"))
		{
			int s = tags.<Integer>getValue("SolidSide");
			if((s & side.flag) != 0) return true;
			else return false;
		}
		return super.isSideSolid(world, x, y, z, side);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(tags.contain("CheckCanStay"))
		{
			boolean value = tags.<Boolean>getValue("CheckCanStay");
			if(value) 
			{
				if(!canBlockStay(world, x, y, z))
				{
					 dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			         world.setBlock(x, y, z, getBlockById(0), 0, 2);
				}
			}
		}
		super.updateTick(world, x, y, z, rand);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if(tags.contain("CheckCanStay"))
		{
			boolean value = tags.<Boolean>getValue("CheckCanStay");
			if(value) 
			{
				if(!canBlockStay(world, x, y, z))
				{
					 dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			         world.setBlock(x, y, z, getBlockById(0), 0, 2);
				}
			}
		}
		super.onNeighborBlockChange(world, x, y, z, block);
	}
}