const Sequelize = require('sequelize');
const config = require('./../config');

var sequelize = new Sequelize(config.database, config.username, config.password, {
    host: config.host,
    dialect: 'mysql',
    pool: {
        max: 5,
        min: 0,
        idle: 30000
    }
});

var Stock = sequelize.define('stock', {
    id: {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    name: Sequelize.STRING(64),
    no: Sequelize.STRING(64),
    date: Sequelize.DATE,
    open: Sequelize.DOUBLE,
    high: Sequelize.DOUBLE,
    low: Sequelize.DOUBLE,
    close: Sequelize.DOUBLE
	}, {
			timestamps: false
});
	

var fn_db = async (ctx, next) => {
    var name = ctx.params.name;
	var ss = await Stock.findAll();
    console.log(`find ${ss.length} stocks:`);
    ctx.render('db.html', {
                stocks: ss
    });
};

module.exports = {
    'GET /db/': fn_db,
    'GET /stock/': fn_db
};