package fle.core.item;

import static fle.core.item.IItemControl.UseType.FINISH;
import static fle.core.item.IItemControl.UseType.START;
import static fle.core.item.IItemControl.UseType.STOP;
import static fle.core.item.IItemControl.UseType.TICK;

import java.lang.reflect.ParameterizedType;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.world.BlockPos;

public abstract class IItemControl<T extends Item>
{
	private static boolean f = false;
	protected Class<T> clazz;

	protected IItemControl()
	{
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		clazz = (Class) type.getActualTypeArguments()[0];
	}
	
	protected boolean doListenItem(ItemStack aStack)
	{
		return aStack == null ? false : clazz.isInstance(aStack.getItem());
	}
	
	protected void onPlayerUsingItem(EntityPlayer player, ItemStack item, int duration, UseType useType)
	{
		
	}

	protected boolean onPlayerBreakBlock(BlockPos pos, Block block, int blockMetadata)
	{
		return true;
	}
	
	@SubscribeEvent
	public void a(PlayerUseItemEvent evt)
	{
		if(doListenItem(evt.item))
			onPlayerUsingItem(evt.entityPlayer, evt.item, evt.duration, evt instanceof PlayerUseItemEvent.Start ? START : evt instanceof PlayerUseItemEvent.Tick ? TICK : evt instanceof PlayerUseItemEvent.Stop ? STOP : FINISH);
	}
	
	@SubscribeEvent
	public void a(PlayerInteractEvent evt)
	{
		if(doListenItem(evt.entityPlayer.getCurrentEquippedItem()))
		{
			if(shouldCheckMOPFirst())
			{
				MovingObjectPosition mop1 = getMovingObjectPositionFromPlayer(evt.world, evt.entityPlayer, true);
				MovingObjectPosition mop2 = getMovingObjectPositionFromPlayer(evt.world, evt.entityPlayer, false);
				if(onPlayerMOPActive(evt.entityPlayer, evt.world, mop1, mop2))
				{
					return;
				}
			}
			if(evt.action == Action.RIGHT_CLICK_AIR)
			{
				onPlayerUseItem(evt.entityPlayer, new BlockPos(evt.world, evt.x, evt.y, evt.z));
			}
			else if(evt.action == Action.RIGHT_CLICK_BLOCK)
			{
				onPlayerClickBlock(evt.entityPlayer, new BlockPos(evt.world, evt.x, evt.y, evt.z), evt.face);
			}
		}
	}
	
	protected boolean onPlayerMOPActive(EntityPlayer entityPlayer, World world, 
			MovingObjectPosition fluidMOP, MovingObjectPosition withoutFluidMOP)
	{
		return false;
	}

	protected boolean shouldCheckMOPFirst(){return false;}
	
	protected void onPlayerClickBlock(EntityPlayer entityPlayer,
			BlockPos blockPos, int face)
	{
		
	}

	protected void onPlayerUseItem(EntityPlayer player, BlockPos pos)
	{
		
	}

	@SubscribeEvent
	public void a(BreakEvent evt)
	{
		if(evt.getPlayer() != null)
			if(doListenItem(evt.getPlayer().getCurrentEquippedItem()))
				if(!onPlayerBreakBlock(new BlockPos(evt.world, evt.x, evt.y, evt.z), evt.block, evt.blockMetadata))
				{
					evt.setCanceled(true);
				}
	}
	
	protected MovingObjectPosition getMovingObjectPositionFromPlayer(World aWorld, EntityPlayer aPlayer, boolean checkWater)
    {
        float f = 1.0F;
        float f1 = aPlayer.prevRotationPitch + (aPlayer.rotationPitch - aPlayer.prevRotationPitch) * f;
        float f2 = aPlayer.prevRotationYaw + (aPlayer.rotationYaw - aPlayer.prevRotationYaw) * f;
        double d0 = aPlayer.prevPosX + (aPlayer.posX - aPlayer.prevPosX) * (double)f;
        double d1 = aPlayer.prevPosY + (aPlayer.posY - aPlayer.prevPosY) * (double)f + (double)(aWorld.isRemote ? aPlayer.getEyeHeight() - aPlayer.getDefaultEyeHeight() : aPlayer.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = aPlayer.prevPosZ + (aPlayer.posZ - aPlayer.prevPosZ) * (double)f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (aPlayer instanceof EntityPlayerMP)
        {
            d3 = ((EntityPlayerMP)aPlayer).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        return aWorld.func_147447_a(vec3, vec31, checkWater, !checkWater, false);
    }

	public static enum UseType
	{
		START,
		TICK,
		STOP,
		FINISH;
	}
}