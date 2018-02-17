package stip.stipperscheckin.services;

public interface ISettingsService {
    public String getApiKey();
    public void setApiKey(String apiKey);
    public boolean getTorchOn();
    public void setTorchOn(boolean torchOn);
    public int getDisplayOrrientation();
    public void setDisplayOrrientation(int displayOrrientation);
}
