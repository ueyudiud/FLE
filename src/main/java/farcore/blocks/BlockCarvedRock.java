/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks;

import farcore.FarCore;
import farcore.FarCoreRegistry;
import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.lib.tesr.TESRCarvedRock;
import farcore.lib.tile.instance.TECustomCarvedStone;
import nebula.common.block.BlockSingleTE;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Game;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCarvedRock extends BlockSingleTE
{
	public BlockCarvedRock()
	{
		super("carved_rock", Material.ROCK);
		EnumBlock.carved_rock.set(this);
		if (!Config.splitBrightnessOfSmallBlock)
		{
			this.useNeighborBrightness = true;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		FarCoreRegistry.registerBuiltInModelBlock(this);
		Game.registerItemModel(this.item, 0, FarCore.ID, "rock.stone.resource");
		ClientRegistry.bindTileEntitySpecialRenderer(TECustomCarvedStone.class, new TESRCarvedRock());
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
		if (tile instanceof TECustomCarvedStone) return ((TECustomCarvedStone) tile).isFullCube();
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
		return EnumToolType.PICKAXE.name;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
}
