package fle.resource.tile;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import farcore.biology.plant.IPlant;
import farcore.block.EnumDirtState;
import farcore.collection.abs.Stack;
import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.Atom;
import farcore.substance.AtomRadical;
import farcore.substance.Ion;
import farcore.substance.Matter;
import farcore.substance.Substance;
import farcore.tileentity.INutritionTileEntity;
import farcore.tileentity.TEBase;
import farcore.util.FleLog;
import farcore.util.Vs;
import fle.init.Substance1;

public class TileEntityDirt extends TEBase
		implements IUpdatePlayerListBox, INutritionTileEntity
{
	@NBTLoad(name = "dirt")
	@NBTSave(name = "dirt")
	public Substance dirt = Substance1.$void;
	
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
	@NBTLoad(name = "plant")
	@NBTSave(name = "plant")
	public IPlant plant;
	
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
	
	public IPlant getPlant()
	{
		return plant;
	}
	
	@Override
	public void update()
	{
		if (isClient())
			return;
		try
		{
			if (plant != null)
				plant.update(this);
		}
		catch (Exception exception)
		{
			FleLog.debug(
					"Catching exception during update plant, plase check wether the plant is valid.");
			throw new RuntimeException(exception);
		}
		long temp = getEnviormentTemperature();
		if (CO3 > 0 && PH < 38000 && rand.nextInt(1000 / (38000 - PH) + 1) == 0)
		{
			--CO3;
			++PH;
		}
		if (NH4 > 0 && PH > 72000 && rand.nextInt(1000 / (PH - 72000) + 1) == 0)
		{
			--NH4;
			--PH;
		}
		if (H2O > 0 && temp > Vs.water_freeze_point
				&& rand.nextLong(100 / (temp - Vs.water_freeze_point) + 1) == 0)
		{
			--H2O;
		}
		syncNBT();
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
	public int getParticleContain(EnumNutrition nutrition)
	{
		switch (nutrition) {
		case Ca:
			return Ca;
		case H2O:
			return H2O;
		case K:
			return K;
		case N:
			return CON2H4 + NH4 + NO3;
		case NH3:
			return NH4;
		case P:
			return PO4;
		default:
			return 0;
		}
	}
	
	@Override
	public int useParticle(EnumNutrition nutrition, int max, boolean process)
	{
		switch (nutrition) {
		case Ca:
			return useCalcium(max, process);
		case H2O:
			return useWater(max, process);
		case K:
			return usePotassium(max, process);
		case N:
			return useNitrogen(max, process);
		case NH3:
			return useAmmonia(max, process);
		case P:
			return usePhosphorus(max, process);
		default:
			break;
		}
		return 0;
	}
}