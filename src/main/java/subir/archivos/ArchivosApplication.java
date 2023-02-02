package subir.archivos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.Resource;
import subir.archivos.service.FileService;

@SpringBootApplication
public class ArchivosApplication implements CommandLineRunner {

	@Resource
	FileService fileService;
	public static void main(String[] args) {
		SpringApplication.run(ArchivosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileService.deleteAll();
		fileService.init();
	}

	

}
