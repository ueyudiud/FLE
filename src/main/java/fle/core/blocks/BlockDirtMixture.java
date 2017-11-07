/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.blocks;

import farcore.data.EnumBlock;
import farcore.data.Materials;
import fle.core.tile.TEDirtMixture;
import nebula.client.ClientProxy;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSingleTE;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockDirtMixture extends BlockSingleTE
{
	public static boolean checkAndSetBlock(World world, BlockPos pos)
	{
		if (Worlds.isSideSolid(world, pos.down(), EnumFacing.UP, false) && Worlds.isSideSolid(world, pos.north(), EnumFacing.SOUTH, false) && Worlds.isSideSolid(world, pos.south(), EnumFacing.NORTH, false) && Worlds.isSideSolid(world, pos.east(), EnumFacing.WEST, false)
				&& Worlds.isSideSolid(world, pos.west(), EnumFacing.EAST, false))
		{
			world.setBlockState(pos, EnumBlock.dirt_mixture.apply());
			return true;
		}
		return false;
	}
	
	public BlockDirtMixture()
	{
		super("dirt.mixture", Materials.DIRT);
		EnumBlock.dirt_mixture.set(this);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		GameRegistry.registerTileEntity(TEDirtMixture.class, "fle.DirtMixture");
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Dirt Mixture");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ClientProxy.registerBuildInModel(this);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEDirtMixture();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
}
