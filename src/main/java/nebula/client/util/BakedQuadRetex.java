/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The baked quad with new texture.
 * <p>
 * The class
 * {@link net.minecraft.client.renderer.block.model.BakedQuadRetextured}, has a
 * bug that it can not reuse like:
 * <p>
 * <code>new BakedQuadRetextured(new BakedQuadRetextured(quad, tex1), tex2)</code>
 * <p>
 * For this will repositioned to wrong texture coordinate.
 * 
 * @author ueyudiud
 * @see net.minecraft.client.renderer.block.model.BakedQuadRetextured
 */
@SideOnly(Side.CLIENT)
public class BakedQuadRetex extends BakedQuad
{
	public BakedQuadRetex(BakedQuad quad, TextureAtlasSprite textureIn)
	{
		super(quad.getVertexData().clone(), quad.getTintIndex(), FaceBakery.getFacingFromVertexData(quad.getVertexData()), textureIn, quad.shouldApplyDiffuseLighting(), quad.getFormat());
		remapQuad(quad.getSprite());
	}
	
	/**
	 * Fixed remapping method.
	 * 
	 * @param sprite the old sprite.
	 */
	private void remapQuad(TextureAtlasSprite sprite)
	{
		for (int i = 0; i < 4; ++i)
		{
			int j = this.format.getIntegerSize() * i;
			int uvIndex = this.format.getUvOffsetById(0) / 4;
			this.vertexData[j + uvIndex] = Float.floatToRawIntBits(this.sprite.getInterpolatedU(sprite.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[j + uvIndex]))));
			this.vertexData[j + uvIndex + 1] = Float.floatToRawIntBits(this.sprite.getInterpolatedV(sprite.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[j + uvIndex + 1]))));
		}
	}
}
