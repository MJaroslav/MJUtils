package com.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mjutils.config.annotations.Name;
import com.github.mjaroslav.mjutils.config.annotations.Range;
import lombok.experimental.UtilityClass;

@Name("general")
@UtilityClass
public class TestGeneralCategory {
    @Range(min = -10, max = 20)
    public int testInt = 5;

    @UtilityClass
    @Name("inner")
    public class InnerCategory {
        @Name("renamed")
        public boolean[] testBooleanArray = new boolean[]{false, true, false};
    }
}
