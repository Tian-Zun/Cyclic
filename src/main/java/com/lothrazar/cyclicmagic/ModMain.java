package com.lothrazar.cyclicmagic;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.*;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", acceptableRemoteVersions = "*", guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigFactory")
public class ModMain {
  private List<ICyclicModule> modules = new ArrayList<ICyclicModule>();
  @Instance(value = Const.MODID)
  public static ModMain instance;
  @SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
  public static CommonProxy proxy;
  public static ModLogger logger;
  public EventRegistry events;
  public static SimpleNetworkWrapper network;
  private static Configuration config;
  public static Configuration getConfig() {
    return config;
  }
  public final static CreativeTabs TAB = new CreativeTabs(Const.MODID) {
    @Override
    public Item getTabIconItem() {
      return ItemRegistry.cyclic_wand_build == null ? Items.DIAMOND : ItemRegistry.cyclic_wand_build;
    }
  };
  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    logger = new ModLogger(event.getModLog());
    config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    PacketRegistry.register(network);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    this.events = new EventRegistry();
    this.events.registerCoreEvents();
    ModuleRegistry.register(this.modules);//all features are in a module
    this.syncConfig();
    for (ICyclicModule module : this.modules) {
      module.onPreInit();
    }
  }
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    for (ICyclicModule module : this.modules) {
      module.onInit();
    }
    ItemRegistry.register();//now that modules have added their content (items), we can register them
    proxy.register();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    this.syncConfig(); //fixes things , stuff was added to items and content that has config
    this.events.registerAll(); //important: register events AFTER modules onInit, since modules add events in this phase.
  }
  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for (ICyclicModule module : this.modules) {
      module.onPostInit();
    }
  }
  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for (ICyclicModule module : this.modules) {
      module.onServerStarting(event);
    }
  }
  public void syncConfig() {
    Configuration c = getConfig();
    // hit on startup and on change event from
    // we cant make this a list/loop because the order does matter
    for (ICyclicModule module : this.modules) {
      module.syncConfig(c);
    }
    //for any modules that have created an item, those items might have inner configs, so hit it up
    Item item;//its a leftover mapping from before modules
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      if (item instanceof IHasConfig) {
        ((IHasConfig) item).syncConfig(config);
      }
    }
    c.save();
  }
  /*
   * 
   * TODO:
   * 
   * NEW filtered miner, maybe even config range like builder

Existing miner: still add gui, but the only feature is progress bar and btn to switch between shovel / axe / pickaxe

block miner advanced: nerf to iron pickaxes


   * idea: achievement for crafting?
   *  http://jabelarminecraft.blogspot.ca/p/minecraft-forge-creating-custom.html 
   *  along with   public void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
   *  
   * circle sometimes gets duplicate positions, slowing it down
   * 
   * [ Trading Tool // gui ] Upgrade villager gui: either make my own or add
   * buttons/some way to view all trades at once --inspired by extrautils
   * trading table that is apparently gone after 1710
   * 
   * exp bottler: item with a gui/inventory put bottles in, toggle on/off and it
   * slowly drains your exp into the bottles at a given ratio
   * 
   * pets live longer and/or respawn
   * 
   * add some of my items to loot tables ?
   * https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/
   * net/minecraftforge/debug/LootTablesDebug.java
   * 
   *
   * ROTATE: STAIRS: allow switch from top to bottom
   * 
   * 
   */
}
