package farcore.lib.block.instance;

import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.lib.block.BlockTE;
import farcore.lib.collection.IRegister;
import farcore.lib.tile.instance.TECustomCarvedStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCarvedRock extends BlockTE
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
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(1, "carved", TECustomCarvedStone.class);
		return true;
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
		return "pickaxe";
	}

	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
}