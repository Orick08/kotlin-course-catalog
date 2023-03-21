package com.kotlingspring.dto

import jakarta.validation.constraints.NotBlank

data class CourseDTO(
    val id : Int?,
    @get:NotBlank(message = "courseDTO.name should not be blank")
    val name : String,
    @get:NotBlank(message = "courseDTO.category should not be blank")
    val category: String
)

