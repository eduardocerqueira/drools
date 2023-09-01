package org.drools.io;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import org.kie.api.definition.KieDescr;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;

public class ResourceFactoryServiceImpl implements KieResources {

    public Resource newByteArrayResource(byte[] bytes) {
        return new ByteArrayResource( bytes );
    }

    public Resource newByteArrayResource(byte[] bytes,
                                         String encoding) {
        return new ByteArrayResource( bytes, encoding );
    }

    public Resource newClassPathResource(String path) {
        return new ClassPathResource( path );
    }

    public Resource newClassPathResource(String path,
                                         ClassLoader classLoader) {
        return new ClassPathResource( path,
                                      classLoader );
    }

    public Resource newClassPathResource(String path,
                                         Class<?> clazz) {
        return new ClassPathResource( path,
                                      clazz );
    }

    public Resource newClassPathResource(String path,
                                         String encoding) {
        return new ClassPathResource( path,
                                      encoding );
    }

    public Resource newClassPathResource(String path,
                                         String encoding,
                                         ClassLoader classLoader) {
        return new ClassPathResource( path,
                                      encoding,
                                      classLoader );
    }

    public Resource newClassPathResource(String path,
                                         String encoding,
                                         Class<?> clazz) {
        return new ClassPathResource( path,
                                      encoding,
                                      clazz );
    }
    
    public Resource newFileSystemResource(File file) {
        return new FileSystemResource( file );
    }

    public Resource newFileSystemResource(File file,
                                          String encoding) {
        return new FileSystemResource( file, encoding );
    }

    public Resource newFileSystemResource(String fileName) {
        return new FileSystemResource( fileName );
    }

    public Resource newFileSystemResource(String fileName,
                                          String encoding) {
        return new FileSystemResource( fileName, encoding );
    }

    public Resource newInputStreamResource(InputStream stream) {
        return new InputStreamResource( stream );
    }

    public Resource newInputStreamResource(InputStream stream,
                                           String encoding) {
        return new InputStreamResource( stream,
                                        encoding);
    }

    public Resource newReaderResource(Reader reader) {
        return new ReaderResource( reader );
    }

    public Resource newReaderResource(Reader reader,
                                      String encoding) {
        return new ReaderResource( reader,
                                   encoding );
    }

    public Resource newDescrResource( KieDescr descr ) {
        return new DescrResource( descr );
    }
}
