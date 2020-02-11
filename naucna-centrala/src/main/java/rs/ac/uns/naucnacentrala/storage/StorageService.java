package rs.ac.uns.naucnacentrala.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void deleteAll();

	Stream<Path> loadAll();

	String store(MultipartFile file);

	Path load(String filename);

	Resource loadAsResource(String filename);

}
