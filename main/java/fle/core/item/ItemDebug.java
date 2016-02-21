package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.registry.GameData;
import flapi.energy.IRotationTileEntity;
import flapi.energy.IThermalTileEntity;
import flapi.enums.EnumDamageResource;
import flapi.enums.EnumWorldNBT;
import flapi.item.ItemFle;
import flapi.util.FleLog;
import flapi.util.FleValue;
import flapi.util.IDebugable;
import flapi.world.BlockPos;
import flapi.world.BlockPos.ChunkPos;
import fle.FLE;

public class ItemDebug extends ItemFle
{
	public ItemDebug(String aUnlocalized)
	{
		super(aUnlocalized);
	}
	
	@Override
	public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer,
			World aWorld, int x, int y, int z,
			int aSide, float xPos, float yPos,
			float zPos)
	{
    	if(!aWorld.isRemote)
    	{
        	BlockPos pos = new BlockPos(aWorld, x, y, z);
        	ChunkPos pos1 = pos.getChunkPos();
        	try
        	{
        		aPlayer.addChatMessage(new ChatComponentText("This block is named " + aWorld.getBlock(x, y, z).getUnlocalizedName() + "."));
        		aPlayer.addChatMessage(new ChatComponentText("Block name is " + GameData.getBlockRegistry().getNameForObject(pos.getBlock()) + ", by id " + Block.getIdFromBlock(pos.getBlock()) + "."));
        		if(pos.getBlockTile() != null)
        			aPlayer.addChatMessage(new ChatComponentText("Block tile is " + pos.getBlockTile().getClass() + "."));
            	aPlayer.addChatMessage(new ChatComponentText("Metadata: " + pos.getBlockMeta() + "."));
            	aPlayer.addChatMessage(new ChatComponentText("Harvest Level: " + pos.getBlock().getHarvestLevel(pos.getBlockMeta()) + "."));
        		aPlayer.addChatMessage(new ChatComponentText("Hardness: " + pos.getBlock().getBlockHardness(aWorld, x, y, z) + "."));
        		
        		//{
        			//for(int y1 = y; y1 > 1; --y1)
        				//if(aWorld.getBlock(x, y1, z) instanceof BlockOre)
        				//{
        					//aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW.toString() + "Here are ore under the surface, position " + y1));
        					//break;
        				//}
        		//}
        		
        		String str1 = "";
            	for(EnumWorldNBT nbt : EnumWorldNBT.values())
        			str1 += FLE.fle.getWorldManager().getData(pos, nbt) + " ";
            	aPlayer.addChatMessage(new ChatComponentText("FWM: " + str1 + "."));
            	if(pos.getBlockTile() instanceof IFluidHandler)
            	{
            		IFluidHandler handler = (IFluidHandler) pos.getBlockTile();
            		FluidTankInfo[] infos = handler.getTankInfo(ForgeDirection.VALID_DIRECTIONS[aSide]);
            		if(infos != null)
            		for(FluidTankInfo info : infos)
            		{
            			aPlayer.addChatMessage(new ChatComponentText(String.format("Capacity: %s.", FleValue.format_L.format_c(info.capacity))));
            			if(info.fluid != null)
            				aPlayer.addChatMessage(new ChatComponentText(String.format("Fluid Amount: %sx%s.", info.fluid.getLocalizedName(), FleValue.format_L.format_c(info.fluid.amount))));
            		}
            	}
        		aPlayer.addChatMessage(new ChatComponentText("FTN :"));
        		aPlayer.addChatMessage(new ChatComponentText(String.format("Enviourment Temp: %s.", FleValue.format_K.format_c(FLE.fle.getThermalNet().getEnvironmentTemperature(pos)))));
            	if(pos.getBlockTile() instanceof IThermalTileEntity)
            	{
            		IThermalTileEntity tile = (IThermalTileEntity) pos.getBlockTile();
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Temperature: %s.", FleValue.format_K.format_c(tile.getTemperature(ForgeDirection.VALID_DIRECTIONS[aSide])))));
//            		aPlayer.addChatMessage(new ChatComponentText(String.format("Heat Current: %s.", FleValue.format_MJ.format_c(tile.getThermalEnergyCurrect(ForgeDirection.VALID_DIRECTIONS[aSide])))));
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Emit Heat: %s.", FleValue.format_MJ.format_c(tile.getPreHeatEmit()))));
            	}
        		aPlayer.addChatMessage(new ChatComponentText("FRN :"));
        		aPlayer.addChatMessage(new ChatComponentText("Wind Speed : " + FLE.fle.getRotationNet().getWindSpeed(pos)));
            	if(pos.getBlockTile() instanceof IRotationTileEntity)
            	{
            		IRotationTileEntity tile = (IRotationTileEntity) pos.getBlockTile();
//            		aPlayer.addChatMessage(new ChatComponentText(String.format("Kinetic Energy Current: %s.", FleValue.format_MJ.format_c(tile.getEnergyCurrect()))));
            		aPlayer.addChatMessage(new ChatComponentText(String.format("Emit Heat: %s.", FleValue.format_MJ.format_c(tile.getPreEnergyEmit()))));
            	}
            	if(pos.getBlock() instanceof IDebugable)
        		{
        			List<String> tList = new ArrayList();
        			((IDebugable) pos.getBlock()).addInfomationToList(aWorld, x, y, z, tList);
        			for(String str : tList)
        			{
        				aPlayer.addChatMessage(new ChatComponentText(str));
        			}
        		}
        	}
        	catch(Throwable e)
        	{
        		FleLog.getLogger().catching(Level.WARN, e);
        		aPlayer.addChatMessage(new ChatComponentText("FLE: This block require a bug place check your log and report this bug."));
        	}
    	}
		return true;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
	}

	@Override
	public void damageItem(ItemStack stack, EntityLivingBase aUser,
			EnumDamageResource aReource, float aDamage)
	{
		
	}
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap map = super.getAttributeModifiers(stack);
        map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), 
        		new AttributeModifier(field_111210_e, "DEBUG", Double.MAX_VALUE, 0));
		return map;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target,
			EntityLivingBase user) 
	{
		if(!user.worldObj.isRemote)
		{
			target.setDead();
		}
		return true;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.epic;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player,
			List list, boolean flag)
	{
		list.add("Don't play this item~");
		super.addInformation(stack, player, list, flag);
	}
}