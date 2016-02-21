package fle.core.item.behavior.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import farcore.util.U.N;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.world.BlockPos;
import fle.core.init.IB;
import fle.tool.ToolMaterialInfo;

public class BehaviorHoe extends BehaviorDigable
{
	boolean flag1;
	boolean flag2;
	
	public BehaviorHoe(boolean isPlantEffective, boolean canPlow)
	{
		flag1 = isPlantEffective;
		flag2 = canPlow;
	}
	
	@Override
	public boolean onItemDamageBlock(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, EntityLivingBase aEntity, World aWorld, int aX,
			int aY, int aZ)
	{
		if(aBlock.getMaterial() == Material.plants || aBlock.getMaterial() == Material.vine)
		{
			item.damageItem(aStack, aEntity, EnumDamageResource.DestoryBlock, 0.05F);
			return true;
		}
		return super.onItemDamageBlock(item, aStack, aBlock, aEntity, aWorld, aX, aY,
				aZ);
	}
	
	@Override
	public boolean isBlockEffective(ItemStack aStack, Block aBlock, int aMeta)
	{
		return super.isBlockEffective(aStack, aBlock, aMeta) || ((aBlock.getMaterial() == Material.plants || aBlock.getMaterial() == Material.vine) && flag1);
	}
	
	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata)
	{
		return isBlockEffective(aStack, aBlock, aMetadata) ? new ToolMaterialInfo(item.setupNBT(aStack)).getHardness() * 10.0F : 0.2F;
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos)
	{
        if (!player.canPlayerEdit(x, y, z, N.side(side), itemstack))
        {
            return false;
        }
        else if(flag2)
        {
            UseHoeEvent event = new UseHoeEvent(player, itemstack, world, x, y, z);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return false;
            }

            if (event.getResult() == Result.ALLOW)
            {
            	item.damageItem(itemstack, player, EnumDamageResource.UseTool, 1F);
                return true;
            }
            if (side == ForgeDirection.UP)
            {
                player.setItemInUse(itemstack, 1000);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
        	return false;
        }
	}

	@Override
	public void onPlayerStoppedUsing(ItemFleMetaBase item, World aWorld,
			EntityPlayer aPlayer, int aUseTick, ItemStack aStack)
	{
		if(aUseTick > 100)
		{
			MovingObjectPosition mop = item.getMovingObjectPositionFromPlayer(aWorld, aPlayer, false);
			if(mop.typeOfHit == MovingObjectType.BLOCK)
			{
				BlockPos pos = new BlockPos(aWorld, mop.blockX, mop.blockY, mop.blockZ);
				Block block = pos.getBlock();
				ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[mop.sideHit];
				if(dir == ForgeDirection.UP && (block == Blocks.dirt || block == Blocks.grass) && pos.toPos(ForgeDirection.UP).isAir())
				{
					if(aPlayer.getRNG().nextInt(100) < aUseTick / 10)
					{
						if(!aWorld.isRemote)
						{
			                Block block1 = IB.farmland;
			                aWorld.playSoundEffect(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
			                aWorld.setBlock(pos.x, pos.y, pos.z, block1);
						}
						item.damageItem(aStack, aPlayer, EnumDamageResource.UseTool, 0.5F);
					}
					else
					{
						item.damageItem(aStack, aPlayer, EnumDamageResource.UseTool, 0.3F);
					}
				}
			}
		}
	}
}