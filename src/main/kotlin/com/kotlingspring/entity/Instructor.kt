package com.kotlingspring.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "INSTRUCTORS")
data class Instructor (
    @Id
    @GeneratedValue
    val id: Int?,
    var name: String
)