module.exports = Span;

function Span(opts){
    this.id =  opts.id;
    this.name = opts.name;
    this.traceId = opts.traceId;
    this.parentId = opts.parentId;
    this.timestamp = opts.timestamp * 1000;
    this.annotations = opts.annotations || [];
    this.binaryAnnotations = opts.binaryAnnotations || [];
    this.duration = opts.duration || 0;
}

Span.prototype.addAnnotation = function(endpoint, timestamp, value){
    timestamp = timestamp*1000;
    this.annotations.push({
        endpoint:endpoint,
        timestamp:timestamp,
        value:value
    });
}

Span.prototype.endpoint = function(serviceName, ipv4, port){
    return {
        serviceName:serviceName,
        ipv4:ipv4,
        port:port
    }
}