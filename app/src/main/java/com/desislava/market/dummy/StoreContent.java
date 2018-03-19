package com.desislava.market.dummy;


import com.desislava.market.beans.Store;
import com.desislava.market.server.communication.ParseServerResponse;

import java.util.ArrayList;
import java.util.List;

public class StoreContent {

    ParseServerResponse serverResponse;
    public static List<Store> stores = new ArrayList<>();

    public StoreContent(ParseServerResponse serverResponse) {
        this.serverResponse = serverResponse;
        stores = serverResponse.getStoreList();
    }


}
