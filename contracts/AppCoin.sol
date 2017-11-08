// AppCoins contract with share splitting among different wallets
// Not fully ERC20 compliant due to tests purposes

pragma solidity ^0.4.8;
contract ERC20Interface {
    function totalSupply() constant returns (uint256 supply);
    function balanceOf(address _owner) constant returns (uint256 balance);
    function transfer(address _to, uint256 _value) returns (bool success);
    event Transfer(address indexed _from, address indexed _to, uint256 _value);
}


contract AppCoinDiv is ERC20Interface {
    string public constant symbol = "APPC";
    string public constant name = "AppCoins";
    uint256 public constant decimals = 2;
    uint256 public _totalSupply = 1000000 * 10 ** decimals;
    address public owner;
    address public oem = 0x0000000000000000000000000000000000000000; // Hardcoded address for division
    address public store = 0x0000000000000000000000000000000000000000; // Hardcoded address for division
    mapping(address => uint256) balances;
    mapping(address => mapping (address => uint256)) allowed;

    modifier onlyOwner () {
        if (msg.sender != owner) {
            assert(false);
        }
        _;
    }

    function AppCoinDiv () {
        owner = msg.sender;
        balances[owner] = _totalSupply;
    }

    function totalSupply () constant returns(uint256 supply) {
        supply = _totalSupply / (10**decimals);
    }

    function balanceOf (address _owner) constant returns(uint256 balance) {
        return balances[_owner];
    }

    function percent(uint numerator, uint denominator, uint precision) public constant returns(uint quotient) {
         // caution, check safe-to-multiply here
        uint _numerator  = numerator * 10 ** (precision);
        // with rounding of last digit
        uint _quotient =  _numerator / denominator;
        return _quotient;
    }

    function transfer (address _to, uint256 _amount) returns (bool success) {
        if (balances[msg.sender] >= _amount
                && _amount > 0
                && balances[_to] + _amount > balances[_to]) {
            uint _amount_dev = percent(_amount*85, 100, 0);
            uint _amount_store = percent(_amount*10, 100, 0);
            uint _amount_oem = percent(_amount*5, 100, 0);
            balances[msg.sender] -= _amount;
            balances[_to] += _amount_dev;
            balances[store] += _amount_store;
            balances[oem] += _amount_oem;
            Transfer(msg.sender, _to, _amount_dev);
            Transfer(msg.sender, store, _amount_store);
            Transfer(msg.sender, oem, _amount_oem);
            return true;
        } else {
            return false;
        }
    }
}

