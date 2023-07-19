package com.rainbow.server.domain.image

import com.rainbow.server.domain.BaseEntity
import javax.persistence.*

@Entity
@Table(name="Image")
class Image(
    id: Long? = null,
    originalFileName: String?,
    saveFileName: String
):BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id

    @Column(nullable = false)
    val originalFileName: String? = originalFileName

    @Column(nullable = false)
    val saveFileName: String = saveFileName
}