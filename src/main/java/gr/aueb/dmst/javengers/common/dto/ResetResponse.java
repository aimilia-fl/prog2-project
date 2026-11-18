package gr.aueb.dmst.javengers.common.dto;

public class ResetResponse {
    private String status;
    private String message;

    public ResetResponse() {}

    public ResetResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
