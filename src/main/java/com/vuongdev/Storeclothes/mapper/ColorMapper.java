package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.ColorRequest;
import com.vuongdev.Storeclothes.dto.response.ColorResponse;
import com.vuongdev.Storeclothes.entity.Color;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    Color mapToColor(ColorRequest request);
    ColorResponse mapToColorResponse(Color color);
    void updateColor(ColorRequest request, @MappingTarget Color color);
}
