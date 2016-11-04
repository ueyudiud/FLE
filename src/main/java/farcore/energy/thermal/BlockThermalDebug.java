package farcore.energy.thermal;

import java.util.List;

import farcore.FarCore;
import farcore.FarCoreRegistry;
import farcore.lib.block.BlockSingleTE;
import farcore.lib.util.CreativeTabBase;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockThermalDebug extends BlockSingleTE
{
	private static final int[] OFF_TEMPERATURE = {-100, 0, 10, 50, 150, 500, 1500, 5000};
	private static final float[] HEAT_CAPACITY = {1E-2F, 10F, 200F, 5000F, 1E5F, 2E6F, 5E7F, 1E9F};
	private static final float[] THERMAL_CONDUCTIVITY = {1E-6F, 5E-6F, 2E-5F, 1E-4F, 5E-4F, 2E-3F, 1E-2F};
	
	public BlockThermalDebug()
	{
		super(FarCore.ID, "debug.thermal", Material.IRON);
		setCreativeTab(new CreativeTabBase("debug.thermal", "Thermal[Debug]")
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return new ItemStack(Blocks.FURNACE);
			}
		});
		FarCoreRegistry.registerTileEntity("farcore.debug.thermal", TEThermalDebuger.class);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(float conductivity : THERMAL_CONDUCTIVITY)
		{
			ItemStack stack = new ItemStack(itemIn);
			NBTTagCompound nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
			nbt.setFloat("conductivity", conductivity);
			nbt.setByte("mode", (byte) 1);
			for(int temperature : OFF_TEMPERATURE)
			{
				nbt.setInteger("temperature", temperature);
				list.add(stack.copy());
			}
			for(float capacity : HEAT_CAPACITY)
			{
				nbt.setFloat("capacity", capacity);
				nbt.setByte("mode", (byte) 0);
				list.add(stack.copy());
				nbt.setByte("mode", (byte) 2);
				list.add(stack.copy());
			}
		}
	}

	@Override
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip,
			boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		NBTTagCompound nbt = U.ItemStacks.setupNBT(stack, false);
		float tc = nbt.getFloat("conductivity");
		tooltip.addLocal("Thermal Conductivity: " + tc + "W/K");
		switch (nbt.getByte("mode"))
		{
		case 2 :
			tooltip.addLocal("Insulate Heat!");
		case 0 :
			float hc = nbt.getFloat("capacity");
			tooltip.addLocal("Heat Capacity: " + hc + "J/K");
			break;
		case 1 :
			int temperature = nbt.getInteger("temperature");
			tooltip.addLocal("Temperature Offset: " + temperature + "K");
			break;
		default:
			break;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEThermalDebuger();
	}
}