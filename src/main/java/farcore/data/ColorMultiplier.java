package farcore.data;

import farcore.lib.block.instance.BlockSoil;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.ItemTool;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The color applying function.
 * @author ueyudiud
 *
 */
@SideOnly(Side.CLIENT)
public class ColorMultiplier
{
	public static final IItemColor MULTI_ITEM_MATERIAL_COLOR =
			(ItemStack stack, int tintIndex) ->
	{
		switch (tintIndex)
		{
		//0 for base.
		case 1 : return ItemMulti.getMaterial(stack).RGB;
		//2 for override.
		default: return 0xFFFFFFFF;
		}
	};
	
	public static final IItemColor TOOL_ITEM_MATERIAL_COLOR =
			(ItemStack stack, int tintIndex) ->
	{
		return ItemTool.getColor(stack, tintIndex);
	};
	
	@Deprecated
	public static final IItemColor ITEMBLOCK_COLOR = (ItemStack stack, int tintIndex) ->
	{
		Block block = Block.getBlockFromItem(stack.getItem());
		return Minecraft.getMinecraft().getBlockColors().colorMultiplier(block.getStateFromMeta(stack.getMetadata()), null, null, tintIndex);
	};
	
	public static final IBlockColor SOIL_COLOR = (IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
	{
		if(tintIndex != 0) return 0xFFFFFFFF;
		BlockSoil.EnumCoverType type = state.getValue(BlockSoil.COVER_TYPE);
		switch (type)
		{
		case GRASS : return worldIn == null || pos == null ? ColorizerGrass.getGrassColor(0.7F, 0.7F) : worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
		default : return 0xFFFFFFFF;
		}
	};
	
	public static final IItemColor ITEM_SOIL_COLOR = (ItemStack stack, int tintIndex) ->
	{
		if(tintIndex != 0) return 0xFFFFFFFF;
		BlockSoil.EnumCoverType type = BlockSoil.EnumCoverType.VALUES[stack.getItemDamage()];
		switch (type)
		{
		case GRASS : return ColorizerGrass.getGrassColor(0.7F, 0.7F);
		default : return 0xFFFFFFFF;
		}
	};
	
	public static final IBlockColor BIOME_COLOR =
			(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
	{
		boolean flag = worldIn == null || pos == null;
		Biome biome = flag ? null : worldIn.getBiomeGenForCoords(pos);
		switch(tintIndex)
		{
		case 0 : return flag ? ColorizerGrass.getGrassColor(0.7F, 0.7F) :
			BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
		case 1 : return flag ? ColorizerFoliage.getFoliageColorBasic() :
			BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
		case 2 : return flag ? -1 : BiomeColorHelper.getWaterColorAtPos(worldIn, pos);
		default: return -1;
		}
	};
}