package com.kotlingspring.controller

import com.kotlingspring.dto.CourseDTO
import com.kotlingspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/courses")
class CoursesController(val courseService: CourseService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO): CourseDTO{
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    fun retrieveAllCourses() : List<CourseDTO>{
        return courseService.retrieveAllCourses()
    }

    @PutMapping("/{course_id}")
    fun updateCourse(@RequestBody courseDTO: CourseDTO, @PathVariable("course_id") courseId: Int) : CourseDTO
    {
        return courseService.updateCourse(courseId, courseDTO)
    }
}