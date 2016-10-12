package farcore.lib.block.instance;

import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.data.EnumToolType;
import farcore.lib.block.BlockSingleTE;
import farcore.lib.tile.instance.TECustomCarvedStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCarvedRock extends BlockSingleTE
{
	public BlockCarvedRock()
	{
		super("carved_rock", Material.ROCK);
		EnumBlock.carved_rock.set(this);
		if(!Config.splitBrightnessOfSmallBlock)
		{
			useNeighborBrightness = true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TECustomCarvedStone();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECustomCarvedStone)
			return ((TECustomCarvedStone) tile).isFullCube();
		return true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolType.pickaxe.name();
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
}