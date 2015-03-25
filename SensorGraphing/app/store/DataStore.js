Ext.define('MyApp.store.DataStore', {
	extend: 'Ext.data.Store',
	model: 'MyApp.model.DataModel',
	storeId: 'DataStore',
	autoload: true,
	buffered: true,
	/*proxy: {
		type: 'direct',
		url: 'mongodb://213.197.167.222:27317/VJG/sensoriai',
		
		reader: {
			type: 'json',
			rootProperty: 'rows'
		}
	}*/
	data: [{
		timevalue: '2015-02-15 13:08:58',
		Device: 'GT-9105P',
		light_value: '180.0',
		Magnetic_x: '25.6',
		Magnetic_y: '1.5',
		Magnetic_z: '-12.0'
	}, {
		timevalue: '2015-02-15 13:09:01',
		Device: 'GT-9105P',
		light_value: '160.0',
		Magnetic_x: '23.4',
		Magnetic_y: '6.3',
		Magnetic_z: '-12.0'
	},{
		timevalue: '2015-02-15 13:09:04',
		Device: 'GT-9105P',
		light_value: '175.0',
		Magnetic_x: '35.4',
		Magnetic_y: '-5.3',
		Magnetic_z: '86.0'
	},{
		timevalue: '2015-02-15 13:09:19',
		Device: 'GT-9105P',
		light_value: '210.0',
		Magnetic_x: '-201.4',
		Magnetic_y: '-52.1',
		Magnetic_z: '258.8'
	},{
		"timevalue" : "2015-02-15 14:52:43",
		"Device" : "GT-I9105P",
		"light_value" : "85.0",
		"Magnetic_x" : "576.6",
		"Magnetic_y" : "16.3",
		"Magnetic_z" : "-324.4"
	},{
		"timevalue" : "2015-02-15 14:52:46",
		"Device" : "GT-I9105P",
		"light_value" : "75.0",
		"Magnetic_x" : "612.2",
		"Magnetic_y" : "102.7",
		"Magnetic_z" : "-653.4"
	},{
		"timevalue" : "2015-02-15 14:52:49",
		"Device" : "GT-I9105P",
		"light_value" : "1759.0",
		"Magnetic_x" : "576.2",
		"Magnetic_y" : "16.7",
		"Magnetic_z" : "-567.0"
	},{
		"timevalue" : "2015-02-15 14:52:52",
		"Device" : "GT-I9105P",
		"light_value" : "1680.0",
		"Magnetic_x" : "352.2",
		"Magnetic_y" : "20.3",
		"Magnetic_z" : "-356.5"
	},{
		"timevalue" : "2015-02-15 14:52:55",
		"Device" : "GT-I9105P",
		"light_value" : "795.0",
		"Magnetic_x" : "476.2",
		"Magnetic_y" : "16.3",
		"Magnetic_z" : "-402.6"
	},{
		"timevalue" : "2015-02-15 14:52:58",
		"Device" : "GT-I9105P",
		"light_value" : "735.0",
		"Magnetic_x" : "256.8",
		"Magnetic_y" : "65.3",
		"Magnetic_z" : "-652.2"
	},{
		"timevalue" : "2015-02-15 14:53:01",
		"Device" : "GT-I9105P",
		"light_value" : "750.0",
		"Magnetic_x" : "95.8",
		"Magnetic_y" : "14.4",
		"Magnetic_z" : "-243.5"
	}]
	
});