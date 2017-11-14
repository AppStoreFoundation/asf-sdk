// AppCoins contract with share splitting among different wallets
// Not fully ERC20 compliant due to tests purposes

pragma solidity ^0.4.8;
contract ERC20Interface {
    // Get the total token supply
    function totalSupply() constant returns (uint256 totalSupply);
    // Get the account balance of another account with address _owner
    function balanceOf(address _owner) constant returns (uint256 balance);
    // Send _value amount of tokens to address _to
    function transfer(address _to, uint256 _value) returns (bool success);
    // Send _value amount of tokens from address _from to address _to
    function transferFrom(address _from, address _to, uint256 _value) returns (bool success);
    // Allow _spender to withdraw from your account, multiple times, up to the _value amount.
    // If this function is called again it overwrites the current allowance with _value.
    // this function is required for some DEX functionality
    function approve(address _spender, uint256 _value) returns (bool success);
    // Returns the amount which _spender is still allowed to withdraw from _owner
    function allowance(address _owner, address _spender) constant returns (uint256 remaining);
    // Triggered when tokens are transferred.
    event Transfer(address indexed _from, address indexed _to, uint256 _value);
    // Triggered whenever approve(address _spender, uint256 _value) is called.
    event Approval(address indexed _owner, address indexed _spender, uint256 _value);
}
contract AppCoin is ERC20Interface {
    string public constant symbol = "APPC";
    string public constant name = "AppCoins";
    uint8 public constant decimals = 2;
    uint256 _totalSupply = 1000000 * 10 ** decimals;
    address public owner;
    address public oem = 0x0000000000000000000000000000000000000000; // Hardcoded address for division
    address public store = 0x0000000000000000000000000000000000000000; // Hardcoded address for division
    mapping(address => uint256) balances;
    mapping(address => mapping (address => uint256)) allowed;
    
    modifier onlyOwner () {
        if (msg.sender != owner) {
            throw;
        }
        _;
    }
    function AppCoin () {
        owner = msg.sender;
        balances[owner] = _totalSupply;
    }

    function totalSupply () constant returns(uint256 totalSupply) {
        totalSupply = _totalSupply;
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

    function transferFrom (address _from, address _to, uint256 _amount) returns(bool success) {
        if (balances[_from] >= _amount
                && allowed[_from][msg.sender] >= _amount
                && _amount > 0
                && balances[_to] + _amount > balances[_to]) {
            balances[_from] -= _amount;
            allowed[_from][msg.sender] -= _amount;
            balances[_to] += _amount;
            Transfer(_from, _to, _amount);
            return true;
        } else {
            return false;
        }
    }

    function approve (address _spender, uint256 _amount) returns (bool success) {
        allowed[msg.sender][_spender] = _amount;
        Approval(msg.sender, _spender, _amount);
        return true;
    }

    function allowance (address _owner, address _spender) constant returns (uint256 remaining) {
        return allowed[_owner][_spender];
    }
}
