/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleDiggingExt extends ParticleDigging
{
	public ParticleDiggingExt(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
	}
	
	/**
	 * The constructor of particle, which allowed replace custom icon.
	 * 
	 * @param worldIn the world.
	 * @param xCoordIn the particle x coordinate.
	 * @param yCoordIn the particle y coordinate.
	 * @param zCoordIn the particle z coordinate.
	 * @param xSpeedIn the particle x speed.
	 * @param ySpeedIn the particle y speed.
	 * @param zSpeedIn the particle z speed.
	 * @param state the block state.
	 * @param location the icon of particle, you can use ResourceLocation or
	 *            TextureAtlasSprite, or block state for particle icon
	 *            represented it.
	 */
	public ParticleDiggingExt(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state, Object location)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
		if (location instanceof ResourceLocation)
		{
			setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		else if (location instanceof IBlockState)
		{
			setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture((IBlockState) location));
		}
		else if (location instanceof TextureAtlasSprite)
		{
			setParticleTexture((TextureAtlasSprite) location);
		}
		else
			throw new RuntimeException("The particle texture " + location + " can not be resolved.");
	}
}
