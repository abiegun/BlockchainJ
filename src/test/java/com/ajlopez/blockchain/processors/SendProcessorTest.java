package com.ajlopez.blockchain.processors;

import com.ajlopez.blockchain.net.messages.MessageEncoder;
import com.ajlopez.blockchain.net.peers.Peer;
import com.ajlopez.blockchain.net.Status;
import com.ajlopez.blockchain.net.messages.Message;
import com.ajlopez.blockchain.net.messages.StatusMessage;
import com.ajlopez.blockchain.test.simples.SimpleMessageChannel;
import com.ajlopez.blockchain.test.utils.FactoryHelper;
import com.ajlopez.blockchain.utils.HashUtilsTest;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Created by ajlopez on 06/04/2018.
 */
public class SendProcessorTest {
    @Test
    public void connectAndDisconnectPeer() {
        SendProcessor processor = new SendProcessor(FactoryHelper.createPeer());
        Peer peer = FactoryHelper.createPeer();
        SimpleMessageChannel channel = new SimpleMessageChannel();

        processor.connectToPeer(peer, channel);

        Assert.assertTrue(processor.isConnected(peer));

        processor.disconnectFromPeer(peer);

        Assert.assertFalse(processor.isConnected(peer));
    }

    @Test
    public void postMessageToNotConnectedPeer() {
        SendProcessor processor = new SendProcessor(FactoryHelper.createPeer());
        Peer peer = FactoryHelper.createPeer();
        SimpleMessageChannel channel = new SimpleMessageChannel();

        Message message = new StatusMessage(new Status(HashUtilsTest.generateRandomPeerId(), 1, 10));
        Assert.assertFalse(processor.postMessage(peer, message));
        Assert.assertTrue(channel.getPeerMessages().isEmpty());
    }

    @Test
    public void postMessageWhenNoPeerIsConnected() {
        SendProcessor processor = new SendProcessor(FactoryHelper.createPeer());

        Message message = new StatusMessage(new Status(HashUtilsTest.generateRandomPeerId(), 1, 10));
        Assert.assertEquals(0, processor.postMessage(message));
    }

    @Test
    public void connectToPeerAndPostMessage() {
        Peer sender = FactoryHelper.createPeer();
        SendProcessor processor = new SendProcessor(sender);
        Peer peer = FactoryHelper.createPeer();
        SimpleMessageChannel channel = new SimpleMessageChannel();

        processor.connectToPeer(peer, channel);

        Message message = new StatusMessage(new Status(HashUtilsTest.generateRandomPeerId(), 1, 10));
        Assert.assertTrue(processor.postMessage(peer, message));

        MessageProcessorTest.expectedMessage(channel, sender, message);
    }

    @Test
    public void connectToPeersAndPostMessage() {
        Peer sender = FactoryHelper.createPeer();
        SendProcessor processor = new SendProcessor(sender);
        Peer peer1 = FactoryHelper.createPeer();
        SimpleMessageChannel channel1 = new SimpleMessageChannel();
        Peer peer2 = FactoryHelper.createPeer();
        SimpleMessageChannel channel2 = new SimpleMessageChannel();

        processor.connectToPeer(peer1, channel1);
        processor.connectToPeer(peer2, channel2);

        Message message = new StatusMessage(new Status(HashUtilsTest.generateRandomPeerId(), 1, 10));

        Assert.assertEquals(2, processor.postMessage(message));

        MessageProcessorTest.expectedMessage(channel1, sender, message);
        MessageProcessorTest.expectedMessage(channel2, sender, message);
    }

    @Test
    public void connectToPeersAndPostMessageSkippingOne() {
        Peer sender = FactoryHelper.createPeer();
        SendProcessor processor = new SendProcessor(sender);
        Peer peer1 = FactoryHelper.createPeer();
        SimpleMessageChannel channel1 = new SimpleMessageChannel();
        Peer peer2 = FactoryHelper.createPeer();
        SimpleMessageChannel channel2 = new SimpleMessageChannel();

        processor.connectToPeer(peer1, channel1);
        processor.connectToPeer(peer2, channel2);

        Message message = new StatusMessage(new Status(HashUtilsTest.generateRandomPeerId(), 1, 10));

        Assert.assertEquals(1, processor.postMessage(message, Collections.singletonList(peer2.getId())));

        MessageProcessorTest.expectedMessage(channel1, sender, message);
        Assert.assertTrue(channel2.getPeerMessages().isEmpty());
    }

    @Test
    public void connectToPeerAndPostMessageToAnotherPeer() {
        Peer sender = FactoryHelper.createPeer();
        SendProcessor processor = new SendProcessor(sender);
        Peer peer = FactoryHelper.createPeer();
        Peer peer2 = FactoryHelper.createPeer();
        SimpleMessageChannel channel = new SimpleMessageChannel();

        processor.connectToPeer(peer, channel);

        Message message = new StatusMessage(new Status(HashUtilsTest.generateRandomPeerId(), 1, 10));
        Assert.assertFalse(processor.postMessage(peer2, message));
        Assert.assertTrue(channel.getPeerMessages().isEmpty());
    }
}