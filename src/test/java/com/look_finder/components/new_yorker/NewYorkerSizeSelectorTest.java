package com.look_finder.components.new_yorker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NewYorkerSizeSelectorTest {

    @Test
    void f_bottomD_should_return_36_for_typical_values() {
        NewYorkerSizeSelector selector = new NewYorkerSizeSelector();

        String result = selector.select_size("m", "bottom", 0, 81, 92);

        assertThat(result).isEqualTo("none+36+A");
    }
}
