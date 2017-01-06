/*
 * copyright© 2016 ueyudiud
 */

package fle.api.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import farcore.FarCore;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.Log;
import farcore.lib.world.IModifiableCoord;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface IDitchTile extends IModifiableCoord
{
	public static final class DitchBlockHandler implements IDataChecker<Mat>
	{
		public static final List<Mat> DITCH_ALLOWED_MATERIALS = new ArrayList();
		
		public static final DitchBlockHandler HANDLER = new DitchBlockHandler();
		
		private static final List<DitchFactory> FACTORIES = new ArrayList();
		public static DitchFactory rawFactory;
		
		public static void addMaterial(Mat material)
		{
			DITCH_ALLOWED_MATERIALS.add(material);
		}
		
		public static void addFactory(DitchFactory factory)
		{
			FACTORIES.add(factory);
		}
		
		public static DitchFactory getFactory(@Nullable Mat material)
		{
			if(material == null) return rawFactory;
			DitchFactory select = null;
			for(DitchFactory factory : FACTORIES)
			{
				if(factory.access(material))
				{
					if(select != null)
					{
						RuntimeException exception = new RuntimeException("The factory " + select + " and " + factory + " both want to handle material " + material.name + ".");
						if(FarCore.debug) throw exception;
						Log.warn("Catching an exception during getting ditch factory.", exception);
					}
					select = factory;
				}
			}
			return select == null ? rawFactory : select;
		}
		
		DitchBlockHandler() {	}
		
		@Override
		public boolean isTrue(Mat target)
		{
			return DITCH_ALLOWED_MATERIALS.contains(target);
		}
	}
	
	public static interface DitchFactory
	{
		boolean access(Mat material);
		
		FluidTank apply(IDitchTile tile);
		
		void onUpdate(IDitchTile tile);
		
		/**
		 * Get multiple speed, use mL/tick for unit.
		 * @param tile
		 * @return
		 */
		int getSpeedMultiple(IDitchTile tile);
		
		/**
		 * Get max fluid transfer limit, use L for unit.
		 * @param tile
		 * @return
		 */
		int getMaxTransferLimit(IDitchTile tile);
		
		@SideOnly(Side.CLIENT)
		TextureAtlasSprite getMaterialIcon(Mat material);
	}
	
	default Fluid getFluidContain()
	{
		return getTank().getFluid() != null ? getTank().getFluid().getFluid() : null;
	}
	
	Mat getMaterial();
	
	FluidTank getTank();
	
	float getFlowHeight();
	
	boolean isLinked(Direction direction);
	
	void setLink(Direction direction, boolean state);
}