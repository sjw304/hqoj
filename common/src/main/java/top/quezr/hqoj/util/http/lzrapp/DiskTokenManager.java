package top.quezr.hqoj.util.http.lzrapp;

public class DiskTokenManager implements TokenManager {

    private String accessToken;

    private String refreshToken;

    @Override
    public String loadAccessToken() {
        return accessToken;
    }

    @Override
    public void saveAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String loadRefreshToken() {
        return refreshToken;
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
