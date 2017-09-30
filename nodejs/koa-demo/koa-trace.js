//module.exports = crtTrace;
const Span = require('./Span');
let kafka = require('kafka-node');  
let Producer = kafka.Producer; 
let Client = kafka.Client;  

const CLIENT_SEND		=	'cs';
const CLIENT_RECEIVCE	=	'cr';
const SERVER_SEND		=	'ss';
const SERVER_RECEIVE	=	'sr';
const SID				=	'ctsspanid';
const PID				=	'ctsparentspanid';
const CTS_TRACE_ID		=	'ctstraceid';
const CTS_TIMESTAMP		=	'ctstimestamp';
const DEFAULT_TRACE_TOPIC		=	'zipkin';
//是否开启追踪
let traceOpen			=	true;
let traceTopic   		=	DEFAULT_TRACE_TOPIC;
let app,Logger=null;
let debug				=	true;
let ip,port = 0;
let defaultServiceName	=	'mid';
let kafkaProducer		= 	null;

module.exports = (app,opts) => {
	opts = opts || {};

	port = opts.port || port;
	defaultServiceName = opts.defaultServiceName || defaultServiceName;
	if(app.Logger!=null){
		Logger = app.Logger.getLogger('koa-trace');
	}
	initTrace(app);
	//////
	return async (ctx,next) => {
		if(!traceOpen){
			await next();
			return;
		}		
		let ip = ctx.ip;	
		ctx.header[CTS_TRACE_ID] = guid();	
		ctx.header[CTS_TIMESTAMP] = new Date().getTime()-100;

		let mockSpan = mockFrontSpan(ctx);

		debugMsg('mock span --->'+JSON.stringify(mockSpan));

		let span = createFaSpan(ctx);
		
		debugMsg('span --->'+JSON.stringify(span));
		// for next;
		await next();
		// after next;
		addAfterAnnotation(ctx,span,SERVER_SEND);
		send(span);
		if(mockSpan!=null){			
			addAfterAnnotation(ctx,mockSpan,CLIENT_RECEIVCE);
			send(mockSpan);
		}
	}
};

function initTrace(app){
	if(app.context==null || app.context.config==null){
		log('trace config is null')
		return;
	}
	let {traceConfig} = app.context.config;
	if (traceConfig.traceOpen!=null){
		traceOpen = traceConfig.traceOpen;
	}
	if(!traceOpen){
		log('trace is close')
		return;
	}
	let zkServer  = traceConfig.zkServer;
	let client = new Client(zkServer);  
	debug = traceConfig.traceDebug;
	traceTopic = traceConfig.traceTopic || DEFAULT_TRACE_TOPIC;
	kafkaProducer = new Producer(client, {  
		requireAcks: 0  
	}); 
	kafkaProducer.on('ready', function () {
		log(`trace kafka ready at zk:${zkServer},topic:${traceTopic}`);
	});
 
	kafkaProducer.on('error', function (err) {
		log(`trace kafka error:${err}`)
	});		
}	
/**
 * 创建请求span
 */
function createFaSpan(ctx){
	let traceId = ctx.header[CTS_TRACE_ID];		
	let spanId = ctx.header[SID];
	let parentId = ctx.header[PID];

	let startTime=new Date().getTime();
	let url = ctx.url;
	let ip = ctx.ip;	
	traceId = traceId==null?guid():traceId;
	spanId = spanId==null?guid():spanId;
	parentSpanId = parentId==null ? traceId : parentId;
	/////
	let defaultName = defaultSpanServiceName(ctx);
	let span = new Span({
		id:spanId,
		name:defaultName.spanName,
		traceId:traceId,
		parentId:parentSpanId,
		timestamp:startTime
	});
	let endpoint = span.endpoint(defaultName.serviceName, ip, port);
	span.addAnnotation(endpoint, startTime, SERVER_RECEIVE);
	return span;
}
function addAfterAnnotation(ctx,span,annoationValue){
	let endTime = new Date().getTime();
	let duration = endTime * 1000 - span.timestamp;
	let defaultName = defaultSpanServiceName(ctx);
	let endpoint = span.endpoint(defaultName.serviceName, ip, port);
	span.duration = duration;
	span.addAnnotation(endpoint, endTime, annoationValue);
}
/***
如果H5,APP等传入ctstraceid&ctstimestamp
则帮它模拟span然后待发送
*/
function mockFrontSpan(ctx){
	let traceId = ctx.header[CTS_TRACE_ID];
	let timestamp = ctx.header[CTS_TIMESTAMP];
	if(!traceId || !timestamp){
		return null;
	}
	
	let sId = ctx.header[SID];
	let pId = ctx.header[PID];
	if(sId!=null || pId!=null){//如果有这些值，则视调用方已经埋点
		return null;
	}
	let spanId = guid();
	let defaultName = defaultSpanServiceName(ctx);

	let span = new Span({
		id:spanId,
		name:defaultName.spanName,
		traceId:traceId,
		parentId:traceId,
		timestamp:timestamp
	});
	let endpoint = span.endpoint(defaultName.serviceName, ip, port);
	span.addAnnotation(endpoint, timestamp, CLIENT_SEND);
	return span;
}

function defaultSpanServiceName(ctx){
	let urls = ctx.url.split('/');
	let spanName = null;
	let serviceName = null;
	if(urls.length>0){
		serviceName = urls.slice(0,urls.length-1).join('.');
		spanName = urls[urls.length-1];
	}
	serviceName = serviceName || defaultServiceName;
	spanName = spanName || defaultServiceName;
	return {serviceName:serviceName,spanName:spanName};
}

async function send(span){
	if(!traceOpen){
		return;
	}
	if(kafkaProducer==null){
		log('trace kafka not init');
		return;
	}
	let spanStr = JSON.stringify([span]); 
	debugMsg('sapn:' + spanStr);
	let payloads = [{
		topic: traceTopic,
		messages: spanStr,
		partition:0
	}];
	
	try{
		kafkaProducer.send(payloads, function (err, data) {
			if(err!=null){
				log(err);
			}
		});
	}catch(error){
		log(error);		
	}
}
module.exports.add=(aa) => {
	debugMsg(`dddddddddddddddddddd ${aa},port:${port}`);
}

function guid() {
    function S4() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
}
function log(msg){
	if(Logger == null) return;
	Logger['info'](msg);
}


function debugMsg(msg){
	if(!debug) return;
	if(Logger!=null){
		Logger['debug'](msg);
	}else{
		console.log(msg);
	}
}