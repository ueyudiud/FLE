package farcore.energy.thermal;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.tile.IDebugableTile;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import farcore.lib.tile.TESynchronization;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TEThermalDebuger extends TESynchronization
implements IThermalHandler, IDebugableTile, ITB_BlockPlacedBy
{
	private byte mode = (byte) 0xFF;
	
	private double heat;
	private double input;
	private double lastInput;
	private double tc;
	private double hc;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("mode", mode);
		nbt.setDouble("heat", heat);
		nbt.setDouble("conductivity", tc);
		nbt.setDouble("capacity", hc);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		mode = nbt.getByte("mode");
		heat = nbt.getDouble("heat");
		tc = nbt.getDouble("conductivity");
		hc = nbt.getDouble("capacity");
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}

	@Override
	protected void updateServer()
	{
		super.updateServer();
		lastInput = input;
		input = 0;
	}

	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	@Override
	public boolean canConnectTo(Direction direction)
	{
		return true;
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (float) (heat / hc);
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		if(mode == 2)
		{
			if(!(getTE(direction) instanceof IThermalHandler))
				return tc / 100;
		}
		return tc;
	}
	
	@Override
	public void onHeatChange(Direction direction, double value)
	{
		if(mode < 0) return;
		if(mode != 1)
		{
			heat += value;
		}
		input += value;
	}

	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		NBTTagCompound nbt = U.ItemStacks.getOrSetupNBT(stack, false);
		tc = nbt.getFloat("conductivity");
		switch (mode = nbt.getByte("mode"))
		{
		case 0 :
		case 2 :
			hc = nbt.getFloat("capacity");
			break;
		case 1 :
			hc = 1;
			heat = nbt.getInteger("temperature");
			break;
		default:
			break;
		}
	}

	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add("HC:" + ChatFormatting.GREEN + hc + "J/K");
		list.add("TC:" + ChatFormatting.GREEN + tc + "W/K");
		list.add("Heat IO:" + ChatFormatting.RED + heat + "J" + ChatFormatting.WHITE + "(" + ChatFormatting.YELLOW + lastInput + ChatFormatting.WHITE + "W)");
	}
}