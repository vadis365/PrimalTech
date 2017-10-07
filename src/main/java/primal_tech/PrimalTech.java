package primal_tech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import primal_tech.configs.ConfigHandler;
import primal_tech.events.TorchLightEvent;
import primal_tech.network.FireSticksMessage;
import primal_tech.network.FireSticksPacketHandler;
import primal_tech.proxy.CommonProxy;
import primal_tech.recipes.ModRecipes;

@Mod(modid = "primal_tech", name = "primal_tech", version = "0.2.6", guiFactory = "primal_tech.configs.ConfigGuiFactory", dependencies = "after:*")

public class PrimalTech {

	@Instance("primal_tech")
	public static PrimalTech INSTANCE;

	@SidedProxy(clientSide = "primal_tech.proxy.ClientProxy", serverSide = "primal_tech.proxy.CommonProxy")
	public static CommonProxy PROXY;
	public static ToolMaterial TOOL_BONE = EnumHelper.addToolMaterial("BONE_TOOLS", 1, 100, 5.0F, 0.0F, 15);
	public static ToolMaterial TOOL_BONE_KNIFE = EnumHelper.addToolMaterial("BONE_KNIFE", 0, 10, 2.0F, 0.0F, 0);
	public static SimpleNetworkWrapper NETWORK_WRAPPER;

	public static CreativeTabs TAB = new CreativeTabs("primal_tech") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.BONE_KNIFE);
		}
	};

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.INSTANCE.loadConfig(event);
		ModBlocks.init();
		ModItems.init();
		ModSounds.init();
		ModRecipes.init();

		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if (fuel.getItem() == Item.getItemFromBlock(ModBlocks.CHARCOAL_BLOCK))
					return 6400;
				return 0;
			}
		});

		//ModRecipes.addRecipes();

		PROXY.registerTileEntities();
		PROXY.registerRenderers();

		NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("primal_tech");
		NETWORK_WRAPPER.registerMessage(FireSticksPacketHandler.class, FireSticksMessage.class, 0, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModRecipes.addKilnRecipes();
		ModRecipes.addWaterSawRecipes();
		ModRecipes.addStoneAnvilRecipes();
		ModRecipes.addWoodenBasinRecipes();
		MinecraftForge.EVENT_BUS.register(new TorchLightEvent());
		MinecraftForge.EVENT_BUS.register(ConfigHandler.INSTANCE);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit();
	}
}