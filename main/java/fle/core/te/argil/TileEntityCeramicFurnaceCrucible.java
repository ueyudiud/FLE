package fle.core.te.argil;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.energy.IThermalTileEntity;
import flapi.material.IChemCondition;
import flapi.material.IMolecular;
import flapi.material.MaterialAbstract;
import flapi.material.MaterialAlloy;
import flapi.material.Matter.AtomStack;
import flapi.material.MatterDictionary;
import flapi.te.TEIFluidTank;
import flapi.te.interfaces.IMatterContainer;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Lang;
import fle.core.init.Materials;
import fle.core.net.FleMatterUpdatePacket;
import fle.core.util.MatterContainer;

public class TileEntityCeramicFurnaceCrucible extends TEIFluidTank implements IThermalTileEntity, IChemCondition, IMatterContainer
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	public int[] progress = new int[3];
	public MatterContainer container = new MatterContainer();
	private int buf1 = 0;

	public TileEntityCeramicFurnaceCrucible()
	{
		super(3, 3000);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
		nbt.setIntArray("Progress", progress);
		nbt.setTag("Matters", container.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
		progress = nbt.getIntArray("Progress");
		container.readFromNBT(nbt.getCompoundTag("Matters"));
	}
	
	@Override
	public void update()
	{
		int tem = getTemperature();
		for(int i = 0; i < stacks.length; ++i)
		{
			int[] is = MatterDictionary.getMeltRequires(stacks[i]);
			if(is[0] <= tem)
			{
				int tick = (tem - is[0]) * 2;
				onHeatEmit(ForgeDirection.UNKNOWN, tick);
				progress[i] += tick;
				if(progress[i] >= is[1])
				{
					meltItem(i);
				}
			}
			else if(stacks[i] == null)
			{
				progress[i] = 0;
			}
			else if(progress[i] > 0)
			{
				--progress[i];
			}
		}
		if(buf1 < 20) ++buf1;
		else
		{
			updateMatter();
			buf1 = 0;
			sendToNearBy(new FleMatterUpdatePacket(this, this), 16.0F);
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	private void updateMatter()
	{
		if(isClient()) return;
		container.update(getBlockPos(), this);
	}
	private static final float lostSize = 0.25F;
	
	private void meltItem(int slotID)
	{
		AtomStack stack = MatterDictionary.getMatter(stacks[slotID]).copy();
		stack.size = (int) (stack.size() * lostSize);
		container.add(stack);
		--stacks[slotID].stackSize;
		if(stacks[slotID].stackSize <= 0) stacks[slotID] = null;
		progress[slotID] = 0;
	}
	
	public void outputStack(TileEntityCeramicFurnaceCrucible tile)
	{
		MaterialAbstract m = MaterialAlloy.findAlloy(container.getHelper());
		if(m != null)
		{
			FluidStack stack = MatterDictionary.getFluid(new AtomStack(m.getMatter(), container.size()));
			if(tank.fill(stack, false) != 0)
			{
				tank.fill(stack, true);
				container.clear();
			}
		}
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return dir == ForgeDirection.DOWN && getBlockPos().toPos(dir).getBlockTile() instanceof IThermalTileEntity ?
				tc.getThermalConductivity() * 6 : tc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return tc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		tc.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		tc.emitHeat(heatValue);
	}
	
	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}

	@Override
	public EnumPH getPHLevel()
	{
		return EnumPH.Water;
	}

	@Override
	public EnumOxide getOxideLevel()
	{
		return EnumOxide.C;
	}

	@Override
	public int getTemperature()
	{
		return getTemperature(ForgeDirection.UNKNOWN);
	}
	
	@SideOnly(Side.CLIENT)
	public Map<IMolecular, Integer> getContainerMap()
	{
		return container.getMatterContain();
	}

	@Override
	public Map<IMolecular, Integer> getMatterContain()
	{
		return container.getMatterContain();
	}

	@Override
	public void setMatterContain(Map<IMolecular, Integer> map)
	{
		container.setMatterContain(map);
	}

	public void onOutput()
	{
		MaterialAbstract m = MaterialAlloy.findAlloy(container.getHelper());
		if(m != null)
		{
			FluidStack stack = MatterDictionary.getFluid(new AtomStack(m.getMatter(), container.size()));
			if(tank.fill(stack, false) != 0)
			{
				tank.fill(stack, true);
				container.clear();
			}
		}
	}

	public void drain()
	{
		tank.drain(tank.getCapacity(), true);
	}

	@Override
	public EnumEnviorment isOpenEnviorment()
	{
		return EnumEnviorment.Open;
	}
	
	public int[] getProgress()
	{
		return progress;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP ? MatterDictionary.getMatter(aResource) != null : false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_ceramicFurnace;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i < 3;
	}
}