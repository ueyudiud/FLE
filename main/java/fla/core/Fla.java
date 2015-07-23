package fla.core;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.FlaAPI;
import fla.api.energy.heat.IHeatManager;
import fla.api.util.FlaValue;
import fla.api.util.IColorMapManager;
import fla.api.world.IWorldManager;
import fla.core.gui.ContainerClayModel;
import fla.core.gui.ContainerDryingTable;
import fla.core.gui.ContainerPolishTable;
import fla.core.gui.ContainerTerrine;
import fla.core.gui.ContainerWashing;
import fla.core.gui.GuiClayModel;
import fla.core.gui.GuiDryingTable;
import fla.core.gui.GuiPolishTable;
import fla.core.gui.GuiTerrine;
import fla.core.gui.GuiWashing;
import fla.core.init.C;
import fla.core.init.FlaRecipe;
import fla.core.init.Fuels;
import fla.core.init.IB;
import fla.core.network.NetWorkManager;
import fla.core.proxy.CommonProxy;
import fla.core.recipe.FlaRecipeRegistry;
import fla.core.render.RenderAsh;
import fla.core.render.RenderCrop;
import fla.core.render.RenderDryingTable;
import fla.core.render.RenderHandler;
import fla.core.render.RenderOilLamp;
import fla.core.render.RenderOre;
import fla.core.render.RenderUnsmeltedArgil;
import fla.core.render.TESRDryingTable;
import fla.core.tech.PlayerTechLearningHandler;
import fla.core.tech.TechManager;
import fla.core.tileentity.TileEntityArgilUnsmelted;
import fla.core.tileentity.TileEntityCrops;
import fla.core.tileentity.TileEntityDryingTable;
import fla.core.tileentity.TileEntityFirewood;
import fla.core.tileentity.TileEntityOilLamp;
import fla.core.tileentity.TileEntityPolishTable;
import fla.core.tileentity.argil.TileEntityTerrine;
import fla.core.tool.AxeManager;
import fla.core.tool.ShovelManager;
import fla.core.tool.ToolManager;
import fla.core.tool.WasherManager;
import fla.core.util.ColorMapManager;
import fla.core.util.Keyboard;
import fla.core.util.SideGateway;
import fla.core.world.FWM;
import fla.core.world.FlaWorldGen;
import fla.core.world.FlaWorldHandler;
import fla.core.world.HeatManager;

@Mod(modid = Fla.MODID, name = Fla.NAME, version = Fla.VERSION, dependencies = "after:NotEnoughItems; after:gregtech; after:IC2")
public class Fla implements IGuiHandler, IFuelHandler, fla.api.Mod
{	
    public static final String MODID = "fla";
    public static final String NAME = "Far Land Era";
    public static final String VERSION = "2.01h";

	public static boolean IC2Loading = false;
    public static boolean RCLoading = false;
	public static boolean GTLoading = false;
	public static boolean NEILoading = false;
    
    @Instance(MODID)
    public static Fla fla;
    
    public IWorldManager wm;
    public IHeatManager hm;
    @SideOnly(Side.CLIENT)
    public IColorMapManager cmm;
    public SideGateway<Platform> p;
    public SideGateway<NetWorkManager> nwm;
    public SideGateway<Keyboard> km;
    
    @SidedProxy(modId = MODID, clientSide="fla.core.proxy.ClientProxy", serverSide="fla.core.proxy.CommonProxy")
    public static CommonProxy proxy = new CommonProxy();
    
    public Fla()
    {
    	FlaAPI.mod = (fla = this);
    	FlaAPI.registry = new FlaRecipeRegistry();
    	FlaAPI.techManager = new TechManager();
    	wm = new FWM();
    	hm = new HeatManager();
    	cmm = new ColorMapManager();
    	nwm = new SideGateway("fla.core.network.NetWorkManager", "fla.core.network.NetWorkClient");
    	p = new SideGateway("fla.core.Platform", "fla.core.PlatformClient");
    	km = new SideGateway("fla.core.util.Keyboard", "fla.core.util.KeyboardClient");
    }
    
    @EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
        System.out.println("Far Land Age is starting pre loading.");
        IB.reloadIB();
        FlaRecipe.reloadRecipe();
        C.init();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	System.out.println("Far Land Age is starting loading.");
    	IB.init();
    	Fuels.init();
    	GameRegistry.registerWorldGenerator(new FlaWorldGen(), 1);
    	((TechManager) FlaAPI.techManager).init();
    	FlaRecipe.init();
    	C.postInit();
    	FWM.init();
		loadClient();
    }
    
    @EventHandler
    public void postLoad(FMLPostInitializationEvent event)
    {
    	System.out.println("Far Land Age is starting post loading.");
    	NetworkRegistry.INSTANCE.registerGuiHandler(MODID, this);
		MinecraftForge.EVENT_BUS.register(new AxeManager());
		MinecraftForge.EVENT_BUS.register(new ShovelManager());
		MinecraftForge.EVENT_BUS.register(new ToolManager());
		MinecraftForge.EVENT_BUS.register(new WasherManager());
		MinecraftForge.ORE_GEN_BUS.register(new FlaWorldHandler());
		MinecraftForge.EVENT_BUS.register(new FlaPlayerHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerTechLearningHandler());
		MinecraftForge.EVENT_BUS.register(new FlaRecipeHandler());
		MinecraftForge.EVENT_BUS.register(new FlaTickHandler());
		FMLCommonHandler.instance().bus().register(new AxeManager());
		FMLCommonHandler.instance().bus().register(new ShovelManager());
		FMLCommonHandler.instance().bus().register(new ToolManager());
		FMLCommonHandler.instance().bus().register(new WasherManager());
		FMLCommonHandler.instance().bus().register(new FlaWorldHandler());
		FMLCommonHandler.instance().bus().register(new FlaPlayerHandler());
		FMLCommonHandler.instance().bus().register(new FlaRecipeHandler());
		FMLCommonHandler.instance().bus().register(new FlaTickHandler());
		GameRegistry.registerTileEntity(TileEntityPolishTable.class, "polishTable");
		GameRegistry.registerTileEntity(TileEntityOilLamp.class, "oilLamp");
		GameRegistry.registerTileEntity(TileEntityCrops.class, "crop");
		GameRegistry.registerTileEntity(TileEntityDryingTable.class, "dryingTable");
		GameRegistry.registerTileEntity(TileEntityFirewood.class, "firewood");
		GameRegistry.registerTileEntity(TileEntityArgilUnsmelted.class, "argil_unsmelted");
		GameRegistry.registerTileEntity(TileEntityTerrine.class, "terrine");
    }

	@SideOnly(Side.CLIENT)
    public void loadClient()
    {
        FlaValue.ALL_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryingTable.class, new TESRDryingTable());
        RenderHandler.register(FlaBlocks.crops, OreDictionary.WILDCARD_VALUE, RenderCrop.class);
		RenderHandler.register(FlaBlocks.ore1, OreDictionary.WILDCARD_VALUE, RenderOre.class);
		RenderHandler.register(FlaBlocks.oilLamp, OreDictionary.WILDCARD_VALUE, RenderOilLamp.class);
        RenderHandler.register(FlaBlocks.dryingTable, OreDictionary.WILDCARD_VALUE, RenderDryingTable.class);
        RenderHandler.register(FlaBlocks.plantAsh, OreDictionary.WILDCARD_VALUE, RenderAsh.class);
        RenderHandler.register(FlaBlocks.argil_unsmelted, OreDictionary.WILDCARD_VALUE, RenderUnsmeltedArgil.class);
        RenderHandler.register(FlaBlocks.argil_smelted, OreDictionary.WILDCARD_VALUE, RenderUnsmeltedArgil.class);
    	RenderingRegistry.registerBlockHandler(new RenderHandler());
    	IReloadableResourceManager irrm = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
		irrm.registerReloadListener(cmm);
    }
    
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		switch(ID)
		{
		case 1 : return new ContainerPolishTable(player.inventory, (TileEntityPolishTable) world.getTileEntity(x, y, z));
		case 2 : return new ContainerDryingTable(player.inventory, (TileEntityDryingTable) world.getTileEntity(x, y, z));
		case 3 :
		{
			switch(world.getBlockMetadata(x, y, z))
			{
			case 0 : return new ContainerTerrine(player.inventory, (TileEntityTerrine) world.getTileEntity(x, y, z));
			default : return null;
			}
		}
		case 101 : return new ContainerWashing(player.inventory);
		case 102 : return new ContainerClayModel(world, x, y, z, player.inventory);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		switch(ID)
		{
		case 1 : return new GuiPolishTable(player, (TileEntityPolishTable) world.getTileEntity(x, y, z));
		case 2 : return new GuiDryingTable(player, (TileEntityDryingTable) world.getTileEntity(x, y, z));
		case 3 :
		{
			switch(world.getBlockMetadata(x, y, z))
			{
			case 0 : return new GuiTerrine(player, (TileEntityTerrine) world.getTileEntity(x, y, z));
			default : return null;
			}
		}
		case 101 : return new GuiWashing(player);
		case 102 : return new GuiClayModel(world, x, y, z, player);
		}
		return null;
	}

	public int getBurnTime(ItemStack fuel) 
	{
		return 0;
	}

	public final String getModName() 
	{
		return this.NAME;
	}

	@Override
	public final IWorldManager getWorldManager() 
	{
		return wm;
	}

	@Override
	public IColorMapManager getColorMapManager() 
	{
		return cmm;
	}
	
	@NetworkCheckHandler
	public static boolean checkHandler(Map<String, String> version, Side side)
	{
		return true;
	}
}