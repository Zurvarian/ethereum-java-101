package ether.block;

import ether.block.dto.BlockDto;
import ether.block.model.BlockNames;
import ether.ethereum.model.EthBlock;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping(path = "/block", produces = APPLICATION_JSON_VALUE)
public class BlockResource {

    BlockService blockService;

    @GetMapping(path = "/number/{number}")
    public BlockDto findBlockByNumber(@PathVariable("number") BigInteger number) {
        return ethBlockToDto(blockService.findBlockByNumber(number));
    }

    @GetMapping(path = "/name/{name}")
    public BlockDto findBlockByName(@PathVariable("name") BlockNames name) {
        return ethBlockToDto(blockService.findBlockByName(name));
    }

    private BlockDto ethBlockToDto(EthBlock ethBlock) {
        return new BlockDto(
                ethBlock.getNumber(),
                ethBlock.getHash(),
                ethBlock.getParentHash(),
                ethBlock.getNonce(),
                ethBlock.getSha3Uncles(),
                ethBlock.getLogsBloom(),
                ethBlock.getTransactionsRoot(),
                ethBlock.getStateRoot(),
                ethBlock.getReceiptsRoot(),
                ethBlock.getAuthor(),
                ethBlock.getMiner(),
                ethBlock.getMixHash(),
                ethBlock.getDifficulty(),
                ethBlock.getTotalDifficulty(),
                ethBlock.getExtraData(),
                ethBlock.getSize(),
                ethBlock.getGasLimit(),
                ethBlock.getGasUsed(),
                ethBlock.getTimestamp(),
                ethBlock.getTransactionHashes(),
                ethBlock.getUncles(),
                ethBlock.getSealFields()
        );
    }
}
