package fle.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockFle;
import fle.api.block.IDebugableBlock;
import fle.api.block.IFacingBlock;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;
import fle.api.world.BlockPos;
import fle.api.world.TreeInfo;
import fle.core.tree.TreeCitron;
import fle.core.tree.TreeSisal;
import fle.core.util.LanguageManager;

public class BlockFleLog extends BlockFle implements IFacingBlock, IDebugableBlock
{
	public static final Register<TreeInfo> trees = new Register();

	public static void init()
	{
		register(new TreeCitron());
		register(new TreeSisal());
	}
	
	public static void register(TreeInfo aTree)
	{
		Block log = new BlockFleLog(aTree);
		Block leaves = new BlockFleLeaves(aTree);
		aTree.initTreeBlock(log, leaves);
		trees.register(aTree, aTree.getName());
	}
	
	TreeInfo info;
	private IIcon[] icons;
	
	public BlockFleLog(TreeInfo aInfo)
	{
		super(ItemLog.class, "log_" + aInfo.getName(), Material.wood);
		info = aInfo;
		setTickRandomly(true);
		setHardness(aInfo.getLogHardness());
		setCreativeTab(FleValue.tabFLE);
		String s = aInfo.getName();
		FleAPI.lm.registerLocal(new ItemStack(this).getUnlocalizedName(), Character.toString(s.charAt(0)).toUpperCase() + s.substring(1) + " Log");
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return icons[info.getDefaultLogIconID(ForgeDirection.values()[side])];
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int side)
	{
		int dir = ForgeDirection.ROTATION_MATRIX[world.getBlockMetadata(x, y, z)][side];
		return icons[info.getLogIconID(ForgeDirection.VALID_DIRECTIONS[dir], world, x, y, z)];
	}
	
	@Override
	public void registerBlockIcons(IIconRegister aRegister)
	{
		ITextureLocation locate = info.getTextureLocate(true);
		icons = new IIcon[locate.getLocateSize()];
		for(int i = 0; i < icons.length; ++i)
		{
			icons[i] = aRegister.registerIcon(locate.getTextureFileName(i) + ":" + locate.getTextureName(i));
		}
		
	}
	
	@Override
	public void updateTick(World aWorld, int aX, int aY,
			int aZ, Random aRandom)
	{
		onNeighborBlockChange(aWorld, aX, aY, aZ, null);
		info.onLogUpdate(aWorld, aX, aY, aZ, aRandom);
	}
	
	@Override
	public int onBlockPlaced(World aWorld, int aX, int aY, int aZ, int aSide,
			float xPos, float yPos, float zPos, int aMeta) 
	{
		ForgeDirection dir = getPointFacing(aWorld, aX, aY, aZ, xPos, yPos, zPos);
		return dir == ForgeDirection.UP || dir == ForgeDirection.DOWN ?
				0 : dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH ? 5 : 3;
	}
    
    @Override
    public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer,
    		ForgeDirection aSide, float xPos, float yPos, float zPos)
    {
    	return BlockLog.trees.get(BlockLog.getTreeInfoID(aWorld, x, y, z)).onLogToss(aWorld, x, y, z, aPlayer, aPlayer.getCurrentEquippedItem());
    }

	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		return pos.getBlockMeta() == 0 ? ForgeDirection.UP : pos.getBlockMeta() == 1 ? ForgeDirection.NORTH : ForgeDirection.EAST;
	}
	
	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z,
			ForgeDirection axis)
	{
		ForgeDirection dir = getDirction(new BlockPos(worldObj, x, y, z));
		if(dir == axis) return false;
		worldObj.setBlockMetadataWithNotify(x, y, z, axis == ForgeDirection.UP || axis == ForgeDirection.DOWN ? 0 : axis == ForgeDirection.NORTH || axis == ForgeDirection.SOUTH ? 1 : 2, 2);
		return true;
	}
	
	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y,
			int z)
	{
		return ForgeDirection.VALID_DIRECTIONS;
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
		rotateBlock(world, pos.x, pos.y, pos.z, ForgeDirection.UP);
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList) 
	{
		try
		{
			aList.add(new StringBuilder().append("Tree Name : ").append(info.getName()).toString());
			info.getLogInfomation(aWorld, x, y, z, aList);
		}
		catch(Throwable e)
		{
			aList.add("This tree log is lost NBT! Please report this bug if this world did'n lost chunk NBT before.");
		}
	}
	
	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z,
			ForgeDirection face)
	{
		return info.getFlammability(world, x, y, z, true);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<>();
		ret.add(new ItemStack(this, 1, 0));
		return ret;
	}
}