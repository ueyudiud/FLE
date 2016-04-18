package farcore.block.plant.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.substance.SubstanceWood;
import farcore.util.FleLog;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLogNatural extends BlockLog
{
	private SubstanceWood wood;
	private int cacheDrop = 0;
	
	protected BlockLogNatural(SubstanceWood wood)
	{
		super("log." + wood.getName(), Material.wood);
		setTickRandomly(true);
		setBlockTextureName("fle:log/" + wood.getName());
		setHarvestLevel("axe", 0);
		setResistance(0.3F);
		blockHardness = wood.hardness / 5F + 1F;
		this.wood = wood;
	}
	
	@Override
	public int tickRate(World world)
	{
		return 20;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(!world.isRemote)
		{
			if(world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP))
				return;
		    for (int h = -1; h <= 1; h++)
		    {
		    	for (int g = -1; g <= 1; g++)
		    	{
		    		for (int f = -1; f <= 1; f++)
		    		{
		    			if ((h | g | f) != 0 && isLog(world, x + h, y + g, z + f))
		    			{
		    				return;
		    			}
		    		}
		    	}
		    }
		    world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		if(world.isRemote) return;
		if(world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP))
			return;
	    for (int h = -1; h <= 1; h++)
	    {
	    	for (int g = -1; g <= 1; g++)
	    	{
	    		for (int f = -1; f <= 1; f++)
	    		{
	    			if ((h | g | f) != 0 && isLog(world, x + h, y + g, z + f))
	    			{
	    				return;
	    			}
	    		}
	    	}
	    }
	    world.setBlockToAir(x, y, z);
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int p_149681_5_,
			EntityPlayer player)
	{
		harvestBlock(world, player, x, y, z, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		boolean isAxe = EnumToolType.axe.match(stack);
		if(!world.isRemote)
		{
			if(isAxe)
			{
				breakTree(world, x, y, z);
			}
			else
			{
				world.setBlock(x, y, z, this, meta, 3);
			}
		}
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		try
		{
			breakTree(world, x, y, z);
		}
		catch(OutOfMemoryError error)
		{
			FleLog.getCoreLogger().warn("The out of memory prevent this tree destory.");
		}
	}
	
	public boolean isLog(World world, int x, int y, int z)
	{
		return U.Worlds.isBlock(world, x, y, z, this, -1, false);
	}
	
	public void breakTree(World world, int x, int y, int z)
	{
		boolean[][][] cacheArray;
		scanLog(world, x, y, z, cacheArray = new boolean[2 * V.treeScanRange + 1][256][2 * V.treeScanRange + 1], (byte) 0, (byte) 0, (byte) 0);
		if(cacheDrop > 0)
		{
			dropBlockAsItem(world, x, y, z, EnumItem.log.instance(1, wood, cacheDrop));
		}
		cacheDrop = 0;
	}
			
	public void scanLog(World world, int i, int j, int k, boolean[][][] array, byte x, byte y, byte z)
	{
		if(y >= 0 && j + y < 256)
		{
			int offsetX = 0,
					offsetY = 0,
					offsetZ = 0;
			array[x + V.treeScanRange][y][z + V.treeScanRange] = true;
			for (offsetX = -3; offsetX <= 3; offsetX++)
				for (offsetZ = -3; offsetZ <= 3; offsetZ++)
					for (offsetY = 0; offsetY <= 2; offsetY++)
						if((offsetX != 0 || offsetY != 0 || offsetZ != 0) &&
								Math.abs(x + offsetX) <= V.treeScanRange &&
								Math.abs(z + offsetZ) <= V.treeScanRange &&
								j + y + offsetY < 256)
						{
							if(!array[x + offsetX + V.treeScanRange][y + offsetY][z + offsetZ + V.treeScanRange] && 
									isLog(world, i + x + offsetX, j + y + offsetY, k + z + offsetZ))
							{
								scanLog(world, i, j, k, array, (byte) (x + offsetX), (byte) (y + offsetY), (byte) (z + offsetZ));
							}
						}
			++cacheDrop;
			world.setBlock(x + i, y + j, z + k, Blocks.air, 0, 2);
			U.Worlds.spawnDropsInWorld(world, i + x, j + y, k + z, otherDrop());
		}
	}
	
	private List<ItemStack> otherDrop()
	{
		return new ArrayList();
	}
	
	private void notifyLeaves(World world, int x, int y, int z)
	{
		world.notifyBlockChange(x + 1, y, z, Blocks.air);
		world.notifyBlockChange(x - 1, y, z, Blocks.air);
		world.notifyBlockChange(x, y + 1, z, Blocks.air);
		world.notifyBlockChange(x, y - 1, z, Blocks.air);
		world.notifyBlockChange(x, y, z + 1, Blocks.air);
		world.notifyBlockChange(x, y, z - 1, Blocks.air);
	}
	
	@Override
	public boolean onBurned(World world, int x, int y, int z)
	{
		breakTree(world, x, y, z);
		return true;
	}
	
	@Override
	public int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness)
	{
		return 100;
	}

	@Override
	public int getBurningTemperature(World world, int x, int y, int z, int level)
	{
		return 500;
	}
}