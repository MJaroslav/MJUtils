package io.github.mjaroslav.mjutils.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<A, B> {
    protected A a;
    protected B b;
}
