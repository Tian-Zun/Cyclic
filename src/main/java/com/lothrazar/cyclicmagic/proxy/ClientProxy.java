package com.lothrazar.cyclicmagic.proxy;

import org.lwjgl.input.Keyboard;
import net.minecraft.item.Item;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.projectile.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityList;

public class ClientProxy extends CommonProxy {
	public static KeyBinding keySpellUp;
	public static KeyBinding keySpellDown;
	//public static KeyBinding keySpellToggle;

	private static final String keyCategorySpell = "key.categories.spell";

	@Override
	public void register() {
		registerKeyBindings();

		registerModels();

		registerEntities();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void registerEntities() {
		
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();

		RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, new RenderSnowball(rm, EntityTorchBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, new RenderSnowball(rm, EntityFishingBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, EntityBlazeBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningballBolt.class, new RenderSnowball(rm, EntityLightningballBolt.item, ri));

		RenderingRegistry.registerEntityRenderingHandler(EntitySnowballBolt.class, new RenderSnowball(rm, EntitySnowballBolt.item, ri));

		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, EntityBlazeBolt.item, ri));

		RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new RenderSnowball(rm, EntityDynamite.item, ri));

		RenderingRegistry.registerEntityRenderingHandler(EntityWaterBolt.class, new RenderSnowball(rm, EntityWaterBolt.item, ri));

		RenderingRegistry.registerEntityRenderingHandler(EntityShearingBolt.class, new RenderSnowball(rm, EntityShearingBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityRespawnEgg.class, new RenderSnowball(rm, EntityRespawnEgg.item, ri));

	}

	private void registerModels() {
		// More info on proxy rendering
		// http://www.minecraftforge.net/forum/index.php?topic=27684.0
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		String name;

		for (Item i : ItemRegistry.items) {
			name = Const.TEXTURE_LOCATION + i.getUnlocalizedName().replaceAll("item.", "");

			mesher.register(i, 0, new ModelResourceLocation(name, "inventory"));
		}
		
		if(ItemRegistry.respawn_egg != null) { 
			for(Object key : EntityList.entityEggs.keySet()) {
				  mesher.register(ItemRegistry.respawn_egg, (Integer)key, new
				  ModelResourceLocation(Const.TEXTURE_LOCATION + "respawn_egg" ,"inventory")); 
			} 
		}
	}

	private void registerKeyBindings() {

		keySpellUp = new KeyBinding("key.spell.up", Keyboard.KEY_Z, keyCategorySpell);
		ClientRegistry.registerKeyBinding(ClientProxy.keySpellUp);

		keySpellDown = new KeyBinding("key.spell.down", Keyboard.KEY_C, keyCategorySpell);
		ClientRegistry.registerKeyBinding(ClientProxy.keySpellDown);
	}
}
