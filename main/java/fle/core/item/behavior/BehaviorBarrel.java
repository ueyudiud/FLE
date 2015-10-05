package fle.core.item.behavior;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerRegisterEvent;
import net.minecraftforge.fluids.FluidStack;
import fle.api.item.ItemFleMetaBase;

public class BehaviorBarrel extends BehaviorBase
{
	protected Fluid currect;
	
	public BehaviorBarrel(Fluid aCurrect)
	{
		currect = aCurrect;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemFleMetaBase item,
			ItemStack itemstack, World aWorld, EntityPlayer player)
	{
        boolean flag = currect == null;
        MovingObjectPosition movingobjectposition = item.getMovingObjectPositionFromPlayer(aWorld, player, flag);

        if (movingobjectposition == null)
        {
            return itemstack;
        }
        else
        {
            FillBucketEvent event = new FillBucketEvent(player, itemstack, aWorld, movingobjectposition);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return itemstack;
            }
            if (event.getResult() == Event.Result.ALLOW)
            {
                if (player.capabilities.isCreativeMode)
                {
                    return itemstack;
                }

                if (--itemstack.stackSize <= 0)
                {
                    return event.result;
                }

                if (!player.inventory.addItemStackToInventory(event.result))
                {
                    player.dropPlayerItemWithRandomChoice(event.result, false);
                }

                return itemstack;
            }
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!aWorld.canMineBlock(player, i, j, k))
                {
                    return itemstack;
                }

                if (flag)
                {
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemstack))
                    {
                        return itemstack;
                    }

                    Fluid fluid = FluidRegistry.lookupFluidForBlock(aWorld.getBlock(i, j, k));
                    int l = aWorld.getBlockMetadata(i, j, k);

                    if (fluid != null && FluidContainerRegistry.isEmptyContainer(itemstack) && l == 0)
                    {
                    	ItemStack ret = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, 1000), itemstack);
                    	if(ret != null)
                    	{
                            aWorld.setBlockToAir(i, j, k);
                            return func_150910_a(itemstack, player, ret);
                    	}
                    }
                }
                else
                {
                    if (currect == null)
                    {
                        return func_150910_a(itemstack, player, FluidContainerRegistry.drainFluidContainer(itemstack));
                    }

                    ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[movingobjectposition.sideHit];
                    i += dir.offsetX;
                    j += dir.offsetY;
                    k += dir.offsetZ;

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemstack))
                    {
                        return itemstack;
                    }

                    if (tryPlaceContainedLiquid(aWorld, i, j, k) && !player.capabilities.isCreativeMode)
                    {
                        return func_150910_a(itemstack, player, FluidContainerRegistry.drainFluidContainer(itemstack));
                    }
                }
            }

            return itemstack;
        }
	}
	
	private ItemStack func_150910_a(ItemStack aStack, EntityPlayer aPlayer, ItemStack aResult)
    {
        if (aPlayer.capabilities.isCreativeMode)
        {
            return aStack;
        }
        else if (--aStack.stackSize <= 0)
        {
            return aResult;
        }
        else
        {
            if (!aPlayer.inventory.addItemStackToInventory(aResult))
            {
                aPlayer.dropPlayerItemWithRandomChoice(aResult, false);
            }

            return aStack;
        }
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World aWorld, int x, int y, int z)
    {
        if (currect == null)
        {
            return false;
        }
        else if(!currect.canBePlacedInWorld())
        {
        	return false;
        }
        else
        {
            Material material = aWorld.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();

            if (!aWorld.isAirBlock(x, y, z) && !flag)
            {
                return false;
            }
            else
            {
                if (aWorld.provider.isHellWorld && currect == FluidRegistry.WATER)
                {
                    aWorld.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (aWorld.rand.nextFloat() - aWorld.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                        aWorld.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!aWorld.isRemote && flag && !material.isLiquid())
                    {
                        aWorld.func_147480_a(x, y, z, true);
                    }

                    //aWorld.setBlock(x, y, z, currect.getBlock(), 0, 3);
                }

                return true;
            }
        }
    }
}