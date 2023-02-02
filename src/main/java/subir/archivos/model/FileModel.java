package subir.archivos.model;

public class FileModel {

    private String name;
    private String url;
   

    public FileModel() {
    }

    public FileModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

   
    
}
