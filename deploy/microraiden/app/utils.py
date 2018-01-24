ok = {'success': True}, 200, {'ContentType':'application/json'}
error = {'success': False}, 500, {'ContentType':'application/json'}

def convert_to_utf(s):
            return s.decode('utf-8')
