###############################################
###### P.O.C. rest api using m2m microraiden ##
###  Some functions need further testing ######
### Channel settling hardcoded atm with  ######
###	receivers pkey                       ######
###############################################

import json
import utils
import config
from microraiden import Client
from microraiden.crypto import sign_balance_proof
from flask import Flask, abort, request, jsonify

app = Flask(__name__)

channel_object = None

@app.route('/api/1/openChannel', methods=['POST'])
def openChannel():
    global channel_object
    with Client(config.privkey) as client:
        channel_object = client.get_suitable_channel(config.receiver, int(utils.convert_to_utf(request.get_data())))
        if not channel_object:
                return json.dumps(utils.error)
        return json.dumps(utils.ok)

@app.route('/api/1/balance', methods=['GET'])
def balance():
        if not channel_object:
                return json.dumps(utils.error)
        return json.dumps({'balance':channel_object.balance}), 200, {'ContentType':'application/json'}

@app.route('/api/1/close', methods=['GET'])
def close():
        channel_close = channel_object.close()
        if not channel_close:
                return json.dumps(utils.error)
        return json.dumps(channel_close, 200, {'ContentType':'application/json'})

@app.route('/api/1/transfer', methods=['POST'])
def transfer():
        make_transfer = channel_object.create_transfer(int(utils.convert_to_utf(request.get_data())))
        if not make_transfer:
                return json.dumps(utils.error)
        return json.dumps(utils.ok)

@app.route('/api/1/settle', methods=['GET'])
def settle():
        closing_sig = sign_balance_proof(config.receiver_privkey, channel_object.receiver, channel_object.block, channel_object.balance)
        settled = channel_object.close_cooperatively(closing_sig)
        if not settled:
                return json.dumps(utils.error)
        return json.dumps(settled, 200, {'ContentType':'application/json'})

@app.route('/api/1/topup', methods=['POST'])
def topup():
        top_up = channel_object.create_transfer(int(utils.convert_to_utf(request.get_data())))
        if not top_up:
                return json.dumps(utils.error)
        return json.dumps(utils.ok)

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5555, debug=True)


