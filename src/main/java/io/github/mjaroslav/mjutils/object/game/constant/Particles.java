package io.github.mjaroslav.mjutils.object.game.constant;

import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Particles {
    public final String HUGE_EXPLOSION = "hugeexplosion";
    public final String LARGE_EXPLODE = "largeexplode";
    public final String FIREWORKS_SPARK = "fireworksSpark";
    public final String BUBBLE = "bubble";
    public final String SUSPENDED = "suspended";
    public final String DEATH_SUSPEND = "depthsuspend";
    public final String TOWN_AURA = "townaura";
    public final String CRIT = "crit";
    public final String MAGIC_CRIT = "magicCrit";
    public final String SMOKE = "smoke";
    public final String MOB_SPELL = "mobSpell";
    public final String MOB_SPELL_AMBIENT = "mobSpellAmbient";
    public final String SPELL = "spell";
    public final String INSTANT_SPELL = "instantSpell";
    public final String WITCH_MAGIC = "witchMagic";
    public final String NOTE = "note";
    public final String PORTAL = "portal";
    public final String ENCHANTMENT_TABLE = "enchantmenttable";
    public final String EXPLODE = "explode";
    public final String FLAME = "flame";
    public final String LAVA = "lava";
    public final String FOOTSTEP = "footstep";
    public final String SPLASH = "splash";
    public final String WAKE = "wake";
    public final String LARGE_SMOKE = "largesmoke";
    public final String CLOUD = "clound";
    public final String RED_DUST = "reddust";
    public final String SNOWBALL_POOF = "snowballpoof";
    public final String DRIP_LAVA = "dripLava";
    public final String DRIP_WATER = "dripWater";
    public final String SHOW_SHOVEL = "snowshovel";
    public final String SLIME = "slime";
    public final String HEART = "heart";
    public final String ANGRY_VILLAGER = "angryVillager";
    public final String HAPPY_VILLAGER = "happyVillager";

    public @NotNull String blockcrack(@NotNull Block block) {
        return "blockcrack_" + Block.getIdFromBlock(block);
    }

    public @NotNull String blockdust(@NotNull Block block) {
        return "blockdust_" + Block.getIdFromBlock(block);
    }
}
