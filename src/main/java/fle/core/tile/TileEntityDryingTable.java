package fle.core.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.gui.IHasGui;
import farcore.lib.tile.TileEntityInventory;
import farcore.util.U;
import fle.api.recipe.machine.DryingRecipe;
import fle.api.recipe.machine.DryingRecipe.$Recipe;
import fle.core.container.alpha.ContainerDrying;
import fle.core.gui.alpha.GuiDrying;
import fle.load.Langs;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityDryingTable extends TileEntityInventory
implements IHasGui
{
	public $Recipe[] recipes = new $Recipe[3];
	public int[] ticks = new int[3];
	
	public TileEntityDryingTable()
	{
		super(6, Langs.inventoryDrying, 64);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		for(int i = 0; i < recipes.length; 
				DryingRecipe.saveRecipe(nbt, "recipe_" + i, recipes[i]),
				i++);
		nbt.setIntArray("progress", ticks);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		for(int i = 0; i < recipes.length;
				recipes[i] = DryingRecipe.loadRecipe(nbt, "recipe_" + i));
		ticks = nbt.getIntArray("progress");
		if(ticks.length == 0)
		{
			ticks = new int[3];
		}
	}
	
	@Override
	protected void updateServer1()
	{
		float temp = U.Worlds.getTemp(worldObj, xCoord, yCoord, zCoord);
		super.updateServer1();
		for(int i = 0; i < 3; ++i)
		{
			if(recipes[i] == null)
			{
				if((recipes[i] = DryingRecipe.matchRecipe(inventory.stacks[i])) != null)
				{
					inventory.decrStack(i, recipes[i].input, true);
					ticks[i] = 0;
				}
			}
			if(recipes[i] != null)
			{
				if(temp > recipes[i].maxTemp) continue;
				if(++ticks[i] >= recipes[i].tick)
				{
					if(inventory.addStack(i + 3, recipes[i].output))
					{
						recipes[i] = null;
						ticks[i] = 0;
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getProgressScale(int i, int scale)
	{
		return recipes[i] == null ? 0 :
			(int) ((float) (ticks[i] * scale) / (float) recipes[i].tick);
	}

	@Override
	public Gui openGUI(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GuiDrying(this, player);
	}

	@Override
	public Container openContainer(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ContainerDrying(this, player);
	}
}