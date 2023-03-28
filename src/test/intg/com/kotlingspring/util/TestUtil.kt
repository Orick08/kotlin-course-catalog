package com.kotlingspring.util

import com.kotlingspring.dto.CourseDTO
import com.kotlingspring.entity.Course
import com.kotlingspring.entity.Instructor

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


fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(null,
        "Build REST APIS with Kotlin Spring boot", "Development",
        instructor),
    Course(null,
        "Micronauting with Micronaut", "Development"
        ,instructor
    ),
    Course(null,
        "Social Life for Java Developers", "Development" ,
        instructor)
)

fun instructorEntity(name : String = "Dilip Sundarraj") = Instructor(null, name)