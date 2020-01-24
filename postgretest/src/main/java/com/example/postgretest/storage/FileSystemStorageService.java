package com.example.postgretest.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.stream.Stream;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}
        public static String toSHA1(byte[] convertme) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return Base64.getEncoder().encodeToString(md.digest(convertme));
        }
        
        public String toSHA1( MultipartFile file ) throws IOException
        {
            String sha1 = null;
            MessageDigest digest;
            try
            {
                digest = MessageDigest.getInstance( "SHA-1" );
            }
            catch ( NoSuchAlgorithmException e1 )
            {
                throw new IOException( "Impossible to get SHA-1 digester", e1 );
            }
            try (InputStream input = file.getInputStream();
                 DigestInputStream digestStream = new DigestInputStream( input, digest ) )
            {
                while(digestStream.read() != -1){
                    // read file stream without buffer
                }
                MessageDigest msgDigest = digestStream.getMessageDigest();
                sha1 = new HexBinaryAdapter().marshal( msgDigest.digest() );
            }
            return sha1;
        }
	public void salvar(MultipartFile file, String filename){
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}
	
    public void remover(String filename){
    
            File arq = new File(this.rootLocation + "/" + filename);
            
            if(arq.delete())
                System.out.println("Remocao ok!");
            else
                System.out.println("Falha ao remover");
        
    }

	public String getExtensao(String filename){
		int pos = filename.lastIndexOf(".");
		System.out.println(pos + " e a posicao");

		String s = "";

		for(int i = pos + 1 ; i < filename.length() ; i++){
			s += filename.charAt(i);
		}

		return s;
	}

	@Override
	public void store(MultipartFile file) {
		String filename = file.getOriginalFilename();

		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
        

        


}