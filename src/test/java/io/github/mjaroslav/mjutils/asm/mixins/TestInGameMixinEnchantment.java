package io.github.mjaroslav.mjutils.asm.mixins;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import lombok.val;
import net.minecraft.enchantment.Enchantment;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGameMixinEnchantment {
    @Test
    void test$enchantmentsListLength() {
        val len = Enchantment.enchantmentsList.length;
        Assert.isEquals(1024, len, "Array size not changed");
    }
}
