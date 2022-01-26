package model;

public class AccessToken {
	
	
    public AccessToken(String accessToken, Integer expiresIn, Integer refreshExpiresIn, String refreshToken,
			String tokenType, String idToken, Integer notBeforePolicy, String sessionState, String scope) {
		super();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshExpiresIn = refreshExpiresIn;
		this.refreshToken = refreshToken;
		this.tokenType = tokenType;
		this.idToken = idToken;
		this.notBeforePolicy = notBeforePolicy;
		this.sessionState = sessionState;
		this.scope = scope;
	}
	String accessToken;
    Integer expiresIn;
    Integer refreshExpiresIn;
    String refreshToken;
    String tokenType;
    String idToken;
    Integer notBeforePolicy;
    String sessionState;
    String scope;
    
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Integer getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}
	public Integer getRefreshExpiresIn() {
		return refreshExpiresIn;
	}
	public void setRefreshExpiresIn(Integer refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getIdToken() {
		return idToken;
	}
	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
	public Integer getNotBeforePolicy() {
		return notBeforePolicy;
	}
	public void setNotBeforePolicy(Integer notBeforePolicy) {
		this.notBeforePolicy = notBeforePolicy;
	}
	public String getSessionState() {
		return sessionState;
	}
	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
    
    
}
