package io.extact.msa.rms.console;

import static io.extact.msa.rms.console.ui.ClientConstants.*;

import jakarta.enterprise.inject.spi.CDI;

import io.extact.msa.rms.console.ui.ScreenController;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;
import io.extact.msa.rms.platform.fw.webapi.BootstrapWebApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleMain {

    public static void main(String[] args) throws Exception {
        // Fiddlerの設定
        // System.setProperty("http.proxyHost", "localhost");
        // System.setProperty("http.proxyPort", "8888");
        // System.setProperty("web-api/mp-rest/url", "http://pmr216n.primo.mamezou.com:7001");
        // System.err.println("★Fiddlerの設定が入ってるのでFiddler立ち上げてね！");
        // System.err.println("★あと宛先アドレスはlocalhostではなくFQCN指定の方に変えてね！");
        try {
            BootstrapWebApi.start(args);
            startupLogo();
            ScreenController controller = CDI.current().select(ScreenController.class).get();
            while (true) {
                try {
                    controller.start();
                    break;
                } catch (Exception e) {
                    log.error("Back to start..", e);
                    TextIoUtils.printErrorInformation(UNKNOWN_ERROR_INFORMATION);
                }
            }
            // swingコンソールはmainプロセスが残るためexitする
            System.exit(0);
        } catch (Throwable e) {
            log.error("error occured.", e);
            throw e;
        }
    }

    private static final String START_UP_LOGO ="""
            ____    __  ___  _____
           / __ \\  /  |/  / / ___/
          / /_/ / / /|_/ /  \\__ \\
         / _, _/ / /  / /_ ___/ /
        /_/ |_(_)_/  /_/(_)____(_)
        """;

    private static void startupLogo() {
        TextIoUtils.println(START_UP_LOGO);
    }
}
