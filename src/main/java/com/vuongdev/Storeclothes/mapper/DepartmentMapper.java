package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.DepartmentRequest;
import com.vuongdev.Storeclothes.dto.response.DepartmentResponse;
import com.vuongdev.Storeclothes.entity.Departments;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Departments mapToDepartment(DepartmentRequest departmentRequest);
    DepartmentResponse mapToDepartmentResponse(Departments departments);
    void updateDepartment(DepartmentRequest departmentRequest,@MappingTarget Departments departments);
}
