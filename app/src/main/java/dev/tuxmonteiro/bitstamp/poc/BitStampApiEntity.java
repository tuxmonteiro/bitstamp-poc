package dev.tuxmonteiro.bitstamp.poc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public enum BitStampApiEntity {

    // public
    Currencies("currencies", HttpMethod.GET, ContentType.JSON),
    Ticker("ticker", HttpMethod.GET, ContentType.JSON),
    MarketTicker("ticker/%s", HttpMethod.GET, ContentType.JSON),
    TickerHour("ticker_hour/%s", HttpMethod.GET, ContentType.JSON),
    OrderBook("order_book/%s", HttpMethod.GET, ContentType.JSON),
    Transactions("transactions/%s", HttpMethod.GET, ContentType.JSON),
    TravelRule("travel_rule/vasp", HttpMethod.GET, ContentType.JSON),
    EurUsdConversionRate("eur_usd", HttpMethod.GET, ContentType.JSON),
    Markets("markets", HttpMethod.GET, ContentType.JSON),
    OHLC("ohlc/%s", HttpMethod.GET, ContentType.JSON),

    // private
    AccountBalances("account_balances", HttpMethod.POST, ContentType.FORM_URLENCODED),
    AccountBalancesForCurrency("account_balances/%s", HttpMethod.POST, ContentType.JSON),
    TradingFees("fees/trading", HttpMethod.POST, ContentType.JSON),
    TradingFeesForMarket("fees/trading/%s", HttpMethod.POST, ContentType.JSON),
    WithdrawalFees("fees/withdrawal", HttpMethod.POST, ContentType.JSON),
    WithdrawalFeesForCurrency("fees/withdrawal/%s", HttpMethod.POST, ContentType.JSON),
    BuyInstantOrder("buy/instant/%s", HttpMethod.POST, ContentType.JSON),
    BuyMarketOrder("buy/market/%s", HttpMethod.POST, ContentType.JSON),
    BuyLimitOrder("buy/%s", HttpMethod.POST, ContentType.JSON),    
    CancelAllOrders("cancel_all_orders", HttpMethod.POST, ContentType.JSON),
    CancelAllOrdersForMarket("cancel_all_orders/%s", HttpMethod.POST, ContentType.JSON),
    CancelOrder("cancel_order", HttpMethod.POST, ContentType.JSON),
    MyMarkets("my_markets", HttpMethod.GET, ContentType.JSON),
    OpenOrders("open_orders", HttpMethod.POST, ContentType.JSON),
    OpenOrdersForMarket("open_orders/%s", HttpMethod.POST, ContentType.JSON),
    OrderStatus("order_status", HttpMethod.POST, ContentType.JSON),
    SellInstantOrder("sell/instant/%s", HttpMethod.POST, ContentType.JSON),
    SellMarketOrder("sell/market/%s", HttpMethod.POST, ContentType.JSON),
    SellLimitOrder("sell/%s", HttpMethod.POST, ContentType.JSON),
    
    WebSocketsToken("websockets_token", HttpMethod.POST, ContentType.NULL);

    private final String value;
    private final HttpMethod method;
    private final ContentType contentType;

    private BitStampApiEntity(String value, HttpMethod method, ContentType contentType) {
        this.value = value;
        this.method = method;
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return this.value;    
    }

    // public String format(String... args) {
    //     return String.format(this.value, args);
    // }

    public HttpMethod getHttpMethod() {
        return this.method;
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public String getUrlPath() {
        return String.format("%s/%s/%s/", "/api", DefaultString.BITSTAMP_VERSION.toString(), this.value);
    }

    public String getUrlHost() {
        return String.format("www.%s", DefaultString.BITSTAMP_DOMAIN.toString());
    }

    public String execute(BitstampAuthentication authentication) 
        throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        return this.execute(authentication, "");
    }

    public String execute(BitstampAuthentication authentication, String payloadString) 
        throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        return this.execute(authentication, payloadString, "");
    }

    public String execute(BitstampAuthentication authentication, String payloadString, String urlQuery) 
        throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        return this.execute(HttpClient.newHttpClient(), authentication, payloadString, urlQuery);
    }

    public String execute(HttpClient httpClient, BitstampAuthentication authentication, String payloadString, String urlQuery) 
        throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        return new Requester().setClient(httpClient).execute(authentication, this, payloadString, urlQuery);
    }
}
