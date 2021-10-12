package br.com.carv.book.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.carv.book.response.Cambio;

@FeignClient(name = "cambio-service")
public interface CambioProxy {
	
	@RequestMapping(method = RequestMethod.GET, value = "/cambio-service/{amount}/{from}/{to}")
	public Cambio getCambio(@PathVariable("amount") Double amount,
			@PathVariable("from") String from,
			@PathVariable("to") String to);

}
