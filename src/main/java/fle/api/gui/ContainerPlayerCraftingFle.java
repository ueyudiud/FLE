package fle.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.handler.FarCoreAchievementHandler;
import farcore.inventory.Inventory;
import farcore.lib.container.ContainerBase;
import farcore.lib.container.SlotBase;
import farcore.lib.container.SlotHolo;
import farcore.lib.container.SlotOutput;
import farcore.lib.recipe.FleCraftingManager;
import farcore.lib.recipe.ICraftingInventory;
import farcore.lib.recipe.IFleRecipe;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.world.World;

public class ContainerPlayerCraftingFle extends ContainerBase<InventoryCraftingFle> implements ICraftingInventory, IUpdatePlayerListBox
{
	protected Inventory result = new Inventory(1, "Result", 64);
	protected InventoryCraftingDisplay display = new InventoryCraftingDisplay();
    private World worldObj;
    private int posX;
    private int posY;
    private int posZ;
        
    private boolean isChecking = false;
    private boolean isCrafting = false;
    private IFleRecipe recipe;
    private int tick;
    private int maxTick;
	
	public ContainerPlayerCraftingFle(World world, EntityPlayer player)
	{
		super(new InventoryCraftingFle(2, 2, 1), player);
		inventory.container = this;
		this.worldObj = world;
		this.posX = (int) player.posX;
		this.posY = (int) player.posY;
		this.posZ = (int) player.posZ;
		addPlayerSlot();
		int offset = inventorySlots.size();
		for(int i = 0; i < 2; ++i)
			for(int j = 0; j < 2; ++j)
			{
				addSlotToContainer(new SlotBase(inventory, i * 2 + j, 68 + 18 * j, 26 + 18 * i));
			}
		addSlotToContainer(new SlotBase(inventory, 4, 16, 35));
		addSlotToContainer(new SlotBase(inventory, 5, 36, 35));
		addSlotToContainer(new SlotHolo(display, 0, 109, 29));
		addSlotToContainer(new SlotOutput(result, 0, 133, 43));
		TransLocate locateCrafting, locateOutput;
		addTransLocate(locateCrafting = new TransLocate("input", offset + 0, offset + 6, false, true));
		addTransLocate(locateOutput = new TransLocate("Output", offset + 7, false));
		locateBag.append(locateHand);
		locateHand.append(locateBag);
		locateCrafting.append(locatePlayer);
		locateOutput.append(locatePlayer);
	}
	
	@Override
	public ItemStack slotClick(int id, int mouseClick, int shiftHold, EntityPlayer player)
	{
		if(id >= 0 && id < inventorySlots.size())
		{
			Slot slot = getSlot(id);
			if(slot.inventory == display)
			{
				if(isCrafting) return null;
				else
				{
					if(recipe != null)
					{
						isCrafting = true;
					}
					return null;
				}
			}
		}
		return super.slotClick(id, mouseClick, shiftHold, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int idx)
	{
		ItemStack stack = super.transferStackInSlot(player, idx);
		if(isCrafting)
		{
			if(!recipe.matchRecipe(this))
			{
				tick = 0;
				maxTick = 0;
				recipe = null;
				isCrafting = false;
			}
		}
		return stack;
	}
	
	@Override
	protected int getUpdateSize()
	{
		return 2;
	}
	
	@Override
	protected int getUpdate(int id)
	{
		return id == 0 ? tick : maxTick;
	}
	
	@Override
	protected void setUpdate(int id, int value)
	{
		if(id == 0)
		{
			tick = value;
		}
		if(id == 1)
		{
			maxTick = value;
		}
	}
	
	@Override
	public void update()
	{
		if(isCrafting && recipe != null)
		{
			if(tick++ >= maxTick)
			{
				if(worldObj.isRemote) return;
				ItemStack output = recipe.getOutput(this);
				if(result.addStack(0, output, false) == output.stackSize)
				{
					isChecking = true;
					recipe.onOutputStack(this);
					result.addStack(0, output, true);
					if(!recipe.matchRecipe(this))
					{
						isChecking = false;
						recipe = null;
						isCrafting = false;
						maxTick = 0;
						onCraftMatrixChanged(result);
					}
					else
					{
						
					}
					tick = 0;
					detectAndSendChanges();
				}
				else
				{
					tick = maxTick;
				}
			}
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		if(isChecking) return;
		if(isCrafting)
		{
			if(!recipe.matchRecipe(this))
			{
				recipe = null;
				tick = 0;
				maxTick = 0;
				isCrafting = false;
			}
			else return;
		}
		recipe = FleCraftingManager.findRecipe(this);
		if(recipe != null)
		{
			display.setInventorySlotContents(0, recipe.getOutput(this));
			maxTick = recipe.getRecipeTick(this);
		}
		else
		{
			display.setInventorySlotContents(0, null);
			tick = 0;
			maxTick = 0;
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		U.Worlds.spawnDropsInWorldByPlayerOpeningContainer(player, inventory);
		U.Worlds.spawnDropsInWorldByPlayerOpeningContainer(player, result);
	}
	
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
    
    @Override
    public boolean func_94530_a(ItemStack stack, Slot slot)
    {
    	return slot.inventory != result && super.func_94530_a(stack, slot);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isCrafting()
    {
		return isCrafting;
	}
    
    @SideOnly(Side.CLIENT)
    public int getCraftingScale(int length)
    {
    	return (int) ((double) tick * length / (double) maxTick);
    }

	public int getCraftingMatrixSize(){return inventory.getCraftingMatrixSize();}
	public int getToolSlotSize() {return inventory.getToolSlotSize();}
	public int getInventoryMaxWidth() {return inventory.getInventoryMaxWidth();}
	public int getInventoryMaxHeight() {return inventory.getInventoryMaxHeight();}
	public ItemStack getStackByCoord(int x, int y) {return inventory.getStackByCoord(x, y);}
	public ItemStack getStackInMatrix(int id) {return inventory.getStackInMatrix(id);}
	public ItemStack getTool(int id) {return inventory.getTool(id);}
	public ItemStack getToolMatrial(int id) {return inventory.getToolMatrial(id);}
	public World getWorld() {return worldObj;}
	public EntityPlayer getPlayer() {return player;}
	public void setStackInMatrix(int x, int y, ItemStack stack) {inventory.setStackInMatrix(x, y, stack);}
	public void setStackInMatrix(int id, ItemStack stack) {inventory.setStackInMatrix(id, stack);}
	public void setToolStack(int id, ItemStack stack) {inventory.setToolStack(id, stack);}
	public void setToolMatrialStack(int id, ItemStack stack) {inventory.setToolMatrialStack(id, stack);}
	public void decrStackInMatrix(int x, int y, int size) {inventory.decrStackInMatrix(x, y, size);}
	public void decrStackInMatrix(int id, int size) {inventory.decrStackInMatrix(id, size);}
	public void decrStackInTool(int id, int size) {inventory.decrStackInTool(id, size);}
	public void decrStackInToolMatrial(int id, int size) {inventory.decrStackInToolMatrial(id, size);}
}