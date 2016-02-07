package farcore.fluid;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import flapi.util.Values;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockFluidBase extends BlockFluidFinite implements ISmartFluidBlock
{
	public BlockFluidBase(FluidBase fluid, Material material)
	{
		super(fluid, material);
		setBlockName(fluid.getUnlocalized());
		GameRegistry.registerBlock(this, ItemFluid.class, "fluid." + fluidName);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(this, 1, 15));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		((FluidBase) getFluid()).registerFluidIcons(register);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return ((FluidBase) getFluid()).getIcon();
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return side == 0 || side == 1 ? ((FluidBase) getFluid()).getIcon() : ((FluidBase) getFluid()).getFlowingIcon();
	}
	
	@Override
	public int getRenderColor(int meta)
	{
		return ((FluidBase) getFluid()).getColor();
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		return ((FluidBase) getFluid()).getColor(world, x, y, z);
	}
	
	public void setQuantaValue(World world, int x, int y, int z, int value)
	{
		if(value == 0) world.setBlockToAir(x, y, z);
		else world.setBlock(x, y, z, this, value - 1, 2);
	}
	
	/**
	 * Use better update method.
	 * This method add this function.
	 */	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		boolean changed = false;
		int amount = getQuantaValue(world, x, y, z),
				preAmount = amount;
		amount = tryToFlowVerticallyInto(world, x, y, z, amount);
		
	}
	
	@Override
	public int tryToFlowVerticallyInto(World world, int x, int y, int z, int amtToInput)
	{
		return super.tryToFlowVerticallyInto(world, x, y, z, amtToInput);
	}
	
	@Override
	public FluidStack drain(World world, int x, int y, int z, int maxDrain, boolean doDrain)
	{
		if(world.getBlock(x, y, z) != this)
			return null;
		int value = getQuantaValue(world, x, y, z);
		int level = Math.min(value, (int) (maxDrain / ((float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat)));
        if (doDrain)
        {
        	if(value == level)
        	{
        		world.setBlock(x, y, z, Blocks.air);
        	}
        	else
        	{
                world.setBlockMetadataWithNotify(x, y, z, value - level - 1, 2);
        	}
        }
        
        return new FluidStack(getFluid(),
                MathHelper.floor_float(level * FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlock));
	}

	@Override
	public int fill(World world, int x, int y, int z, int maxFill, boolean doFill)
	{
		int value = (int) Math.floor(maxFill / ((float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat));
		if(world.getBlock(x, y, z) != this)
		{
			if(!world.getBlock(x, y, z).isAir(world, x, y, z))
				return 0;
			if(value > quantaPerBlock)
				value = quantaPerBlock;
			if(doFill)
				world.setBlockMetadataWithNotify(x, y, z, value - 1, 2);
		}
		else
		{
			int quanta = getQuantaValue(world, maxFill, y, z);
			value = Math.min(value, quantaPerBlock - quanta);
			if(doFill)
				world.setBlockMetadataWithNotify(x, y, z, quanta + value - 1, 2);
		}
		return (int) (value * ((float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlock));
	}
}