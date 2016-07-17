package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.MC;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.block.ItemBlockBase;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import farcore.util.U.OreDict;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRock extends BlockBase
implements ISmartFallableBlock, IBurnCustomBehaviorBlock
{
	static final boolean[] burnable =
		{false, false, false, false, true, false, false, true, false, false, false};
	static final byte[] noMossy =
		{0, 1, 2, 3, 2, 5, 6, 6, 8, 9, 10};
	static final boolean[] displayInCreativeTab =
		{true, false, true, true, true, true, true, true, true, true, true};
	static final byte[] nonSilktouchingDropMeta =
		{2, 2, 2, 2, 3, 5, 6, 6, 8, 9, 10};
	static final String[] rockType =
		{"resource", "resource", "cobble", "smoothed", "mossy",
				"brick", "brick_crushed", "brick_mossy", "brick_smoothed", "chiseled", "brick_compact"};

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
	//10 Compact type.

	public final Mat material;
	public final float hardnessMultiplier;
	public final float resistanceMultiplier;
	public final int harvestLevel;
	private final int detTempCap;
	public final BlockRockSlab[] slabGroup;

	public BlockRock(String name, Mat material, String localName, int detTempCap)
	{
		super(name, Material.rock);
		this.material = material;
		harvestLevel = material.blockHarvestLevel;
		this.detTempCap = detTempCap;
		slabGroup = makeSlabs(name, ItemBlockSlab.class, material, localName, material.blockHardness, material.blockExplosionResistance, harvestLevel);
		setStepSound(soundTypeStone);
		setHardness(hardnessMultiplier = material.blockHardness);
		setResistance(resistanceMultiplier = material.blockExplosionResistance);
		setCreativeTab(FarCore.tabResourceBlock);
		setTickRandomly(true);

		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Heated" + localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), localName + " Cobblestone");
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), "Smoothed " + localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(4), "Mossy " + localName + " Cobblestone");
		LanguageManager.registerLocal(getTranslateNameForItemStack(5), localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(6), "Crushed " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(7), "Mossy " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(8), "Smoothed " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(9), "Chiseled " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(10), "Compact " + localName + " Brick");
		MC.stone.registerOre(material, new ItemStack(this, 1, 0));
		MC.stone.registerOre(material, new ItemStack(this, 1, 1));
		MC.cobble.registerOre(material, new ItemStack(this, 1, 2));
		MC.cobble.registerOre(material, new ItemStack(this, 1, 4));
		MC.brick.registerOre(material, new ItemStack(this, 1, 5));
		MC.brick.registerOre(material, new ItemStack(this, 1, 6));
		MC.brick.registerOre(material, new ItemStack(this, 1, 7));
		MC.brick.registerOre(material, new ItemStack(this, 1, 8));
		MC.brick.registerOre(material, new ItemStack(this, 1, 9));
		MC.brick.registerOre(material, new ItemStack(this, 1, 10));
		OreDict.registerValid("stoneSmoothed" + material.oreDictName, new ItemStack(this, 1, 3));
		OreDict.registerValid("stoneSlab" + material.oreDictName, slabGroup[0]);
		OreDict.registerValid("cobbleSlab" + material.oreDictName, slabGroup[2]);
		OreDict.registerValid("stoneSmoothedSlab" + material.oreDictName, slabGroup[3]);
		OreDict.registerValid("cobbleSlab" + material.oreDictName, slabGroup[4]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[5]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[6]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[7]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[8]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[9]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[10]);
	}
	public BlockRock(String name, Class<? extends ItemBlockBase> clazz1, Class<? extends ItemBlockSlab> clazz2, Mat material, String localName, int detTempCap,
			Object...objects)
	{
		super(name, clazz1, Material.rock, objects);
		this.material = material;
		harvestLevel = material.blockHarvestLevel;
		this.detTempCap = detTempCap;
		slabGroup = makeSlabs(name, clazz2, material, localName, material.blockHardness, material.blockHarvestLevel, harvestLevel, objects);
		setStepSound(soundTypeStone);
		setHardness(hardnessMultiplier = material.blockHardness);
		setResistance(resistanceMultiplier = material.blockExplosionResistance);
		setCreativeTab(FarCore.tabResourceBlock);
		setTickRandomly(true);

		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Heated " + localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), localName + " Cobblestone");
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), "Smoothed " + localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(4), "Mossy " + localName + " Cobblestone");
		LanguageManager.registerLocal(getTranslateNameForItemStack(5), localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(6), "Crushed " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(7), "Mossy " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(8), "Smoothed " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(9), "Chiseled " + localName + " Brick");
		LanguageManager.registerLocal(getTranslateNameForItemStack(10), "Compact " + localName + " Brick");
		MC.stone.registerOre(material, new ItemStack(this, 1, 0));
		MC.stone.registerOre(material, new ItemStack(this, 1, 1));
		MC.cobble.registerOre(material, new ItemStack(this, 1, 2));
		MC.cobble.registerOre(material, new ItemStack(this, 1, 4));
		MC.brick.registerOre(material, new ItemStack(this, 1, 5));
		MC.brick.registerOre(material, new ItemStack(this, 1, 6));
		MC.brick.registerOre(material, new ItemStack(this, 1, 7));
		MC.brick.registerOre(material, new ItemStack(this, 1, 8));
		MC.brick.registerOre(material, new ItemStack(this, 1, 9));
		MC.brick.registerOre(material, new ItemStack(this, 1, 10));
		OreDict.registerValid("stoneSmoothed" + material.oreDictName, new ItemStack(this, 1, 3));
		OreDict.registerValid("stoneSlab" + material.oreDictName, slabGroup[0]);
		OreDict.registerValid("cobbleSlab" + material.oreDictName, slabGroup[2]);
		OreDict.registerValid("stoneSmoothedSlab" + material.oreDictName, slabGroup[3]);
		OreDict.registerValid("cobbleSlab" + material.oreDictName, slabGroup[4]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[5]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[6]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[7]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[8]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[9]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[10]);
	}

	protected BlockRockSlab[] makeSlabs(String name, Class<? extends ItemBlockSlab> clazz, Mat material, String localName, float hardness, float resistance, int harvestLevel,
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
		ret[8] = new BlockRockSlab(8, this, ret, name, clazz, material, "Smoothed " + localName + " Brick", hardness, resistance, harvestLevel, objects);
		ret[9] = new BlockRockSlab(9, this, ret, name, clazz, material, "Chiseled " + localName + " Brick", hardness, resistance, harvestLevel, objects);
		ret[10] = new BlockRockSlab(10, this, ret, name, clazz, material, "Compact " + localName + " Brick", hardness, resistance, harvestLevel, objects);
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
		int meta = world.getBlockMetadata(x, y, z);
		if(burnable[meta])
			if(U.Worlds.isBlockNearby(world, x, y, z, Blocks.fire, -1, true))
				world.setBlockMetadataWithNotify(x, y, z, noMossy[meta], 3);
		switch (meta)
		{
		//		case 0 :
		//			int det = (int) ThermalNet.getTempDifference(world, x, y, z);
		//			if(det > detTempCap || (det >= detTempCap / 3 && rand.nextInt(detTempCap * 2 / 3) < det))
		//				world.setBlockMetadataWithNotify(x, y, z, 1, 3);
		//			break;
		//		case 1 :
		//			det = (int) ThermalNet.getTempDifference(world, x, y, z);
		//			if(det < detTempCap / 5)
		//				world.setBlockMetadataWithNotify(x, y, z, 0, 3);
		//			else if(det > detTempCap || (det >= detTempCap / 3 && rand.nextInt(detTempCap * 2 / 3) < det))
		//				if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block(), -1, true))
		//				{
		//					world.setBlockMetadataWithNotify(x, y, z, 2, 3);
		//					world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "sound.fizz", 0.5F, 2.6F + rand.nextFloat() - rand.nextFloat());
		//
		//					for (int l = 0; l < 6; ++l)
		//						world.spawnParticle("largesmoke", x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D);
		//				}
		//			break;
		case 2 :
		case 3 :
			if(!canBlockStay(world, x, y, z))
				U.Worlds.fallBlock(world, x, y, z, this);
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
			U.Worlds.fallBlock(world, x, y, z, this);
		int meta;
		if((meta = world.getBlockMetadata(x, y, z)) == 1)
			if(U.Worlds.isBlockNearby(world, x, y, z, EnumBlock.water.block, -1, true))
				world.setBlockMetadataWithNotify(x, y, z, 2, 3);
		if(burnable[meta] && U.Worlds.isBlockNearby(world, x, y, z, Blocks.fire, -1, true))
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return meta == 2 || meta == 3 ? world.isSideSolid(x, y - 1, z, ForgeDirection.UP) : true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		for (String element : rockType)
			register.registerIcon(element, getTextureName() + "/" + element);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return register.getIconFromName(rockType[meta % rockType.length]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i = 0; i < rockType.length; ++i)
			if(displayInCreativeTab[i])
				list.add(new ItemStack(item, 1, i));
		for(BlockRockSlab slab : slabGroup)
			slab.getSubBlocks(Item.getItemFromBlock(slab), tab, list);
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
			ret.add(stack);
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

	@Override
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
				world.spawnParticle("depthsuspend", x + u1, y + v1, z - 0.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .1;
				world.spawnParticle("depthsuspend", x + u1, y, z + v1, 0D, t1, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = -rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", x - 0.1, y + u1, z + v1, t1, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", x + u1, y + v1, z + 1.1, 0D, 0D, t1);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				world.spawnParticle("depthsuspend", x + u1, y + 1D, z + v1, 0D, 0D, 0D);
				u1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				v1 = (1D + rand.nextDouble() - rand.nextDouble()) * .5;
				t1 = rand.nextDouble() * .05;
				world.spawnParticle("depthsuspend", x + 1.1, y + u1, z + v1, t1, 0D, 0D);
			}
		}
	}

	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return burnable[world.getBlockMetadata(x, y, z)];
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return burnable[world.getBlockMetadata(x, y, z)] ? 180 : 0;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return 0;
	}

	@Override
	public boolean onBurn(World world, int x, int y, int z, float burnHardness, Direction direction)
	{
		world.setBlockMetadataWithNotify(x, y, z, noMossy[world.getBlockMetadata(x, y, z)], 2);
		return true;
	}

	@Override
	public boolean onBurningTick(World world, int x, int y, int z, Random rand, Direction fireSourceDir)
	{
		return false;
	}

	@Override
	public float getThermalConduct(World world, int x, int y, int z)
	{
		return material.thermalConduct;
	}

	@Override
	public int getFireEncouragement(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, noMossy[world.getBlockMetadata(x, y, z)], 2);
		return 1;
	}
}