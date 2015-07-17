package fla.core.item.tool;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.item.tool.IDigableTool;
import fla.api.item.tool.IWeapon;
import fla.api.item.tool.ItemDamageResource;
import fla.api.item.tool.ToolDamageEvent;
import fla.api.item.tool.ToolDamageEvent.BBDamageEvent;
import fla.api.util.FlaValue;

public abstract class ItemFlaDigableTool extends ItemFlaTool implements IDigableTool, IWeapon
{
    /** Damage versus entities. */
	private float damageVsEntity;

	protected ItemFlaDigableTool(float attackDamage)
	{
        this.damageVsEntity = attackDamage;
    }
    
    protected void damageItem(EntityLivingBase player, ItemStack tool, ToolDamageEvent evt)
    {
    	if(player instanceof EntityPlayer)
    	{
    		if(((EntityPlayer) player).capabilities.isCreativeMode) return;
        	MinecraftForge.EVENT_BUS.post(evt);
        	if(evt.isCanceled())
        	{
        		
        	}
        	else if(evt.getResult() != Result.DENY)
        	{
        		damageItem(player, tool, evt.resource);
        	}
    	}
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack item, EntityLivingBase target, EntityLivingBase hitter)
    {
    	if(hitter instanceof EntityPlayer)
    	{
    		damageItem(hitter, item, new ToolDamageEvent((EntityPlayer) hitter, item, ItemDamageResource.HitEntity));
    	}
    	else
    	{
    		damageItem(hitter, item, ItemDamageResource.HitEntity);
    	}
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase base)
    {
        if ((double)block.getBlockHardness(world, x, y, z) != 0.0D)
        {
        	if(base instanceof EntityPlayer)
        	{
        		damageItem(base, stack, new BBDamageEvent((EntityPlayer) base, block, stack, x, y, z, world.getBlockMetadata(x, y, z)));
        	}
        	else
        	{
        		damageItem(base, stack, ItemDamageResource.BreakBlock);
        	}
        }
        return true;
    }
	
    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
    	Set<String> set = new HashSet();
    	set.add(getToolType(stack));
        return set;
       }
    
}