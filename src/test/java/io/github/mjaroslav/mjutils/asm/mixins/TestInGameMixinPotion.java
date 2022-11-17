package io.github.mjaroslav.mjutils.asm.mixins;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import lombok.val;
import net.minecraft.potion.Potion;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGameMixinPotion {
    @Test
    void test$potionTypesLength() {
        val len = Potion.potionTypes.length;
        Assert.isEquals(1024, len, "Array size not changed");
    }

    @Test(expected = IllegalArgumentException.class)
    void test$duplicateId() {
        val potionA = new Potion(256, false, 0) {
        };
        val potionB = new Potion(256, false, 0) {
        }; // Exception must be thrown
    }
}
