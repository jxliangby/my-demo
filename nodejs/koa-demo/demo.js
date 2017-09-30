const Koa = require('koa');
const trace = require('./koa-trace.js');
const app = new Koa();
const port = 8000;
app.context.config={
  traceConfig:{ 
    traceOpen:true,
    zkServer:'10.0.53.134:2181,10.0.53.135:2181,10.0.53.136:2181',
    traceTopic:'zipkin',
    traceOpen:true,
    traceDebug:true
  }
}

app.Logger = {
  getLogger:(name)=>{ 
          return {
            'debug':(msg)=>{
                console.log(`${name}--debug-->${msg}`);
            },
            'info':(msg)=>{
                console.log(`${name}--info-->${msg}`);
            }
          }
        
    }
}

app.use(trace(app,{port:port}));

app.use(async function (ctx, next) {
  console.log('response start.............');
  const start = new Date();
  await next();
  const ms = new Date() - start;
  ctx.set('X-Response-Time', `${ms}ms`);
  
  console.log('response end.............');
});

// logger

app.use(async function (ctx, next) {
 console.log('logger start.............');
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log('logger end......'+`${ctx.method} ${ctx.url} - ${ms}`);
});

// response

app.use(ctx => {
  ctx.body = 'Hello World';
});
trace.add('hahah');
app.listen(port);
console.log('app started at port 8000...');