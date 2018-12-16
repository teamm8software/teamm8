package DropBoxClass;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class DropBoxClass {

    private static final String ACCESS_TOKEN = "AazcItZCN9AAAAAAAAAADOKNdq2tRPKm-oKQzUiY_-C2uux0gpyJdsRiizMvjgxw";

    public DbxClientV2 clientV2(){
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/teamm-software").build();
        return new DbxClientV2(config, ACCESS_TOKEN);
    }
}
