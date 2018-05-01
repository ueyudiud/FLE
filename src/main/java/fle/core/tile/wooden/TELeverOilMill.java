/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.wooden;

import static fle.api.recipes.instance.RecipeMaps.LEVER_OIL_MILL;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import farcore.data.Keys;
import farcore.data.M;
import farcore.lib.container.Container04Solid;
import farcore.lib.material.Mat;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.RecipeMaps;
import fle.api.tile.TE08Recipe;
import nebula.NebulaProxy;
import nebula.base.A;
import nebula.base.function.F;
import nebula.base.function.Judgable;
import nebula.client.gui.GuiBackground;
import nebula.client.gui.GuiContainer02TE;
import nebula.common.gui.FluidSlot;
import nebula.common.gui.ISlotInitalizer;
import nebula.common.gui.ItemSlot;
import nebula.common.gui.ItemSlotOutput;
import nebula.common.inventory.FluidContainerSingle;
import nebula.common.inventory.FluidContainers;
import nebula.common.inventory.IContainer;
import nebula.common.inventory.ItemContainersArray;
import nebula.common.inventory.task.TaskBuilder;
import nebula.common.network.PacketBufferExt;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_CustomModelData;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@GuiBackground("fle:textures/gui/lever_oil_mill.png")
public class TELeverOilMill extends TE08Recipe<ItemStack, TemplateRecipeCache<ItemStack>>
implements ITB_BlockActived, ISidedInventory, INetworkedSyncTile, ITP_BlockHardness,
ITP_ExplosionResistance, ITP_CustomModelData, ITP_Drops, ITB_BlockPlacedBy
{
	private static final int[] IN = A.rangeIntArray(0, 1), OUT = A.rangeIntArray(1, 2);
	
	protected Mat materialFrame = M.oak;
	protected Mat materialMill = M.stone;
	
	protected Direction		facing;
	protected int			energy;
	protected int			angle;
	
	public TELeverOilMill()
	{
		this.items = new ItemContainersArray(2, 64);//TODO
		this.fluids = new FluidContainers<>(new FluidContainerSingle(4000));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.materialFrame = Mat.getMaterialByNameOrDefault(compound, "frame", M.oak);
		this.materialMill = Mat.getMaterialByNameOrDefault(compound, "mill", M.stone);
		this.facing = Direction.readFromNBT(compound, "facing", Direction.T_2D, Direction.N);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("frame", this.materialFrame.name);
		compound.setString("mill", this.materialMill.name);
		this.facing.writeToNBT(compound, "facing", Direction.T_2D);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.materialFrame = Mat.getMaterialByIDOrDefault(nbt, "f", this.materialFrame);
		this.materialMill = Mat.getMaterialByIDOrDefault(nbt, "m", this.materialMill);
		this.facing = Direction.readFromNBT(nbt, "d", Direction.T_2D, this.facing);
	}
	
	@Override
	public void writeToClientInitalization(NBTTagCompound nbt)
	{
		super.writeToClientInitalization(nbt);
		nbt.setShort("f", this.materialFrame.id);
		nbt.setShort("m", this.materialMill.id);
		this.facing.writeToNBT(nbt, "d", Direction.T_2D);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = new BaseStack(state).instance();
		Mat.setMaterialToStack(stack, "frame", this.materialFrame);
		Mat.setMaterialToStack(stack, "mill", this.materialMill);
		return Lists.newArrayList(stack);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.materialFrame = Mat.getMaterialFromStack(stack, "frame", M.oak);
		this.materialMill = Mat.getMaterialFromStack(stack, "mill", M.stone);
		this.facing = Direction.heading(placer).opposite();
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
	protected ItemStack getRecipeInputHandler()
	{
		return this.items.getStackInContainer(0);
	}
	
	@Override
	protected IRecipeMap<?, TemplateRecipeCache<ItemStack>, ItemStack> getRecipeMap()
	{
		return LEVER_OIL_MILL;
	}
	
	@Override
	protected void onRecipeInput()
	{
		this.items.getContainer(0).taskDecr(this.cache.<AbstractStack> get1(0), IContainer.PROCESS).invoke();
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
		return TaskBuilder.builder()
				.add(this.items.getContainer(1).taskIncr(this.cache.<ItemStack> get(1), IContainer.PROCESS))
				.add(this.fluids.getContainer(0).taskIncr(this.cache.<FluidStack> get(2), IContainer.PROCESS))
				.build().run();
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
			int amount = sendFluidStackTo(this.fluids.getContainer(0).decrStack(drain, 0), Direction.DIRECTIONS_2D[i], true);
			if (amount > 0)
			{
				this.fluids.getContainer(0).decrStack(amount, IContainer.PROCESS);
				drain -= amount;
			}
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
			if (NebulaProxy.isKeyDown(player, Keys.ROTATE))
			{
				onRotateMill(player);
				markTileUpdate(0);
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
	public void initalizeContainer(Container04Solid container, ISlotInitalizer initalizer)
	{
		initalizer.addSlot("input", new ItemSlot(this.items.getContainer(0), this, 0, 61, 21).setPredicate(((Judgable<Object>) F.P_ANY).from(RecipeMaps.LEVER_OIL_MILL::findRecipe)))
		.addLocation("player", false);
		initalizer.addSlot("output", new ItemSlotOutput(this.items.getContainer(1), this, 1, 98, 21))
		.addLocation("player", false);
		initalizer.straegyPlayerBag().addLocation("input", false).addLocation("hand", false);
		initalizer.straegyPlayerHand().addLocation("input", false).addLocation("bag", false);
		initalizer.addSlot(new FluidSlot(this.fluids.getContainer(0), 60, 56, 20, 8).setRenderHorizontal());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackgroundSecondLayer(GuiContainer02TE<?> gui, int x, int y, float partialTicks, int mouseX, int mouseY)
	{
		if (this.energy > 0)
		{
			gui.drawTexturedModalRect(35, 60, 176, 24, 11, 11);
			gui.drawProgressScaleDTU(26, 16, 176, 35, 8, 54, this.energy, 100);
		}
		if (getMaxProgress() > 0)
		{
			gui.drawProgressScaleUTD(63, 38, 176, 0, 10, 16, getProgress(), getMaxProgress());
		}
		gui.drawTexturedModalRect(60, 56, 176, 16, 20, 8);
	}
	
	@Override
	public void onDataRecieve(Container04Solid container, byte type, long value)
	{
		switch (type)
		{
		case 0:
			onRotateMill(container.player);
			break;
		default:
			break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public float getRotationAngle()
	{
		return -MathHelper.cos((float) (this.angle * Math.PI / 50));
	}
	
	//	@Override
	//	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	//	{
	//		return capability == Capabilities.CAPABILITY_FLUID && facing != EnumFacing.UP || capability == Capabilities.CAPABILITY_ITEM || super.hasCapability(capability, facing);
	//	}
	//
	//	@Override
	//	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	//	{
	//		return capability == Capabilities.CAPABILITY_FLUID && facing != EnumFacing.UP ? Capabilities.CAPABILITY_FLUID.cast(new IFluidHandlerHelper.FluidHandlerWrapper(this, facing))
	//				: capability == Capabilities.CAPABILITY_ITEM ? Capabilities.CAPABILITY_ITEM.cast(InventoryWrapFactory.wrap(getName(), this)) : super.getCapability(capability, facing);
	//	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getCustomModelData(String key)
	{
		switch (key)
		{
		case "frame" :
			return this.materialFrame.name;
		case "mill" :
			return this.materialMill.name;
		case "facing" :
			return getFacing().getName();
		default:
			return null;
		}
	}
	
	@Override
	public Direction getFacing()
	{
		return this.facing == null ? Direction.N : this.facing;
	}
}
