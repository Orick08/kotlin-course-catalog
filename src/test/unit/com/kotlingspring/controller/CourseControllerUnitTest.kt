package com.kotlingspring.controller

import com.kotlingspring.dto.CourseDTO
import com.kotlingspring.entity.Course
import com.kotlingspring.service.CourseService
import com.kotlingspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebMvcTest(controllers = [CoursesController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient
    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse(){
        val courseDTO = CourseDTO(null, "Test Course", "Test", 1)

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

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
    fun courseValidation(){
        val courseDTO = CourseDTO(null, "", "",1)

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("courseDTO.category should not be blank, courseDTO.name should not be blank", response)
    }

    @Test
    fun courseRuntimeException(){
        val courseDTO = CourseDTO(null, "Test Course", "Test", 1)
        val errorMessage = "Unexpected error"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }

    @Test
    fun retrieveAllCourses(){
        every { courseServiceMock.retrieveAllCourses(any()) }.returnsMany(
            listOf(
                courseDTO(1, "Hello world"),
                courseDTO(2, "Test book"),
                courseDTO(3, "Help F1")
            )
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs: $courseDTOs")
        Assertions.assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun updateCourse(){
        // Save a new course in the database
        val course = Course(
            null, "Kotlin", "Development"
        )

        every { courseServiceMock.updateCourse(any(), any())} returns (
            courseDTO(100, "Kotlin 2, Now its personal", "Comedy")
        )

        // New course values
        val updatedCourseDTO = CourseDTO(
            null, "Kotlin 2, Now its personal", "Comedy"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Kotlin 2, Now its personal", updatedCourse!!.name)
        Assertions.assertEquals("Comedy", updatedCourse!!.category)
    }

    @Test
    fun deleteCourse(){
        every { courseServiceMock.deleteCourse(any()) } just runs

        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent
    }
}