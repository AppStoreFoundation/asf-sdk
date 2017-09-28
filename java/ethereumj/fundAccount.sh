#!/bin/bash

geth --rinkeby --fast --rpc --unlock="$1" --exec "eth.sendTransaction({from: '0x$1', to: '0x$2', value: $3})" attach ipc://home/neuro/.ethereum/rinkeby/geth.ipc

