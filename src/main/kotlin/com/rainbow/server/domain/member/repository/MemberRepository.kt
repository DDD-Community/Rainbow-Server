package com.rainbow.server.domain.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.entity.QMember.member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/***
 * 예시 Repository
 * */
@Repository
interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom {
    fun findByEmail(email: String): Member?

    fun existsByEmail(email: String): Boolean
    fun existsByNickName(nickName: String): Boolean
}

interface MemberRepositoryCustom {
//    fun findSuggestedMemberList(standardMember: Member): List<Member>

    fun findMemberListBySalary(salaryStart: Int, salaryEnd: Int): List<Member>

    fun findAllByNickName(nickName: String): List<Member>?

    fun findNewbies(): List<Member>
}

class MemberRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : MemberRepositoryCustom {

//    override fun findSuggestedMemberList(standardMember: Member): List<Member> {
//        return queryFactory.selectFrom(member)
//            .join(follow).on(member.memberId.eq(follow.toMember))
//            .where(
//                (member.salary.eq(standardMember.salary))
//                    .or(member.birthDate.eq(standardMember.birthDate)).and(!follow.fromMember.eq(standardMember.memberId)).and(
//                        !member.memberId.eq(standardMember.memberId)
//                    )
//            )
//            .fetch()
//    }

    override fun findAllByNickName(nickName: String): List<Member>? {
        return queryFactory.selectFrom(member)
            .where(member.nickName.contains(nickName))
            .fetch()
    }
    override fun findMemberListBySalary(salaryStart: Int, salaryEnd: Int): List<Member> {
        return queryFactory.select(member)
            .from(member)
            .limit(5)
            .where(member.salary.eq(salaryStart))
            .fetch()
    }

    override fun findNewbies(): List<Member> {
        return queryFactory.selectFrom(member)
            .limit(10)
            .orderBy(member.createdAt.desc())
            .fetch()
    }
}
