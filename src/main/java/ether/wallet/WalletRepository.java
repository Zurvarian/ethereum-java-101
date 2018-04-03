package ether.wallet;

import ether.wallet.model.Wallet;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WalletRepository {

    Map<UUID, Wallet> walletById = new ConcurrentHashMap<>();

    public Option<Wallet> findWalletById(UUID walletId) {
        return Option.of(walletById.get(walletId));
    }

    public Option<Wallet> findWalletByAddress(String address) {
        return List.ofAll(walletById.values()).filter(wallet -> wallet.getCredentials().getAddress().equals(address)).headOption();
    }

    void save(Wallet wallet) {
        walletById.put(wallet.getWalletId(), wallet);
    }
}
