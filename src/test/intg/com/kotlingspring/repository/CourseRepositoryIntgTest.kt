package com.kotlingspring.repository

import com.kotlingspring.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp(){
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining(){
        val courses = courseRepository.findByNameContaining("Micronaut")
        println("courses : $courses")

        Assertions.assertEquals(1, courses.size)
    }

    @Test
    fun findCoursesByName(){
        val courses = courseRepository.findCoursesByName("Micronaut")
        println("courses : $courses")

        Assertions.assertEquals(1, courses.size)
    }


}
