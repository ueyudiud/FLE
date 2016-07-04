package farcore.alpha.block;

import static farcore.alpha.block.BlockRock.burnable;
import static farcore.alpha.block.BlockRock.displayInCreativeTab;
import static farcore.alpha.block.BlockRock.nonSilktouchingDropMeta;
import static farcore.alpha.block.BlockRock.rockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.alpha.enums.EnumBlockType;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.block.ISmartBurnableBlock;
import farcore.alpha.interfaces.block.ISmartFallableBlock;
import farcore.alpha.util.IconHook;
import farcore.energy.thermal.ThermalNet;
import farcore.entity.EntityFallingBlockExtended;
import farcore.enums.Direction;
import farcore.enums.EnumBlock;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * The sub block type of rock block, all config see BlockRock.
 * @author ueyudiud
 * @see farcore.alpha.block.BlockRock
 */
public class BlockRockSlab extends BlockBase
implements ISmartBurnableBlock
{
	//Meta meaning
	//0 for half - down
	//1 for half - up
	//2 for half - south
	//3 for half - north
	//4 for half - west
	//5 for half - east
	//6 for half - du all
	//7 for half - sn all
	//8 for half - ew all
	//Don't have opposite stair type yet.
	
	private final BlockRock parent;
	/**
	 * The meta of main block.
	 */
	private int meta;
	/**
	 * The group of rock slab.
	 */
	private final BlockRockSlab[] group;
	public final float hardnessMultiplier;
	public final float resistanceMultiplier;
	public final int harvestLevel;
	
	BlockRockSlab(int meta, BlockRock parent, BlockRockSlab[] group, String name, Material material, String localName, float hardness, float resistance, int harvestLevel)
	{
		super(EnumBlockType.building, name, ItemBlockSlab.class, material);
		this.meta = meta;
		this.parent = parent;
		this.group = group;
		this.harvestLevel = harvestLevel;
		setStepSound(soundTypeStone);
		setHardness((this.hardnessMultiplier = hardness) * 0.6F);
		setResistance((this.resistanceMultiplier = resistance) * 0.4F);

		setTickRandomly(true);
		
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(1), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(2), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(3), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(4), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(5), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(6), "Double " + localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(7), "Double " + localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(8), "Double " + localName + " Slab");
	}
	BlockRockSlab(int meta, BlockRock parent, BlockRockSlab[] group, String name, Class<? extends ItemBlockSlab> clazz, Material material, String localName, float hardness, float resistance, int harvestLevel,
			Object...objects)
	{
		super(EnumBlockType.building, name + ".slab." + meta, clazz, material, objects);
		this.meta = meta;
		this.parent = parent;
		this.harvestLevel = harvestLevel;
		this.group = group;
		setStepSound(soundTypeStone);
		setHardness((this.hardnessMultiplier = hardness) * 0.6F);
		setResistance((this.resistanceMultiplier = resistance) * 0.4F);

		setTickRandomly(true);

		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(1), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(2), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(3), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(4), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(5), localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(6), "Double " + localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(7), "Double " + localName + " Slab");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(8), "Double " + localName + " Slab");
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		return Direction.oppsite[side];
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y,
			int z)
	{
		maxX = 1.0F;
		maxY = 1.0F;
		maxZ = 1.0F;
		minX = 0.0F;
		minY = 0.0F;
		minZ = 0.0F;
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0 :
			maxY = 0.5F;
			break;
		case 1 :
			minY = 0.5F;
			break;
		case 2 :
			maxZ = 0.5F;
			break;
		case 3 :
			minZ = 0.5F;
			break;
		case 4 :
			maxX = 0.5F;
			break;
		case 5 : 
			minX = 0.5F;
			break;
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y,
			int z)
	{
		maxX = 1.0F;
		maxY = 1.0F;
		maxZ = 1.0F;
		minX = 0.0F;
		minY = 0.0F;
		minZ = 0.0F;
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0 :
			maxY = 0.5F;
			break;
		case 1 :
			minY = 0.5F;
			break;
		case 2 :
			maxZ = 0.5F;
			break;
		case 3 :
			minZ = 0.5F;
			break;
		case 4 :
			maxX = 0.5F;
			break;
		case 5 : 
			minX = 0.5F;
			break;
		}
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return true;
	}
	
	@Override
	public String getHarvestTool(int metadata)
	{
		return "pickaxe";
	}
	
	@Override
	public boolean isToolEffective(String type, int metadata)
	{
		return "pickaxe".equals(type);
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		switch (this.meta)
		{
		case 1 :
			return 1;
		default:
			return super.getHarvestLevel(metadata);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		int meta = world.getBlockMetadata(x, y, z);
		switch (this.meta)
		{
		case 0 :
			int det = (int) ThermalNet.getTempDifference(world, x, y, z);
			if(det > 149 || (det >= 50 && rand.nextInt(150) < det))
			{
				world.setBlock(x, y, z, group[1], meta, 3);
			}
			break;
		case 1 :
			det = (int) ThermalNet.getTempDifference(world, x, y, z);
			if(det < 30)
			{
				world.setBlock(x, y, z, group[0], meta, 3);
			}
			else if(det > 149 || (det >= 50 && rand.nextInt(150) < det))
			{
				if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block(), -1, true))
				{
					world.setBlock(x, y, z, group[2], meta, 3);
					world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "sound.fizz", 0.5F, 2.6F + rand.nextFloat() - rand.nextFloat());

                    for (int l = 0; l < 6; ++l)
                    {
                        world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		if(!canBlockStay(world, x, y, z))
		{
			U.Worlds.fallBlock(world, x, y, z, this);
		}
		if(world.getBlockMetadata(x, y, z) == 1)
		{
			if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block(), -1, true))
			{
				world.setBlockMetadataWithNotify(x, y, z, 2, 3);
			}
		}
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) > 5;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return FarCore.handlerB.getRenderId();
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		int meta;
		return (meta = world.getBlockMetadata(x, y, z)) > 5 ? true : 
			meta == side.ordinal();
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) > 5 ? 3 : 255;
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register){}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return IconHook.get(parent, rockType[this.meta]);
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item, 1, 0));
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return 0;
	}
		
	@Override
	protected ItemStack createStackedBlock(int meta)
	{
		return new ItemStack(this, meta > 5 ? 2 : 1, damageDropped(meta));
	}
	
	protected ItemStack createStackedBlock(int meta, boolean silkTouching)
	{
		return new ItemStack(silkTouching ? this :
			group[this.meta], meta > 5 ? 2 : 1, damageDropped(meta));
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		ItemStack stack = createStackedBlock(metadata, true);
		if(stack != null)
		{
			ret.add(stack);
		}
		return ret;
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		if(this.meta == 1)
		{
			double u1, v1, t1;
			for(int i = 0; i < 2; ++i)
			{
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y + v1, (double) z - 0.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .1;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y, (double) z + v1, 0D, t1, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x - 0.1, (double) y + u1, (double) z + v1, t1, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y + v1, (double) z + 1.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				world.spawnParticle("depthsuspend", (double) x + u1, (double) y + 1D, (double) z + v1, 0D, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", (double) x + 1.1, (double) y + u1, (double) z + v1, t1, 0D, 0D);
			}
		}
	}
	
	@Override
	public boolean onBurning(World world, int x, int y, int z, int level)
	{
		return false;
	}
	
	@Override
	public int canBeBurned(World world, int x, int y, int z, int level)
	{
		return burnable[this.meta] ? 20 : 0;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face, int level)
	{
		return burnable[this.meta];
	}
	
	@Override
	public boolean onFlame(World world, int x, int y, int z, ForgeDirection face, int level)
	{
		return burnable[this.meta];
	}
	
	@Override
	public boolean onBurned(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(burnable[this.meta])
		{
			world.setBlock(x, y, z, group[this.meta], meta, 3);
		}
		return true;
	}
	
	@Override
	public int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness)
	{
		return 10;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getFireIcon(IBlockAccess world, int x, int y, int z) {return null;}
	
	@Override
	public int getBurningTemperature(World world, int x, int y, int z, int level)
	{
		return 600;
	}
}