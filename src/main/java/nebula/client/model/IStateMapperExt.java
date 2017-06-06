/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;

/**
 * @author ueyudiud
 */
public interface IStateMapperExt extends IStateMapper
{
	ModelResourceLocation getModelResourceLocation(IBlockState state);
}