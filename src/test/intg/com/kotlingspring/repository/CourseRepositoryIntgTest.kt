package com.kotlingspring.repository

import com.kotlingspring.util.PostgresSQLContainerInitializer
import com.kotlingspring.util.courseEntityList
import com.kotlingspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryIntgTest: PostgresSQLContainerInitializer() {

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

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesByName_approach2(name: String, expectedSize: Int){
        val courses = courseRepository.findCoursesByName(name)
        println("courses : $courses")

        Assertions.assertEquals(expectedSize, courses.size)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments>{
            return Stream.of(Arguments.arguments("Micronaut", 1),
                Arguments.arguments("Java", 1))
        }
    }
}
