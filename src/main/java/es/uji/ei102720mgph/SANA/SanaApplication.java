package es.uji.ei102720mgph.SANA;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.logging.Logger;

//L'anotació permet que l'app s'autoconfigure, que puga buscar components
//i que es puguen configurar dependències entre ells
@SpringBootApplication
public class SanaApplication {

	private static final Logger log = Logger.getLogger(SanaApplication.class.getName());

	public static void main(String[] args) {
		// Auto-configura l'aplicació
		new SpringApplicationBuilder(SanaApplication.class).run(args);
	}

}
