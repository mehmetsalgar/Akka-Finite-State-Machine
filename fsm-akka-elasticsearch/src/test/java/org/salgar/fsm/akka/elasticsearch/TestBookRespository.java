package org.salgar.fsm.akka.elasticsearch;


import org.salgar.fsm.akka.elasticsearch.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
interface TestBookRespository extends CrudRepository<Book, String> {
}
