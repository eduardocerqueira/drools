package org.drools.io;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;

import org.drools.util.IoUtils;
import org.kie.api.io.Resource;

import static org.drools.util.IoUtils.readBytesFromInputStream;

public class InputStreamResource extends BaseResource implements InternalResource {

    private transient InputStream stream;
    private String encoding;

    public InputStreamResource() { }

    public InputStreamResource(InputStream stream) {
        this(stream, null);
    }

    public InputStreamResource(InputStream stream,
                               String encoding) {
        if ( stream == null ) {
            throw new IllegalArgumentException( "stream cannot be null" );
        }
        this.stream = stream;
        this.encoding = encoding;
    }

    @Override
    public void readExternal( ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal( in );
        encoding = (String) in.readObject();
    }

    @Override
    public void writeExternal( ObjectOutput out) throws IOException {
        getBytes();
        super.writeExternal( out );
        out.writeObject( encoding );
    }

    @Override
    public byte[] getBytes() {
        if (bytes == null) {
            try {
                bytes = readBytesFromInputStream( stream );
            } catch (IOException e) {
                throw new RuntimeException( e );
            }
        }
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream( getBytes() );
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public Reader getReader() throws IOException {
        if (this.encoding != null) {
            return new InputStreamReader( getInputStream(), this.encoding );
        } else {
            return new InputStreamReader( getInputStream(), IoUtils.UTF8_CHARSET );
        }
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException( "InputStream cannot be resolved to URL" );
    }
    
    public boolean hasURL() {
        return false;
    }
    
    public boolean isDirectory() {
        return false;
    }

    public Collection<Resource> listResources() {
        throw new RuntimeException( "This Resource cannot be listed, or is not a directory" );
    }

}
