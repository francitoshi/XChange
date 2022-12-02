package info.bitrich.xchangestream.binance;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BinanceFuturesPublicStreamsTest {

    StreamingExchange exchange;
    Instrument instrument = new FuturesContract("BTC/USDT/PERP");

    @Before
    public void setup(){
        exchange = StreamingExchangeFactory.INSTANCE.createExchange(BinanceFutureStreamingExchange.class);
        exchange.connect(ProductSubscription.create().addOrderbook(instrument).addTicker(instrument).addTrades(instrument).build()).blockingAwait();
    }
    @Test
    public void checkOrderBookStream() throws InterruptedException {

        Disposable dis = exchange.getStreamingMarketDataService().getOrderBook(instrument).subscribe(orderBook -> {
            System.out.println(orderBook.toString());
            assertThat(orderBook.getBids().get(0).getLimitPrice()).isLessThan(orderBook.getAsks().get(0).getLimitPrice());
            assertThat(orderBook.getBids().get(0).getInstrument()).isEqualTo(new FuturesContract("BTC/USDT/PERP"));
        });

        TimeUnit.SECONDS.sleep(3);
        dis.dispose();
    }

    @Test
    public void checkTickerStream() throws InterruptedException {

        Disposable dis = exchange.getStreamingMarketDataService().getTicker(instrument).subscribe(orderBook -> {
            System.out.println(orderBook.toString());
            assertThat(orderBook.getInstrument()).isEqualTo(new FuturesContract("BTC/USDT/PERP"));
        });

        TimeUnit.SECONDS.sleep(3);
        dis.dispose();
    }

    @Test
    public void checkTradesStream() throws InterruptedException {

        Disposable dis = exchange.getStreamingMarketDataService().getTrades(instrument).subscribe(orderBook -> {
            System.out.println(orderBook.toString());
            assertThat(orderBook.getInstrument()).isEqualTo(new FuturesContract("BTC/USDT/PERP"));
        });

        TimeUnit.SECONDS.sleep(3);
        dis.dispose();
    }
}
