package farcore.block;

import java.util.List;
import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.render.block.ItemBlockRender;
import flapi.util.Values;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBase3Render extends BlockBase2Control
{
	private ThreadLocal<Boolean> hitThread = new ThreadLocal();
	private ThreadLocal<Boolean> destoryThread = new ThreadLocal();
	
	BlockBase3Render(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
	}
	
	/**
	 * Add tool tip on GUI when display this item on slot.
	 * @param aStack
	 * @param aList
	 * @param aPlayer
	 */
	public void addInformation(ItemStack stack, List<String> list, EntityPlayer player)
	{
		;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		if(destoryThread.get() == Boolean.TRUE) return false;
		destoryThread.set(true);
		effectRenderer.addBlockHitEffects(target.blockX, target.blockY, target.blockZ, target);
		destoryThread.set(false);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		if(destoryThread.get() == Boolean.TRUE) return false;
		destoryThread.set(true);
        effectRenderer.addBlockDestroyEffects(x, y, z, this, meta);
		destoryThread.set(false);
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int x, int y, int z,
			Random random)
	{
		;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		super.registerBlockIcons(register);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int side)
	{
		return super.getIcon(world, x, y, z, side);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public final IIcon getIcon(int side, int meta)
	{
		if(destoryThread.get() == Boolean.TRUE) return getParticleIcon(meta);
		if(hitThread.get() == Boolean.TRUE) return getHitIcon(meta);
		return getBlockIcon(side, meta);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getBlockIcon(int side, int meta)
	{
		try
		{
			return getIcon(side, ItemBlockRender.getStack());
		}
		catch(Exception exception)
		{
			return Values.EMPTY_BLOCK_ICON;
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, ItemStack stack)
	{
		return super.getIcon(side, stack.getItemDamage());
	}

	@SideOnly(Side.CLIENT)
	public IIcon getHitIcon(int meta)
	{
		return getParticleIcon(meta);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getParticleIcon(int meta)
	{
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final int getRenderColor(int meta)
	{
		if(ItemBlockRender.getStack() != null)
			return getRenderColor(ItemBlockRender.getStack());
		return 0xFFFFFF;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(ItemStack stack)
	{
		return super.getRenderColor(stack.getItemDamage());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public final int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		if(destoryThread.get() == Boolean.TRUE || hitThread.get() == Boolean.TRUE)
			return particleColor(world, x, y, z);
		return blockColor(world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public int blockColor(IBlockAccess world, int x, int y, int z)
	{
		return super.colorMultiplier(world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public int particleColor(IBlockAccess world, int x, int y, int z)
	{
		return 0xFFFFFF;
	}
}