package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.gui.SlotOutput;
import fle.api.net.INetEventListener;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemOreStack;
import fle.core.inventory.InventoryWashing;
import fle.core.net.FleGuiPacket;
import fle.core.recipe.RecipeHelper;
import fle.core.recipe.WashingRecipe;

public class ContainerWashing extends ContainerCraftable implements INetEventListener
{
	World world;
	GuiCondition type = null;
	String recipeName;
	int washTime;
	
	public ContainerWashing(InventoryPlayer player)
	{
		super(player, null, 0, 0);
		this.inv = new InventoryWashing(this);
		this.addSlotToContainer(new Slot(inv, 0, 35, 19));
		this.addSlotToContainer(new Slot(inv, 1, 53, 19));
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				this.addSlotToContainer(new SlotOutput(inv, 2 + i + j * 3, 92 + i * 18, 19 + j * 18));
		this.locateRecipeInput = new TransLocation("inventory_input", 36);
		this.locateRecipeOutput = new TransLocation("inventory_output", 37, 46);
		world = player.player.worldObj;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	boolean isWashing()
	{
		return recipeName != null;
	}

	@SideOnly(Side.CLIENT)
	int getWashPrograss(int a)
	{
		return a * washTime / 5;
	}
	
	void washItem()
	{
		if(recipeName == null)
		{
			recipeName = WashingRecipe.getRecipeName(inv.getStackInSlot(0));
			if(recipeName != null)
			{
				RecipeHelper.onInputItemStack(inv, 0);
				type = GuiError.DEFAULT;
			}
			else
			{
				type = GuiError.CAN_NOT_INPUT;
			}
		}
		if(recipeName != null)
		{
			if(matchBarGrizzy())
			{
				useBarGrizzy();
				++washTime;
			}
			if(washTime > 5)
			{
				if(!world.isRemote)
				{
					ItemStack[] output = WashingRecipe.outputRecipe(recipeName);
					if(RecipeHelper.matchOutput(inv, 2, 11, output))
					{
						RecipeHelper.onOutputShapelessStacks(inv, 2, 11, output);
						washTime = 0;
						recipeName = null;
						onCraftMatrixChanged(inv);
						type = GuiError.DEFAULT;
					}
					else
					{
						type = GuiError.CAN_NOT_OUTPUT;
					}
				}
				else
				{
					ItemStack[] output = WashingRecipe.outputRecipe(recipeName);
					if(RecipeHelper.matchOutput(inv, 2, 11, output))
					{
						washTime = 0;
						recipeName = null;
						type = GuiError.DEFAULT;
					}
					else
					{
						type = GuiError.CAN_NOT_OUTPUT;
					}
				}
			}
		}
	}
	
	private static final ItemAbstractStack stack = new ItemOreStack("craftingToolBarGrizzy");
	
	boolean matchBarGrizzy()
	{
		return inv.getStackInSlot(1) != null ? stack.isStackEqul(inv.getStackInSlot(1)) : false;
	}
	
	void useBarGrizzy()
	{
		FleAPI.damageItem(null, inv.getStackInSlot(1), EnumDamageResource.UseTool, 0.03125F);
		if(inv.getStackInSlot(1).stackSize == 0)
			inv.setInventorySlotContents(1, null);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) 
	{
		super.onContainerClosed(player);
		dropInventoryItem(inv, player);
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 1)
		{
			washItem();
		}
		else if(type == 2)
		{
			this.type = (GuiCondition) contain;
		}
	}
}