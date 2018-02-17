package stip.stipperscheckin.messages;

public class ApiKeyChangedMessage {
    private String apiKey;

    public ApiKeyChangedMessage(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
