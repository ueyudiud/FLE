/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import java.util.List;

import mezz.jei.api.IModRegistry;
import nebula.client.util.UnlocalizedList;
import nebula.common.util.Game;
import nebula.common.world.ICoord;
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
public abstract class TileDelegateBlank<B extends BlockDelegated, T extends TileEntity> implements ITileDelegate<B, T>
{
	protected Material material;
	protected SoundType sound;
	
	public TileDelegateBlank(Material material, SoundType sound)
	{
		this.material = material;
		this.sound = sound;
	}
	
	protected abstract String getUnlocalizedName(B block);
	
	@Override
	public String getUnlocalizedName(B block, ItemStack stack)
	{
		return getUnlocalizedName(block);
	}
	
	@Override
	public String getUnlocalizedName(B block, T tile, IBlockState stack, World world, ICoord coord)
	{
		return getUnlocalizedName(block);
	}
	
	@Override
	public void load(B block)
	{
		
	}
	
	@Override
	public void postload(B block)
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderload(B block)
	{
		
	}
	
	@Override
	@Optional.Method(modid = Game.MOD_JEI)
	public void loadJEI(B block, Item item, IModRegistry registry)
	{
		
	}
	
	@Override
	public boolean isOpaqueCube(B block, IBlockState state)
	{
		return true;
	}
	
	@Override
	public boolean isTranslucent(B block, IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean canCollideCheck(B block, IBlockState state, boolean hitIfLiquid)
	{
		return true;
	}
	
	@Override
	public void addInformation(B block, ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(B block, IBlockState state)
	{
		return 0.2F;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(B block, IBlockState state)
	{
		return this.material.getMobilityFlag();
	}
	
	@Override
	public boolean hasTileEntity(B block, IBlockState state)
	{
		return false;
	}
	
	@Override
	public T createTileEntity(B block, IBlockState state, World world)
	{
		return null;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.SOLID;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(B block, IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public float getBlockHardness(B block, T tile, IBlockState state, World world, ICoord coord)
	{
		return 0;
	}
	
	@Override
	public float getExplosionResistance(B block, T tile, World world, ICoord coord, Entity exploder, Explosion explosion)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addSubBlock(B block, int meta, Item item, List<ItemStack> list, CreativeTabs tab)
	{
		list.add(new ItemStack(item, 1, meta));
	}
	
	@Override
	public boolean isFullCube(B block, IBlockState state)
	{
		return true;
	}
	
	@Override
	public Material getMaterial(B block, IBlockState state)
	{
		return this.material;
	}
	
	@Override
	public MapColor getMapColor(B block, IBlockState state)
	{
		return this.material.getMaterialMapColor();
	}
	
	@Override
	public SoundType getSoundType(B block, T tile, IBlockState state, World world, ICoord coord, Entity entity)
	{
		return this.sound;
	}
}
