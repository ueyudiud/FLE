package fle.core.te.argil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.energy.IThermalTileEntity;
import fle.api.enums.EnumDamageResource;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.net.INetEventListener;
import fle.api.recipe.ItemOreStack;
import fle.api.te.TEIT;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Materials;
import fle.core.inventory.InventoryBoilingHeater;

public class TileEntityBoilingHeater extends TEIT<InventoryBoilingHeater> implements IThermalTileEntity, INetEventListener
{
	private ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	public GuiCondition type = GuiError.DEFAULT;
	
	public TileEntityBoilingHeater()
	{
		super(new InventoryBoilingHeater());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
	}

	@Override
	protected void updateInventory()
	{
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		inv.updateEntity(this);
		tc.update();
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + tc.getTempreture();
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return tc.getThermalConductivity();
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

	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int i)
	{
		return inv.getRecipeProgress(i);
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnProgress(int i)
	{
		return inv.getBurnProgress(i);
	}

	public void onToolClick(ItemStack tStack, EntityPlayer aPlayer)
	{
		if(new ItemOreStack("craftingToolFirestarter").isStackEqul(tStack))
		{
			FleAPI.damageItem(aPlayer, tStack, EnumDamageResource.UseTool, 0.125F);
			inv.setBurning();
		}
	}

	public boolean isWorking()
	{
		return inv.key != null;
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 0)
			inv.setBT((Integer) contain);
		if(type == 1)
			inv.setCBT((Integer) contain);
	}
}