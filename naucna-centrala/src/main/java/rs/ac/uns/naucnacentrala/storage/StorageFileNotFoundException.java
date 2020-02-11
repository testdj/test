package rs.ac.uns.naucnacentrala.storage;

public class StorageFileNotFoundException extends StorageException {

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageFileNotFoundException(String message) {
		super(message);
	}
}
