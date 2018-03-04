/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.wooden;

import static fle.api.recipes.instance.RecipeMaps.LEVER_OIL_MILL;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import farcore.data.Capabilities;
import farcore.data.Keys;
import farcore.data.M;
import farcore.lib.capability.IFluidHandlerHelper;
import farcore.lib.material.Mat;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.tile.TEITSRecipe;
import fle.core.client.gui.wooden.GuiLeverOilMill;
import fle.core.common.gui.wooden.ContainerLeverOilMill;
import nebula.base.A;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.fluid.FluidTankN;
import nebula.common.inventory.InventoryWrapFactory;
import nebula.common.network.PacketBufferExt;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_CustomModelData;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TELeverOilMill extends TEITSRecipe<ItemStack, TemplateRecipeCache<ItemStack>>
implements ITB_BlockActived, IGuiTile, ISidedInventory, INetworkedSyncTile, ITP_BlockHardness,
ITP_ExplosionResistance, ITP_CustomModelData, ITP_Drops, ITB_BlockPlacedBy
{
	private static final int[] IN = A.rangeIntArray(0, 1), OUT = A.rangeIntArray(1, 2);
	
	protected Mat materialFrame = M.oak;
	
	public final FluidTankN	tank	= new FluidTankN(4000);
	protected int			energy;
	protected int			angle;
	
	public TELeverOilMill()
	{
		super(2);
		this.syncTankState = false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.materialFrame = Mat.getMaterialByNameOrDefault(compound, "frame", M.oak);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("frame", this.materialFrame.name);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.materialFrame = Mat.getMaterialByIDOrDefault(nbt, "f", this.materialFrame);
	}
	
	@Override
	public void writeToClientInitalization(NBTTagCompound nbt)
	{
		super.writeToClientInitalization(nbt);
		nbt.setShort("f", this.materialFrame.id);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = new BaseStack(state).instance();
		Mat.setMaterialToStack(stack, "frame", this.materialFrame);
		return Lists.newArrayList(stack);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.materialFrame = Mat.getMaterialFromStack(stack, "frame", M.oak);
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 2.0F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 4.0F;
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
	protected ItemStack getRecipeInputHandler()
	{
		return this.stacks[0];
	}
	
	@Override
	protected IRecipeMap<?, TemplateRecipeCache<ItemStack>, ItemStack> getRecipeMap()
	{
		return LEVER_OIL_MILL;
	}
	
	@Override
	protected void onRecipeInput()
	{
		decrStackSize(0, this.cache.<AbstractStack> get1(0));
		this.recipeMaxTick = this.cache.get(0);
	}
	
	@Override
	protected int getPower()
	{
		return this.energy >= 80 ? 1 : 0;
	}
	
	@Override
	protected boolean onRecipeOutput()
	{
		if (instItem(0, this.cache.get(1), false) &&
				// this.tank.insertFluid(this.cache.<FluidStack>get(2), true)
				this.tank.insertFluid(this.cache.<FluidStack> get(2), false))
		{
			// this.tank.insertFluid(this.cache.<FluidStack>get(2), false);
			incrItem(1, this.cache.get(1), true);
			return true;
		}
		return false;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
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
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tileGUICheck(hand))
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
		case 2:
			return this.energy;
		default:
			return super.getField(id);
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
		case 2:
			this.energy = value;
			break;
		default:
			super.setField(id, value);
			break;
		}
	}
	
	public int getMaxProgress()
	{
		return this.recipeMaxTick;
	}
	
	public int getProgress()
	{
		return this.recipeTick;
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
		return -MathHelper.cos((float) (this.angle * Math.PI / 50));
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.CAPABILITY_FLUID && facing != EnumFacing.UP || capability == Capabilities.CAPABILITY_ITEM || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == Capabilities.CAPABILITY_FLUID && facing != EnumFacing.UP ? Capabilities.CAPABILITY_FLUID.cast(new IFluidHandlerHelper.FluidHandlerWrapper(this, facing))
				: capability == Capabilities.CAPABILITY_ITEM ? Capabilities.CAPABILITY_ITEM.cast(InventoryWrapFactory.wrap(getName(), this)) : super.getCapability(capability, facing);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getCustomModelData(String key)
	{
		switch (key)
		{
		case "frame" :
			return this.materialFrame.name;
		default:
			return null;
		}
	}
}
