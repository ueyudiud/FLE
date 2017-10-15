/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import farcore.lib.block.terria.BlockSoil;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.ItemTool;
import nebula.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.world.ColorizerGrass;
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
	public static final IItemColor MULTI_ITEM_MATERIAL_COLOR = (stack, tintIndex) ->
	{
		switch (tintIndex)
		{
		//0 for base.
		case 1 : return ItemMulti.getMaterial(stack).RGB;
		//2 for override.
		default: return 0xFFFFFFFF;
		}
	};
	
	public static final IItemColor TOOL_ITEM_MATERIAL_COLOR = (stack, tintIndex) ->
	{
		return ItemTool.getColor(stack, tintIndex);
	};
	
	@Deprecated
	public static final IItemColor ITEMBLOCK_COLOR = (stack, tintIndex) ->
	{
		Block block = Block.getBlockFromItem(stack.getItem());
		return Minecraft.getMinecraft().getBlockColors().colorMultiplier(block.getStateFromMeta(stack.getMetadata()), null, null, tintIndex);
	};
	
	public static final IBlockColor SOIL_COLOR = (state, worldIn, pos, tintIndex) ->
	{
		if(tintIndex != 0) return 0xFFFFFFFF;
		BlockSoil.EnumCoverType type = state.getValue(BlockSoil.COVER_TYPE);
		switch (type.getNoCover())
		{
		case GRASS : return worldIn == null || pos == null ? ColorizerGrass.getGrassColor(0.7F, 0.7F) : worldIn.getBiome(pos).getGrassColorAtPos(pos);
		default : return 0xFFFFFFFF;
		}
	};
	
	public static final IItemColor ITEM_SOIL_COLOR = (stack, tintIndex) ->
	{
		if(tintIndex != 0) return 0xFFFFFFFF;
		BlockSoil.EnumCoverType type = BlockSoil.EnumCoverType.VALUES[stack.getItemDamage()];
		switch (type.getNoCover())
		{
		case GRASS : return ColorizerGrass.getGrassColor(0.7F, 0.7F);
		default : return 0xFFFFFFFF;
		}
	};
	
	public static final IBlockColor BIOME_COLOR = ClientProxy.BIOME_COLOR;
}