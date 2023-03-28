package com.kotlingspring.controller

import com.kotlingspring.dto.CourseDTO
import com.kotlingspring.entity.Course
import com.kotlingspring.repository.CourseRepository
import com.kotlingspring.repository.InstructorRepository
import com.kotlingspring.util.PostgresSQLContainerInitializer
import com.kotlingspring.util.courseEntityList
import com.kotlingspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest: PostgresSQLContainerInitializer()  {
    @Autowired
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var courseRepository: CourseRepository
    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp(){
        courseRepository.deleteAll()
        instructorRepository.deleteAll()
        val instructor = instructorEntity()
        instructorRepository.save(instructor)
        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse(){
        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(null, "Test Course", "Test", instructor.id)
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue{
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun retrieveAllCourses(){
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs: $courseDTOs")
        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun retrieveCoursesByName(){
        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name", "Java")
            .toUriString()

        val courseDTOs = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs: $courseDTOs")
        assertEquals(1, courseDTOs!!.size)
    }

    @Test
    fun updateCourse(){
        val instructor = instructorRepository.findAll().first()
        // Save a new course in the database
        val course = Course(
            null, "Kotlin", "Development", instructor
        )
        courseRepository.save(course)
        // New course values
        val updatedCourseDTO = CourseDTO(
            null, "Kotlin 2, Now its personal", "Comedy", course.instructor!!.id
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Kotlin 2, Now its personal", updatedCourse!!.name)
        assertEquals("Comedy", updatedCourse!!.category)
    }

    @Test
    fun deleteCourse(){
        val instructor = instructorRepository.findAll().first()
        // Save a new course in the database
        val course = Course(
            null, "Kotlin", "Development", instructor
        )
        courseRepository.save(course)

        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}