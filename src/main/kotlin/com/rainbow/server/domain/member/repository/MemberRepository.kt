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
interface MemberRepository: JpaRepository<Member, Long>, MemberRepositoryCustom {
    fun findByEmail(email: String): Member?

}

    interface MemberRepositoryCustom{
        fun findSuggestedMemberList(standardMember: Member):List<Member>

        fun findMemberListBySalary(salaryStart:Int,salaryEnd:Int) :List<Member>

        fun findNewbies():List<Member>
    }

    class MemberRepositoryImpl(
        private val queryFactory: JPAQueryFactory
    ): MemberRepositoryCustom{

        override fun findSuggestedMemberList(standardMember: Member):List<Member> {
         return   queryFactory.selectFrom(member)
                .where((member.salaryStart.eq(standardMember.salaryStart).and(member.salaryEnd.eq(standardMember.salaryEnd)))
                    .or(member.birthDate.eq(standardMember.birthDate))
                    )
                .fetch()
        }

        override fun findMemberListBySalary(salaryStart: Int, salaryEnd: Int):List<Member> {
         return   queryFactory.selectFrom(member)
                .limit(5)
                .where(member.salaryStart.eq(salaryStart),
                    member.salaryEnd.eq(salaryEnd))
                .fetch()
        }

        override fun findNewbies(): List<Member> {
            return queryFactory.selectFrom(member)
                .limit(5)
                .orderBy(member.createdAt.desc())
                .fetch()
        }

    }

