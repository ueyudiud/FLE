package fle.core.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.recipe.CraftingState;
import flapi.recipe.stack.OreStack;
import flapi.te.TEInventory;
import fle.core.init.Lang;
import fle.core.net.FleTEPacket;
import fle.core.recipe.ColdForgingRecipe;
import fle.core.recipe.RecipeHelper;

public class TileEntityColdForgingPlatform extends TEInventory
{
	public int dir = 0;
	public int hard = 0;
	public char[] array = "         ".toCharArray();
	
	public TileEntityColdForgingPlatform()
	{
		super(6);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		array = nbt.getString("Recipe").toCharArray();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("Recipe", new String(array));
	}

	@Override
	public void update()
	{
		;
	}
	
	public void onOutput()
	{
		array = "         ".toCharArray();
		for(int i = 0; i < 4; ++i)
			RecipeHelper.onInputItemStack(this, i);
	}
	
	public boolean canTakeStack()
	{
		for(char c : array)
		{
			if(c != ' ') return false;
		}
		return true;
	}

	public void onSlotChange()
	{
		stacks[5] = ColdForgingRecipe.getResult(this, new String(array));
	}

	public void onToolClick(EntityPlayer aPlayer, int activeSlot)
	{
		int[] array = new int[2];
		switch(dir)
		{
		case 0 : array[0] = hard;
		break;
		case 1 : array[1] = hard;
		break;
		case 2 : array[0] = -hard;
		break;
		case 3 : array[1] = -hard;
		}
		if(new OreStack("craftingToolHardHammer").equal(stacks[4]))
		{
			FleAPI.damageItem(aPlayer, stacks[4], EnumDamageResource.Crafting, 1.0F);
			this.array[activeSlot] = ColdForgingRecipe.getForgingMapOutputChar(this.array[activeSlot], ColdForgingRecipe.getState(array));
		}
		onSlotChange();
		sendToNearBy(new FleTEPacket(this, (byte) 2), 16.0F);
	}
	
	public void setup()
	{
		dir = 0;
		hard = 1;
	}
	
	public void switchType(int type)
	{
		if(type == 0)
		{
			if(++dir == 4) 
				dir = 0;
		}
		else if(type == 1)
		{
			if(++hard == 4) 
				hard = 1;
		}
		else if(type == 2)
		{
			for(int i = 0; i < 4; ++i)
				decrStackSize(i, 1);
			array = "         ".toCharArray();
			sendToNearBy(new FleTEPacket(this, (byte) 2), 16.0F);
		}
		sendToNearBy(new FleTEPacket(this, (byte) 3), 16.0F);
		sendToNearBy(new FleTEPacket(this, (byte) 4), 16.0F);
	}
	
	public int getDir()
	{
		return dir;
	}
	
	public int getHarness()
	{
		return hard;
	}

	@SideOnly(Side.CLIENT)
	public CraftingState getState(int i)
	{
		return CraftingState.getState(array[i]);
	}
	
	@Override
	public Object onEmit(byte type)
	{
		switch(type)
		{
		case 2 : return new String(array);
		case 3 : return hard;
		case 4 : return dir;
		}
		return null;
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		switch(type)
		{
		case 2 : array = ((String) contain).toCharArray();
		break;
		case 3 : hard = (Integer) contain;
		break;
		case 4 : dir = (Integer) contain;
		}
	}

	@Override
	protected String getDefaultName() 
	{
		return Lang.inventory_coldForgingPlatform;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i != 5;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return false;
	}
}