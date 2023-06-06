package org.salgar.fsm.pekko.elasticsearch;


import org.salgar.fsm.pekko.elasticsearch.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
interface TestBookRespository extends CrudRepository<Book, String> {
}
