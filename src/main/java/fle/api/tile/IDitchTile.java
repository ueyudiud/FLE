/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import farcore.lib.capability.IFluidHandler;
import farcore.lib.material.Mat;
import nebula.Log;
import nebula.Nebula;
import nebula.common.util.Direction;
import nebula.common.util.IDataChecker;
import nebula.common.world.IModifiableCoord;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface IDitchTile extends IModifiableCoord, IFluidHandler
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
						if(Nebula.debug) throw exception;
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
	
	@Override
	default boolean canFill(Direction direction, FluidStack stack)
	{
		switch (direction)
		{
		case D :
		case A :
		case B :
			return false;
		case Q :
			return getTank().canFillFluidType(stack);
		default:
			return isLinked(direction) && getTank().canFillFluidType(stack);
		}
	}
	
	@Override
	default boolean canDrain(Direction direction, FluidStack stack)
	{
		switch (direction)
		{
		case D :
		case A :
		case B :
			return false;
		case Q :
			return getTank().canDrainFluidType(stack);
		default:
			return isLinked(direction) && getTank().canDrainFluidType(stack);
		}
	}
	
	@Override
	default SidedFluidIOProperty getProperty(Direction direction)
	{
		return new IFluidHandler.SidedFluidIOTankPropertyWrapper(getTank());
	}
	
	@Override
	default int fill(Direction direction, FluidStack resource, boolean process)
	{
		switch (direction)
		{
		case D :
		case A :
		case B :
			return 0;
		case Q :
			return getTank().fill(resource, process);
		default:
			return !isLinked(direction) ? 0 : getTank().fill(resource, process);
		}
	}
	
	@Override
	default FluidStack drain(Direction direction, FluidStack required, boolean process)
	{
		switch (direction)
		{
		case D :
		case A :
		case B :
			return null;
		case Q :
			return getTank().drain(required, process);
		default:
			return !isLinked(direction) ? null : getTank().drain(required, process);
		}
	}
	
	@Override
	default FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		switch (direction)
		{
		case D :
		case A :
		case B :
			return null;
		case Q :
			return getTank().drain(maxAmount, process);
		default:
			return !isLinked(direction) ? null : getTank().drain(maxAmount, process);
		}
	}
}