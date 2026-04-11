package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.DepartmentRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.DepartmentResponse;
import com.vuongdev.Storeclothes.service.DepartmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/departments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
    DepartmentService departmentService;

    @PostMapping
    ApiResponse<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest request){
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.createDepartment(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<DepartmentResponse> getDepartmentById(@PathVariable Long id){
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.getDepartmentById(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<DepartmentResponse>> getAllDepartments(){
        return ApiResponse.<List<DepartmentResponse>>builder()
                .result(departmentService.getAllDepartments())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<DepartmentResponse> updateDepartments(@PathVariable Long id, @RequestBody DepartmentRequest request){
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.updateDepartments(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    ApiResponse<String> deleteDepartments(@PathVariable Long id){
        departmentService.deleteDepartments(id);
        return ApiResponse.<String>builder()
                .result("Department deleted successfully")
                .build();
    }
}
