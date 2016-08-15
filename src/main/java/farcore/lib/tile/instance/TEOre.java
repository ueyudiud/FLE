package farcore.lib.tile.instance;

import farcore.data.EnumOreAmount;
import farcore.data.M;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.TEStatic;
import farcore.lib.util.Direction;
import farcore.network.IPacket;
import farcore.util.U.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TEOre extends TEStatic implements IUpdatableTile
{
	private static final Mat STONE = Mat.register.get("stone");

	private Mat ore = M.VOID;
	public EnumOreAmount amount = EnumOreAmount.normal;
	public Mat rock = STONE;
	public RockType rockType = RockType.resource;
	public NBTTagCompound customDatas;
	
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
	public void onLoad()
	{
		if(isServer())
		{
			initServer();
		}
		else
		{
			if(Worlds.isAirNearby(worldObj, pos, true))
			{
				sendToServer(new PacketTEAsk(worldObj, pos));
			}
		}
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
		ore = Mat.register.get(compound.getString("ore"), M.VOID);
		amount = EnumOreAmount.values()[compound.getByte("amount")];
		rock = Mat.register.get(compound.getString("rock"), STONE);
		rockType = RockType.values()[compound.getByte("type")];
		if(compound.hasKey("custom"))
		{
			customDatas = compound.getCompoundTag("custom");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("ore", ore.name);
		compound.setByte("amount", (byte) amount.ordinal());
		compound.setString("rock", rock.name);
		compound.setByte("type", (byte) rockType.ordinal());
		if(customDatas != null)
		{
			compound.setTag("custom", customDatas);
		}
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		ore = Mat.register.get(nbt.getString("o"), M.VOID);
		amount = EnumOreAmount.values()[nbt.getByte("a")];
		rock = Mat.register.get(nbt.getString("r"), STONE);
		rockType = RockType.values()[nbt.getByte("t")];
		if(nbt.hasKey("c"))
		{
			customDatas = nbt.getCompoundTag("c");
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("o", ore.name);
		nbt.setByte("a", (byte) amount.ordinal());
		nbt.setString("r", rock.name);
		nbt.setByte("t", (byte) rockType.ordinal());
		if(customDatas != null)
		{
			nbt.setTag("c", customDatas);
		}
	}

	@Override
	public boolean onBlockClicked(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		return ore.oreProperty.onBlockClicked(this, player, side);
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		return ore.oreProperty.onBlockActivated(this, player, hand, stack, side, hitX, hitY, hitZ);
	}

	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if(Worlds.isNotOpaqueNearby(worldObj, pos))
		{
			syncToNearby();
		}
	}

	public NBTTagCompound getCustomData()
	{
		return customDatas;
	}

	public float getThermalConduct()
	{
		return ore.thermalConduct * 0.2F + rock.thermalConduct * 0.8F;
	}
	
	public int getHarvestLevel()
	{
		switch (rockType)
		{
		case cobble_art :
			return 1;
		case cobble :
			return Math.max(ore.blockHarvestLevel, rock.blockHarvestLevel) / 2;
		default:
			return Math.max(ore.blockHarvestLevel, rock.blockHarvestLevel);
		}
	}
	
	public float getHardness()
	{
		return ore.blockHardness * .8F + rock.blockHardness * .2F;
	}
	
	public float getExplosionResistance()
	{
		return Math.max(ore.blockExplosionResistance, rock.blockExplosionResistance);
	}
}