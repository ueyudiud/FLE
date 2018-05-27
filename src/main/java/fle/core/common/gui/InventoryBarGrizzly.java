/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.common.gui;

import static nebula.common.inventory.IContainer.PROCESS;

import farcore.data.EnumToolTypes;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.RecipeMaps;
import nebula.common.inventory.InventoryHelper;
import nebula.common.inventory.InventorySimple;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

/**
 * @author ueyudiud
 */
public class InventoryBarGrizzly extends InventorySimple implements ITickable
{
	private ContainerBarGrizzly container;
	
	private TemplateRecipeCache<ItemStack>	cache;
	
	public int maxProgress = 1;
	public int progress;
	public int power;
	
	public InventoryBarGrizzly(ContainerBarGrizzly container)
	{
		super("inventory.bar.grizzly", false, 11);
		this.container = container;
	}
	
	@Override
	public void update()
	{
		if (!this.container.world.isRemote)
		{
			if (this.cache == null)
			{
				this.cache = RecipeMaps.WASHING_BARGRIZZLY.findRecipe(this.stacks[0]);
				if (this.cache != null)
				{
					this.containers.getContainer(0).decrStack(this.cache.<ItemStack> get1(0), PROCESS);
					this.maxProgress = this.cache.<Integer> get(0);
					this.container.detectAndSendChanges();
				}
			}
			else if (this.cache != null && this.power > 100)
			{
				if (this.stacks[1] != null && EnumToolTypes.BAR_GRIZZLY.match(this.stacks[1]))
				{
					if (this.progress < this.maxProgress)
					{
						ItemStacks.damageTool(this.stacks[1], 0.01F, this.container.player, EnumToolTypes.BAR_GRIZZLY);
						this.progress ++;
					}
					else
					{
						if (InventoryHelper.insertSlotShapelessStacks(this.stacks, 2, 11, this.cache.get(1), true))
						{
							this.progress = 0;
							this.cache = null;
							this.maxProgress = 1;
						}
					}
					this.container.detectAndSendChanges();
				}
			}
		}
		if (this.power > 0)
		{
			this.power--;
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
		case 1 : return this.power;
		case 2 : return this.maxProgress;
		default:return 0;
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
		case 0 : this.progress = value; return;
		case 1 : this.power = value; return;
		case 2 : this.maxProgress = value; return;
		}
	}
}
