package fle.core.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.block.BlockHasSub;
import fle.api.block.IDebugableBlock;
import fle.api.block.IFacingBlock;
import fle.api.util.Register;
import fle.api.world.BlockPos;
import fle.api.world.TreeInfo;
import fle.core.tree.TreeCitron;

public class BlockLog extends BlockHasSub implements IFacingBlock, IDebugableBlock
{
	public static final Register<TreeInfo> trees = new Register();
	private Map<String, IIcon[]> iconMap;
	
	static
	{
		register(new TreeCitron());
	}
	
	public static void register(TreeInfo aTree)
	{
		trees.register(aTree, aTree.getName());
	}
	
	public BlockLog(String aName) 
	{
		super(ItemLog.class, aName, Material.wood);
		setTickRandomly(true);
		setHardness(1.0F);
	}
	
	@Override
	public void updateTick(World aWorld, int aX, int aY,
			int aZ, Random aRandom)
	{
		trees.get(getTreeInfoID(aWorld, aX, aY, aZ)).onLogUpdate(aWorld, aX, aY, aZ, aRandom);
	}
	
	@Override
	public int getMetadata(World world, int x, int y, int z) 
	{
		return getTreeInfoID(world, x, y, z);
	}

	@Override
	public void setMetadata(World world, int x, int y, int z, int metadata) 
	{
		FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 0, metadata);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return iconMap.get(trees.name(meta))[trees.get(meta).getDefaultLogIconID(side != 0 && side != 1)];
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int side)
	{
		int id = getTreeInfoID(world, x, y, z);
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[world.getBlockMetadata(x, y, z)];
		boolean isSide = !(ForgeDirection.VALID_DIRECTIONS[side] == dir || ForgeDirection.VALID_DIRECTIONS[side] == dir.getOpposite());
		return iconMap.get(trees.name(id))[trees.get(id).getLogIconID(isSide, world, x, y, z)];
	}
	
	@Override
	public void registerBlockIcons(IIconRegister aRegister)
	{
		iconMap = new HashMap();
		for(TreeInfo tree : trees)
		{
			IIcon icons[] = new IIcon[tree.getTextureLocate(true).getLocateSize()];
			for(int i = 0; i < icons.length; ++i)
			{
				icons[i] = aRegister.registerIcon(tree.getTextureLocate(true).getTextureFileName(i) + ":" + tree.getTextureLocate(true).getTextureName(i));
			}
			iconMap.put(trees.name(tree), icons);
		}
	}
	
	@Override
	public int onBlockPlaced(World aWorld, int aX, int aY, int aZ, int aSide,
			float xPos, float yPos, float zPos, int aMeta) 
	{
		ForgeDirection dir = getPointFacing(aWorld, aX, aY, aZ, xPos, yPos, zPos);
		return dir == ForgeDirection.UP || dir == ForgeDirection.DOWN ?
				0 : dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH ? 1 : 2;
	}
    
    @Override
    public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer,
    		int aSide, float xPos, float yPos, float zPos)
    {
    	return BlockLog.trees.get(BlockLog.getTreeInfoID(aWorld, x, y, z)).onLogToss(aWorld, x, y, z, aPlayer, aPlayer.getCurrentEquippedItem());
    }

	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		return pos.getBlockMeta() == 0 ? ForgeDirection.UP : pos.getBlockMeta() == 1 ? ForgeDirection.NORTH : ForgeDirection.EAST;
	}

	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public void setDirection(World world, BlockPos pos, ItemStack tool,
			float xPos, float yPos, float zPos) 
	{
		
	}
	
	public static int getTreeInfoID(IBlockAccess world, int x, int y, int z)
	{
		return FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 0);
	}
	
	public static void setData(BlockPos aPos, int meta)
	{
		FLE.fle.getWorldManager().setData(aPos, 0, meta);
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList) 
	{
		TreeInfo tree = trees.get(getTreeInfoID(aWorld, x, y, z));
		if(tree == null)
			aList.add("This tree log is lost NBT! Please report this bug if this world did'n lost chunk NBT before.");

		aList.add(new StringBuilder().append("Tree Name:").append(tree.getName()).toString());
	}
}