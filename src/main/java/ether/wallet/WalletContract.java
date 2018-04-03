package ether.wallet;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 * <p>
 * <p>Generated with web3j version 3.2.0.
 */
public class WalletContract extends Contract {
    private static final String BINARY =
            "6060604052341561000f57600080fd5b60405161138138038061138183398101604052808051820191906020018051919060200180519150819050838360008251600190810181553360016"
                    + "0a060020a031660038190556000908152610102602052604081209190915590505b82518110156100e75782818151811061008157fe5b90602001906020020151600160a060020a03166002"
                    + "82810161010081106100a457fe5b01556002810161010260008584815181106100bb57fe5b90602001906020020151600160a060020a0316815260208101919091526040016000205560010"
                    + "161006c565b506000555061010581905561010764010000000061110461011582021704565b610107555061011e92505050565b62015180420490565b6112548061012d6000396000f30060"
                    + "60604052600436106100fb5763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663173825d9811461014b5780632f54bf6e1461016a57806"
                    + "34123cb6b1461019d57806352375093146101c25780635c52c2f5146101d5578063659010e7146101e85780637065cb48146101fb578063746c91711461021a578063797af6271461022d57"
                    + "8063b20d30a914610243578063b61d27f614610259578063b75c7dc614610288578063ba51a6df1461029e578063c2cf7326146102b4578063c41a360a146102d6578063cbf0b0c01461030"
                    + "8578063f00d4b5d14610327578063f1736d861461034c575b6000341115610149577fe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c3334604051600160a0"
                    + "60020a03909216825260208201526040908101905180910390a15b005b341561015657600080fd5b610149600160a060020a036004351661035f565b341561017557600080fd5b610189600"
                    + "160a060020a036004351661044c565b604051901515815260200160405180910390f35b34156101a857600080fd5b6101b061046d565b60405190815260200160405180910390f35b341561"
                    + "01cd57600080fd5b6101b0610473565b34156101e057600080fd5b61014961047a565b34156101f357600080fd5b6101b06104b5565b341561020657600080fd5b610149600160a060020a0"
                    + "3600435166104bc565b341561022557600080fd5b6101b06105b5565b341561023857600080fd5b6101896004356105bb565b341561024e57600080fd5b6101496004356107eb565b341561"
                    + "026457600080fd5b6101b060048035600160a060020a0316906024803591604435918201910135610824565b341561029357600080fd5b610149600435610a28565b34156102a957600080f"
                    + "d5b610149600435610ad2565b34156102bf57600080fd5b610189600435600160a060020a0360243516610b57565b34156102e157600080fd5b6102ec600435610bac565b604051600160a0"
                    + "60020a03909116815260200160405180910390f35b341561031357600080fd5b610149600160a060020a0360043516610bc7565b341561033257600080fd5b610149600160a060020a03600"
                    + "43581169060243516610c05565b341561035757600080fd5b6101b0610d0b565b6000803660405180838380828437820191505092505050604051809103902061038781610d12565b151561"
                    + "039257600080fd5b600160a060020a0383166000908152610102602052604090205491508115156103ba57610447565b600180540360005411156103cd57610447565b60006002836101008"
                    + "1106103dd57fe5b0155600160a060020a03831660009081526101026020526040812055610401610e71565b610409610ef2565b7f58619076adf5bb0943d100ef88d52d7c3fd691b19d3a90"
                    + "71b555b651fbf418da83604051600160a060020a03909116815260200160405180910390a15b505050565b600160a060020a03811660009081526101026020526040812054115b919050565"
                    + "b60015481565b6101075481565b6000366040518083838082843782019150509250505060405180910390206104a181610d12565b15156104ac57600080fd5b50600061010655565b610106"
                    + "5481565b6000366040518083838082843782019150509250505060405180910390206104e381610d12565b15156104ee57600080fd5b6104f78261044c565b15610501576105b1565b61050"
                    + "9610e71565b60015460fa901061051c5761051c610ef2565b60015460fa901061052c576105b1565b60018054810190819055600160a060020a03831690600290610100811061054f57fe5b"
                    + "0155600154600160a060020a0383166000908152610102602052604090819020919091557f994a936646fe87ffe4f1e469d3d6aa417d6b855598397f323de5b449f765f0c39083905160016"
                    + "0a060020a03909116815260200160405180910390a15b5050565b60005481565b6000816105c781610d12565b15156105d257600080fd5b60008381526101086020526040902054600160a0"
                    + "60020a0316156107e557600083815261010860205260409081902080546001820154600160a060020a039091169290916002019051808280546001816001161561010002031660029004801"
                    + "561067d5780601f106106525761010080835404028352916020019161067d565b820191906000526020600020905b81548152906001019060200180831161066057829003601f168201915b"
                    + "505091505060006040518083038185876187965a03f19250505015156106a257600080fd5b6000838152610108602052604090819020600181015481547fe7c957c06e9a662c1a6c7736617"
                    + "9f5b702b97651dc28eee7d5bf1dff6e40bb4a933393889392600160a060020a03169160029091019051600160a060020a038087168252602082018690526040820185905283166060820152"
                    + "60a0608082018181528354600261010060018316150260001901909116049183018290529060c08301908490801561078d5780601f106107625761010080835404028352916020019161078"
                    + "d565b820191906000526020600020905b81548152906001019060200180831161077057829003601f168201915b5050965050505050505060405180910390a1600083815261010860205260"
                    + "408120805473ffffffffffffffffffffffffffffffffffffffff1916815560018101829055906107de600283018261110e565b5050600191505b50919050565b60003660405180838380828"
                    + "437820191505092505050604051809103902061081281610d12565b151561081d57600080fd5b5061010555565b600061082f3361044c565b151561083a57600080fd5b6108438461100b56"
                    + "5b15610902577f92ca3a80853e6663fa31fa10b99225f18d4902939b4c53a9caae9043f6efd0043385878686604051600160a060020a0380871682526020820186905284166040820152608"
                    + "06060820181815290820183905260a082018484808284378201915050965050505050505060405180910390a184600160a060020a0316848484604051808383808284378201915050925050"
                    + "5060006040518083038185876187965a03f19250505015156108fa57600080fd5b506000610a20565b600036436040518084848082843790910192835250506020019150604090505180910"
                    + "390209050610932816105bb565b158015610955575060008181526101086020526040902054600160a060020a0316155b15610a2057600081815261010860205260409020805473ffffffff"
                    + "ffffffffffffffffffffffffffffffff1916600160a060020a038716178155600181018590556109a4906002018484611152565b507f1733cbb53659d713b79580f79f3f9ff215f78a7c7aa"
                    + "45890f3b89fc5cddfbf32813386888787604051868152600160a060020a038087166020830152604082018690528416606082015260a06080820181815290820183905260c0820184848082"
                    + "8437820191505097505050505050505060405180910390a15b949350505050565b600160a060020a033316600090815261010260205260408120549080821515610a5057610acc565b50506"
                    + "000828152610103602052604081206001810154600284900a929083161115610acc578054600190810182558101805483900390557fc7fb647e59b18047309aa15aad418e5d7ca96d173ad7"
                    + "04f1031a2c3d7591734b3385604051600160a060020a03909216825260208201526040908101905180910390a15b50505050565b60003660405180838380828437820191505092505050604"
                    + "0518091039020610af981610d12565b1515610b0457600080fd5b600154821115610b13576105b1565b6000829055610b20610e71565b7facbdb084c721332ac59f9b8e392196c9eb0e4932"
                    + "862da8eb9beaf0dad4f550da8260405190815260200160405180910390a15050565b600082815261010360209081526040808320600160a060020a038516845261010290925282205482811"
                    + "515610b8f5760009350610ba3565b8160020a9050808360010154166000141593505b50505092915050565b60006002600183016101008110610bbf57fe5b015492915050565b6000366040"
                    + "51808383808284378201915050925050506040518091039020610bee81610d12565b1515610bf957600080fd5b81600160a060020a0316ff5b6000803660405180838380828437820191505"
                    + "0925050506040518091039020610c2d81610d12565b1515610c3857600080fd5b610c418361044c565b15610c4b57610acc565b600160a060020a0384166000908152610102602052604090"
                    + "20549150811515610c7357610acc565b610c7b610e71565b600160a060020a0383166002836101008110610c9357fe5b0155600160a060020a0380851660009081526101026020526040808"
                    + "22082905591851681528190208390557fb532073b38c83145e3e5135377a08bf9aab55bc0fd7c1179cd4fb995d2a5159c908590859051600160a060020a0392831681529116602082015260"
                    + "40908101905180910390a150505050565b6101055481565b600160a060020a033316600090815261010260205260408120548180821515610d3a57610e69565b60008581526101036020526"
                    + "040902080549092501515610d9957600080548355600180840191909155610104805491610d75919083016111d0565b6002830181905561010480548792908110610d8c57fe5b6000918252"
                    + "602090912001555b8260020a90508082600101541660001415610e69577fe1c52dc63b719ade82e8bea94cc41a0d5d28e4aaf536adb5e9cccc9ff8c1aeda3386604051600160a060020a039"
                    + "09216825260208201526040908101905180910390a1815460019011610e56576000858152610103602052604090206002015461010480549091908110610e1f57fe5b600091825260208083"
                    + "2090910182905586825261010390526040812081815560018082018390556002909101919091559350610e69565b8154600019018255600182018054821790555b505050919050565b61010"
                    + "45460005b81811015610eea57610108600061010483815481101515610e9557fe5b600091825260208083209091015483528201929092526040018120805473ffffffffffffffffffffffff"
                    + "ffffffffffffffff191681556001810182905590610ee0600283018261110e565b5050600101610e78565b6105b1611081565b60015b600154811015611008575b60015481108015610f1f5"
                    + "7506002816101008110610f1a57fe5b015415155b15610f2c57600101610f00565b60018054118015610f4d57506001546002906101008110610f4957fe5b0154155b15610f615760018054"
                    + "600019019055610f2c565b60015481108015610f8357506001546002906101008110610f7e57fe5b015415155b8015610f9c57506002816101008110610f9857fe5b0154155b15611003576"
                    + "001546002906101008110610fb257fe5b01546002826101008110610fc257fe5b01558061010260006002836101008110610fd857fe5b015481526020019081526020016000208190555060"
                    + "0060026001546101008110151561100057fe5b01555b610ef5565b50565b60006110163361044c565b151561102157600080fd5b6101075461102d611104565b11156110465760006101065"
                    + "5611041611104565b610107555b610106548281011080159061106357506101055482610106540111155b1561107957506101068054820190556001610468565b506000919050565b610104"
                    + "5460005b818110156110f75761010480548290811061109f57fe5b600091825260209091200154156110ef576101036000610104838154811015156110c557fe5b600091825260208083209"
                    + "09101548352820192909252604001812081815560018101829055600201555b600101611088565b6105b161010460006111f4565b6201518042045b90565b50805460018160011615610100"
                    + "020316600290046000825580601f106111345750611008565b601f016020900490600052602060002090810190611008919061120e565b82805460018160011615610100020316600290049"
                    + "0600052602060002090601f016020900481019282601f106111935782800160ff198235161785556111c0565b828001600101855582156111c0579182015b828111156111c0578235825591"
                    + "6020019190600101906111a5565b506111cc92915061120e565b5090565b8154818355818115116104475760008381526020902061044791810190830161120e565b5080546000825590600"
                    + "05260206000209081019061100891905b61110b91905b808211156111cc57600081556001016112145600a165627a7a72305820683265173b79e4deccfd717ff1eed5a1a76b85c3c79e183c"
                    + "b501a46c36cdc5040029";

    protected WalletContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected WalletContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<ConfirmationEventResponse> getConfirmationEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Confirmation",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bytes32>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ConfirmationEventResponse> responses = new ArrayList<ConfirmationEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ConfirmationEventResponse typedResponse = new ConfirmationEventResponse();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ConfirmationEventResponse> confirmationEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Confirmation",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bytes32>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ConfirmationEventResponse>() {
            @Override
            public ConfirmationEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ConfirmationEventResponse typedResponse = new ConfirmationEventResponse();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<RevokeEventResponse> getRevokeEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Revoke",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bytes32>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RevokeEventResponse> responses = new ArrayList<RevokeEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RevokeEventResponse typedResponse = new RevokeEventResponse();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RevokeEventResponse> revokeEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Revoke",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bytes32>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RevokeEventResponse>() {
            @Override
            public RevokeEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RevokeEventResponse typedResponse = new RevokeEventResponse();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<OwnerChangedEventResponse> getOwnerChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnerChanged",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnerChangedEventResponse> responses = new ArrayList<OwnerChangedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnerChangedEventResponse typedResponse = new OwnerChangedEventResponse();
            typedResponse.oldOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnerChangedEventResponse> ownerChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnerChanged",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnerChangedEventResponse>() {
            @Override
            public OwnerChangedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnerChangedEventResponse typedResponse = new OwnerChangedEventResponse();
                typedResponse.oldOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<OwnerAddedEventResponse> getOwnerAddedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnerAdded",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnerAddedEventResponse> responses = new ArrayList<OwnerAddedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnerAddedEventResponse typedResponse = new OwnerAddedEventResponse();
            typedResponse.newOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnerAddedEventResponse> ownerAddedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnerAdded",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnerAddedEventResponse>() {
            @Override
            public OwnerAddedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnerAddedEventResponse typedResponse = new OwnerAddedEventResponse();
                typedResponse.newOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<OwnerRemovedEventResponse> getOwnerRemovedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnerRemoved",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnerRemovedEventResponse> responses = new ArrayList<OwnerRemovedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnerRemovedEventResponse typedResponse = new OwnerRemovedEventResponse();
            typedResponse.oldOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnerRemovedEventResponse> ownerRemovedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnerRemoved",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnerRemovedEventResponse>() {
            @Override
            public OwnerRemovedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnerRemovedEventResponse typedResponse = new OwnerRemovedEventResponse();
                typedResponse.oldOwner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<RequirementChangedEventResponse> getRequirementChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("RequirementChanged",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RequirementChangedEventResponse> responses = new ArrayList<RequirementChangedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RequirementChangedEventResponse typedResponse = new RequirementChangedEventResponse();
            typedResponse.newRequirement = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RequirementChangedEventResponse> requirementChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("RequirementChanged",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RequirementChangedEventResponse>() {
            @Override
            public RequirementChangedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RequirementChangedEventResponse typedResponse = new RequirementChangedEventResponse();
                typedResponse.newRequirement = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<DepositEventResponse> getDepositEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Deposit",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<DepositEventResponse> responses = new ArrayList<DepositEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            DepositEventResponse typedResponse = new DepositEventResponse();
            typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DepositEventResponse> depositEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Deposit",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, DepositEventResponse>() {
            @Override
            public DepositEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                DepositEventResponse typedResponse = new DepositEventResponse();
                typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<SingleTransactEventResponse> getSingleTransactEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("SingleTransact",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<SingleTransactEventResponse> responses = new ArrayList<SingleTransactEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            SingleTransactEventResponse typedResponse = new SingleTransactEventResponse();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SingleTransactEventResponse> singleTransactEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("SingleTransact",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SingleTransactEventResponse>() {
            @Override
            public SingleTransactEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                SingleTransactEventResponse typedResponse = new SingleTransactEventResponse();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public List<MultiTransactEventResponse> getMultiTransactEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MultiTransact",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bytes32>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<MultiTransactEventResponse> responses = new ArrayList<MultiTransactEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            MultiTransactEventResponse typedResponse = new MultiTransactEventResponse();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MultiTransactEventResponse> multiTransactEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MultiTransact",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bytes32>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MultiTransactEventResponse>() {
            @Override
            public MultiTransactEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                MultiTransactEventResponse typedResponse = new MultiTransactEventResponse();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public List<ConfirmationNeededEventResponse> getConfirmationNeededEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ConfirmationNeeded",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ConfirmationNeededEventResponse> responses = new ArrayList<ConfirmationNeededEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ConfirmationNeededEventResponse typedResponse = new ConfirmationNeededEventResponse();
            typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.initiator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ConfirmationNeededEventResponse> confirmationNeededEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ConfirmationNeeded",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ConfirmationNeededEventResponse>() {
            @Override
            public ConfirmationNeededEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ConfirmationNeededEventResponse typedResponse = new ConfirmationNeededEventResponse();
                typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.initiator = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> removeOwner(String _owner) {
        Function function = new Function(
                "removeOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> isOwner(String _addr) {
        Function function = new Function(
                "isOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> m_numOwners() {
        Function function = new Function("m_numOwners",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> m_lastDay() {
        Function function = new Function("m_lastDay",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> resetSpentToday() {
        Function function = new Function(
                "resetSpentToday",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> m_spentToday() {
        Function function = new Function("m_spentToday",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> addOwner(String _owner) {
        Function function = new Function(
                "addOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> m_required() {
        Function function = new Function("m_required",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> confirm(byte[] _h) {
        Function function = new Function(
                "confirm",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_h)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setDailyLimit(BigInteger _newLimit) {
        Function function = new Function(
                "setDailyLimit",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_newLimit)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> execute(String _to, BigInteger _value, byte[] _data) {
        Function function = new Function(
                "execute",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value),
                        new org.web3j.abi.datatypes.DynamicBytes(_data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> revoke(byte[] _operation) {
        Function function = new Function(
                "revoke",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_operation)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeRequirement(BigInteger _newRequired) {
        Function function = new Function(
                "changeRequirement",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_newRequired)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> hasConfirmed(byte[] _operation, String _owner) {
        Function function = new Function("hasConfirmed",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_operation),
                        new org.web3j.abi.datatypes.Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getOwner(BigInteger ownerIndex) {
        Function function = new Function("getOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(ownerIndex)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> kill(String _to) {
        Function function = new Function(
                "kill",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeOwner(String _from, String _to) {
        Function function = new Function(
                "changeOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from),
                        new org.web3j.abi.datatypes.Address(_to)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> m_dailyLimit() {
        Function function = new Function("m_dailyLimit",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<WalletContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, List<String> _owners, BigInteger _required, BigInteger _daylimit) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_owners, org.web3j.abi.datatypes.Address.class)),
                new org.web3j.abi.datatypes.generated.Uint256(_required),
                new org.web3j.abi.datatypes.generated.Uint256(_daylimit)));
        return deployRemoteCall(WalletContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<WalletContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, List<String> _owners, BigInteger _required, BigInteger _daylimit) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_owners, org.web3j.abi.datatypes.Address.class)),
                new org.web3j.abi.datatypes.generated.Uint256(_required),
                new org.web3j.abi.datatypes.generated.Uint256(_daylimit)));
        return deployRemoteCall(WalletContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static WalletContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new WalletContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static WalletContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new WalletContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ConfirmationEventResponse {
        public String owner;

        public byte[] operation;
    }

    public static class RevokeEventResponse {
        public String owner;

        public byte[] operation;
    }

    public static class OwnerChangedEventResponse {
        public String oldOwner;

        public String newOwner;
    }

    public static class OwnerAddedEventResponse {
        public String newOwner;
    }

    public static class OwnerRemovedEventResponse {
        public String oldOwner;
    }

    public static class RequirementChangedEventResponse {
        public BigInteger newRequirement;
    }

    public static class DepositEventResponse {
        public String _from;

        public BigInteger value;
    }

    public static class SingleTransactEventResponse {
        public String owner;

        public BigInteger value;

        public String to;

        public byte[] data;
    }

    public static class MultiTransactEventResponse {
        public String owner;

        public byte[] operation;

        public BigInteger value;

        public String to;

        public byte[] data;
    }

    public static class ConfirmationNeededEventResponse {
        public byte[] operation;

        public String initiator;

        public BigInteger value;

        public String to;

        public byte[] data;
    }
}
