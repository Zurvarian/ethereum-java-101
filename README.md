# How to run
Run mvn spring-boot:run in your terminal

# How to see the api
Type http://localhost:8080/swagger-ui.html in your browser

# Web3j requirements 
Run geth: ./geth --rinkeby --datadir=$HOME/.rinkeby --rpc --rpcapi="personal,eth,network,web3,db,admin" --rpcaddr "127.0.0.1"

Or 

Obtain a token from https://infura.io (Currently not working as it returns invalid method...)

# Testing accounts
I've included couple of testing wallets (Rinkeby based).

To utilize them, use _PUT /wallet/unlock_ to load them into the in-memory map.

Password for _UTC--2018-01-23T17-12-38.825302485Z--c626f43b5e72ba6b819358ec29515545f522ed04_ is _hackmenow_

Password for _UTC--2018-01-25T15-38-56.617000000Z--da333dab7975708612e0adc271f88f78d8e35157_ is _12345_

Mind those are testing accounts in a Test network, i.e.: there is no point in stealing those funds :P

# Api Usage
## Transaction Send
1. Use _POST /wallet_ to register a new Wallet 
2. Use _POST /transaction_ to register a new Transaction.
3.  Sign the transaction in two steps:
    1. Sign the transaction using the signature endpoint, data required for signing is present in the POST/GET Transaction responses.
    2. Update the transaction with the signature, use _PUT /transaction/{transactionId}/signature_
4. Or use the endpoint to sign transaction with its wallet, _PUT /transaction/{transactionId}/sign-with-wallet_
5. Broadcast the transaction, use _PUT /transaction/{transactionId}/broadcast_
4. Check the logs to see if the transaction is confirmed by a block, or just do polling on _GET /transaction/{transactionId}_ until it contains blockHash and blockNumber attributes

## Wallet
1. Use _POST /wallet_ to register a new Wallet.

   Response contains the path where the wallet has been created, in case you want to copy it into your local geth keystore.
2. Use _GET /wallet/{walletId}_ to get details of a wallet.

## Wallet Unlock
1. Use _PUT /wallet/unlock_ to unlock an existing Wallet.
    
   Unlock allows you to load an existing wallet file from the file system, could be any wallet created by this tool or a wallet created with Geth.
2. Use _GET /wallet/{walletId}_ to get details of a wallet.

## Wallet with Mnemonics
1. Use _GET /wallet/mnemonic_ to obtain a list of mnemonic words.
2. Use _POST /wallet/mnemonic_ to create a Wallet using the mnemonic words.

   Response contains the path where the wallet has been created, in case you want to copy it into your local geth keystore.
3. Use _GET /wallet/{walletId}_ to get details of a wallet.

## Wallet's balance
1. Use _POST /wallet_ to register a new Wallet 
2. Use _GET /wallet/{walletId}/balance_ to see the wallet's balance in ETHER

## Blocks
* Use _GET /block/number/{number}_ to find block details of a given number  
* Use _GET /block/name/{name}_ to find block details of the block that matches with the alias name
   * _EARLIEST_: First block known by the node, either genesis block or the fist download block in case of light nodes.
   * _LATEST_: Latest block known by the node, eventually will be the latest one mined.
   * _PENDING_: Block being constructed by the network, it is a alias for the transaction mempool
   
# Wallet Contract
## How to compile the contract
1. Download [web3j compiler tool](https://github.com/web3j/web3j/releases)
2. Download [Wallet Contract](https://github.com/ethereum/dapp-bin/blob/master/wallet/wallet.sol)
3. Compile Wallet.sol with a solidity compiler, this should produce two files _Wallet.bin_ and _Wallet.abi_
   * Use an static compiler from [solidity github](https://github.com/ethereum/solidity/releases)
   
     e.g.: Using linux solidity compiler
     ```bash
     ./$SOLC_HOME/solc-static-linux wallet.sol --bin --abi --optimize -o wallet-output/
     ```
   * Use [Truffle](https://github.com/trufflesuite/truffle)
4. Generate the Contract java class from _Wallet.bin_ and _Wallet.abi_ using the Web3j compiler tool, output should be a class named Wallet.java 

   e.g: Using linux
   ```bash
   ./$WEB3J_HOME/bin/web3j solidity generate wallet-output/Wallet.bin wallet-output/Wallet.abi -o wallet-output/ -p ether.wallet
   ```
   
## How to use the Contract
Included in this project there is a test class with methods to deploy and destroy a wallet contract.
It is using a Rinkeby testing account, so please, be careful with it.

**Note**: The contract is not following one of the main recommendations when working with Contracts, it is sending the funds to the destination accounts directly when "execute" function is invoked.

This is causing a curious side effect, despite the funds are being transferred from the contract account to the destination account, no transaction is being created, so there is no trace of the movement, except for the logs in the contract.
