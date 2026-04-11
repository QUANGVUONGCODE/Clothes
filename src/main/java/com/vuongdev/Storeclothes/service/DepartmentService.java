package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.DepartmentRequest;
import com.vuongdev.Storeclothes.dto.response.CategoryResponse;
import com.vuongdev.Storeclothes.dto.response.DepartmentResponse;
import com.vuongdev.Storeclothes.entity.Departments;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.DepartmentMapper;
import com.vuongdev.Storeclothes.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {
    DepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;

    public DepartmentResponse createDepartment(DepartmentRequest request){
        if(departmentRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.DEPARTMENT_EXISTS);
        }
        Departments department = departmentMapper.mapToDepartment(request);
        return departmentMapper.mapToDepartmentResponse(departmentRepository.save(department));
    }

    public List<DepartmentResponse> getAllDepartments(){
        return departmentRepository.findAll().stream().map(departmentMapper::mapToDepartmentResponse).toList();
    }

    public DepartmentResponse getDepartmentById(Long id){
        return departmentMapper.mapToDepartmentResponse(departmentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.DEPARTMENT_EXISTS)));
    }

    public DepartmentResponse updateDepartments(Long id, DepartmentRequest request){
        Departments departments = departmentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_DEPARTMENT)
        );

        if(departmentRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.DEPARTMENT_EXISTS);
        }
        departmentMapper.updateDepartment(request, departments);
        return departmentMapper.mapToDepartmentResponse(departmentRepository.save(departments));
    }


    public void deleteDepartments(Long id){
        if(!departmentRepository.existsById(id)){
            throw new AppException(ErrorCode.INVALID_ID_DEPARTMENT);
        }
        departmentRepository.deleteById(id);
    }
}
