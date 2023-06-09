package com.kotlingspring.service

import com.kotlingspring.dto.CourseDTO
import com.kotlingspring.entity.Course
import com.kotlingspring.exception.CourseNotFoundException
import com.kotlingspring.exception.InstructorNotValidException
import com.kotlingspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository, val instructorService: InstructorService) {
    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO{
        val instructorOptional = instructorService.findInstructorById(courseDTO.instructorId!!)

        if(!instructorOptional.isPresent){
            throw InstructorNotValidException("Instructor not valid : ${courseDTO.instructorId}")
        }

        val courseEntity = courseDTO.let{
            Course(null, it.name, it.category, instructorOptional.get())
        }
        courseRepository.save(courseEntity)
        logger.info("Save course is $courseEntity")
        return courseEntity.let{
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {

        val courses = courseName?.let{
            courseRepository.findByNameContaining(it)
        } ?: courseRepository.findAll()

        return courses
            .map{
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)
        return if (existingCourse.isPresent){
            existingCourse.get()
                .let{
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    // Return statement in kotlin
                    CourseDTO(it.id, it.name, it.category)
                }
        }
        else{
            throw CourseNotFoundException("No course found fot the id $courseId")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)
        if(existingCourse.isPresent){
            existingCourse.get()
                .let{
                    courseRepository.deleteById(courseId)
                }
        }
        else{
            throw CourseNotFoundException("No course found fot the id $courseId")
        }
    }
}
