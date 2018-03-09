/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import java.util.List;

import mezz.jei.api.IModRegistry;
import nebula.client.util.UnlocalizedList;
import nebula.common.util.Game;
import nebula.common.world.ICoord;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public final class TileDelegateMissing implements ITileDelegate<Block, TileEntity>
{
	public static final TileDelegateMissing INSTANCE = new TileDelegateMissing();
	
	private TileDelegateMissing()
	{
	}
	
	@Override
	public String getUnlocalizedName(Block block, ItemStack stack)
	{
		return "<missing>";
	}
	
	@Override
	public String getUnlocalizedName(Block block, TileEntity tile, IBlockState stack, World world, ICoord coord)
	{
		return "<missing>";
	}
	
	@Override
	public void load(Block block)
	{
		
	}
	
	@Override
	public void postload(Block block)
	{
		
	}
	
	@Override
	public void renderload(Block block)
	{
		
	}
	
	@Override
	@Optional.Method(modid = Game.MOD_JEI)
	public void loadJEI(Block block, Item item, IModRegistry registry)
	{
		
	}
	
	@Override
	public boolean isOpaqueCube(Block block, IBlockState state)
	{
		return true;
	}
	
	@Override
	public boolean isTranslucent(Block block, IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean canCollideCheck(Block block, IBlockState state, boolean hitIfLiquid)
	{
		return true;
	}
	
	@Override
	public void addInformation(Block block, ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		tooltip.add("info.tile.missing.data");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(Block block, IBlockState state)
	{
		return 0.2F;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(Block block, IBlockState state)
	{
		return EnumPushReaction.NORMAL;
	}
	
	@Override
	public boolean hasTileEntity(Block block, IBlockState state)
	{
		return false;
	}
	
	@Override
	public TileEntity createTileEntity(Block block, IBlockState state, World world)
	{
		return null;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.SOLID;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(Block block, IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public float getBlockHardness(Block block, TileEntity tile, IBlockState state, World world, ICoord coord)
	{
		return 0.0F;
	}
	
	@Override
	public float getExplosionResistance(Block block, TileEntity tile, World world, ICoord coord, Entity exploder, Explosion explosion)
	{
		return 0.0F;
	}
	
	@Override
	public void addSubBlock(Block block, int meta, Item item, List<ItemStack> list, CreativeTabs tab)
	{
		
	}
	
	@Override
	public boolean isFullCube(Block block, IBlockState state)
	{
		return true;
	}
	
	@Override
	public Material getMaterial(Block block, IBlockState state)
	{
		return Material.ROCK;
	}
	
	@Override
	public MapColor getMapColor(Block block, IBlockState state)
	{
		return MapColor.PURPLE;
	}
	
	@Override
	public SoundType getSoundType(Block block, TileEntity tile, IBlockState state, World world, ICoord coord, Entity entity)
	{
		return SoundType.GROUND;
	}
}
