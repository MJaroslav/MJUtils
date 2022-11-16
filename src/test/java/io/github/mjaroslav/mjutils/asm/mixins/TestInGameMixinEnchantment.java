package io.github.mjaroslav.mjutils.asm.mixins;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import lombok.val;
import net.minecraft.enchantment.Enchantment;

@Common
public class TestInGameMixinEnchantment {
    @Test
    void test$enchantmentsListLength() {
        val len = Enchantment.enchantmentsList.length;
        Assert.isEquals(1024, len, "Array size not changed");
    }
}
