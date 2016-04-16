package fle.core.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockBase;
import farcore.enums.EnumBlock;
import farcore.interfaces.energy.thermal.IThermalProviderBlock;
import farcore.util.U;
import farcore.util.Unit;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIce extends BlockBase implements IThermalProviderBlock
{
	public BlockIce()
	{
		super("ice", Material.ice);
		this.slipperiness = 0.96F;
		this.blockHardness = 0.6F;
		this.setTickRandomly(true);
		EnumBlock.ice.setBlock(this);
	}
	
	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		super.registerBlockIcons(register);
	}
	
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;  
	}
	
	@Override
	protected void onBlockHarvest(World world, EntityPlayer player, int x, int y, int z, int meta,
			boolean silkTouching)
	{
		if(!silkTouching)
		{
			EnumBlock.water.spawn(world, x, y, z, new Object[0]);
		}
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		return Math.max(6 - world.getBlockMetadata(x, y, z), 1) * blockHardness * .5F;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		int meta = world.getBlockMetadata(x, y, z);
        if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 1 + meta)
        {
        	if (meta < 16)
        	{
        		world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
        		return;
        	}
        	
            if (world.provider.isHellWorld)
            {
                world.setBlockToAir(x, y, z);
                return;
            }
            else
            {
            	EnumBlock.water.spawn(world, x, y, z, new Object[0]);
            }
        }
	}
	
    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int getMobilityFlag()
    {
        return 0;
    }
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		return new ArrayList();
	}

	@Override
	public float getBlockTemperature(World world, int x, int y, int z)
	{
		return Unit.minIceTemp + (Unit.C_0_Point - Unit.minIceTemp) * world.getBlockMetadata(x, y, z) / 16F;
	}

	@Override
	public float getThermalConductivity(World world, int x, int y, int z)
	{
		return Unit.iceThermalConductivity;
	}

	@Override
	public void onHeatChanged(World world, int x, int y, int z, float input)
	{
		if(input > Unit.iceSpecificHeat)
		{
			int floor = (int) (input / Unit.iceSpecificHeat);
			int meta = world.getBlockMetadata(x, y, z);
			if(meta + floor < 16)
			{
				world.setBlockMetadataWithNotify(x, y, z, meta - floor, 3);
			}
			else
			{
				EnumBlock.water.spawn(world, x, y, z);
			}
		}
	}
}