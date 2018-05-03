package cn.wow.common.utils.cookie;



public class KeyProvider {
    public static final KeyProvider INSTANCE = new KeyProvider();
    private KeyProvider() {}
    
    public String getTokenKey() {
        return "roBu$SofT　mDesk";
    }
    
    public String getVerifyCodeKey() {
        return "RoBu$SofT　mDEsk";
    }
    
    public String getRandomPassKey() {
    	return "robu$SofT　mDEsk";
    }
}
