package com.ajlopez.blockchain.net.peers;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ajlopez on 19/11/2018.
 */
public class TcpPeerClient {
    private final PeerNode peerNode;
    private final String host;
    private final int port;

    public TcpPeerClient(String host, int port, PeerNode peerNode) {
        this.host = host;
        this.port = port;
        this.peerNode = peerNode;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(this.host, this.port);
        Peer peer = Peer.createRandomPeer();

        PeerConnection peerConnection = new PeerConnection(peer, socket.getInputStream(), socket.getOutputStream(), this.peerNode);
        this.peerNode.connectTo(peerConnection);
        peerConnection.start();
    }
}
