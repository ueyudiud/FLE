package nebula.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nebula.Log;
import nebula.client.util.IRenderRegister;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.util.Game;
import nebula.common.util.IRegisteredNameable;
import nebula.common.util.L;
import nebula.common.util.ToolHooks;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements IRegisteredNameable, IRenderRegister
{
	private static List<BlockBase> list = new ArrayList();
	
	/**
	 * Called when all others object(fluids, blocks, configurations, materials, etc)
	 * are already initialized.
	 */
	public static void post()
	{
		Log.info("Nubula reloading blocks...");
		for(BlockBase block : list)
		{
			block.postInitalizedBlocks();
		}
		list = null;
	}
	
	private final ThreadLocal<TileEntity> thread1 = new ThreadLocal();
	
	public final String blockName;
	protected final Item item;
	
	protected float harvestableSpeedMultiplier = 30F;
	protected float unharvestableSpeedMultiplier = 100F;
	
	public BlockBase(String name, Material materialIn)
	{
		this(Game.getActiveModID(), name, materialIn);
	}
	public BlockBase(String modid, String name, Material materialIn)
	{
		super(materialIn);
		if(list == null)
			throw new RuntimeException("The item has already post registered, please create new item before pre-init.");
		setUnlocalizedName(this.blockName = name);
		setDefaultState(initDefaultState(getDefaultState()));
		Game.registerBlock(this, modid, name, this.item = createItemBlock());
		list.add(this);//Added for re-register.
	}
	public BlockBase(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		this(Game.getActiveModID(), name, blockMaterialIn, blockMapColorIn);
	}
	public BlockBase(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(blockMaterialIn, blockMapColorIn);
		if(list == null)
			throw new RuntimeException("The item has already post registered, please create new item before pre-init.");
		setUnlocalizedName(this.blockName = name);
		setDefaultState(initDefaultState(getDefaultState()));
		Game.registerBlock(this, modid, name, this.item = createItemBlock());
		list.add(this);//Added for re-register.
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		
	}
	
	public void postInitalizedBlocks()
	{
		
	}
	
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state;
	}
	
	protected Item createItemBlock()
	{
		return new ItemBlockBase(this);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return "block." + this.blockName;
	}
	
	@Override
	public String getLocalizedName()
	{
		return LanguageManager.translateToLocal(getUnlocalizedName() + ".name");
	}
	
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		return getTranslateNameForItemStack(this.item.getDamage(stack));
	}
	
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName() + "@" + metadata;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return REGISTRY.getNameForObject(this).toString();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return this instanceof IExtendedDataBlock ?
				((IExtendedDataBlock) this).getDataFromState(state) & 0xF : super.getMetaFromState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ToolHooks.isToolHarvestable(this, world, pos, player);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.025F);
		
		if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
		{
			if(!onBlockHarvest(worldIn, pos, state, player, true))
			{
				List<ItemStack> items = L.castToArrayListOrWrap(getDrops(worldIn, pos, state, te, 0, true));
				
				ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0F, true, player);
				for (ItemStack item : items)
				{
					spawnAsEntity(worldIn, pos, item);
				}
			}
		}
		else if(!onBlockHarvest(worldIn, pos, state, player, false))
		{
			this.thread1.set(te);
			this.harvesters.set(player);
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = L.castToArrayListOrWrap(getDrops(worldIn, pos, state, te, i, false));
			
			ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, i, 1.0F, false, player);
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
			//			dropBlockAsItem(worldIn, pos, state, i);
			this.harvesters.set(null);
			this.thread1.remove();
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return this.item;
	}
	
	/**
	 * Called when block harvest.
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @param player
	 * @return If return true, will prevent drop item on the ground.
	 */
	protected boolean onBlockHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, boolean silkHarvest)
	{
		return false;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		float hardness = state.getBlockHardness(worldIn, pos);
		if (hardness < 0.0F)
			return 0.0F;
		
		if (!canBreakBlock(worldIn, pos, player))
			return 0F;
		else if(!canBreakEffective(state, player, worldIn, pos))
			return player.getDigSpeed(state, pos) / hardness / this.unharvestableSpeedMultiplier;
		else
			return player.getDigSpeed(state, pos) / hardness / this.harvestableSpeedMultiplier;
	}
	
	/**
	 * Match the block can be break by player (Not similar with harvest).
	 * @param world
	 * @param pos
	 * @param player
	 * @return
	 */
	public boolean canBreakBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return ToolHooks.isToolBreakable(world.getBlockState(pos), player);
	}
	
	public boolean canBreakEffective(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return canHarvestBlock(worldIn, pos, player);
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
		{
			List<ItemStack> items = getDrops(worldIn, pos, state, this.thread1.get(), fortune, false);
			chance = ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, this.harvesters.get());
			
			for (ItemStack item : items)
				if (worldIn.rand.nextFloat() <= chance)
				{
					spawnAsEntity(worldIn, pos, item);
				}
		}
	}
	
	@Override
	public final List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return L.castToArrayListOrWrap(getDrops(world, pos, state, world.getTileEntity(pos), fortune, false));
	}
	
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		List<ItemStack> ret = new ArrayList<>();
		
		if(silkTouch)
		{
			ItemStack stack = getSilkTouchDrop(state);
			if(stack != null)
			{
				ret.add(stack);
			}
		}
		else
		{
			Random rand = world instanceof World ? ((World) world).rand : RANDOM;
			int count = quantityDropped(state, fortune, rand);
			for(int i = 0; i < count; i++)
			{
				Item item = getItemDropped(state, rand, fortune);
				if (item != null)
				{
					ret.add(new ItemStack(item, 1, damageDropped(state)));
				}
			}
		}
		return ret;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		addUnlocalizedInfomation(stack, player, new UnlocalizedList(tooltip), advanced);
	}
	
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		
	}
	
	@Override
	public boolean isFertile(World world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			IPlantable plantable)
	{
		return false;
	}
	
	@Override
	public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
	{
		
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return true;
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
	{
		return false;
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		return false;
	}
	
	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color)
	{
		return false;
	}
	
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[]{getCreativeTabToDisplayOn()};
	}
	
	@Override
	public final IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack)
	{
		return getBlockPlaceState(world, pos, facing, hitX, hitY, hitZ, stack, placer);
	}
	
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stackIn.getItem().getMetadata(stackIn), placer, stackIn);
	}
}