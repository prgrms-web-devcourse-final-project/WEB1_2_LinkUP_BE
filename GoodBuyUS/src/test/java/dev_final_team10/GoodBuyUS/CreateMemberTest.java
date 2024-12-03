/*
package dev_final_team10.GoodBuyUS;

package h2test.hi.member;

import h2test.hi.entity.Member;
import h2test.hi.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("멤버가 생성되는지 테스트")
public class CreateMemberTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    @DisplayName("더미데이터생성")
    public void init(){
        Member member = Member.createMember("홍공진");
        memberRepository.save(member);
    }
    @Test
    @DisplayName("initDB에서 데이터가 제대로 들어갔는지 확인")
    public void checkMember(){
        init();
        List<Member> members = memberRepository.findAll();
        Assertions.assertThat(members.size()).isEqualTo(2);
    }
}*/
