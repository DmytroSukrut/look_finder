package com.look_finder.DTO;

import lombok.Data;

public record ErrorDTO(
        String code,
        String msg
) {}
