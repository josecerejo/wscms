/**
 *
 */
function toString2(value, whitelist)
{

	var m = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        };
    var a,
        i,
        k,
        l,
        r = /["\\\x00-\x1f\x7f-\x9f]/g,
        v;

    switch (typeof value) {
    case 'string':
    	if(value.replace(' ','').length==0)return "\"\"";
        return r.test(value) ?
            '"' + value.replace(r, function (a) {
                var c = m[a];
                if (c) {
                    return c;
                }
                c = a.charCodeAt();
                return '\\u00' + Math.floor(c / 16).toString(16) +
                                           (c % 16).toString(16);
            }) + '"' :
            '"' + value + '"';

    case 'number':

        return isFinite(value) ? String(value) : 'null';

    case 'boolean':
    case 'null':
        return String(value);

    case 'object':

        if (!value) {
            return 'null';
        }

        if (typeof value.toJSON === 'function') {
            return toString2(value.toJSON());
        }
        a = [];
        if (typeof value.length === 'number' &&
                !(value.propertyIsEnumerable('length'))) {

            l = value.length;
            for (i = 0; i < l; i += 1) {
                a.push(toString2(value[i], whitelist) || 'null');
            }

            return '[' + a.join(',') + ']';
        }
        if (whitelist) {

            l = whitelist.length;
            for (i = 0; i < l; i += 1) {
                k = whitelist[i];
                if (typeof k === 'string') {
                    v = toString2(value[k], whitelist);
                    if (v) {
                        a.push(toString2(k) + ':' + v);
                    }
                }
            }
        } else {

            for (k in value) {
                if (typeof k === 'string') {
                    v = toString2(value[k], whitelist);
                    if (v || v==='' ||v=="") {
                        a.push(toString2(k) + ':' + v);
                    }
                }
            }
        }
        return '{' + a.join(',') + '}';
    }

}