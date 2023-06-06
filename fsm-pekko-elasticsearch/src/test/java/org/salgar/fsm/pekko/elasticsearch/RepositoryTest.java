package org.salgar.fsm.pekko.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.salgar.fsm.pekko.elasticsearch.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@ActiveProfiles("itest")
@RequiredArgsConstructor
@SpringBootTest(classes = {TestApplication.class})
public class RepositoryTest {
    @Autowired
    private TestBookRespository testBookRespository;

    @Test
    public void test() {
        Book book = new Book();
        book.setId("12345678");
        book.setName("TestName");
        book.setSummary("Bla blaaaaa");
        book.setPrice(546);
        Book result = testBookRespository.save(book);

        Assert.assertNotNull(result);
    }
}
