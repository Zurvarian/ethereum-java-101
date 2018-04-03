package ether.ethereum;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
@AllArgsConstructor
public class EthereumConfig {

    Environment environment;

    @Bean
    public Web3j localWeb3j() {
        return Web3j.build(new HttpService(environment.getRequiredProperty("ethereum.host")));
    }

}
