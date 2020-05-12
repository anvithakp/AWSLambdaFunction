package rekognition;

public class Request {


    private String fileName;


    public String getFileName() {
        return fileName;
    }

    public void setFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Request{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
