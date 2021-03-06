package br.com.carv.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.carv.book.model.Book;
import br.com.carv.book.proxy.CambioProxy;
import br.com.carv.book.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {

	@Autowired
	public Environment environment;
	
	@Autowired
	public BookRepository bookRepository;
	
	@Autowired
	private CambioProxy cambioProxy;
	
	/*
		@RequestMapping(method = RequestMethod.GET, value = "/{id}/{currency}")
		public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
			
			String port = environment.getProperty("local.server.port");
			
			Book book = bookRepository.getById(id);
			
			if (book == null) {
				throw new RuntimeException("Book not found");
			}
			
			HashMap<String, String> params = new HashMap<>();
			params.put("amount", book.getPrice().toString());
			params.put("from", "USD");
			params.put("to", currency);
			
			
			var cambioResult = new RestTemplate().getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}", Cambio.class, params);
			
			book.setEnvinronment(port);
			book.setPrice(cambioResult.getBody().getConvertedValue());
			
			return book;
			
			}
	 */	
	
	@Operation(summary = "Find a specific book by your ID")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/{currency}")
	public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		
		String port = environment.getProperty("local.server.port");
		
		Book book = bookRepository.getById(id);
		
		if (book == null) {
			throw new RuntimeException("Book not found");
		}
		
		var cambioResult = cambioProxy.getCambio(book.getPrice(),"USD", currency);
		
		book.setEnvinronment(port);
		book.setPrice(cambioResult.getConvertedValue());
		
		return book;
		
	}
	
}
