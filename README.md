# BlockChainJ

Simple blockchain implementation, in Java, WIP

## Description

This blockchain implementation has:

- Block: with number, parent block and list of transactions
- Transaction: send value from sender to receiver account
- Account: with balance and optional smart contract code
- Virtual Machine: to execute smart contracts

The world state keeps the account states. Each account has a balance and smart contract storage.

## References

- [SpongyCastle](https://rtyley.github.io/spongycastle)
- [BouncyCastle Java](http://www.bouncycastle.org/java.html)
- [RLP](https://github.com/ethereum/wiki/wiki/RLP)
- [RLP Tests](https://github.com/ethereum/tests/blob/develop/RLPTests/rlptest.json)
- [How to Build Your Own Blockchain Part 3 — Writing Nodes that Mine and Talk](https://bigishdata.com/2017/11/02/build-your-own-blockchain-part-3-writing-nodes-that-mine/)
- [Create a simple HTTP Web Server in Java](https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd)
- [Function Pointers in Java](https://programming.guide/java/function-pointers-in-java.html)
- [A Simple Java TCP Server and TCP Client](https://systembash.com/a-simple-java-tcp-server-and-tcp-client/)
- [BlockBench: A Framework for Analyzing Private Blockchains](https://github.com/ooibc88/blockbench)
- [Pantheon: An enterprise-grade Java-based, Apache 2.0 licensed Ethereum client](https://github.com/PegaSysEng/pantheon)
- [Add timer metrics for EVM operations #551](https://github.com/PegaSysEng/pantheon/pull/551)
- [Do a Simple HTTP Request in Java](https://www.baeldung.com/java-http-request)
- [Model 0: low-level defence measures for Sybil attacks in P2P networks](https://blog.golemproject.net/model-0-low-level-defence-measures-for-sybil-attacks-in-p2p-networks/)
- [Introducing Hobbits: A lightweight wire protocol for ETH 2.0](https://medium.com/whiteblock/introducing-hobbits-a-lightweight-wire-protocol-for-eth-2-0-b1bfae5e4843)
- [Ethereum 2.0’s Nodes Need to Talk – A Solution Is ‘Hobbits’](https://www.coindesk.com/testing-ethereum-2-0-requires-basic-signaling-a-solution-is-hobbits)
- [Wisps: The Magical World of Create2](https://blog.ricmoo.com/wisps-the-magical-world-of-create2-5c2177027604)
- [Quantifying Immutability](https://medium.com/ethereum-classic/quantifying-immutability-e8f2b1bb9301)
- [Empirically Analyzing Ethereum’s Gas Mechanism](https://arxiv.org/pdf/1905.00553.pdf)
- [Blockchain Scalability: Do Layer I Solutions Hold the Key?](https://hackernoon.com/blockchain-scalability-do-layer-i-solutions-hold-the-key-f3d9388c60f3)
- [Overview of Layer 2 approaches: Plasma, State Channels, Side Chains, Roll Ups](https://nearprotocol.com/blog/layer-2/)
- [Blockchain Consensus: The Past, Present, and Future](https://hackernoon.com/blockchain-consensus-the-past-present-and-future-112cd1a4189a)
- [Warp Sync](https://wiki.parity.io/Warp-Sync)
- [What is a light client and why you should care?](https://www.parity.io/what-is-a-light-client/)

## To Do

- Coin type
- Signed transactions
- DataWord or Coin in Transaction value, instead of BigInteger
- DataWord or Coin in Account balance, instead of BigInteger
- Block JSON serialization
- Block transaction hash should be the hash of a trie
- Difficulty in block
- Total difficulty in block JSON serialization
- Numeric values in hexadecimal in JSON serialization
- Transaction receipts
- Log emit
- Create contracts
- Call contracts in Virtual Machine

## License

MIT

