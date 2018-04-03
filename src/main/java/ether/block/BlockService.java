package ether.block;

import ether.block.model.BlockNames;
import ether.ethereum.EthereumClient;
import ether.ethereum.model.EthBlock;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BlockService {

    EthereumClient ethereumClient;

    EthBlock findBlockByNumber(BigInteger number) {
        return ethereumClient.findBlockByParameter(DefaultBlockParameter.valueOf(number));
    }

    EthBlock findBlockByName(BlockNames name) {
        return ethereumClient.findBlockByParameter(DefaultBlockParameter.valueOf(name.toString()));
    }

}
