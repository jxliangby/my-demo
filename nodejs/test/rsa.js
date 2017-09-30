
var crypto = require('crypto');  // 加载crypto库
var fs = require('fs');
function signer(algorithm,key,data){
    var sign = crypto.createSign(algorithm);
    sign.update(data);
    sig = sign.sign(key, 'hex');
    return sig;
}

function verify(algorithm,pubkey,sig,data){
    var verify = crypto.createVerify(algorithm);
    verify.update(data);
    return verify.verify(pubkey, sig, 'hex')
}
console.log(crypto.getHashes()); //打印支持的hash算法
console.log(crypto.getCiphers());

//var algorithm = 'RSA-SHA256';
var algorithm = 'md5WithRSAEncryption';
var data = 'Api_ID=crt.payment.public.PosPay&Api_Version=2.0.0&App_Pub_ID=1000100103EC&App_Sub_ID=T000002302TK&App_Token=a9968c04589b402986c9aaab5c2b1f02&Format=json&Partner_ID=10001000&REQUEST_DATA={"transactionUuid":"b9c74c4767c54bd288124fc44f8d1c1b","channelId":"STORE","merchantCode":"1679000000068","sysId":"10001001","spbillCreateIp":"127.0.0.1","channelBizCode":"100001","storeCode":"ZZSNS","terminalId":"ZZSNS","operatorId":"123","authCode":"9011111","trasactionTime":"20170810104543","requestTime":"20170810104543","outTradeNo":"20170810104543","currency":"CNY","payAmount":"1","subject":"太平洋咖啡","desc":"太平洋咖啡"}&Sign_Method=rsa&Sys_ID=10001001&Time_Stamp=2017-08-10 10:45:43:749&MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5cw6SWL0Qh7CLXiEzoWQmGlOFOwQADq7GV8Z7TUyey3VBMGLSWAUKgLUIkiusrFogw69EdJ+gjUfvvMZWDaV3MwVP6nnWdNZg0CM0tEfH7Ozsl8S5dddj24RKuM1gOgVD3p5NgsFA/Zftlez/tB3Ldl8NTkfK3z6tNrlP5ZDsCQIDAQAB';   //传输的数据
var privatePem = fs.readFileSync('rsa_pri.key');
var privateKey = privatePem.toString();
console.log('private key : \n' + privateKey);
var privateKey = 'MIICXgIBAAKBgQC5cw6SWL0Qh7CLXiEzoWQmGlOFOwQADq7GV8Z7TUyey3VBMGLSWAUKgLUIkiusrFogw69EdJ+gjUfvvMZWDaV3MwVP6nnWdNZg0CM0tEfH7Ozsl8S5dddj24RKuM1gOgVD3p5NgsFA/Zftlez/tB3Ldl8NTkfK3z6tNrlP5ZDsCQIDAQABAoGBAK7gsggNiFSTMEdsf3C5q8BGqSYFDfOj5OqDEVdrAGrqP+s2XqoueKp7zruDY44UhXVKMvgh5fm34PNRF9NEURgm+6xGu5zaKRyM6ZKwZ4RAQpZqdVfGaEiFhjacUF/BFrm028kCRwd9onTU+HBMaSAoTpJAFaiQHg1ZX7JSTHKRAkEA7+XFSYy3GSoMFkIjE865Qus1F4ERrAVIy+dXcJhjH9QeQcEyFRuKtBVZL3vbPlzOAiUpBYarQGGX3aVFr7c63QJBAMXlsE44k6No/6WPf/IGs5NB+VOaSo7avRW1IfbCtsjNMYLgeKSR/cwr90A7ZQTOsdWChFDwpEEy6iVXJZhltR0CQQDpM9dg9CVZlQJC8O/gZWi6oKLvwkxHiKuHa9AYaEqTukPfb8sbsbZX8RjNi/1I1jZ851rFQHBhGX3jvLHNarURAkEAwf7w/y0dJcLHYZAzt5l0LjYtaRRGZJXvaDlBSzoDnwLvt0G/YmyvJJgw8CmJVNYepJQmi4XrLvLvb51ngGarzQJAEuk6Sk/fYeQ9YKtVyyUb6SQ28DZiMNCkXan3joRqYkN5sm06U/a1N+1Ydx/ucgG3q25HimnIWtHdbw+bgzqO0w==';
var sig = signer(algorithm,privateKey,data); //数字签名

console.log('sign data:\n'+sig);

var publicPem = fs.readFileSync('rsa_pub.key');
var publicKey = publicPem.toString();
console.log('public key : \n' + publicKey);
var publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5cw6SWL0Qh7CLXiEzoWQmGlOFOwQADq7GV8Z7TUyey3VBMGLSWAUKgLUIkiusrFogw69EdJ+gjUfvvMZWDaV3MwVP6nnWdNZg0CM0tEfH7Ozsl8S5dddj24RKuM1gOgVD3p5NgsFA/Zftlez/tB3Ldl8NTkfK3z6tNrlP5ZDsCQIDAQAB';
console.log(verify(algorithm,publicKey,sig,data));   