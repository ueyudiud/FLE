package fle.api.gui;

import farcore.lib.recipe.ICraftingInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class InventoryCraftingFle implements ICraftingInventory, IInventory
{
	public Container container;
	
	private int width;
	private int height;
	
	private ItemStack[] tools;
	private ItemStack[] toolMaterials;
	private ItemStack[] craftingMatrix;
	
	public InventoryCraftingFle(int width, int height, int tool)
	{
		this.width = width;
		this.height = height;
		tools = new ItemStack[tool];
		toolMaterials = new ItemStack[tool];
		craftingMatrix = new ItemStack[width * height];
	}

	@Override
	public int getCraftingMatrixSize()
	{
		return craftingMatrix.length;
	}

	@Override
	public int getToolSlotSize()
	{
		return tools.length;
	}

	@Override
	public int getInventoryMaxWidth()
	{
		return width;
	}

	@Override
	public int getInventoryMaxHeight()
	{
		return height;
	}

	@Override
	public ItemStack getStackByCoord(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return null;
		return craftingMatrix[y * width + x];
	}

	@Override
	public ItemStack getStackInMatrix(int id)
	{
		return craftingMatrix[id];
	}

	@Override
	public ItemStack getTool(int id)
	{
		return tools[id];
	}

	@Override
	public ItemStack getToolMatrial(int id)
	{
		return toolMaterials[id];
	}

	/**
	 * Handle in container.
	 * @return
	 */
	public World getWorld() {return null;}
	public EntityPlayer getPlayer() {return null;}

	@Override
	public void setStackInMatrix(int x, int y, ItemStack stack)
	{
		craftingMatrix[y * width + x] = stack;
		container.onCraftMatrixChanged(this);
	}
	
	@Override
	public void setStackInMatrix(int id, ItemStack stack)
	{
		craftingMatrix[id] = stack;
		container.onCraftMatrixChanged(this);
	}

	@Override
	public void setToolStack(int id, ItemStack stack)
	{
		tools[id] = stack;
		container.onCraftMatrixChanged(this);
	}

	@Override
	public void setToolMatrialStack(int id, ItemStack stack)
	{
		toolMaterials[id] = stack;
		container.onCraftMatrixChanged(this);
	}
	
	private ItemStack decrStack(ItemStack[] stacks, int id, int size)
	{
		if (stacks[id] != null)
        {
            ItemStack itemstack;

            if (stacks[id].stackSize <= size)
            {
                itemstack = stacks[id];
                stacks[id] = null;
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
            else
            {
                itemstack = stacks[id].splitStack(size);

                if (stacks[id].stackSize == 0)
                {
                    stacks[id] = null;
                }

                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
        }
        else
        {
            return null;
        }		
	}

	@Override
	public void decrStackInMatrix(int x, int y, int size)
	{
		decrStack(craftingMatrix, x + y * width, size);
	}
	
	@Override
	public void decrStackInMatrix(int id, int size)
	{
		decrStack(craftingMatrix, id, size);
	}

	@Override
	public void decrStackInTool(int id, int size)
	{
		decrStack(tools, id, size);
	}

	@Override
	public void decrStackInToolMatrial(int id, int size)
	{
		decrStack(toolMaterials, id, size);
	}

	@Override
	public int getSizeInventory()
	{
		return getCraftingMatrixSize() + getToolSlotSize() * 2;
	}

	@Override
	public ItemStack getStackInSlot(int id)
	{
		if(id < getCraftingMatrixSize())
		{
			return craftingMatrix[id];
		}
		id -= getCraftingMatrixSize();
		if(id < getToolSlotSize())
		{
			return tools[id];
		}
		id -= getToolSlotSize();
		return toolMaterials[id];
	}

	@Override
	public ItemStack decrStackSize(int id, int size)
	{
		if(id < getCraftingMatrixSize())
		{
			return decrStack(craftingMatrix, id, size);
		}
		id -= getCraftingMatrixSize();
		if(id < getToolSlotSize())
		{
			return decrStack(tools, id, size);
		}
		id -= getToolSlotSize();
		return decrStack(toolMaterials, id, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int id)
	{
		return decrStackSize(id, getInventoryStackLimit());
	}

	@Override
	public void setInventorySlotContents(int id, ItemStack stack)
	{
		stack = ItemStack.copyItemStack(stack);
		if(id < getCraftingMatrixSize())
		{
			craftingMatrix[id] = stack;
			container.onCraftMatrixChanged(this);
			return;
		}
		id -= getCraftingMatrixSize();
		if(id < getToolSlotSize())
		{
			tools[id] = stack;
			container.onCraftMatrixChanged(this);
			return;
		}
		id -= getToolSlotSize();
		toolMaterials[id] = stack;
		container.onCraftMatrixChanged(this);
	}

	public String getInventoryName() {return "Fle Crafting";}
	public boolean hasCustomInventoryName() {return false;}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
		container.onCraftMatrixChanged(this);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void openInventory() {	}
	public void closeInventory() {	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack)
	{
		return true;
	}
}