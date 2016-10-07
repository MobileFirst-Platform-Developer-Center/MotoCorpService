(function(module){

	if(typeof Object.values === 'undefined') {
		Object.prototype.values = function(object) {
			return Object.keys(object).map(function(key) {
				return object[key];
			});
		};
	}

	function DashDB(ibmdb, config) {
		this.driver = ibmdb;
		this.config = config;
		this.connection = null;
	}

	DashDB.prototype.connect = function(){
		var promise = function(resolve, reject){
			var connectionCallback = function(error, connection) {
				if(error) {
					return reject(error);
				}

				return resolve(this.connection = connection);
			}.bind(this);

			this.driver.open("DRIVER={DB2};DATABASE=" + this.config.db + ";UID=" + this.config.username + ";PWD=" + this.config.password + ";HOSTNAME=" + this.config.hostname + ";port=" + this.config.port, connectionCallback);

		}.bind(this);

		return this.isConnected().catch(function(){
			return new Promise(promise);
		});
	};

	DashDB.prototype.isConnected = function() {
		if(!this.connection) {
			return Promise.reject('call connect first before using this method');
		}

		return Promise.resolve(this.connection);
	};

	DashDB.prototype.create = function(table, data){

		var promise = function(connection){
			var keys = Object.keys(data);
			var values = Object.values(data);
			var filler = (new Array(keys.length)).fill('?');

			return this.query('INSERT INTO ' + table + ' ("' + keys.join('", "') + '") VALUES('+ filler.join(',') +')', values);
		}.bind(this);

		return this.isConnected().then(promise);
	};

	DashDB.prototype.query = function(query, params) {

		var promise = function(resolve, reject) {
			if(!this.connection) {
				return reject('call connect first before using the query method');
			}

			if(!(params instanceof Array)) {
				params = [];
			}

			this.connection.query(query, params,function(error, result) {
				if(error) {
					return reject(error);
				}

				resolve(result);
			});

		}.bind(this);

		return new Promise(promise);
	};

	DashDB.prototype.disconnect = function(){
		var promise = function(resolve, reject){
			if(this.connection) {
				this.connection.close(resolve);
			} else {
				resolve('connection is not open');
			}
		}.bind(this);

		return new Promise(promise);
	};

	module.exports = DashDB;

})(module);