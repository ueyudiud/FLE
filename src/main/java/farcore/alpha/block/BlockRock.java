package farcore.alpha.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.alpha.enums.EnumBlockType;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.block.ISmartBurnableBlock;
import farcore.alpha.interfaces.block.ISmartFallableBlock;
import farcore.energy.thermal.ThermalNet;
import farcore.entity.EntityFallingBlockExtended;
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
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRock extends BlockBase
implements ISmartFallableBlock, ISmartBurnableBlock
{
	static final boolean[] burnable =
		{false, false, false, true, false, false, false, true, false, false};
	static final boolean[] displayInCreativeTab =
		{true, false, true, true, true, true, true, true, true, true};
	static final int[] nonSilktouchingDropMeta =
		{2, 2, 2, 2, 3, 5, 6, 6, 8, 9};
	static final String[] rockType = 
		{"resource", "resource", "cobble", "mossy", "smoothed", 
				"brick", "brick_crushed", "brick_mossy", "brick_smoothed", "chiseled"};
	
	//Stone meta meaning.
	//0 Rock type.
	//1 Heated type, use to crush to cobble.
	//2 Cobble type.
	//3 Smoothed type.
	//4 Mossy type.
	//5 Brick type.
	//6 Crush brick type.
	//7 Mossy brick type.
	//8 Smoothed brick type.
	//9 Chiseled type.

	public final float hardnessMultiplier;
	public final float resistanceMultiplier;
	public final int harvestLevel;
	public final BlockRockSlab[] slabGrop;
	 
	public BlockRock(String name, Material material, String localName, float hardness, float resistance, int harvestLevel)
	{
		super(EnumBlockType.building, name, material);
		this.harvestLevel = harvestLevel;
		this.slabGrop = makeSlabs(name, ItemBlockSlab.class, material, localName, hardness, resistance, harvestLevel);
		setStepSound(soundTypeStone);
		setHardness(this.hardnessMultiplier = hardness);
		setResistance(this.resistanceMultiplier = resistance);

		setTickRandomly(true);
		
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(1), "Heated" + localName);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(2), localName + " Cobblestone");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(3), "Smoothed " + localName);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(4), "Mossy " + localName + " Cobblestone");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(5), localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(6), "Crushed " + localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(7), "Mossy " + localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(8), "Smoothed " + localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(9), "Chiseled " + localName);
	}
	public BlockRock(String name, Class<? extends ItemBlockBase> clazz1, Class<? extends ItemBlockSlab> clazz2, Material material, String localName, float hardness, float resistance, int harvestLevel,
			Object...objects)
	{
		super(EnumBlockType.building, name, clazz1, material, objects);
		this.harvestLevel = harvestLevel;
		this.slabGrop = makeSlabs(name, clazz2, material, localName, hardness, resistance, harvestLevel, objects);
		setStepSound(soundTypeStone);
		setHardness(this.hardnessMultiplier = hardness);
		setResistance(this.resistanceMultiplier = resistance);

		setTickRandomly(true);

		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(1), "Heated " + localName);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(2), localName + " Cobblestone");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(3), "Smoothed " + localName);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(4), "Mossy " + localName + " Cobblestone");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(5), localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(6), "Crushed " + localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(7), "Mossy " + localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(8), "Smoothed " + localName + " Brick");
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(9), "Chiseled " + localName);
	}
	
	protected BlockRockSlab[] makeSlabs(String name, Class<? extends ItemBlockSlab> clazz, Material material, String localName, float hardness, float resistance, int harvestLevel,
			Object...objects)
	{
		BlockRockSlab[] ret = new BlockRockSlab[rockType.length];
		ret[0] = new BlockRockSlab(0, this, ret, name, clazz, material, localName, hardness, resistance, harvestLevel, objects);
		ret[1] = new BlockRockSlab(1, this, ret, name, clazz, material, "Heated " + localName, hardness, resistance, harvestLevel, objects);
		ret[2] = new BlockRockSlab(2, this, ret, name, clazz, material, localName + " Cobblestone", hardness, resistance, harvestLevel, objects);
		ret[3] = new BlockRockSlab(3, this, ret, name, clazz, material, "Smoothed " + localName, hardness, resistance, harvestLevel, objects);
		ret[4] = new BlockRockSlab(4, this, ret, name, clazz, material, "Mossy " + localName + " Cobblestone", hardness, resistance, harvestLevel, objects);
		ret[5] = new BlockRockSlab(5, this, ret, name, clazz, material, localName + " Brick", hardness, resistance, harvestLevel, objects);
		ret[6] = new BlockRockSlab(6, this, ret, name, clazz, material, "Crushed " + localName + " Brick", hardness, resistance, harvestLevel, objects);
		ret[7] = new BlockRockSlab(7, this, ret, name, clazz, material, "Mossy " + localName + " Brick", hardness, resistance, harvestLevel, objects);
		ret[8] = new BlockRockSlab(8, this, ret, name, clazz, material, "Chiseled " + localName + " Brick", hardness, resistance, harvestLevel, objects);
		ret[9] = new BlockRockSlab(9, this, ret, name, clazz, material, "Chiseled " + localName, hardness, resistance, harvestLevel, objects);
		return ret;
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
		switch (metadata)
		{
		case 1 :
		case 2 :
		case 3 :
			return 1;
		default:
			return harvestLevel;
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0 :
			int det = (int) ThermalNet.getTempDifference(world, x, y, z);
			if(det > 149 || (det >= 50 && rand.nextInt(150) < det))
			{
				world.setBlockMetadataWithNotify(x, y, z, 1, 3);
			}
			break;
		case 1 :
			det = (int) ThermalNet.getTempDifference(world, x, y, z);
			if(det < 30)
			{
				world.setBlockMetadataWithNotify(x, y, z, 0, 3);
			}
			else if(det > 149 || (det >= 50 && rand.nextInt(150) < det))
			{
				if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block(), -1, true))
				{
					world.setBlockMetadataWithNotify(x, y, z, 2, 3);
					world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "sound.fizz", 0.5F, 2.6F + rand.nextFloat() - rand.nextFloat());

                    for (int l = 0; l < 6; ++l)
                    {
                        world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
				}
			}
			break;
		case 2 : 
		case 3 :
			if(!canBlockStay(world, x, y, z))
			{
				U.Worlds.fallBlock(world, x, y, z, this);
			}
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
		int meta = world.getBlockMetadata(x, y, z);
		return meta == 2 || meta == 3 ? world.isSideSolid(x, y - 1, z, ForgeDirection.UP) : true;
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		for(int i = 0; i < rockType.length; ++i)
		{
			register.registerIcon(rockType[i], getTextureName() + "/" + rockType[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return register.getIconFromName(rockType[meta % rockType.length]);
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i = 0; i < rockType.length; ++i)
		{
			if(displayInCreativeTab[i])
			{
				list.add(new ItemStack(item, 1, i));
			}
		}
		for(BlockRockSlab slab : slabGrop)
		{
			slab.getSubBlocks(Item.getItemFromBlock(slab), tab, list);
		}
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta == 1 ? 0 : meta;
	}
	
	public int damageDropped(int meta, boolean silkTouching)
	{
		return silkTouching ? damageDropped(meta) : nonSilktouchingDropMeta[meta];
	}
	
	@Override
	protected ItemStack createStackedBlock(int meta)
	{
		return new ItemStack(this, 1, damageDropped(meta));
	}
	
	protected ItemStack createStackedBlock(int meta, boolean silkTouching)
	{
		return new ItemStack(this, 1, damageDropped(meta, silkTouching));
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
	
	@Override
	public void onStartFalling(World world, int x, int y, int z)
	{
		
	}
	
	@Override
	public boolean canFallingBlockStay(World world, int x, int y, int z, int meta)
	{
		return canBlockStay(world, x, y, z);
	}
	
	@Override
	public boolean onFallOnGround(World world, int x, int y, int z, int meta, int height, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	@Override
	public boolean onDropFallenAsItem(World world, int x, int y, int z, int meta, NBTTagCompound tileNBT)
	{
		U.Worlds.spawnDropsInWorld(world, x, y, z, getDrops(world, x, y, z, meta, 0, false));
		return true;
	}
	
	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		return 2.5F + hardnessMultiplier / 4F;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 1)
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
		return burnable[world.getBlockMetadata(x, y, z)] ? 20 : 0;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face, int level)
	{
		return burnable[world.getBlockMetadata(x, y, z)];
	}
	
	@Override
	public boolean onFlame(World world, int x, int y, int z, ForgeDirection face, int level)
	{
		return burnable[world.getBlockMetadata(x, y, z)];
	}
	
	@Override
	public boolean onBurned(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(burnable[meta])
		{
			world.setBlockMetadataWithNotify(x, y, z, nonSilktouchingDropMeta[meta], 3);
		}
		return true;
	}
	
	@Override
	public int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness)
	{
		return 20;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getFireIcon(IBlockAccess world, int x, int y, int z) {return null;}
	
	@Override
	public int getBurningTemperature(World world, int x, int y, int z, int level)
	{
		return 640;
	}
}