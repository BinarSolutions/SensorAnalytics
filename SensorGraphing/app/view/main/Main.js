Ext.define('SensorsData.view.main.Main', {
	
	
    extend: 'Ext.container.Container',
	
    requires: [
        'SensorsData.view.main.MainController',
        'SensorsData.view.main.MainModel'
    ],
	alias: 'widget.Main',
    xtype: 'app-main',
	
	var: model = Ext.define('User', {
		extend: 'Ext.data.Model',
		fields: ['Device', 'timevalue', 'light_value', 'Magnetic_x', 'Magnetic_y', 'Magnetic_x']
	}),
	
	var: model1 = Ext.define('User1', {
		extend: 'Ext.data.Model',
		fields: ['_type', '_id', 'Device', 'Name', 'Date', "X", "Y", "Z"]
	}),
	
	var: LightStore = Ext.create('Ext.data.Store', {
                model: model,
                proxy: {
                        type: 'ajax',
                        url: '/duomenys/Light.txt',
                        reader: {
                                type: 'json'
                        }
                },
                autoLoad: true
        }),


	var: myStore = Ext.create('Ext.data.Store', {
		model: model1,
		proxy: {
			type: 'rest',
			url: '/domenys/Magnetic_field.txt',
			reader: {
				type: 'json',
				rootProperty: 'rh:doc',
				idProperty: '_id'
			}
		},
		autoLoad: true
	}),
	
	
   
    controller: 'main',
    viewModel: {
        type: 'main'
    },

    layout: {
        type: 'border'
    },

    items: [
	{
	region: 'north',
	xtype : 'component',
	padding : 10,
	height : 40,		
        html : '<font size="5" color="white"><b>Sensors data.</b></font'
	},{
    xtype: 'panel',
    title: 'Sensors',
    region: 'west',
    width: 250,
    split: true,
	collapsible: true,
	collapsed: true
    },{
        region: 'center',
        xtype: 'tabpanel',
        items:[{
			title: 'Light values',
			layout: 'fit',
				items: [{
					xtype: 'chart',
					store: LightStore,
					interactions: ['panzoom'],
					/*	
					legend: {
						docked: 'bottom'
					},*/
					//define x and y axis.
					axes: [
						{
							type: 'numeric',
							position: 'left'
						},
						{
							type: 'category',
							visibleRange: [0, 1],
							position: 'bottom'
						}
					],
					//define the actual series
					series: [{
						type: 'line',
						xField: 'timevalue',
						yField: 'light_value',
						title: 'Light values',
						style: {
							//fill: "#a61115",
							stroke: "#a61115",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					}]
				}]
		},{
			title: 'Magnetic field values',
			layout: 'fit',
				items: [{
					xtype: 'chart',
					store: myStore,
					interactions: ['crosszoom'],
						
					legend: {
						docked: 'bottom'
					},
					//define x and y axis.
					axes: [
						{
							type: 'numeric',
							position: 'left'
						},
						{
							type: 'category',
							visibleRange: [0, 1],
							position: 'bottom'
						}
					],
					//define the actual series
					series: [{
						type: 'line',
						xField: 'Date',
						yField: 'X',
						title: 'Magnetic field x',
						style: {
							//smooth: true,
							//fill: "#94ae0a",
							stroke: "#94ae0a",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					},{
						type: 'line',
						xField: 'Date',
						yField: 'Y',
						title: 'Magnetic field y',
						style: {
							//smooth: true,
							//fill: "#0528ed",
							stroke: "#0528ed",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					},{
						type: 'line',
						xField: 'Date',
						yField: 'Z',
						title: 'Magnetic field z',
						style: {
							//smooth: true,
							//fill: "#edca05",
							stroke: "#edca05",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					}]
				}]
		},{
			title: 'Data',
			layout: 'fit',
			items: [{
				xtype: 'grid',
				store: myStore, 
				columns: [
					{ text: 'Name',  dataIndex: 'Name', flex: 1 },
					{ text: 'Device',  dataIndex: 'Device', flex: 1 },
					{ text: 'Time', dataIndex: 'Date', flex: 1.5 },
					{ text: 'Magnetic field x', dataIndex: 'X', flex: 1 },
					{ text: 'Magnetic field y', dataIndex: 'Y', flex: 1 },
					{ text: 'Magnetic field z', dataIndex: 'Z', flex: 1 }
				]
			}]
		}]
    }]
});
