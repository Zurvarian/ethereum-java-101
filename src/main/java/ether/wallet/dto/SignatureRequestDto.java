package ether.wallet.dto;

import lombok.Value;

import java.math.BigInteger;

@Value
public class SignatureRequestDto {

    String to;

    BigInteger gas;

    BigInteger gasPrice;

    BigInteger value;

    String input;

    BigInteger nonce;

}
