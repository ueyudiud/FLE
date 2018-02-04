/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.blocks.terria.BlockRock;
import farcore.blocks.terria.ItemOre;
import farcore.data.EnumOreAmount;
import farcore.data.EnumRockType;
import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.energy.thermal.IThermalHandler;
import farcore.lib.material.Mat;
import nebula.client.util.Client;
import nebula.common.network.IPacket;
import nebula.common.network.packet.PacketTEAsk;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddLandingEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockHarvest;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_Burn;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_EntityWalk;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_Update;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Burn;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_HarvestCheck;
import nebula.common.tile.IToolableTile;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TEStatic;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import nebula.common.util.Players;
import nebula.common.util.Server;
import nebula.common.util.SubTag;
import nebula.common.util.Worlds;
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

public class TEOre extends TEStatic
implements IUpdatableTile, ITP_BlockHardness, ITP_ExplosionResistance, ITB_EntityWalk, ITB_BlockPlacedBy, ITP_HarvestCheck, ITB_Update, ITB_Burn, ITP_Burn, IThermalHandler, ITB_AddDestroyEffects, ITB_AddHitEffects, ITB_AddLandingEffects, ITB_BlockHarvest, ITP_Drops, IToolableTile
{
	private static enum DropType
	{
		ore, chipped, gem, gem_chipped, nugget, cluster, protore;
	}
	
	private static final Mat STONE = M.stone;
	
	private Mat				ore			= Mat.VOID;
	public EnumOreAmount	amount		= EnumOreAmount.normal;
	public Mat				rock		= STONE;
	public EnumRockType		rockType	= EnumRockType.resource;
	public long				heat;
	
	public TEOre(Mat ore, EnumOreAmount amount, Mat rock, EnumRockType type)
	{
		this.ore = ore;
		this.amount = amount;
		this.rock = rock;
		this.rockType = type;
		this.initialized = true;
	}
	
	public TEOre()
	{
		this.initialized = false;
	}
	
	@Override
	protected float getSyncRange()
	{
		return 256F;
	}
	
	@Override
	public void sendToNearby(IPacket packet, float range)
	{
		if (Worlds.isAirNearby(this.world, this.pos, true))
		{
			super.sendToNearby(packet, range);
		}
	}
	
	public Mat getOre()
	{
		if (this.world.isRemote && !this.initialized)
		{
			sendToServer(new PacketTEAsk(this.world, this.pos));
		}
		return this.ore;
	}
	
	public void setOre(Mat ore)
	{
		this.ore = ore;
		this.initialized = true;
		syncToNearby();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.ore = Mat.material(compound.getString("ore"));
		this.amount = EnumOreAmount.values()[compound.getByte("amount")];
		this.rock = Mat.material(compound.getString("rock"), STONE);
		this.rockType = EnumRockType.values()[compound.getByte("type")];
		this.heat = compound.getLong("heat");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("ore", this.ore.name);
		compound.setByte("amount", (byte) this.amount.ordinal());
		compound.setString("rock", this.rock.name);
		compound.setByte("type", (byte) this.rockType.ordinal());
		compound.setLong("heat", this.heat);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.ore = Mat.getMaterialByIDOrDefault(nbt, "o", Mat.VOID);
		this.amount = NBTs.getEnumOrDefault(nbt, "a", EnumOreAmount.normal);
		this.rock = Mat.getMaterialByIDOrDefault(nbt, "r", STONE);
		this.rockType = NBTs.getEnumOrDefault(nbt, "t", EnumRockType.resource);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("o", this.ore.id);
		nbt.setByte("a", (byte) this.amount.ordinal());
		nbt.setShort("r", this.rock.id);
		nbt.setByte("t", (byte) this.rockType.ordinal());
	}
	
	@Override
	public boolean onBlockClicked(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		return this.ore.getProperty(MP.property_ore).onBlockClicked(this, player, side);
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		return this.ore.getProperty(MP.property_ore).onBlockActivated(this, player, hand, stack, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if (Worlds.isNotOpaqueNearby(this.world, pos))
		{
			markBlockRenderUpdate();
		}
	}
	
	public int getHarvestLevel()
	{
		switch (this.rockType)
		{
		case cobble_art:
			return 1;
		case cobble:
			return Math.max(this.ore.getProperty(MP.property_ore).harvestLevel, this.rock.getProperty(MP.property_rock).harvestLevel) / 2;
		default:
			return Math.max(this.ore.getProperty(MP.property_ore).harvestLevel, this.rock.getProperty(MP.property_rock).harvestLevel);
		}
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		float hardness = this.ore.getProperty(MP.property_ore).hardness * .8F + this.rock.getProperty(MP.property_ore).hardness * .2F;
		return hardness;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return Math.max(this.ore.getProperty(MP.property_ore).explosionResistance, this.rock.getProperty(MP.property_rock).explosionResistance);
	}
	
	@Override
	public void onEntityWalk(Entity entity)
	{
		this.ore.getProperty(MP.property_ore).onEntityWalk(this, entity);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			this.amount = ItemOre.getAmount(nbt);
			this.rock = ItemOre.getRockMaterial(nbt);
			this.rockType = ItemOre.getRockType(nbt);
		}
		setOre(Mat.material(stack.getItemDamage()));
	}
	
	@Override
	public void onUpdateTick(IBlockState state, Random random, boolean isTickRandomly)
	{
		this.ore.getProperty(MP.property_ore).updateTick(this, random);
	}
	
	@Override
	public boolean onBlockHarvest(IBlockState state, EntityPlayer player, boolean silkHarvest)
	{
		Worlds.spawnDropsInWorld(this, getDrops(Players.getCurrentToolType(player)));
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		return getDrops(EnumToolType.HAND_USABLE_TOOL);
	}
	
	private SubTag getOreType()
	{
		return this.ore.contain(SubTags.ORE_GEM) ? SubTags.ORE_GEM
				: this.ore.contain(SubTags.ORE_NOBLE) ? SubTags.ORE_NOBLE : this.ore.contain(SubTags.ORE_ROCKY) ? SubTags.ORE_ROCKY : this.ore.contain(SubTags.ORE_SALT) ? SubTags.ORE_SALT : this.ore.contain(SubTags.ORE_SIMPLE) ? SubTags.ORE_SIMPLE : null;
	}
	
	public List<ItemStack> getDrops(List<EnumToolType> types)
	{
		SubTag tag = getOreType();
		if (tag == null || types.contains(EnumToolType.HAND)) return new ArrayList();
		ArrayList<ItemStack> list = new ArrayList();
		if (types.contains(EnumToolTypes.ROCK_CUTTER))
		{
			addDrops(list, DropType.ore);
		}
		else if (types.contains(EnumToolTypes.LASER))
		{
			addDrops(list, tag == SubTags.ORE_NOBLE ? DropType.protore : tag == SubTags.ORE_SALT ? DropType.cluster : DropType.ore);
		}
		else if (types.contains(EnumToolTypes.DRILL))
		{
			addDrops(list, tag == SubTags.ORE_SALT ? DropType.cluster : tag == SubTags.ORE_NOBLE ? DropType.gem : DropType.chipped);
		}
		else if (types.contains(EnumToolTypes.EXPLOSIVE))
		{
			addDrops(list, tag == SubTags.ORE_SALT ? DropType.cluster : tag == SubTags.ORE_NOBLE ? DropType.gem_chipped : DropType.chipped);
		}
		else if (types.contains(EnumToolType.PICKAXE))
		{
			addDrops(list, tag == SubTags.ORE_SALT ? DropType.cluster : tag == SubTags.ORE_GEM ? DropType.gem : tag == SubTags.ORE_NOBLE ? DropType.nugget : DropType.ore);
		}
		else if (types.contains(EnumToolTypes.HAMMER_DIGABLE))
		{
			addDrops(list, tag == SubTags.ORE_SALT ? DropType.cluster : tag == SubTags.ORE_GEM ? DropType.gem : tag == SubTags.ORE_NOBLE ? DropType.nugget : DropType.chipped);
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
		String tool = EnumToolType.PICKAXE.name;
		if (stack == null) return player.canHarvestBlock(getBlockState(0, 0, 0));
		int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
		if (toolLevel < 0) return player.canHarvestBlock(getBlockState(0, 0, 0));
		return toolLevel >= getHarvestLevel();
	}
	
	@Override
	public boolean onBurn(float burnHardness, Direction direction)
	{
		this.ore.getProperty(MP.property_ore).onBurn(this, burnHardness, direction);
		if (this.rockType.isBurnable())
		{
			this.rockType = this.rockType.burned();
			markBlockUpdate();
		}
		return false;
	}
	
	@Override
	public boolean onBurningTick(Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		this.ore.getProperty(MP.property_ore).onBurningTick(this, rand, fireSourceDir, fireState);
		return false;
	}
	
	@Override
	public boolean isFireSource(Direction side)
	{
		return this.ore.contain(SubTags.FIRE_SOURCE);
	}
	
	@Override
	public int getFireSpreadSpeed(Direction side)
	{
		return this.rockType.isBurnable() ? 80 : 0;
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
		return this.initialized;
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (float) (this.heat / this.rock.heatCapacity);
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return this.ore.thermalConductivity * 0.3F + this.rock.thermalConductivity * 0.7F;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.heat += value;
	}
	
	@Override
	public double getHeatCapacity(Direction direction)
	{
		return this.rock.heatCapacity;
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		return this.ore.getProperty(MP.property_ore).onToolClick(player, tool, stack, this, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean addLandingEffects(IBlockState state, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		Server.addBlockLandingEffects(this.world, this.pos, this.rock.getProperty(MP.property_rock).block.getDefaultState().withProperty(BlockRock.TYPE, this.rockType), entity, numberOfParticles);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		Client.addBlockHitEffect(this.world, this.random, this.rock.getProperty(MP.property_rock).block.getDefaultState().withProperty(BlockRock.TYPE, this.rockType), target.sideHit, this.pos, manager);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		Client.addBlockDestroyEffects(this.world, this.pos, this.rock.getProperty(MP.property_rock).block.getDefaultState().withProperty(BlockRock.TYPE, this.rockType), manager);
		return true;
	}
}
