var fs = require('fs');
var apis = fs.readFileSync('out-api-id.txt', 'utf-8').split('\r\n');
var logApis = fs.readFileSync('out-log-api-id.txt', 'utf-8').split('\r\n');
var nots = logApis.filter((s)=>{
	return apis.indexOf(s)==-1;
}).map((x)=>{
	return x.replace(/"/g,"'");
});
fs.writeFileSync('out-api-out.txt', nots.join(',\r\n'));