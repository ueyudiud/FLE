/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.blockstate;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;

/**
 * @author ueyudiud
 */
public class NebulaStateMap
{
	public Function<IBlockState, String> variantApplier;
	
	public static class Variant
	{
		Optional<ModelRotation> rotation;
	}
}