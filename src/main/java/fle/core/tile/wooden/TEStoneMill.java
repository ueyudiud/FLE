/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.wooden;

import java.io.IOException;

import farcore.data.Keys;
import farcore.lib.solid.container.SolidTank;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.RecipeMaps;
import fle.api.tile.TEITSRecipe;
import fle.core.client.gui.wooden.GuiStoneMill;
import fle.core.common.gui.wooden.ContainerStoneMill;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.fluid.FluidTankN;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.util.Direction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TEStoneMill extends TEITSRecipe<ItemStack, TemplateRecipeCache<ItemStack>>
implements ITB_BlockActived, IGuiTile, INetworkedSyncTile
{
	private SolidTank tank1 = new SolidTank(1000);
	public FluidTankN tank2 = new FluidTankN(4000);
	private int buffer;
	@SideOnly(Side.CLIENT)
	private int angle;
	
	public TEStoneMill()
	{
		super(3);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.tank1.readFromNBT(compound, "solid");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		this.tank1.writeToNBT(compound, "solid");
		return compound;
	}
	
	@Override
	public String getName()
	{
		return "inventory.stone.mill";
	}
	
	@Override
	protected FluidTankN tank()
	{
		return this.tank2;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.buffer > 0)
		{
			this.buffer--;
		}
	}
	
	@Override
	protected void updateClient()
	{
		super.updateClient();
		if (this.buffer > 0)
		{
			this.buffer--;
			this.angle++;
			if (this.angle >= 80)
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
		buf.writeInt(this.buffer);
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		this.buffer = buf.readInt();
	}
	
	
	public void onRotateMill(EntityPlayer player)
	{
		this.buffer = 10;
		if (isServer())
		{
			player.addExhaustion(0.0025F);
		}
	}
	
	@Override
	protected ItemStack getRecipeInputHandler()
	{
		return this.stacks[0];
	}
	
	@Override
	protected IRecipeMap<?, TemplateRecipeCache<ItemStack>, ItemStack> getRecipeMap()
	{
		return RecipeMaps.STONE_MILL;
	}
	
	@Override
	protected void onRecipeInput()
	{
		decrStackSize(0, this.cache.get1(0));
		this.recipeMaxTick = this.cache.get(0);
	}
	
	@Override
	protected boolean onRecipeOutput()
	{
		if (this.tank1.insertSolid(this.cache.get(1), true) && this.tank2.insertFluid(this.cache.get(2), false))
		{
			return this.tank1.insertSolid(this.cache.get(1), false);
		}
		return false;
	}
	
	@Override
	protected int getPower()
	{
		return this.buffer > 0 ? 1 : 0;
	}
	
	@SideOnly(Side.CLIENT)
	public float getRotationAngle()
	{
		return 360.0F * this.angle / 80.0F;
	}
	
	@Override
	public Container openContainer(int id, EntityPlayer player)
	{
		return new ContainerStoneMill(this, player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(int id, EntityPlayer player)
	{
		return new GuiStoneMill(this, player);
	}
}