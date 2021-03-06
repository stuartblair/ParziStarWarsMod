package com.parzivail.pswm;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.util.DamageSource;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.io.IOUtils;

import com.parzivail.pswm.Resources.ConfigOptions;
import com.parzivail.pswm.achievement.StarWarsAchievements;
import com.parzivail.pswm.commands.CommandJediRobes;
import com.parzivail.pswm.exception.UserError;
import com.parzivail.pswm.handlers.ClientEventHandler;
import com.parzivail.pswm.handlers.CommonEventHandler;
import com.parzivail.pswm.handlers.GuiHandler;
import com.parzivail.pswm.items.crafting.ItemLightsaberCrystal;
import com.parzivail.pswm.items.weapons.ItemBlasterHeavy;
import com.parzivail.pswm.items.weapons.ItemBlasterPistol;
import com.parzivail.pswm.items.weapons.ItemBlasterRifle;
import com.parzivail.pswm.items.weapons.ItemEwokSpear;
import com.parzivail.pswm.items.weapons.ItemGaffiStick;
import com.parzivail.pswm.items.weapons.ItemGamorreanAx;
import com.parzivail.pswm.items.weapons.ItemLightsaber;
import com.parzivail.pswm.items.weapons.ItemLightsaberOff;
import com.parzivail.pswm.items.weapons.ItemSequelBlasterPistol;
import com.parzivail.pswm.items.weapons.ItemSequelBlasterRifle;
import com.parzivail.pswm.items.weapons.ItemSequelLightsaber;
import com.parzivail.pswm.items.weapons.ItemSequelLightsaberOff;
import com.parzivail.pswm.items.weapons.ItemWookieeBowcaster;
import com.parzivail.pswm.network.MessageAddEffectTo;
import com.parzivail.pswm.network.MessageCreateBlasterBolt;
import com.parzivail.pswm.network.MessageCreateDestructionBolt;
import com.parzivail.pswm.network.MessageDrainKnowledge;
import com.parzivail.pswm.network.MessageEntityAlterMotion;
import com.parzivail.pswm.network.MessageEntityGrab;
import com.parzivail.pswm.network.MessageEntityHurt;
import com.parzivail.pswm.network.MessageEntityReverse;
import com.parzivail.pswm.network.MessageHeal;
import com.parzivail.pswm.network.MessageHoloTableUpdate;
import com.parzivail.pswm.network.MessageHyperdrive;
import com.parzivail.pswm.network.MessageRobesBooleanNBT;
import com.parzivail.pswm.network.MessageRobesIntNBT;
import com.parzivail.pswm.network.MessageRobesPowerNBT;
import com.parzivail.pswm.network.MessageRobesStringNBT;
import com.parzivail.pswm.network.MessageSFoil;
import com.parzivail.pswm.network.MessageSetEntityTarget;
import com.parzivail.pswm.network.MessageTransmute;
import com.parzivail.pswm.network.PacketShipTargetLock;
import com.parzivail.pswm.network.PacketTogglePlayerLightsaber;
import com.parzivail.pswm.network.PacketTogglePlayerSequelLightsaber;
import com.parzivail.pswm.network.PacketUpdateRobes;
import com.parzivail.pswm.registry.BlockRegister;
import com.parzivail.pswm.registry.DamageSourceRegister;
import com.parzivail.pswm.registry.EntityRegister;
import com.parzivail.pswm.registry.ItemRegister;
import com.parzivail.pswm.registry.MaterialRegister;
import com.parzivail.pswm.registry.RecipeRegister;
import com.parzivail.pswm.registry.WorldRegister;
import com.parzivail.pswm.tabs.SequelStarWarsTab;
import com.parzivail.pswm.tabs.StarWarsTab;
import com.parzivail.util.ui.Lumberjack;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Resources.MODID, version = Resources.VERSION, name = "Parzi's Star Wars Mod", acceptedMinecraftVersions = "[1.7.10]")
public class StarWarsMod
{
	public static boolean hasShownNeedUpdate = false;
	public static boolean hasShownLeaderboardPart = false;

	public static Configuration config;

	public static int packetId = 0;

	public static Random rngGeneral = new Random();
	public static Random rngChromium = new Random();
	public static Random rngTitanium = new Random();

	@Mod.Instance(Resources.MODID)
	public static StarWarsMod instance;

	@SideOnly(Side.CLIENT)
	public static Minecraft mc;

	public static ClientEventHandler clientHandler;
	public static CommonEventHandler commonHandler;

	@SidedProxy(clientSide = "com.parzivail.pswm.StarWarsClientProxy", serverSide = "com.parzivail.pswm.StarWarsCommonProxy")
	public static StarWarsCommonProxy proxy;
	public static SimpleNetworkWrapper network;

	public static CreativeTabs StarWarsTab;
	public static CreativeTabs SequelStarWarsTab;

	public static ItemGaffiStick gaffiStick;
	public static ItemLightsaber lightsaber;
	public static ItemLightsaberOff lightsaberOff;
	public static ItemSequelLightsaber sequelLightsaber;
	public static ItemSequelLightsaberOff sequelLightsaberOff;
	public static ItemBlasterPistol blasterPistol;
	public static ItemBlasterRifle blasterRifle;
	public static ItemSequelBlasterRifle sequelBlasterRifle;
	public static ItemSequelBlasterPistol sequelBlasterPistol;
	public static ItemBlasterHeavy blasterHeavy;
	public static ItemEwokSpear ewokSpear;
	public static ItemWookieeBowcaster bowcaster;
	public static ItemGamorreanAx gamorreanAx;
	public static ItemLightsaberCrystal lightsaberCrystal;

	public static Item customTest;

	public static Item hiltMetelCompound;
	public static Item hiltMetelAlloy;
	public static Item plasmaEmitter;
	public static Item containmentField;
	public static Item blasterXWingBolt;
	public static Item blasterTIEBolt;
	public static Item blasterBolt;
	public static Item blasterRifleBolt;
	public static Item banthaHorn;
	public static Item droidCaller;
	public static Item droidHacker;
	public static Item imperialCredit;
	public static Item silverImperialCredit;
	public static Item goldImperialCredit;
	public static Item waterDroplet;

	public static Item binoculars;
	public static Item binocularsHoth;

	public static Item debugLootGen;
	public static Item debugLandspeederNPC;

	public static Item chromiumDust;
	public static Item titaniumDust;
	public static Item titaniumChromiumDust;
	public static Item titaniumChromiumIngot;

	public static Item hyperdriveMotivator;
	public static Item hyperdriveEngine;
	public static Item hyperdriveEarth;
	public static Item hyperdriveTatooine;
	public static Item hyperdriveHoth;
	public static Item hyperdriveKashyyyk;
	public static Item hyperdriveYavin4;
	public static Item hyperdriveEndor;
	public static Item hyperdriveIlum;
	public static Item hyperdriveDagobah;

	public static Item spawnSpeederBike;
	public static Item spawnJakkuSpeeder;
	public static Item spawnHothSpeederBike;
	public static Item spawnLandspeeder;
	public static Item spawnTie;
	public static Item spawnTieInterceptor;
	public static Item spawnAwing;
	public static Item spawnXwing;
	public static Item spawnDsTurret;
	public static Item spawnAstromech;
	public static Item spawnAstromechImperial;
	public static Item spawnAstromechImperial2;
	public static Item spawnAstromech2;
	public static Item spawnAstromechBb8;
	public static Item spawnProtocol;
	public static Item spawnProtocol2;
	public static Item spawnProbe;
	public static Item spawnMouse;
	public static Item spawnGonk;
	public static Item spawnSurgical;
	public static Item spawnTreadwell;

	public static Item jediRobes;

	public static Item recordTheme;
	public static Item recordThrone;
	public static Item recordBinary;
	public static Item recordImperial;
	public static Item recordCantina;

	public static Item endorHelmet;
	public static Item endorChest;
	public static Item endorLegs;
	public static Item endorBoots;

	public static Item rebelPilotHelmet;
	public static Item rebelPilotChest;
	public static Item rebelPilotLegs;
	public static Item rebelPilotBoots;

	public static Item stormtrooperHelmet;
	public static Item stormtrooperChest;
	public static Item stormtrooperLegs;
	public static Item stormtrooperBoots;

	public static Item stormtrooperNewHelmet;
	public static Item stormtrooperNewChest;
	public static Item stormtrooperNewLegs;
	public static Item stormtrooperNewBoots;

	public static Item stormtrooperSilverNewHelmet;
	public static Item stormtrooperSilverNewChest;
	public static Item stormtrooperSilverNewLegs;
	public static Item stormtrooperSilverNewBoots;

	public static Item snowtrooperHelmet;
	public static Item snowtrooperChest;
	public static Item snowtrooperLegs;
	public static Item snowtrooperBoots;

	public static Item scoutTrooperHelmet;
	public static Item scoutTrooperChest;
	public static Item scoutTrooperLegs;
	public static Item scoutTrooperBoots;

	public static Item sandtrooperHelmet;
	public static Item sandtrooperChest;
	public static Item sandtrooperLegs;
	public static Item sandtrooperBoots;

	public static Item bobaHelmet;
	public static Item bobaChest;
	public static Item bobaJetpack;
	public static Item bobaJetpackChest;
	public static Item bobaLegs;
	public static Item bobaBoots;

	public static Item tiePilotHelmet;
	public static Item tiePilotChest;
	public static Item tiePilotLegs;
	public static Item tiePilotBoots;

	public static Item fleetHelmet;
	public static Item fleetChest;
	public static Item fleetLegs;
	public static Item fleetBoots;

	public static Item atatPilotHelmet;
	public static Item atatPilotChest;
	public static Item atatPilotLegs;
	public static Item atatPilotBoots;

	public static Item hothHelmet;
	public static Item hothChest;
	public static Item hothLegs;
	public static Item hothBoots;

	public static Item leiasBuns;

	public static ItemFood banthaChop;
	public static ItemFood banthaChopCooked;
	public static ItemFood banthaMilk;
	public static ItemFood acidBeets;
	public static ItemFood banthaPlatter;
	public static ItemFood canron;
	public static ItemFood chasuka;
	public static ItemFood dewbackRibs;
	public static ItemFood gorrnar;

	public static BiomeGenBase biomeTatooine;
	public static BiomeGenBase biomeHoth;
	public static BiomeGenBase biomeKashyyyk;
	public static BiomeGenBase biomeYavin4;
	public static BiomeGenBase biomeEndor;
	public static BiomeGenBase biomeEndorPlains;
	public static BiomeGenBase biomeDagobah;
	public static BiomeGenBase biomeIlum;

	public static boolean isWorldRegistered = false;

	public static boolean isOverlayOnscreen = false;

	public static Block blockMV;
	public static Block blockFieldEmitter;
	public static Block blockDeathStarDoor;
	public static Block blockHoloTable;
	public static Block blockTable;
	public static Block blockTable2;
	public static Block blockChromiumOre;
	public static Block blockTitaniumOre;
	public static Block blockTitaniumChromiumBlock;
	public static Block blockDeathStarGlass;
	public static Block blockEndorBaseWall;
	public static Block blockEndorBaseWallStairs;
	public static Block blockMudStairs;
	public static Block blockDeathStarLight;
	public static Block blockDeathStarBlock;
	public static Block blockTatooineSand;
	public static Block blockTatooineSandstone;
	public static Block blockSpaceLamp;
	public static Block blockDagobahMud;
	public static Block blockHangingCauldron;
	public static Block blockHangingBucket;
	public static Block blockBasket;

	public static Item.ToolMaterial materialGaffi;
	public static Item.ToolMaterial materialEwok;
	public static Item.ToolMaterial materialGamorrean;
	public static Item.ToolMaterial materialPlasma;
	public static Item.ToolMaterial materialPlasmaOff;

	public static ArmorMaterial jediRobesMat;
	public static ArmorMaterial endorArmorMat;
	public static ArmorMaterial fleetArmorMat;
	public static ArmorMaterial rebelPilotArmorMat;
	public static ArmorMaterial stormtrooperArmorMat;
	public static ArmorMaterial stormtrooperNewArmorMat;
	public static ArmorMaterial snowtrooperArmorMat;
	public static ArmorMaterial scoutTrooperArmorMat;
	public static ArmorMaterial tiePilotArmorMat;
	public static ArmorMaterial atatPilotArmorMat;
	public static ArmorMaterial hothArmorMat;
	public static ArmorMaterial sandtrooperArmorMat;
	public static ArmorMaterial bobaArmorMat;
	public static ArmorMaterial leiaBunsArmorMat;

	public static DamageSource blasterDamageSource;

	private void checkCompat() throws UserError
	{
		boolean flag = false;
		ArrayList<String> m = new ArrayList<String>();
		for (String mod : Resources.checkCompatList)
			if (Loader.isModLoaded(mod))
			{
				flag = true;
				m.add(mod);
			}
		if (flag)
		{
			Lumberjack.warn("WARNING! It is known that Parzi's Star Wars Mod may be incompatible with the following mods:");
			Lumberjack.warn("-> " + String.join(", ", m));
			Lumberjack.warn("Please report any issues to our GitHub: https://github.com/Parzivail-Modding-Team/ParziStarWarsMod/issues");
		}
	}

	private void checkJavaVersion() throws UserError
	{
		String versionString = System.getProperty("java.version");
		int pos = versionString.indexOf('.');
		pos = versionString.indexOf('.', pos + 1);
		double version = Double.parseDouble(versionString.substring(0, pos));
		if (version < 1.8)
			throw new UserError("Parzi's Star Wars Mod requires Java 1.8 and above. You are currently using Java " + version);
	}

	private void checkModVersion()
	{
		InputStream in = null;
		try
		{
			in = new URL(Resources.remoteVersionLink).openStream();
			Resources.ONLINE_VERSION = IOUtils.toString(in).replace("\n", "");
			if (!Resources.VERSION.equalsIgnoreCase(Resources.ONLINE_VERSION))
				Lumberjack.log("New version of Parzi's Star Wars Mod available: " + Resources.ONLINE_VERSION + "!");
		}
		catch (Exception e)
		{
			Lumberjack.warn("Couldn't check version!");
		}
		finally
		{
			if (in != null)
				IOUtils.closeQuietly(in);
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Lumberjack.info("========== Begin Star Wars Mod init() ==========");

		Lumberjack.info("This is Parzi's Star Wars Mod v" + Resources.VERSION);

		this.checkModVersion();

		instance = this;

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		StarWarsMod.commonHandler = new CommonEventHandler();
		StarWarsMod.clientHandler = new ClientEventHandler();

		FMLCommonHandler.instance().bus().register(StarWarsMod.commonHandler);
		MinecraftForge.EVENT_BUS.register(StarWarsMod.clientHandler);

		proxy.doSidedThings();

		EntityRegister.registerAll();

		MaterialRegister.registerAll();

		if (ConfigOptions.enableTabOriginal)
			StarWarsTab = new StarWarsTab();
		else
			StarWarsTab = CreativeTabs.tabAllSearch;

		if (Resources.IS_SEQUEL_RELEASE)
		{
			Lumberjack.log("Sequel update! Suck it, JJ!");
			if (ConfigOptions.enableTabSequel)
				SequelStarWarsTab = new SequelStarWarsTab();
			else
				SequelStarWarsTab = CreativeTabs.tabAllSearch;
		}

		ItemRegister.registerAll();

		BlockRegister.registerAll();

		WorldRegister.registerAll();

		RecipeRegister.registerAll();

		StarWarsAchievements.registerAll();

		DamageSourceRegister.registerAll();

		proxy.registerRendering();

		Lumberjack.info("=========== End Star Wars Mod init() ===========");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws UserError
	{
		Lumberjack.info("========== Begin Star Wars Mod preInit() ==========");

		this.checkJavaVersion();

		this.checkCompat();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Resources.MODID + "." + "chan");

		this.registerMessage(MessageEntityGrab.class);
		this.registerMessage(MessageAddEffectTo.class);
		this.registerMessage(MessageHoloTableUpdate.class);
		this.registerMessage(MessageSetEntityTarget.class);
		this.registerMessage(MessageCreateDestructionBolt.class);
		this.registerMessage(MessageEntityAlterMotion.class);
		this.registerMessage(MessageHyperdrive.class);
		this.registerMessage(MessageEntityHurt.class);
		this.registerMessage(MessageTransmute.class);
		this.registerMessage(MessageEntityReverse.class);
		this.registerMessage(MessageRobesBooleanNBT.class);
		this.registerMessage(MessageRobesIntNBT.class);
		this.registerMessage(MessageRobesStringNBT.class);
		this.registerMessage(MessageSFoil.class);
		this.registerMessage(MessageRobesPowerNBT.class);
		this.registerMessage(MessageHeal.class);
		this.registerMessage(MessageDrainKnowledge.class);

		network.registerMessage(MessageCreateBlasterBolt.class, MessageCreateBlasterBolt.class, packetId++, Side.SERVER);
		network.registerMessage(PacketTogglePlayerLightsaber.Handler.class, PacketTogglePlayerLightsaber.class, packetId++, Side.SERVER);
		network.registerMessage(PacketTogglePlayerSequelLightsaber.Handler.class, PacketTogglePlayerSequelLightsaber.class, packetId++, Side.SERVER);
		network.registerMessage(PacketShipTargetLock.Handler.class, PacketShipTargetLock.class, packetId++, Side.SERVER);
		network.registerMessage(PacketUpdateRobes.Handler.class, PacketUpdateRobes.class, packetId++, Side.SERVER);

		Lumberjack.log("Network registered " + String.valueOf(packetId) + " packets!");

		config = new Configuration(event.getSuggestedConfigurationFile(), Resources.VERSION);
		config.load();

		ConfigOptions.enableTabOriginal = config.get("core", "enableTabOriginal", true).getBoolean();
		ConfigOptions.enableTabSequel = config.get("core", "enableTabSequel", true).getBoolean();
		ConfigOptions.enableBetaFeatures = config.get("core", "enableBetaFeatures", false).getBoolean();
		ConfigOptions.beshOverride = config.get("core", "aurebeshInsteadOfEnglish", false).getBoolean();
		ConfigOptions.enableGlobalLeaderboard = config.get("core", "participateInGlobalLeaderboard", true).getBoolean();

		ConfigOptions.dimTatooineId = config.get("dimensions", "tatooine", 2).getInt();
		ConfigOptions.dimHothId = config.get("dimensions", "hoth", 3).getInt();
		ConfigOptions.dimKashyyykId = config.get("dimensions", "kashyyyk", 4).getInt();
		ConfigOptions.dimYavin4Id = config.get("dimensions", "yavin", 5).getInt();
		ConfigOptions.dimEndorId = config.get("dimensions", "endor", 6).getInt();
		ConfigOptions.dimIlumId = config.get("dimensions", "ilum", 7).getInt();
		ConfigOptions.dimDagobahId = config.get("dimensions", "dagobah", 8).getInt();

		ConfigOptions.biomeTatooineId = config.get("biomes", "tatooine", 196).getInt();
		ConfigOptions.biomeHothId = config.get("biomes", "hoth", 197).getInt();
		ConfigOptions.biomeKashyyykId = config.get("biomes", "kashyyyk", 198).getInt();
		ConfigOptions.biomeYavin4Id = config.get("biomes", "yavin", 199).getInt();
		ConfigOptions.biomeEndorId = config.get("biomes", "endor", 200).getInt();
		ConfigOptions.biomeIlumId = config.get("biomes", "ilum", 201).getInt();
		ConfigOptions.biomeDagobahId = config.get("biomes", "dagobah", 195).getInt();

		ConfigOptions.enableCreditsOverlay = config.get("gui", "enableGuiOverlay", true).getBoolean();

		ConfigOptions.lightsaberDamage = config.get("items", "lightsaberDamage", 26).getInt();
		ConfigOptions.enableLightsaber = config.get("items", "enableLightsaberRecipe", true).getBoolean();
		ConfigOptions.enableLightsaberHum = config.get("items", "enableLightsaberIdleSound", true).getBoolean();
		ConfigOptions.enableBlasterFire = config.get("items", "enableBlasterFire", true).getBoolean();
		ConfigOptions.enableLightsaberStrobe = config.get("items", "enableLightsaberAnimation", true).getBoolean();
		ConfigOptions.enableBuckets = config.get("items", "enableGettingThatDumbFreeBucketFromWaterDroplets", true).getBoolean();

		config.save();

		Lumberjack.info("Configuration loaded!");

		Lumberjack.info("=========== End Star Wars Mod preInit() ===========");
	}

	public void registerMessage(Class messageHandler)
	{
		network.registerMessage(messageHandler, messageHandler, packetId, Side.SERVER);
		Lumberjack.log("Registered packet \"" + messageHandler + "\" as packet ID " + packetId);
		packetId += 1;
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandJediRobes());
	}
}
/*
 * Location: C:\Users\Colby\Downloads\Parzi's Star Wars Mod
 * v1.2.0-dev7.jar!\com\parzi\starwarsmod\StarWarsMod.class Java compiler
 * version: 6 (50.0) JD-Core Version: 0.7.1
 */