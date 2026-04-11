package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ColorRequest;
import com.vuongdev.Storeclothes.dto.response.ColorResponse;
import com.vuongdev.Storeclothes.entity.Color;
import com.vuongdev.Storeclothes.enums.ColorStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.ColorMapper;
import com.vuongdev.Storeclothes.repository.ColorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ColorService {
    ColorRepository colorRepository;
    ColorMapper colorMapper;

    public ColorResponse createColor(ColorRequest request){
        if(colorRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.COLOR_EXISTS);
        }
        Color color = colorMapper.mapToColor(request);
        color.setStatus(ColorStatus.ACTIVE.name());
        colorRepository.save(color);
        return colorMapper.mapToColorResponse(color);
    }

    public List<ColorResponse> getAllColor(){
        return colorRepository.findAll()
                .stream()
                .map(colorMapper::mapToColorResponse)
                .toList();
    }

    public ColorResponse updateColor(Long id, ColorRequest request){
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_COLOR));
        colorMapper.updateColor(request, color);
        color.setStatus(request.getStatus());
        colorRepository.save(color);
        return colorMapper.mapToColorResponse(color);
    }


    public void deleteColor(Long id){
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_COLOR));
        colorRepository.delete(color);
    }
}
