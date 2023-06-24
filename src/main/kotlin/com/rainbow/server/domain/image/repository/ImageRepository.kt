package com.rainbow.server.domain.image.repository

import com.rainbow.server.domain.image.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository: JpaRepository<Image, Long> {
}
