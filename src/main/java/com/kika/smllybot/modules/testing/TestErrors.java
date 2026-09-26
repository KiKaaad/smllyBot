package com.kika.smllybot.modules.testing;

import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.other.BaseCmd;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class TestErrors extends BaseCmd {

    public TestErrors() {
        super(Set.of("debug", "дебаг"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        String[] arg = raw.trim().split("\\s+");

        if (arg.length < 3) return null;
        String argument = arg[2];

        switch (argument.toLowerCase()) {
            case "noaccesspermission" -> ErrorThrow.noAccessPermission(event, Permission.BYPASS_SLOWMODE);
            case "usernotfound" -> ErrorThrow.userNotFound(event, "kef123");
            case "timeoverhead" -> ErrorThrow.timeoutOverhead(event);
            case "onlyonstaging" -> ErrorThrow.onlyOnStaging(event);
            case "undefined" -> ErrorThrow.undefined(event, """
                    [13:42:07] [JDA MainWS-ReadThread | ERROR]: One of the EventListeners had an uncaught exception
                    java.lang.IllegalArgumentException: URL may not be null
                            at net.dv8tion.jda.internal.utils.Checks.notNull(Checks.java:89)
                            at net.dv8tion.jda.internal.utils.Checks.notBlank(Checks.java:103)
                            at net.dv8tion.jda.api.components.thumbnail.Thumbnail.fromUrl(Thumbnail.java:96)
                            at com.kika.smllybot.modules.statistic.ui.StatisticUI.buildStatistic(StatisticUI.java:62)
                            at com.kika.smllybot.modules.statistic.Statistic.execute(Statistic.java:40)
                            at com.kika.smllybot.Manager.onMessageReceived(Manager.java:153)
                            at net.dv8tion.jda.api.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:699)
                            at net.dv8tion.jda.api.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:89)
                            at net.dv8tion.jda.internal.hooks.EventManagerProxy.handleInternally(EventManagerProxy.java:76)
                            at net.dv8tion.jda.internal.hooks.EventManagerProxy.handle(EventManagerProxy.java:63)
                            at net.dv8tion.jda.internal.JDAImpl.handleEvent(JDAImpl.java:183)
                            at net.dv8tion.jda.internal.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:126)
                            at net.dv8tion.jda.internal.handle.SocketHandler.handle(SocketHandler.java:39)
                            at net.dv8tion.jda.internal.requests.WebSocketClient.onDispatch(WebSocketClient.java:936)
                            at net.dv8tion.jda.internal.requests.WebSocketClient.onEvent(WebSocketClient.java:826)
                            at net.dv8tion.jda.internal.requests.WebSocketClient.handleEvent(WebSocketClient.java:809)
                            at net.dv8tion.jda.internal.requests.WebSocketClient.onBinaryMessage(WebSocketClient.java:980)
                            at com.neovisionaries.ws.client.ListenerManager.callOnBinaryMessage(ListenerManager.java:385)
                            at com.neovisionaries.ws.client.ReadingThread.callOnBinaryMessage(ReadingThread.java:276)
                            at com.neovisionaries.ws.client.ReadingThread.handleBinaryFrame(ReadingThread.java:996)
                            at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:755)
                            at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:108)
                            at com.neovisionaries.ws.client.ReadingThread.runMain(ReadingThread.java:64)
                            at com.neovisionaries.ws.client.WebSocketThread.run(WebSocketThread.java:45)
                    """);
        }

        return null;
    }

}
