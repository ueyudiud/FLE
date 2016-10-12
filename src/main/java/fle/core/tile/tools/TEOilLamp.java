package fle.core.tile.tools;

import farcore.data.EnumToolType;
import farcore.lib.material.Mat;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Containerable;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Light;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.TESynchronization;
import farcore.lib.util.Direction;
import fle.core.gui.ContainerOilLampCrafting;
import fle.core.gui.GuiOilLampCrafting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TEOilLamp extends TESynchronization
implements ITP_Light, IToolableTile, ITB_Containerable
{
	public static final byte HasWick = 0x10;
	public static final byte Burning = 0x11;
	public static final byte HasSmoke = 0x12;
	
	public Mat material;
	public long fuelAmount;
	public byte fuelLightValue;
	public long fuelBurnTime;
	public long fuelTotalBurnTime;
	
	public TEOilLamp()
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = Mat.material(nbt.getString("material"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", material.name);
		return super.writeToNBT(nbt);
	}
	
	@Override
	protected void updateClient()
	{
		super.updateClient();
		if(is(Burning) && is(HasSmoke))
		{
			if(random.nextInt(4) == 0)
			{
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + .5, pos.getY() + .8, pos.getZ() + .5, 0F, 0.01F, 0F);
			}
		}
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		burnFuel();
	}

	protected void burnFuel()
	{
		if(is(Burning))
		{
			if(!is(HasWick))
			{
				disable(Burning);
				return;
			}
			if(fuelBurnTime <= 0)
			{
				if(fuelAmount > 0)
				{
					fuelAmount --;
					fuelBurnTime = fuelTotalBurnTime;
				}
				else
				{
					disable(Burning);
					return;
				}
			}
			--fuelBurnTime;
		}
	}

	@Override
	public int getLightValue(IBlockState state)
	{
		return is(Burning) ? fuelLightValue : 0;
	}

	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		if(tool == EnumToolType.firestarter)
		{
			if(is(HasWick) && !is(Burning))
			{
				change(Burning);
				return new ActionResult(EnumActionResult.SUCCESS, 0.2F);
			}
			else
				return new ActionResult(EnumActionResult.FAIL, 0F);
		}
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new ContainerOilLampCrafting(player, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGUI(int id, EntityPlayer player)
	{
		return new GuiOilLampCrafting(player, this);
	}
}