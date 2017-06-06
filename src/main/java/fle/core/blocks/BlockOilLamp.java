package fle.core.blocks;

import java.util.List;
import java.util.Random;

import farcore.data.EnumItem;
import fle.core.FLE;
import fle.core.tile.tools.TEOilLamp;
import nebula.base.IRegister;
import nebula.common.block.BlockTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOilLamp extends BlockTE
{
	public static final AxisAlignedBB AABB_OIL_LAMP = new AxisAlignedBB(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
	
	public BlockOilLamp()
	{
		super(FLE.MODID, "oil.lamp", Material.ROCK);
		setLightOpacity(2);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return EnumItem.tool.item;
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(this.property_TE, this.property_TE.parseValue("oillamp").get());
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register("oillamp", TEOilLamp.class);
		return true;
	}
	
	@Override
	public boolean canBreakBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_OIL_LAMP;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		super.getSubBlocks(item, tab, list);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}