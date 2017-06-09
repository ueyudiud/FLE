/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface IStateMapperExt extends IStateMapper
{
	ModelResourceLocation getLocationFromState(IBlockState state);
}