package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void saveTest() {
        // given
        Member member = new Member("ysw", 24);

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Member foundMember = memberRepository.findById(savedMember.getId());
        assertThat(foundMember).isEqualTo(savedMember);
    }

    @Test
    void findAllTest() {
        // given
        Member memberA = new Member("memberA", 20);
        Member memberB = new Member("memberB", 21);
        Member savedMemberA = memberRepository.save(memberA);
        Member savedMemberB = memberRepository.save(memberB);

        // when
        List<Member> savedMemberList = memberRepository.findAll();

        // then
        assertThat(savedMemberList.size()).isEqualTo(2);

        // 가져온 리스트 속에 해당 객체가 있는가?(.contains())
        assertThat(savedMemberList).contains(memberA, memberB);
    }

}