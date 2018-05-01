package fle.core.blocks;

import java.util.List;
import java.util.Random;

import farcore.FarCoreRegistry;
import farcore.data.EnumItem;
import farcore.data.Materials;
import fle.core.FLE;
import fle.core.client.render.TESRFluidFase;
import fle.core.tile.tools.TEAdobeDrying;
import fle.core.tile.tools.TEOilLamp;
import fle.core.tile.tools.TEWoodenBowl;
import nebula.base.register.IRegister;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.StateMapperExt;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTools extends BlockTE
{
	public BlockTools()
	{
		super(FLE.MODID, "tools", Materials.ROCK);
		setLightOpacity(2);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Oillamp");
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Wooden Bowl");
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), "Brick Mold");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt map = new StateMapperExt(FLE.MODID, "tool", this.property_TE);
		registerRenderMapper(map);
		
		registerCustomBlockRender(map, 0, "tool/oillamp");
		registerCustomBlockRender(map, 1, "tool/wooden_bowl");
		registerCustomBlockRender(map, 2, "tool/wooden_brick_mold");
		
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FLE.MODID, "tool/brick_mold/duration"), state -> Integer.toString(BlockStateTileEntityWapper.<TEAdobeDrying> unwrap(state).duration));
		
		FarCoreRegistry.registerTESR(TEWoodenBowl.class, new TESRFluidFase<TEWoodenBowl>(0.3125F, 0.0625F, 0.3125F, 0.6875F, 0.25F, 0.6875F, t -> t.getTank().getInfo()));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return EnumItem.tool.item;
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "oillamp", TEOilLamp.class);
		register.register(1, "woodenbowl", TEWoodenBowl.class);
		register.register(2, "wooden_brick_mold", TEAdobeDrying.class);
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
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		// The item were already set, blocks needn't to be display.
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
