package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.SizeRequest;
import com.vuongdev.Storeclothes.dto.response.SizeResponse;
import com.vuongdev.Storeclothes.entity.Size;
import com.vuongdev.Storeclothes.enums.SizeStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.SizeMapper;
import com.vuongdev.Storeclothes.repository.SizeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeService {
    SizeRepository sizeRepository;
    SizeMapper sizeMapper;

    public SizeResponse createSize(SizeRequest request){
        if(sizeRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.SIZE_EXISTS);
        }
        Size size = sizeMapper.mapToSize(request);
        size.setStatus(SizeStatus.ACTIVE.name());
        return sizeMapper.mapToSizeResponse(sizeRepository.save(size));
    }


    public List<SizeResponse> getAllSizes(){
        return sizeRepository.findAll()
                .stream()
                .map(sizeMapper::mapToSizeResponse)
                .toList();
    }

    public SizeResponse updateSize(Long id, SizeRequest request){
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_SIZE));
        sizeMapper.updateSize(request, size);
        size.setStatus(request.getStatus());
        return sizeMapper.mapToSizeResponse(sizeRepository.save(size));
    }

    public void deleteSize(Long id){
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_SIZE));
        sizeRepository.delete(size);
    }
}
