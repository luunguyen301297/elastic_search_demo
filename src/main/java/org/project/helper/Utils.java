package org.project.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j(topic = "UTILS")
public class Utils {
	public static String loadAsString(String path) {
		try {
			File file = new ClassPathResource(path).getFile();

			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
