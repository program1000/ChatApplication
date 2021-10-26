package chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileManager {

    private final String ROOT_DIRECTORY = "../chat-web-server-jersey/html/";
    private static final SimpleDateFormat LAST_MODIFIED_DATEFORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy  HH:mm:ss z" );
    private Map<String, HtmlFileData> localContent;
    private HtmlFileFilter fileFilter;

    public FileManager() {
        localContent = new HashMap<String, HtmlFileData>();
        fileFilter = new HtmlFileFilter();
        initLocalContent();
    }

    private void initLocalContent() {
        File rootDir = new File( ROOT_DIRECTORY );

        File[] files = rootDir.listFiles( fileFilter );
        HtmlFileData data = null;
        for( File file : files ) {
            data = new HtmlFileData();
            data.setLastModified( fixLastModified( file.lastModified() ) );
            data.setExtension( parseExtension(file.getName() ) );
            localContent.put( file.getName(), data );
        }

    }
    
    private String parseExtension( String name ) {
        int index = name.lastIndexOf( "." );
        if( index==-1) {
            return "";
        }
        return name.substring( index+1 );
    }

    private Date fixLastModified( long lastModified ) {
        Date result = new Date( lastModified );
        // remove milliseconds from date
        String lastModText = LAST_MODIFIED_DATEFORMAT.format( result );
        try {
            result = LAST_MODIFIED_DATEFORMAT.parse( lastModText );
        } catch( ParseException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public Date getLastMod( String file ) {
        HtmlFileData data = localContent.get( file );
        if( data == null ) {
            return null;
        }
        return data.getLastModified();
    }

    public String getContent( String file ) {
        HtmlFileData data = localContent.get( file );
        String content = data.getContent();
        String newline = System.lineSeparator();
        if( content == null ) {
            try( BufferedReader br = new BufferedReader( new FileReader( ROOT_DIRECTORY + file ) ); ) {
                content = br.lines().collect(Collectors.joining(newline));
            } catch( FileNotFoundException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch( IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            data.setContent( content );
        }
        return content;
    }
    
    public String getExtension( String file ) {
        return localContent.get( file ).getExtension();
    }

}
