var AppCoin = artifacts.require("./AppCoin.sol");

module.exports = function(deployer) {
  deployer.deploy(AppCoin);
};
