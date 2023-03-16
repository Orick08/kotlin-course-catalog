package com.kotlingspring.repository

import com.kotlingspring.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository: CrudRepository<Course, Int> {
}