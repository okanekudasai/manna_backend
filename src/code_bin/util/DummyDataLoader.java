package com.example.manna.util;

import com.example.manna.entity.feed.FeedDto;
import com.example.manna.entity.user.UserDto;
import com.example.manna.repository.FeedRepository;
import com.example.manna.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class DummyDataLoader  {
    final UserRepository userRepository;
    final FeedRepository feedRepository;
    @PostConstruct
    public void init() {
        UserDto user1 = UserDto.builder()
                .name("aaaa")
                .profile_url("https://img3.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202303/13/sbsnoriter/20230313145738118regu.jpg")
                .build();
        userRepository.save(user1);

        for (int i = 0; i < 20; i++) {
            FeedDto feed = FeedDto.builder()
                    .writer(user1)
                    .article_title(i + "  가나다라마바사")
                    .article_content(i + "  Lorem ipsum doloafklsejfasejf;asefji jaslief jasleifj a;self jsaeil fjaseli jfaef j;aefjaseifj aseilfjaseijaeilfjaeilfjaeilfj;ase jaeil jfaeil ;jsaelf jaseilf jase;lfijaeilfjaeil jas;el ijase jgrij gsdirg jsd; ijrgsridg jsdrigj sdrigj sdril gjsdlri gjsdrilg jsdlirg jsdlrigjir sit amet consectetur adipisicing elit. Ullam, consequatur. Laboriosam tenetur laborum illo nemo explicabo labore, itaque placeat reiciendis at illum earum molestiae, esse distinctio rem. Repudiandae, unde doloribus.")
                    .article_thumbnail("https://i.ytimg.com/vi/HHCGkGA-Sis/maxresdefault.jpg")
                    .heart_count(i+1)
                    .reply_count(i+2)
                    .createdTime(LocalDateTime.now())
                    .deleted(false)
                    .build();

            feedRepository.save(feed);
        }
    }
}
