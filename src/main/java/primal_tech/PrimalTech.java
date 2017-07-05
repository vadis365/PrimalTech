package primal_tech;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
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
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.blocks.BlockCharcoal;
import primal_tech.blocks.BlockClayKiln;
import primal_tech.blocks.BlockFibreTorch;
import primal_tech.blocks.BlockFibreTorchLit;
import primal_tech.blocks.BlockFlint;
import primal_tech.blocks.BlockStickBundle;
import primal_tech.blocks.BlockStoneGrill;
import primal_tech.blocks.BlockWaterSaw;
import primal_tech.blocks.BlockWoodenHopper;
import primal_tech.blocks.BlockWorkStump;
import primal_tech.blocks.BlockWorkStumpUpgrade;
import primal_tech.configs.ConfigHandler;
import primal_tech.events.TorchLightEvent;
import primal_tech.items.ItemBoneAxe;
import primal_tech.items.ItemBonePickaxe;
import primal_tech.items.ItemBoneShears;
import primal_tech.items.ItemClub;
import primal_tech.items.ItemFireSticks;
import primal_tech.items.ItemFluidBladder;
import primal_tech.items.ItemMaterials;
import primal_tech.network.FireSticksMessage;
import primal_tech.network.FireSticksPacketHandler;
import primal_tech.proxy.CommonProxy;
import primal_tech.recipes.ModRecipes;

@Mod(modid = "primal_tech", name = "primal_tech", version = "0.1.24", guiFactory = "primal_tech.configs.ConfigGuiFactory", dependencies = "after:*")

public class PrimalTech {

	@Instance("primal_tech")
	public static PrimalTech INSTANCE;

	@SidedProxy(clientSide = "primal_tech.proxy.ClientProxy", serverSide = "primal_tech.proxy.CommonProxy")
	public static CommonProxy PROXY;

	public static Item FIRE_STICKS, BONE_PICKAXE, BONE_AXE, BONE_SHOVEL, BONE_SWORD, BONE_SHEARS, BONE_KNIFE, FLUID_BLADDER,
			BONE_SHARD, FLINT_BLOCK_ITEM, CHARCOAL_BLOCK_ITEM, CLAY_KILN_ITEM, STICK_BUNDLE_ITEM, FIBRE_TORCH_ITEM, WOODEN_HOPPER_ITEM, CHARCOAL_HOPPER_ITEM,
			FIBRE_TORCH_ITEM_LIT, PLANT_FIBRES, TWINE, WORK_STUMP_ITEM, ROCK, WOOD_CLUB, BONE_CLUB, STONE_CLUB, STONE_GRILL_ITEM, WORK_STUMP_II_ITEM, WATER_SAW_ITEM, FLINT_SAW_BLADE;
	public static Block CLAY_KILN, FLINT_BLOCK, STICK_BUNDLE, FIBRE_TORCH, FIBRE_TORCH_LIT, CHARCOAL_BLOCK, WORK_STUMP, STONE_GRILL, WOODEN_HOPPER, WORK_STUMP_II, CHARCOAL_HOPPER, WATER_SAW;
	public static ToolMaterial TOOL_BONE = EnumHelper.addToolMaterial("BONE_TOOLS", 1, 100, 5.0F, 0.0F, 15);
	public static ToolMaterial TOOL_BONE_KNIFE = EnumHelper.addToolMaterial("BONE_KNIFE", 0, 10, 2.0F, 0.0F, 0);
	public static SoundEvent BREAKING_STUFF;
	public static SimpleNetworkWrapper NETWORK_WRAPPER;

	public static CreativeTabs TAB = new CreativeTabs("primal_tech") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BONE_KNIFE);
		}
	};

	static { 
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.INSTANCE.loadConfig(event);
		//Items
		FIRE_STICKS = new ItemFireSticks();
		GameRegistry.register(FIRE_STICKS.setRegistryName("primal_tech", "fire_sticks").setUnlocalizedName("primal_tech.fire_sticks"));

		BONE_PICKAXE = new ItemBonePickaxe(TOOL_BONE);
		GameRegistry.register(BONE_PICKAXE.setRegistryName("primal_tech", "bone_pickaxe").setUnlocalizedName("primal_tech.bone_pickaxe"));

		BONE_AXE = new ItemBoneAxe(TOOL_BONE);
		GameRegistry.register(BONE_AXE.setRegistryName("primal_tech", "bone_axe").setUnlocalizedName("primal_tech.bone_axe"));

		BONE_SHOVEL = new ItemSpade(TOOL_BONE).setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(BONE_SHOVEL.setRegistryName("primal_tech", "bone_shovel").setUnlocalizedName("primal_tech.bone_shovel"));

		BONE_SWORD = new ItemSword(TOOL_BONE) {
			@Override
			public boolean getIsRepairable(ItemStack stack, ItemStack material) {
				return material.getItem() == Items.BONE;
			}
		};
		BONE_SWORD.setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(BONE_SWORD.setRegistryName("primal_tech", "bone_sword").setUnlocalizedName("primal_tech.bone_sword"));

		BONE_KNIFE = new ItemSword(TOOL_BONE_KNIFE) {
			@Override
		    public boolean isRepairable(){
		        return false;
		    }		

			@Override
			public boolean hasContainerItem() {
				 return true;
			}

			@Override
			public ItemStack getContainerItem(ItemStack itemStack) {
			ItemStack returnItem = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()+1);
			if (itemStack.isItemEnchanted()) {
				 NBTTagCompound nbtcompound = itemStack.getTagCompound();
				 returnItem.setTagCompound(nbtcompound);
			}
			return returnItem;
			}
		};
		BONE_KNIFE.setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(BONE_KNIFE.setRegistryName("primal_tech", "bone_knife").setUnlocalizedName("primal_tech.bone_knife"));

		BONE_SHARD = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(BONE_SHARD.setRegistryName("primal_tech", "bone_shard").setUnlocalizedName("primal_tech.bone_shard"));

		FLINT_SAW_BLADE = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(FLINT_SAW_BLADE.setRegistryName("primal_tech", "flint_edged_disc").setUnlocalizedName("primal_tech.flint_edged_disc"));

		PLANT_FIBRES = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(PLANT_FIBRES.setRegistryName("primal_tech", "plant_fibres").setUnlocalizedName("primal_tech.plant_fibres"));

		TWINE = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(TWINE.setRegistryName("primal_tech", "twine").setUnlocalizedName("primal_tech.twine"));

		BONE_SHEARS = new ItemBoneShears();
		GameRegistry.register(BONE_SHEARS.setRegistryName("primal_tech", "bone_shears").setUnlocalizedName("primal_tech.bone_shears"));

		ROCK = new Item().setMaxDamage(ConfigHandler.ROCK_DAMAGE).setMaxStackSize(1);
		GameRegistry.register(ROCK.setRegistryName("primal_tech", "rock").setUnlocalizedName("primal_tech.rock"));
		ROCK.setCreativeTab(PrimalTech.TAB);

		WOOD_CLUB = new ItemClub(ToolMaterial.WOOD).setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(WOOD_CLUB.setRegistryName("primal_tech", "wood_club").setUnlocalizedName("primal_tech.wood_club"));

		STONE_CLUB = new ItemClub(ToolMaterial.STONE).setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(STONE_CLUB.setRegistryName("primal_tech", "stone_club").setUnlocalizedName("primal_tech.stone_club"));

		BONE_CLUB = new ItemClub(TOOL_BONE).setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(BONE_CLUB.setRegistryName("primal_tech", "bone_club").setUnlocalizedName("primal_tech.bone_club"));

		FLUID_BLADDER = new ItemFluidBladder(Fluid.BUCKET_VOLUME).setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(FLUID_BLADDER.setRegistryName("primal_tech", "fluid_bladder").setUnlocalizedName("primal_tech.fluid_bladder"));

		//Blocks and Item Blocks
		CLAY_KILN = new BlockClayKiln();
		CLAY_KILN_ITEM = new ItemBlock(CLAY_KILN);
		GameRegistry.register(CLAY_KILN.setRegistryName("primal_tech", "clay_kiln").setUnlocalizedName("primal_tech.clay_kiln"));
		GameRegistry.register(CLAY_KILN_ITEM.setRegistryName(CLAY_KILN.getRegistryName()).setUnlocalizedName("primal_tech.clay_kiln"));

		FLINT_BLOCK = new BlockFlint();
		FLINT_BLOCK_ITEM = new ItemBlock(FLINT_BLOCK);
		GameRegistry.register(FLINT_BLOCK.setRegistryName("primal_tech", "flint_block").setUnlocalizedName("primal_tech.flint_block"));
		GameRegistry.register(FLINT_BLOCK_ITEM.setRegistryName(FLINT_BLOCK.getRegistryName()).setUnlocalizedName("primal_tech.flint_block"));

		STICK_BUNDLE = new BlockStickBundle();
		STICK_BUNDLE_ITEM = new ItemBlock(STICK_BUNDLE);
		GameRegistry.register(STICK_BUNDLE.setRegistryName("primal_tech", "stick_bundle").setUnlocalizedName("primal_tech.stick_bundle"));
		GameRegistry.register(STICK_BUNDLE_ITEM.setRegistryName(STICK_BUNDLE.getRegistryName()).setUnlocalizedName("primal_tech.stick_bundle"));

		FIBRE_TORCH = new BlockFibreTorch();
		FIBRE_TORCH_ITEM = new ItemBlock(FIBRE_TORCH );
		GameRegistry.register(FIBRE_TORCH.setRegistryName("primal_tech", "fibre_torch").setUnlocalizedName("primal_tech.fibre_torch"));
		GameRegistry.register(FIBRE_TORCH_ITEM.setRegistryName(FIBRE_TORCH.getRegistryName()).setUnlocalizedName("primal_tech.fibre_torch"));

		FIBRE_TORCH_LIT = new BlockFibreTorchLit();
		FIBRE_TORCH_ITEM_LIT = new ItemBlock(FIBRE_TORCH_LIT ){
			@Override
		    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
				target.setFire(5);
		        return true;
		    }
		};
		FIBRE_TORCH_ITEM_LIT.setCreativeTab(PrimalTech.TAB);
		GameRegistry.register(FIBRE_TORCH_LIT.setRegistryName("primal_tech", "fibre_torch_lit").setUnlocalizedName("primal_tech.fibre_torch_lit"));
		GameRegistry.register(FIBRE_TORCH_ITEM_LIT.setRegistryName(FIBRE_TORCH_LIT.getRegistryName()).setUnlocalizedName("primal_tech.fibre_torch_lit"));

		CHARCOAL_BLOCK = new BlockCharcoal();
		CHARCOAL_BLOCK_ITEM = new ItemBlock(CHARCOAL_BLOCK);
		GameRegistry.register(CHARCOAL_BLOCK.setRegistryName("primal_tech", "charcoal_block").setUnlocalizedName("primal_tech.charcoal_block"));
		GameRegistry.register(CHARCOAL_BLOCK_ITEM.setRegistryName(CHARCOAL_BLOCK.getRegistryName()).setUnlocalizedName("primal_tech.charcoal_block"));

		WORK_STUMP = new BlockWorkStump();
		WORK_STUMP_ITEM = new ItemBlock(WORK_STUMP);
		GameRegistry.register(WORK_STUMP.setRegistryName("primal_tech", "work_stump").setUnlocalizedName("primal_tech.work_stump"));
		GameRegistry.register(WORK_STUMP_ITEM.setRegistryName(WORK_STUMP.getRegistryName()).setUnlocalizedName("primal_tech.work_stump"));
		
		STONE_GRILL = new BlockStoneGrill();
		STONE_GRILL_ITEM = new ItemBlock(STONE_GRILL);
		GameRegistry.register(STONE_GRILL.setRegistryName("primal_tech", "stone_grill").setUnlocalizedName("primal_tech.stone_grill"));
		GameRegistry.register(STONE_GRILL_ITEM.setRegistryName(STONE_GRILL.getRegistryName()).setUnlocalizedName("primal_tech.stone_grill"));

		WOODEN_HOPPER = new BlockWoodenHopper();
		WOODEN_HOPPER_ITEM = new ItemBlock(WOODEN_HOPPER);
		GameRegistry.register(WOODEN_HOPPER.setRegistryName("primal_tech", "wooden_hopper").setUnlocalizedName("primal_tech.wooden_hopper"));
		GameRegistry.register(WOODEN_HOPPER_ITEM.setRegistryName(WOODEN_HOPPER.getRegistryName()).setUnlocalizedName("primal_tech.wooden_hopper"));

		CHARCOAL_HOPPER = new BlockWoodenHopper() {
			@SideOnly(Side.CLIENT)
			public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
				if (rand.nextInt(24) == 0)
					world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);

					for (int i = 0; i < 3; ++i) {
						double d0 = (double) pos.getX() + rand.nextDouble();
						double d1 = (double) pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
						double d2 = (double) pos.getZ() + rand.nextDouble();
						world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
					}
			}
		};
		CHARCOAL_HOPPER_ITEM = new ItemBlock(CHARCOAL_HOPPER);
		GameRegistry.register(CHARCOAL_HOPPER.setRegistryName("primal_tech", "charcoal_hopper").setUnlocalizedName("primal_tech.charcoal_hopper"));
		GameRegistry.register(CHARCOAL_HOPPER_ITEM.setRegistryName(CHARCOAL_HOPPER.getRegistryName()).setUnlocalizedName("primal_tech.charcoal_hopper"));

		WORK_STUMP_II = new BlockWorkStumpUpgrade();
		WORK_STUMP_II_ITEM = new ItemBlock(WORK_STUMP_II);
		GameRegistry.register(WORK_STUMP_II.setRegistryName("primal_tech", "work_stump_upgraded").setUnlocalizedName("primal_tech.work_stump_upgraded"));
		GameRegistry.register(WORK_STUMP_II_ITEM.setRegistryName(WORK_STUMP_II.getRegistryName()).setUnlocalizedName("primal_tech.work_stump_upgraded"));

		WATER_SAW = new BlockWaterSaw();
		WATER_SAW_ITEM = new ItemBlock(WATER_SAW);
		GameRegistry.register(WATER_SAW.setRegistryName("primal_tech", "water_saw").setUnlocalizedName("primal_tech.water_saw"));
		GameRegistry.register(WATER_SAW_ITEM.setRegistryName(WATER_SAW.getRegistryName()).setUnlocalizedName("primal_tech.water_saw"));

		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if (fuel.getItem() == Item.getItemFromBlock(CHARCOAL_BLOCK))
					return 6400;
				return 0;
			}
		});

		ModRecipes.addRecipes();
		ModRecipes.addKilnRecipes();
		ModRecipes.addWaterSawRecipes();

		PROXY.registerTileEntities();
		PROXY.registerRenderers();

		NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("primal_tech");
		NETWORK_WRAPPER.registerMessage(FireSticksPacketHandler.class, FireSticksMessage.class, 0, Side.SERVER);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new TorchLightEvent());
		MinecraftForge.EVENT_BUS.register(ConfigHandler.INSTANCE);
		BREAKING_STUFF = new SoundEvent(new ResourceLocation("primal_tech", "breaking_stuff")).setRegistryName("primal_tech", "breaking_stuff");
		GameRegistry.register(BREAKING_STUFF);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit();
	}
}