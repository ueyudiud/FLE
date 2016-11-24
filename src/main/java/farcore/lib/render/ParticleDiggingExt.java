package farcore.lib.render;

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
	public ParticleDiggingExt(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, IBlockState state)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
	}
	
	public ParticleDiggingExt(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, IBlockState state, Object location)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
		if(location instanceof ResourceLocation)
		{
			setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		else if(location instanceof IBlockState)
		{
			setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture((IBlockState) location));
		}
		else if(location instanceof TextureAtlasSprite)
		{
			setParticleTexture((TextureAtlasSprite) location);
		}
		else throw new RuntimeException("The particle texture " + location + " can not be resolved.");
	}
}