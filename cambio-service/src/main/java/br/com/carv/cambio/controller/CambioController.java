package br.com.carv.cambio.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.carv.cambio.model.Cambio;
import br.com.carv.cambio.repository.CambioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cambio endpoint")
@RestController
@RequestMapping(value = "cambio-service")
public class CambioController {
	
	@Autowired
	private Environment envinronment;
	
	@Autowired
	private CambioRepository cambioRepository;

	@Operation(description = "Get cambio from currency")
	@RequestMapping(method = RequestMethod.GET, value = "/{amount}/{from}/{to}")
	public Cambio getCambio(@PathVariable("amount") BigDecimal amount,
			@PathVariable("from") String from,
			@PathVariable("to") String to) {
		
		String port = envinronment.getProperty("local.server.port");
		
		Cambio cambioResult = cambioRepository.findByFromAndTo(from, to);
		
		if (cambioResult == null) {
			throw new RuntimeException("Currency Unsupported");
		}
		
		cambioResult.setEnvironment(port);
		BigDecimal conversionFactor = cambioResult.getConversionFactor();
		BigDecimal conversionValue = conversionFactor.multiply(amount);
		cambioResult.setConvertedValue(conversionValue.setScale(2, RoundingMode.CEILING));
		
		return cambioResult;
	}
}
