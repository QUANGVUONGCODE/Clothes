package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.SizeRequest;
import com.vuongdev.Storeclothes.dto.response.SizeResponse;
import com.vuongdev.Storeclothes.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    Size mapToSize(SizeRequest request);
    SizeResponse mapToSizeResponse(Size size);
    void updateSize(SizeRequest request, @MappingTarget Size size);
}
