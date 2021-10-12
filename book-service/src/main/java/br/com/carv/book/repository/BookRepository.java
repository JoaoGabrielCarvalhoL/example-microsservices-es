package br.com.carv.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.carv.book.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
