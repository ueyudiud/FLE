/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.wooden;

import static fle.api.recipes.instance.RecipeMaps.LEVER_OIL_MILL;

import java.io.IOException;

import farcore.data.Capabilities;
import farcore.data.Keys;
import farcore.lib.capability.IFluidHandler;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.core.client.gui.wooden.GuiLeverOilMill;
import fle.core.common.gui.wooden.ContainerLeverOilMill;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.fluid.FluidTankN;
import nebula.common.inventory.InventoryWrapFactory;
import nebula.common.network.PacketBufferExt;
import nebula.common.stack.AbstractStack;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.TEInventoryTankSingleAbstract;
import nebula.common.util.Direction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TELeverOilMill extends TEInventoryTankSingleAbstract
implements ITB_BlockActived, IGuiTile, ISidedInventory, INetworkedSyncTile
{
	private static final int[] IN = {0}, OUT = {1};
	
	public final FluidTankN tank = new FluidTankN(4000);
	protected int energy;
	protected int progress;
	protected int maxProgress;
	protected int angle;
	protected TemplateRecipeCache<ItemStack> cache;
	
	public TELeverOilMill()
	{
		super(2);
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? OUT : IN;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index == 0 && direction == EnumFacing.UP && LEVER_OIL_MILL.findRecipe(stack) != null;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index == 1 && direction != EnumFacing.UP;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.cache = LEVER_OIL_MILL.readFromNBT(compound, "recipe");
		if (this.cache != null)
		{
			this.maxProgress = this.cache.get(0);
			this.progress = compound.getInteger("progress");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		LEVER_OIL_MILL.writeToNBT(this.cache, compound, "recipe");
		compound.setInteger("progress", this.progress);
		return compound;
	}
	
	@Override
	public String getName()
	{
		return "inventory.lever.oil.mill";
	}
	
	@Override
	protected FluidTankN tank()
	{
		return this.tank;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.cache == null && this.stacks[0] != null)
		{
			this.cache = LEVER_OIL_MILL.findRecipe(this.stacks[0]);
			if (this.cache != null)
			{
				decrStackSize(0, this.cache.<AbstractStack>get1(0));
				this.maxProgress = this.cache.get(0);
			}
		}
		if (this.cache != null && this.energy > 80)
		{
			if (this.progress++ >= this.cache.<Integer>get(0))
			{
				this.progress = this.cache.get(0);
				if (insertStack(0, this.cache.<ItemStack>get(1), false) == this.cache.<ItemStack>get(1).stackSize &&
						this.tank.insertFluid(this.cache.<FluidStack>get(2), true))
				{
					insertStack(1, this.cache.get(1), true);
					this.tank.insertFluid(this.cache.get(2), false);
					this.cache = null;
					this.progress = 0;
				}
			}
		}
		if (this.energy > 0)
		{
			--this.energy;
		}
		int drain = 1000;
		for (int i = 0; i < 4 && drain > 0; ++i)
		{
			drain -= sendFluidTo(drain, Direction.DIRECTIONS_2D[i], true);
		}
	}
	
	@Override
	protected void updateClient()
	{
		super.updateClient();
		if (this.energy > 0)
		{
			this.energy--;
			this.angle++;
			if (this.angle >= 100)
			{
				this.angle = 0;
			}
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		if (isServer())
		{
			if (NebulaKeyHandler.get(player, Keys.ROTATE))
			{
				onRotateMill(player);
				NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
			}
			else
			{
				openGUI(player, 0);
				syncToPlayer(player);
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		buf.writeInt(this.energy);
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		this.energy = buf.readInt();
	}
	
	public void onRotateMill(EntityPlayer player)
	{
		this.energy = Math.min(this.energy + 10, 100);
		if (isServer())
		{
			player.addExhaustion(0.0025F);
		}
	}
	
	@Override
	public int getFieldCount()
	{
		return 3;
	}
	
	@Override
	public int getField(int id)
	{
		switch (id)
		{
		case 0 : return this.progress;
		case 1 : return this.energy;
		case 2 : return this.maxProgress;
		default: return super.getField(id);
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
		case 0 : this.progress = value; break;
		case 1 : this.energy = value; break;
		case 2 : this.maxProgress = value; break;
		default: super.setField(id, value); break;
		}
	}
	
	public int getMaxProgress()
	{
		return this.maxProgress;
	}
	
	public int getProgress()
	{
		return this.progress;
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new ContainerLeverOilMill(this, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(int id, EntityPlayer player)
	{
		return new GuiLeverOilMill(this, player);
	}
	
	@SideOnly(Side.CLIENT)
	public float getRotationAngle()
	{
		return - MathHelper.cos((float) (this.angle * Math.PI / 50));
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.CAPABILITY_FLUID && facing != EnumFacing.UP ||
				capability == Capabilities.CAPABILITY_ITEM ||
				super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == Capabilities.CAPABILITY_FLUID && facing != EnumFacing.UP ?
				Capabilities.CAPABILITY_FLUID.cast(new IFluidHandler.FluidHandlerWrapper(this, facing)) :
					capability == Capabilities.CAPABILITY_ITEM ? Capabilities.CAPABILITY_ITEM.cast(InventoryWrapFactory.wrap(getName(), this)) :
						super.getCapability(capability, facing);
	}
}