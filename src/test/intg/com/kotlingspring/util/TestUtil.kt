package com.kotlingspring.util

import com.kotlingspring.dto.CourseDTO
import com.kotlingspring.entity.Course

fun courseEntityList() = listOf(
    Course(
        null, "Build REST APIS with Kotlin Spring boot", "Development"
    ),
    Course(
        null, "Micronauting with Micronaut", "Development"
    ),
    Course(
        null, "Social Life for Java Developers", "Development"
    )
)

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using Spring Boot and Kotlin",
    category: String = "Development",
    //instructorId: Int? = 1
) = CourseDTO(
    id,
    name,
    category,
    //instructorId
)