package chat;

import java.util.Date;

public class HtmlFileData {
  
    private Date lastModified;
    private String content;
    private String extension;
    
    public Date getLastModified() {
        return lastModified;
    }
    public void setLastModified( Date lastModified ) {
        this.lastModified = lastModified;
    }
    public String getContent() {
        return content;
    }
    public void setContent( String content ) {
        this.content = content;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension( String extension ) {
        this.extension = extension;
    }
   

}
