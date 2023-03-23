package com.kotlingspring.service

import com.kotlingspring.dto.InstructorDTO
import com.kotlingspring.entity.Instructor
import com.kotlingspring.repository.InstructorRepository
import org.springframework.stereotype.Service

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO{
        val instructorEntity = instructorDTO.let{
            Instructor(it.id, it.name)
        }
        instructorRepository.save(instructorEntity)
        return instructorEntity.let{
            InstructorDTO(it.id, it.name)
        }
    }
}