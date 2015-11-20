package fle.core.te.argil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.energy.IThermalTileEntity;
import fle.api.enums.EnumDamageResource;
import fle.api.net.INetEventListener;
import fle.api.recipe.ItemOreStack;
import fle.api.te.TEInventory;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Materials;
import fle.core.inventory.InventoryCeramicFurnaceFirebox;

public class TileEntityCeramicFurnaceFirebox extends TEInventory<InventoryCeramicFurnaceFirebox> implements ISidedInventory, IThermalTileEntity, INetEventListener
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	
	public TileEntityCeramicFurnaceFirebox()
	{
		super(new InventoryCeramicFurnaceFirebox());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
	}

	@Override
	public void updateInventory()
	{
		getTileInventory().updateEntity(this);
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[]{0, 1, 2};
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			int aSide) 
	{
		return super.canInsertItem(aSlotID, aResource, aSide) && FleAPI.getFulBuf(aResource, FLE.fle.getAirConditionProvider().getAirLevel(getBlockPos())) >= 0;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aTarget,
			int aSide)
	{
		return false;
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP && getBlockPos().toPos(ForgeDirection.UP).getBlockTile() instanceof IThermalTileEntity ?
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

	public boolean isBurning()
	{
		return getTileInventory().isBurning();
	}

	@SideOnly(Side.CLIENT)
	public int getBurnProgress(int length)
	{
		return (int) (getTileInventory().getBurnProgress() * (float) length);
	}
	
	public void onToolClick(ItemStack aStack, EntityLivingBase aPlayer)
	{
	    if ((aStack == null) || (aPlayer == null))
	    {
	    	return;
	    }
	    if (new ItemOreStack("craftingToolFirestarter").isStackEqul(aStack))
	    {
	    	getTileInventory().setBurning();
	    	FleAPI.damageItem(aPlayer, aStack, EnumDamageResource.UseTool, 0.25F);
	    }
	}
	
	@Override
	public Object onEmmit(byte aType)
	{
		if(aType == 2) return getTileInventory().getSyncState();
		return super.onEmmit(aType);
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 1)
		{
			setDirction(ForgeDirection.VALID_DIRECTIONS[(Integer) contain]);
		}
		else if(type == 2)
		{
			getTileInventory().markBurnState((Long) contain);
		}
	}
}