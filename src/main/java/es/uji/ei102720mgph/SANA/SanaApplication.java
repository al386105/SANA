package es.uji.ei102720mgph.SANA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.logging.Logger;

//L'anotació permet que l'app s'autoconfigure, que puga buscar components
//i que es puguen configurar dependències entre ells
@SpringBootApplication
public class SanaApplication {

	private static final Logger log = Logger.getLogger(SanaApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(SanaApplication.class, args);
	}

}
