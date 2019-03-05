package model.exeptions;

public class XMLWorkException extends Exception {
    private String file;

    public String getFile() {
        return file;
    }

    public XMLWorkException(String message, String fileName) {
        super(message);
        file = fileName;
    }
}
