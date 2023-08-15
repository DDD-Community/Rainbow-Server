package com.rainbow.server.domain.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.member.entity.Follow
import com.rainbow.server.domain.member.entity.FollowPK
import com.rainbow.server.domain.member.entity.QFollow.follow
import com.rainbow.server.domain.member.entity.QMember.member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository : JpaRepository<Follow, FollowPK>, FollowRepositoryCustom {

    fun existsByFromMemberAndToMember(fromId: Long, toId: Long): Boolean
}

interface FollowRepositoryCustom {

    fun finAllByFromMemberWithMember(fromId: Long): List<String?>
}

class FollowRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : FollowRepositoryCustom {
    override fun finAllByFromMemberWithMember(fromId: Long): List<String?> {
        return queryFactory.select(member.nickName)
            .from(follow)
            .join(member).on(follow.toMember.eq(member.memberId))
            .where(follow.fromMember.eq(fromId))
            .fetch()
    }
}
