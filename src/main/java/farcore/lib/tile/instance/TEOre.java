package farcore.lib.tile.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumOreAmount;
import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.energy.thermal.IThermalHandler;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.block.instance.ItemOre;
import farcore.lib.material.Mat;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddLandingEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockHarvest;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Burn;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_EntityWalk;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Toolable;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Update;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Burn;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Drops;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.TEStatic;
import farcore.lib.util.Direction;
import farcore.lib.util.SubTag;
import farcore.network.IPacket;
import farcore.util.U;
import farcore.util.U.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TEOre extends TEStatic implements IUpdatableTile,
ITP_BlockHardness, ITP_ExplosionResistance, ITB_EntityWalk, ITB_BlockPlacedBy,
ITP_HarvestCheck, ITB_Update, ITB_Burn, ITP_Burn, IThermalHandler, ITB_Toolable,
ITB_AddDestroyEffects, ITB_AddHitEffects, ITB_AddLandingEffects, ITB_BlockHarvest,
ITP_Drops, IToolableTile
{
	private static enum DropType
	{
		ore,
		chipped,
		gem,
		gem_chipped,
		nugget,
		cluster,
		protore;
	}
	
	private static final Mat STONE = M.stone;

	private Mat ore = M.VOID;
	public EnumOreAmount amount = EnumOreAmount.normal;
	public Mat rock = STONE;
	public RockType rockType = RockType.resource;
	public double heat;
	
	public TEOre(Mat ore, EnumOreAmount amount, Mat rock, RockType type)
	{
		this.ore = ore;
		this.amount = amount;
		this.rock = rock;
		rockType = type;
		initialized = true;
	}
	public TEOre()
	{
		initialized = false;
	}
	
	@Override
	protected float getSyncRange()
	{
		return 256F;
	}

	@Override
	public void sendToNearby(IPacket packet, float range)
	{
		if(Worlds.isAirNearby(worldObj, pos, true))
		{
			super.sendToNearby(packet, range);
		}
	}
	
	public Mat getOre()
	{
		if(worldObj.isRemote && !initialized)
		{
			sendToServer(new PacketTEAsk(worldObj, pos));
		}
		return ore;
	}

	public void setOre(Mat ore)
	{
		this.ore = ore;
		initialized = true;
		syncToNearby();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		ore = Mat.material(compound.getString("ore"));
		amount = EnumOreAmount.values()[compound.getByte("amount")];
		rock = Mat.material(compound.getString("rock"), STONE);
		rockType = RockType.values()[compound.getByte("type")];
		heat = compound.getDouble("heat");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("ore", ore.name);
		compound.setByte("amount", (byte) amount.ordinal());
		compound.setString("rock", rock.name);
		compound.setByte("type", (byte) rockType.ordinal());
		compound.setDouble("heat", heat);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		ore = Mat.material(nbt.getString("o"));
		amount = EnumOreAmount.values()[nbt.getByte("a")];
		rock = Mat.material(nbt.getString("r"), STONE);
		rockType = RockType.values()[nbt.getByte("t")];
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("o", ore.name);
		nbt.setByte("a", (byte) amount.ordinal());
		nbt.setString("r", rock.name);
		nbt.setByte("t", (byte) rockType.ordinal());
	}

	@Override
	public boolean onBlockClicked(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		return ore.getProperty(M.property_ore).onBlockClicked(this, player, side);
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		return ore.getProperty(M.property_ore).onBlockActivated(this, player, hand, stack, side, hitX, hitY, hitZ);
	}

	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if(Worlds.isNotOpaqueNearby(worldObj, pos))
		{
			markBlockRenderUpdate();
		}
	}
	
	public int getHarvestLevel()
	{
		switch (rockType)
		{
		case cobble_art :
			return 1;
		case cobble :
			return Math.max(ore.getProperty(M.property_ore).harvestLevel, rock.getProperty(M.property_rock).harvestLevel) / 2;
		default:
			return Math.max(ore.getProperty(M.property_ore).harvestLevel, rock.getProperty(M.property_rock).harvestLevel);
		}
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		float hardness = ore.getProperty(M.property_ore).hardness * .8F + rock.getProperty(M.property_ore).hardness * .2F;
		return hardness;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return Math.max(ore.getProperty(M.property_ore).explosionResistance, rock.getProperty(M.property_rock).explosionResistance);
	}
	
	@Override
	public void onEntityWalk(Entity entity)
	{
		ore.getProperty(M.property_ore).onEntityWalk(this, entity);
	}

	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			amount = ItemOre.getAmount(nbt);
			rock = ItemOre.getRockMaterial(nbt);
			rockType = ItemOre.getRockType(nbt);
		}
		setOre(Mat.material(stack.getItemDamage()));
	}
	
	@Override
	public void onUpdateTick(IBlockState state, Random random, boolean isTickRandomly)
	{
		ore.getProperty(M.property_ore).updateTick(this, random);
	}
	
	@Override
	public boolean onBlockHarvest(IBlockState state, EntityPlayer player, boolean silkHarvest)
	{
		Worlds.spawnDropsInWorld(this, getDrops(U.Players.getCurrentToolType(player)));
		return true;
	}

	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		return getDrops(EnumToolType.HAND_USABLE_TOOL);
	}

	private SubTag getOreType()
	{
		return ore.contain(SubTag.ORE_GEM) ? SubTag.ORE_GEM :
			ore.contain(SubTag.ORE_NOBLE) ? SubTag.ORE_NOBLE :
				ore.contain(SubTag.ORE_ROCKY) ? SubTag.ORE_ROCKY :
					ore.contain(SubTag.ORE_SALT) ? SubTag.ORE_SALT :
						ore.contain(SubTag.ORE_SIMPLE) ? SubTag.ORE_SIMPLE :
							null;
	}
	
	public List<ItemStack> getDrops(List<EnumToolType> types)
	{
		SubTag tag = getOreType();
		if(tag == null || types.contains(EnumToolType.hand))
			return new ArrayList();
		ArrayList<ItemStack> list = new ArrayList();
		if(types.contains(EnumToolType.rock_cutter))
		{
			addDrops(list, DropType.ore);
		}
		else if(types.contains(EnumToolType.laser))
		{
			addDrops(list, tag == SubTag.ORE_NOBLE ? DropType.protore :
				tag == SubTag.ORE_SALT ? DropType.cluster :
					DropType.ore);
		}
		else if(types.contains(EnumToolType.drill))
		{
			addDrops(list, tag == SubTag.ORE_SALT ? DropType.cluster :
				tag == SubTag.ORE_NOBLE ? DropType.gem :
					DropType.chipped);
		}
		else if(types.contains(EnumToolType.explosive))
		{
			addDrops(list, tag == SubTag.ORE_SALT ? DropType.cluster :
				tag == SubTag.ORE_NOBLE ? DropType.gem_chipped :
					DropType.chipped);
		}
		else if(types.contains(EnumToolType.pickaxe))
		{
			addDrops(list, tag == SubTag.ORE_SALT ? DropType.cluster :
				tag == SubTag.ORE_GEM ? DropType.gem :
					tag == SubTag.ORE_NOBLE ? DropType.nugget :
						DropType.ore);
		}
		else if(types.contains(EnumToolType.pickaxe))
		{
			addDrops(list, tag == SubTag.ORE_SALT ? DropType.cluster :
				tag == SubTag.ORE_GEM ? DropType.gem :
					tag == SubTag.ORE_NOBLE ? DropType.nugget :
						DropType.chipped);
		}
		return list;
	}
	
	private void addDrops(List<ItemStack> stacks, DropType type)
	{
		;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player)
	{
		ItemStack stack = player.inventory.getCurrentItem();
		String tool = EnumToolType.pickaxe.name();
		if (stack == null)
			return player.canHarvestBlock(getBlock(0, 0, 0));
		int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
		if (toolLevel < 0)
			return player.canHarvestBlock(getBlock(0, 0, 0));
		return toolLevel >= getHarvestLevel();
	}
	
	@Override
	public boolean onBurn(float burnHardness, Direction direction)
	{
		ore.getProperty(M.property_ore).onBurn(this, burnHardness, direction);
		if(rockType.isBurnable())
		{
			rockType = rockType.burned();
			markBlockUpdate();
		}
		return false;
	}
	
	@Override
	public boolean onBurningTick(Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		ore.getProperty(M.property_ore).onBurningTick(this, rand, fireSourceDir, fireState);
		return false;
	}

	@Override
	public boolean isFireSource(Direction side)
	{
		return ore.contain(SubTag.FIRE_SOURCE);
	}

	@Override
	public int getFireSpreadSpeed(Direction side)
	{
		return rockType.isBurnable() ? 80 : 0;
	}

	@Override
	public int getFlammability(Direction side)
	{
		return 0;
	}

	@Override
	public boolean isFlammable(Direction side)
	{
		return getFlammability(side) != 0;
	}

	@Override
	public boolean canBeBurned()
	{
		return false;
	}

	@Override
	public int getFireEncouragement()
	{
		return 0;
	}

	@Override
	public boolean canFireBurnOn(Direction side, boolean isCatchRain)
	{
		return false;
	}
	
	@Override
	public boolean canConnectTo(Direction direction)
	{
		return initialized;
	}

	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (float) (heat / ore.getProperty(M.property_basic).thermalConduct);
	}

	@Override
	public double getThermalConductivity(Direction direction)
	{
		return ore.getProperty(M.property_basic).thermalConduct * 0.3F + rock.getProperty(M.property_basic).thermalConduct * 0.7F;
	}

	@Override
	public void onHeatChange(Direction direction, double value)
	{
		heat += value;
	}

	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX,
			float hitY, float hitZ)
	{
		return ore.getProperty(M.property_ore).onToolClick(player, tool, stack, this, side, hitX, hitY, hitZ);
	}

	@Override
	public ActionResult<Float> onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, long duration, Direction side,
			float hitX, float hitY, float hitZ)
	{
		return ore.getProperty(M.property_ore).onToolUse(player, tool, stack, this, side, hitX, hitY, hitZ, duration);
	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, IBlockState iblockstate, EntityLivingBase entity,
			int numberOfParticles)
	{
		U.Server.addBlockLandingEffects(worldObj, pos, rock.rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, rockType), entity, numberOfParticles);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		U.Client.addBlockHitEffect(worldObj, random, rock.rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, rockType), target.sideHit, pos, manager);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		U.Client.addBlockDestroyEffects(worldObj, pos, rock.rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, rockType), manager);
		return true;
	}
}