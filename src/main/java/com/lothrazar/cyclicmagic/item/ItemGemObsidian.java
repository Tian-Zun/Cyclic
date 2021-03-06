package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.data.IHasOreDict;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;

public class ItemGemObsidian extends Item implements IHasOreDict, IHasRecipe, IContent {

  @Override
  public String[] getOreDict() {
    return new String[] { "gemObsidian" };
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        "ofo",
        "beb",
        "ofo",
        'e', "gemEmerald",
        'b', "gemAmber",
        'o', "obsidian",
        'f', Blocks.CHORUS_FLOWER);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public String getContentName() {
    return "crystallized_obsidian";
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean(getContentName(), Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText
        + "  Warning, removing this crafting item may cause some recipes to not work correctly or be too inexpensive. "
        + " So be prepared to customize recipes if you disable this.  It has ore dictionary 'gemObsidian' ");
  }

  @Override
  public void register() {
    ItemRegistry.register(this, getContentName(), GuideCategory.GEAR);
  }
}
