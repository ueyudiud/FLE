package fle.resource.tile;

import farcore.block.BlockHook;
import farcore.collection.abs.Stack;
import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Atom;
import farcore.substance.AtomRadical;
import farcore.substance.Ion;
import farcore.substance.Matter;
import farcore.substance.Substance;
import farcore.tile.TEBase;
import farcore.tile.TEUpdatable;
import farcore.util.Vs;
import farcore.world.BlockPos;
import farcore.world.Direction;
import fle.core.enums.EnumDirtCover;
import fle.core.enums.EnumDirtState;
import fle.init.Blocks;
import fle.init.Substances;

public class TileEntityDirt extends TEUpdatable
{
	@NBTLoad(name = "dirt")
	@NBTSave(name = "dirt")
	public Substance dirt = Substances.$void;
	
	@NBTLoad(name = "calcium")
	@NBTSave(name = "calcium")
	public int Ca;
	@NBTLoad(name = "potassium")
	@NBTSave(name = "potassium")
	public int K;
	@NBTLoad(name = "phosphate")
	@NBTSave(name = "phosphate")
	public int PO4;
	@NBTLoad(name = "ammonium")
	@NBTSave(name = "ammonium")
	public int NH4;
	@NBTLoad(name = "nitrate")
	@NBTSave(name = "nitrate")
	public int NO3;
	@NBTLoad(name = "carbamide")
	@NBTSave(name = "carbamide")
	public int CON2H4;
	@NBTLoad(name = "carbonate")
	@NBTSave(name = "carbonate")
	public int CO3;
	@NBTLoad(name = "water")
	@NBTSave(name = "water")
	public int H2O;
	
	@NBTLoad(name = "PH")
	@NBTSave(name = "PH")
	public int PH;
	@NBTLoad(name = "state")
	@NBTSave(name = "state")
	public EnumDirtState state = EnumDirtState.dirt;
	public EnumDirtCover cover = EnumDirtCover.nothing;
//	@NBTLoad(name = "plant")
//	@NBTSave(name = "plant")
//	public IPlant plant;
	int buf = 0;
	
	public void add(Stack<Matter> stack)
	{
		AtomRadical ar = stack.get().get();
		Ca += ar.getParticleCount(Atom.Ca, false) * stack.size;
		K += ar.getParticleCount(Atom.K, false) * stack.size;
		PO4 += ar.getParticleCount(Ion.ion("PO4"), false) * stack.size / 5;
		NH4 += ar.getParticleCount(Ion.ion("NH4"), false) * stack.size / 5;
		NO3 += ar.getParticleCount(Ion.ion("NO3"), false) * stack.size / 4;
		CON2H4 += ar.getParticleCount(AtomRadical.radical("CO(NH2)2"), false)
				* stack.size / 3;
		CO3 += ar.getParticleCount(Ion.ion("CO3"), false) * stack.size / 4;
		H2O += ar.getParticleCount(AtomRadical.radical("H2O"), false) / 3;
		PH += ar.getParticleCount(Ion.ion("OH"), false)
				- ar.getParticleCount(Ion.ion("H"), false);
	}
	
	public int useCalcium(int amount, boolean d)
	{
		int a = Math.min(amount, Ca);
		if (d)
		{
			Ca -= a;
			PH -= a;
		}
		return a;
	}
	
	public int useNitrogen(int amount, boolean d)
	{
		int c = 0;
		if (NH4 != 0)
		{
			++c;
		}
		if (NO3 != 0)
		{
			++c;
		}
		if (CON2H4 != 0)
		{
			++c;
		}
		int b = amount / c;
		int aNH4 = b;
		int aNO3 = b;
		int aCON2H4 = b;
		int b1 = amount % c;
		if (NH4 < b)
		{
			b1 += NH4 - b;
			aNH4 = NH4;
		}
		if (NO3 < b)
		{
			b1 += NO3 - b;
			aNO3 = NO3;
		}
		if (CON2H4 < b)
		{
			b1 += CON2H4 - b;
			aCON2H4 = CON2H4;
		}
		if (CON2H4 > aCON2H4 && b1 > 0)
		{
			int q = Math.min(CON2H4 - aCON2H4, b1);
			aCON2H4 += q;
			b1 -= q;
		}
		if (NO3 > aCON2H4 && b1 > 0)
		{
			int q = Math.min(NO3 - aNO3, b1);
			aNO3 += q;
			b1 -= q;
		}
		if (NH4 > aNH4 && b1 > 0)
		{
			int q = Math.min(NH4 - aNH4, b1);
			aNH4 += q;
			b1 -= q;
		}
		PH += aNH4 - aNO3;
		return amount - b1;
	}
	
	public int usePhosphorus(int amount, boolean d)
	{
		int a = Math.min(amount, PO4);
		if (d)
		{
			PO4 -= a;
			PH += a;
		}
		return a;
	}
	
	public int usePotassium(int amount, boolean d)
	{
		int a = Math.min(amount, K);
		if (d)
		{
			K -= a;
			PH -= a;
		}
		return a;
	}
	
	public int useWater(int amount, boolean d)
	{
		int a = Math.min(amount, H2O);
		if (d)
		{
			H2O -= a;
		}
		return a;
	}
	
	public int useAmmonia(int amount, boolean d)
	{
		int a = Math.min(amount, NH4);
		if (d)
		{
			NH4 -= a;
			PH -= a;
		}
		return a;
	}

	@Override
	public void init()
	{
		
	}

	@Override
	public void onClientUpdate()
	{
		if(++buf == 100)
		{
			buf = 0;
			if(rand.nextInt(10) == 0)
			{
				cover = EnumDirtCover.nothing;
				markRenderForUpdate();
			}
		}
		if(cover != EnumDirtCover.snow && 
				worldObj.getBlock(xCoord, yCoord + 1, zCoord) == net.minecraft.init.Blocks.snow_layer)
		{
			cover = EnumDirtCover.snow;
			markRenderForUpdate();
		}
		else if(cover != EnumDirtCover.water && 
				BlockHook.isBlockWater(new BlockPos(this).offset(Direction.UP)))
		{
			cover = EnumDirtCover.water;
			markRenderForUpdate();
		}
	}

	@Override
	public void update(long tick, long tick1)
	{
//		try
//		{
//			if (plant != null)
//				plant.update(this);
//		}
//		catch (Exception exception)
//		{
//			FleLog.debug(
//					"Catching exception during update plant, plase check wether the plant is valid.");
//			throw new RuntimeException(exception);
//		}
		long temp = getEnviormentTemperature();
		double scale = Math.sqrt(Math.abs(tick1));
		double range;
		if (CO3 > 0 && PH < 38000 && 
				(range = rand.nextDouble() * (double) (38000 - PH) / 1000D * scale) >= 1)
		{
			CO3 -= Math.floor(range);
			PH += Math.floor(range);
		}
		if (NH4 > 0 && PH > 72000 && 
				(range = rand.nextDouble() * (double) (PH - 72000) / 1000D * scale) >= 1)
		{
			NH4 -= Math.floor(range);
			PH -= Math.floor(range);
		}
		if (H2O > 0 && temp > Vs.water_freeze_point
				&& (range = rand.nextDouble() * (double) (temp - Vs.water_freeze_point) / 100D * scale) >= 1)
		{
			H2O -= Math.floor(range);
		}

		syncNBT();
	}
}